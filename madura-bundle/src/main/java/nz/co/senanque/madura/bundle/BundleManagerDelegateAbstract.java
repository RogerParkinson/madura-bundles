/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.senanque.madura.bundlemap.BundleVersion;

/**
 * @author Roger Parkinson
 *
 */
abstract class BundleManagerDelegateAbstract implements BundleManagerDelegate {

    private final Logger m_logger = LoggerFactory.getLogger(this.getClass());
	protected BundleManagerImpl m_bundleManagerImpl;

	protected BundleManagerDelegateAbstract(BundleManagerImpl bundleManagerImpl) {
		m_bundleManagerImpl = bundleManagerImpl;
	}

	protected void cleanup(ClassLoader classLoader, String className, Properties properties, BundleVersion bundleVersion) 
					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		Class<BundleRoot> clazz = (Class<BundleRoot>) classLoader.loadClass((className==null)?"nz.co.senanque.madura.bundle.BundleRootImpl":className);
		BundleRoot root = (BundleRoot) clazz.newInstance();
		Properties exportedProperties = new Properties();
		Properties p = m_bundleManagerImpl.getExportedProperties();
		for (String s: p.stringPropertyNames()) {
			exportedProperties.put(s, p.get(s));
		}
		for (String s: properties.stringPropertyNames()) {
			exportedProperties.put(s, properties.get(s));
		}
		root.init(m_bundleManagerImpl.getBeanFactory(), exportedProperties,
				classLoader, m_bundleManagerImpl.m_exportedBeans);
		String fullBundleName = properties.getProperty("bundle.name");
		
		m_bundleManagerImpl.m_bundleMap.addBundle(bundleVersion, root);
		m_bundleManagerImpl.m_defaultBundle = bundleVersion;
		root.setName(fullBundleName);
		for (BundleListener bundleListener : m_bundleManagerImpl.m_bundleListeners) {
			bundleListener.add(bundleVersion);
		}
	}
    protected Properties getProperties(Attributes attributes) {
        Properties properties = new Properties();
        for (Map.Entry<Object,Object> a :attributes.entrySet())
        {
            Object key = a.getKey();
            Object value = a.getValue();
            properties.setProperty(key.toString(), value.toString());
            m_logger.debug("Property: {} value {}",key.toString(), value.toString());
        }
        properties.setProperty("bundle.name", 
                figureBundleName(
                        String.valueOf(properties.get("Bundle-Name")),
                        String.valueOf(properties.get("Bundle-Version"))));
        return properties;
    }
    protected String figureBundleName(String bundle, String version)
    {
        if (version != null)
            bundle += "-"+version;
        return bundle;
    }
	
}
