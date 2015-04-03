/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.util.Properties;

import nz.co.senanque.madura.bundlemap.BundleVersion;

/**
 * @author Roger Parkinson
 *
 */
abstract class BundleManagerDelegateAbstract implements BundleManagerDelegate {

	protected BundleManagerImpl m_bundleManagerImpl;

	protected BundleManagerDelegateAbstract(BundleManagerImpl bundleManagerImpl) {
		m_bundleManagerImpl = bundleManagerImpl;
	}

	protected void cleanup(ClassLoader classLoader, String className, Properties properties, BundleVersion bundleVersion) 
					throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<BundleRoot> clazz = (Class<BundleRoot>) classLoader.loadClass(className);
		BundleRoot root = (BundleRoot) clazz.newInstance();
		root.init(m_bundleManagerImpl.getBeanFactory(), properties,
				classLoader, m_bundleManagerImpl.m_inheritableBeans);
		String fullBundleName = properties.getProperty("bundle.name")
				.toLowerCase();
		
		m_bundleManagerImpl.m_bundleMap.addBundle(bundleVersion, root);
		m_bundleManagerImpl.m_defaultBundle = bundleVersion;
		root.setName(fullBundleName);
		for (BundleListener bundleListener : m_bundleManagerImpl.m_bundleListeners) {
			bundleListener.add(bundleVersion.getFullVersion(), root);
		}
	}

}
