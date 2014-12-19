/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.util.Properties;

/**
 * @author Roger Parkinson
 *
 */
abstract class BundleManagerDelegateAbstract implements BundleManagerDelegate {

	protected BundleManagerImpl m_bundleManagerImpl;

	protected BundleManagerDelegateAbstract(BundleManagerImpl bundleManagerImpl) {
		m_bundleManagerImpl = bundleManagerImpl;
	}

	protected void cleanup(ClassLoader classLoader, String className,
			long lastModified, Properties properties) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<BundleRoot> clazz = (Class<BundleRoot>) classLoader
				.loadClass(className);
		BundleRoot root = (BundleRoot) clazz.newInstance();
		root.setDate(lastModified);
		root.init(m_bundleManagerImpl.getBeanFactory(), properties,
				classLoader, m_bundleManagerImpl.m_inheritableBeans);
		String fullBundleName = properties.getProperty("bundle.name")
				.toLowerCase();
		m_bundleManagerImpl.m_bundleMap.addBundle(fullBundleName, root);
		m_bundleManagerImpl.m_defaultBundle = fullBundleName;
		root.setName(fullBundleName);
		for (BundleListener bundleListener : m_bundleManagerImpl.m_bundleListeners) {
			bundleListener.add(fullBundleName, root);

		}
	}

}
