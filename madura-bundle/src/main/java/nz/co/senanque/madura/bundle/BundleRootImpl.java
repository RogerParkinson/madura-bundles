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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import nz.co.senanque.madura.bundle.spring.BundleScope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

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
        GenericApplicationContext ctx = null;
		try {
			String contextClassName = properties.getProperty("Bundle-Class");
			if (contextClassName != null) {
				Class<?> contextClass = Class.forName(contextClassName, true, cl);
				m_logger.debug("loading context: {}",contextClassName);
				ctx = new AnnotationConfigApplicationContext();
				((AnnotationConfigApplicationContext)ctx).register(contextClass);
			}
		} catch (Exception e) {
			throw new FailedToLoadBundleContextException(e);
		}
        if (ctx == null) {
	        ctx = new GenericApplicationContext();
	        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
	        String contextPath = properties.getProperty("Bundle-Context","/bundle-spring.xml");
	        m_logger.debug("loading context: {}",contextPath);
	        ClassPathResource classPathResource = new ClassPathResource(contextPath,cl);
	        xmlReader.loadBeanDefinitions(classPathResource);
        }
        MutablePropertySources mps = ctx.getEnvironment().getPropertySources();
        PropertySource<?> propertySource = new PropertiesPropertySource("bundle",properties);
        mps.addFirst(propertySource);
        if (m_logger.isDebugEnabled())
        {
            dumpClassLoader(cl);
        }
        // Get the beans annotated for export
        List<ExportBeanDescriptor> exportBeanList = beansAnnotatedWith(ownerBeanFactory, BundleExport.class);
        for (ExportBeanDescriptor ebd: exportBeanList) {

        	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(InnerBundleFactory.class);
        	beanDefinitionBuilder.addPropertyValue("key", ebd.getOwnerBeanName());
        	beanDefinitionBuilder.addPropertyValue("beanName", ebd.getBeanName());
        	beanDefinitionBuilder.addPropertyValue("type", ebd.getType());
        	beanDefinitionBuilder.addPropertyValue("owner", ownerBeanFactory);
            ctx.registerBeanDefinition(ebd.getBeanName(), beanDefinitionBuilder.getBeanDefinition());
        }
        // These are the XML wired for export beans
        for (Map.Entry<String, Object> entry: exportedBeans.entrySet()) {
        	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(InnerBundleFactory.class);
        	beanDefinitionBuilder.addPropertyValue("key", entry.getKey());
        	beanDefinitionBuilder.addPropertyValue("beanName", entry.getKey());
        	beanDefinitionBuilder.addPropertyValue("owner", ownerBeanFactory);
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
        	ctx.getBeanFactory().registerScope("vaadin-ui", bundleScope);
        }
        ctx.refresh();
        m_applicationContext = ctx;
        Thread.currentThread().setContextClassLoader(classLoader);
    }
    private Class<?> getBeanClass(String name) {
    	if (!StringUtils.hasText(name)) {
    		return null;
    	}
    	String className = name;
    	if (name.indexOf("$$") > -1) {
    		className = StringUtils.split(name, "$$")[0];
    	}
		
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
    	return clazz;
    }
	public List<ExportBeanDescriptor> beansAnnotatedWith(BeanFactory beanFactory, Class<? extends BundleExport> annotationType) {
		List<ExportBeanDescriptor> ret = new ArrayList<>();
		if (beanFactory instanceof DefaultListableBeanFactory) {
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)beanFactory;
			Iterator<String> it = defaultListableBeanFactory.getBeanNamesIterator();
			while( it.hasNext()) {
				String beanName = it.next();
				BeanDefinition bd;
				try {
					bd = defaultListableBeanFactory.getBeanDefinition(beanName);
				} catch (NoSuchBeanDefinitionException e) {
					continue;
				}
				BundleExport a = defaultListableBeanFactory.findAnnotationOnBean(beanName, annotationType);
				if (a != null) {
					// The actual class is annotated so add it to the list
					Class<?> clazz = getBeanClass(bd.getBeanClassName());
					ret.add(new ExportBeanDescriptor(beanName,a,clazz));
					continue;
				}
				Configuration c = defaultListableBeanFactory.findAnnotationOnBean(beanName, Configuration.class);
				if (c != null) {
					// This is a configuration class. Look for beans:
					Class<?> clazz = getBeanClass(bd.getBeanClassName());
					if (clazz == null) {
						continue;
					}
					for (Method method:clazz.getMethods()) {
						Bean beanAnnotation = method.getAnnotation(Bean.class);
						if (beanAnnotation != null && method.isAnnotationPresent(annotationType)) {
							// This method defines a bean with the target annotation
							String methodBeanName = method.getName();
							String[] beanNames = beanAnnotation.name();
							if (beanNames != null && beanNames.length > 0 && StringUtils.hasText(beanNames[0])) {
								methodBeanName = beanAnnotation.name()[0];
							}
							ret.add(new ExportBeanDescriptor(methodBeanName,method.getAnnotation(annotationType),method.getReturnType()));
						}
					}
				}
			}
		}
		return ret;
	}
    
//    private GenericApplicationContext getContext(Class<?> contextClass) {
//    	GenericApplicationContext ret = new AnnotationConfigApplicationContext(contextClass);
//    	return ret;
//    }
    
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
