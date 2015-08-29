/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nz.co.senanque.madura.bundle.spring.BundleScope;
import nz.co.senanque.madura.bundlemap.BundleMap;
import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.util.StringUtils;

/**
 * Bundle managers extend this class.
 * 
 * @author Roger Parkinson
 *
 */
public abstract class AbstractBundleManager implements BundleManager, InitializingBean, BeanFactoryAware, ApplicationListener<ApplicationEvent> {

    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    protected BundleMap m_bundleMap = new BundleMap();
    private ThreadLocal<BundleVersion> m_currentBundle = new ThreadLocal<>();
    protected Thread m_lock = null;
    protected Map<String,Object> m_exportedBeans = new HashMap<String,Object>();
    private DefaultListableBeanFactory m_beanFactory;
    private String m_export;
    protected BundleVersion m_defaultBundle;
    public Set<BundleListener> m_bundleListeners = new HashSet<BundleListener>();
    private boolean m_childFirst = true;
    private BundleScope m_bundleScope = new BundleScope();


    /* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleManager#shutdown()
	 */
	@Override
	abstract public void shutdown();

	abstract public void scan() throws Exception;
	
	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleManager#init()
	 */
	@Override
	public void init() {
        if (m_export != null)
        {
            for (String beanName: StringUtils.commaDelimitedListToSet(m_export))
            {
               m_exportedBeans.put(beanName,m_beanFactory.getBean(beanName));
            }
        }
        for (BundleListener bundleListener:m_beanFactory.getBeansOfType(BundleListener.class).values())
        {
            m_bundleListeners.add(bundleListener);
        }
    	m_bundleScope.setBundleManager(this);
    }

    public void setBundle(String bundleName, String version)
    {
    	BundleVersion bv = m_bundleMap.selectBestBundle(bundleName, version);
        m_currentBundle.set(bv);
        m_logger.debug("set bundle: {}",bv);
        if (bv != null) {
        	bv.increment();
        }
    }
    public void setBundle(String bundleName)
    {
    	BundleVersion bv = m_bundleMap.selectBestBundle(bundleName);
        m_currentBundle.set(bv);
        m_logger.debug("set bundle: {}",bv);
        if (bv != null) {
        	bv.increment();
        }
    }
    public void releaseBundle()
    {
    	BundleVersion bv = m_currentBundle.get();
        m_logger.debug("release bundle: {}",bv);
        if (bv != null) {
        	bv.decrement();
        }
    }
    public void afterPropertiesSet()
    {
        init();
    }

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleManager#getBundle()
	 */
	@Override
    public BundleRoot getBundle()
    {
        // Fairly crude locking mechanism to ensure that the 
        // the method waits if the bundle list is being worked on
        // We don't want to take out a lock here because there's no need to lock out other getters
        while (m_lock != null && m_lock != Thread.currentThread())
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
            }
        }
        BundleVersion bundleVersion = m_currentBundle.get();
        if (bundleVersion == null)
        {
        	bundleVersion = m_defaultBundle;
        }
        if (bundleVersion == null)
        {
            throw new NoBundleSelectedException("No bundle selected and default bundle is not set");
        }
        return bundleVersion.getRoot();
    }
    public Map<String, Object> getExportedBeans()
    {
        return m_exportedBeans;
    }
    public void setExportedBeans(Map<String, Object> inheritableBeans)
    {
        m_exportedBeans = inheritableBeans;
    }

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleManager#getAvailableBundleRoots()
	 */
	@Override
	public Collection<BundleRoot> getAvailableBundleRoots() {
		return Collections.unmodifiableCollection(m_bundleMap.getAvailableBundleRoots());
	}

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleManager#getBeansOfType(java.lang.Class)
	 */
	@Override
    public Map<?,BundleRoot> getBeansOfType(Class<?> clazz)
    {
        Map<Object,BundleRoot> ret = new HashMap<Object,BundleRoot>();
        for (BundleRoot root : m_bundleMap.getAvailableBundleRoots()) {
    		for (Object o: root.getApplicationContext().getBeansOfType(clazz).values()) {
    			ret.put(o,root);
    		}
    	}
        return ret;
    }
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        m_beanFactory = (DefaultListableBeanFactory)beanFactory;
    }

	public boolean isChildFirst() {
		return m_childFirst;
	}

	public void setChildFirst(boolean childFirst) {
		m_childFirst = childFirst;
	}
    public String getExport()
    {
        return m_export;
    }
    public void setExport(String export)
    {
        m_export = export;
    }

	public DefaultListableBeanFactory getBeanFactory() {
		return m_beanFactory;
	}
	@Override
	public BundleScope getScope() {
		return m_bundleScope;
	}
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof HttpSessionDestroyedEvent) {
			HttpSessionDestroyedEvent httpEvent = (HttpSessionDestroyedEvent)event;
			m_bundleScope.sessionDestroyed(httpEvent.getSession().getId());
		}
	}


}
