/*******************************************************************************
 * Copyright (c)2014 Prometheus Consulting
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nz.co.senanque.madura.bundle;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import nz.co.senanque.madura.bundle.spring.BundleScope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * This holds the application context for the bundle
 * Because this class is always loaded under a special classpath the
 * classes and resources in the context are also loaded under that classpath
 * It looks for a context called applicationContext.xml and then for
 * all the *-spring.xml contexts.
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.8 $
 */
public class BundleRootImpl implements BundleRoot 
{
    GenericApplicationContext m_applicationContext;
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    private long m_lastModified;
    private Properties m_properties;
    private static Map<String, Object> m_exportedBeans;
    private ClassLoader m_classLoader;
    private String m_name;
    private boolean m_shutdown = false;
    
    public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer
    {
        protected String resolveSystemProperty(String key)
        {
            return super.resolveSystemProperty(key);
        }
    }
    
    /* (non-Javadoc)
     * @see nz.co.senanque.madura.bundle.BundleRootI#shutdown()
     */
    public void shutdown()
    {
    	m_shutdown = true;
    	m_applicationContext.close();
//        m_applicationContext.stop();
    }

    /* (non-Javadoc)
     * @see nz.co.senanque.madura.bundle.BundleRootI#init()
     */
    public void init(DefaultListableBeanFactory ownerBeanFactory, Properties properties, ClassLoader cl, Map<String, Object> exportedBeans)
    {
        m_properties = properties;
        m_exportedBeans = exportedBeans;
        BundleManager bundleManager = ownerBeanFactory.getBean(BundleManager.class);
        BundleScope bundleScope = bundleManager.getScope();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
        m_classLoader = cl;
        GenericApplicationContext ctx = new GenericApplicationContext();
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        String contextPath = properties.getProperty("Bundle-Context","/bundle-spring.xml");
        m_logger.debug("loading context: {}",contextPath);
        ClassPathResource classPathResource = new ClassPathResource(contextPath,cl);
        xmlReader.loadBeanDefinitions(classPathResource);
        PropertyPlaceholderConfigurer p = new PropertyPlaceholderConfigurer();
        p.setProperties(properties);
        ctx.addBeanFactoryPostProcessor(p);
        if (m_logger.isDebugEnabled())
        {
            dumpClassLoader(cl);
        }
        for (Map.Entry<String, Object> entry: exportedBeans.entrySet())
        {
        	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(InnerBundleFactory.class);
        	beanDefinitionBuilder.addPropertyValue("key", entry.getKey());
        	beanDefinitionBuilder.addPropertyValue("object", exportedBeans.get(entry.getKey()));
            ctx.registerBeanDefinition(entry.getKey(), beanDefinitionBuilder.getBeanDefinition());
        }
        // Registers the bundleroot (ie this) as a bean
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(InnerBundleFactory.class);
    	beanDefinitionBuilder.addPropertyValue("key", "bundleRoot");
    	beanDefinitionBuilder.addPropertyValue("object", this);
        ctx.registerBeanDefinition("bundleRoot", beanDefinitionBuilder.getBeanDefinition());
    	
        Scope scope = ownerBeanFactory.getRegisteredScope("session");
        if (scope != null)
        {
        	ctx.getBeanFactory().registerScope("session", scope);
        }
        if (bundleScope != null)
        {
        	ctx.getBeanFactory().registerScope("bundle", bundleScope);
        }
        ctx.refresh();
        m_applicationContext = ctx;
        Thread.currentThread().setContextClassLoader(classLoader);
    }
    
    private void dumpClassLoader(ClassLoader sysClassLoader)
    {
        //Get the System Classloader
        if (sysClassLoader == null)
        {
            sysClassLoader = ClassLoader.getSystemClassLoader();
        }

        //Get the URLs
        URL[] urls = ((URLClassLoader)sysClassLoader).getURLs();

        for(int i=0; i< urls.length; i++)
        {
            m_logger.debug("{}",urls[i].getFile());
        }       
    }

    /* (non-Javadoc)
     * @see nz.co.senanque.madura.bundle.BundleRootI#getApplicationContext()
     */
    public ApplicationContext getApplicationContext()
    {
        return m_applicationContext;
    }

   public void setDate(long lastModified)
    {
        m_lastModified = lastModified;
        
    }

    public long getDate()
    {
        return m_lastModified;
    }

    public static Map<String, Object> getExportedBeans()
    {
        return m_exportedBeans;
    }

    public Properties getProperties()
    {
    	Properties ret = new Properties();
    	ret.putAll(m_properties);
        return ret;
    }

	public ClassLoader getBundleClassLoader() {
		return m_classLoader;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}

	public boolean isShutdown() {
		return m_shutdown;
	}

}
