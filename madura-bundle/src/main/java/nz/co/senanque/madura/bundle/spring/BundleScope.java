package nz.co.senanque.madura.bundle.spring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.BundleRoot;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.RequestContextHolder;

public class BundleScope implements Scope {
	
	private class BundleMap {
		
		private Map<String,BeanMap> m_map = new HashMap<>();

		public BeanMap getBundle(String bundleId) {
			return m_map.get(bundleId);
		}

		public void putBundle(String bundleId, BeanMap beanMap) {
			m_map.put(bundleId, beanMap);
		}

		public void cleanup() {
			for (BeanMap beanMap: m_map.values()) {
				beanMap.cleanup();
			}
		}
	}
	private class BeanMap {

		private Map<String,BundleBeanHolder> m_map = new HashMap<>();

		public BundleBeanHolder getBundleBeanHolder(String name) {
			return m_map.get(name);
		}

		public void cleanup() {
			for (BundleBeanHolder bundleBeanHolder: m_map.values()) {
				bundleBeanHolder.cleanup();
			}
		}

		public void putBundleBeanHolder(String name, BundleBeanHolder bundleBeanHolder) {
			m_map.put(name, bundleBeanHolder);
		}
	}
    private transient BundleManager m_bundleManager;

    private Map<String, BundleMap> objectMap = Collections
			.synchronizedMap(new HashMap<String, BundleMap>());

    /**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#get(java.lang.String,
	 *      org.springframework.beans.factory.ObjectFactory)
	 */
	public Object get(String name, ObjectFactory<?> objectFactory) {
		BundleBeanHolder bundleBeanHolder = getBeanHolder(name);
		if (bundleBeanHolder.getBean()==null) {
			bundleBeanHolder.setBean(objectFactory.getObject());
		}
		return bundleBeanHolder.getBean();

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#remove(java.lang.String)
	 */
	public Object remove(String name) {
		String keyName = getConversationId()+'/'+name;
		return objectMap.remove(keyName);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#registerDestructionCallback
         *  (java.lang.String, java.lang.Runnable)
	 */
	public void registerDestructionCallback(String name, Runnable callback) {
		BundleBeanHolder bundleBeanHolder = getBeanHolder(name);
		bundleBeanHolder.setCallback(callback);;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#resolveContextualObject(java.lang.String)
	 */
	public Object resolveContextualObject(String key) {
		return null;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#getConversationId()
	 */
	public String getConversationId() {
		String sessionId = "none";
		try {
			sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		} catch (Exception e) {
			// ignore exceptions and use 'none'
		}
		String bundleId = "none";
		BundleRoot bundleRoot = m_bundleManager.getBundle();
		if (bundleRoot != null) {
			bundleId = bundleRoot.getName();
		}
		return sessionId+'/'+bundleId;
	}
	
	private BundleBeanHolder getBeanHolder(String name) {
		String sessionId = "none";
		try {
			sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		} catch (Exception e) {
			// ignore exceptions and use 'none'
		}
		BundleMap bundleMap = objectMap.get(sessionId);
		if (bundleMap == null) {
			bundleMap = new BundleMap();
			objectMap.put(sessionId, bundleMap);
		}
		String bundleId = "none";
		BundleRoot bundleRoot = m_bundleManager.getBundle();
		if (bundleRoot != null) {
			bundleId = bundleRoot.getName();
		}
		BeanMap beanMap = bundleMap.getBundle(bundleId);
		if (beanMap == null) {
			beanMap = new BeanMap();
			bundleMap.putBundle(bundleId,beanMap);
		}
		BundleBeanHolder ret = beanMap.getBundleBeanHolder(name);
		if (ret == null) {
			ret = new BundleBeanHolder(name);
			beanMap.putBundleBeanHolder(name,ret);
		}
	
		return ret;
	}

	/**
	 * clear the beans
	 */
	public void clearBean() {
		objectMap.clear();
	}

	public BundleManager getBundleManager() {
		return m_bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		m_bundleManager = bundleManager;
	}

	public void sessionDestroyed(String sessionId) {
		BundleMap bundleMap = objectMap.remove(sessionId);
		if (bundleMap != null) {
			bundleMap.cleanup();
		}
	}
}
