/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nz.co.senanque.madura.bundle.aether.AetherHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author Roger Parkinson
 *
 */
public class BundleMangerDelegateMaven extends BundleManagerDelegateAbstract {

	private Logger m_logger = LoggerFactory.getLogger(this.getClass());

	protected BundleMangerDelegateMaven(BundleManagerImpl bundleManagerImpl) {
		super(bundleManagerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nz.co.senanque.madura.bundle.BundleManagerDelegate#addBundle(java.lang
	 * .String, java.io.InputStream)
	 */
	@Override
	public void addBundle(String bundleName, InputStream inputStream) {
		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			String className = properties.getProperty("Bundle-Activator",
					"nz.co.senanque.madura.bundle.BundleRootImpl");
			String artifact = properties.getProperty("Bundle-Artifact");
			List<URL> urls = AetherHelper.extractURLClassPath(artifact);
			String url = urls.get(0).toString();
			String[] elements = StringUtils.tokenizeToStringArray(url, "/");
			String realBundleName = StringUtils.delete(
					elements[elements.length - 1], ".jar");
			properties.setProperty("bundle.name", realBundleName);
			properties.setProperty("bundle.file", realBundleName);
			properties
					.setProperty("Bundle-Name", elements[elements.length - 3]);
			properties.setProperty("Bundle-Version",
					elements[elements.length - 2]);
			createClassLoader(urls, properties, realBundleName, className, 0L);
			m_logger.info("Added bundle: {}", bundleName);
		} catch (Exception e) {
			if (m_logger.isDebugEnabled()) {
				m_logger.error(e.getMessage(), e);
			}
			m_logger.warn("{} {}", bundleName, e.getMessage());
		}
	}

	private void createClassLoader(List<URL> urls, Properties properties,
			String bundleName, String className, long lastModified)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		URL[] urlArray = urls.toArray(new URL[] {});
		ClassLoader cl = this.getClass().getClassLoader();
		ClassLoader classLoader;
		if (m_bundleManagerImpl.isChildFirst()) {
			classLoader = new BundleClassLoader(true, urlArray, null, cl);
		} else {
			classLoader = new URLClassLoader(urlArray, cl); // parent first
		}
		cleanup(classLoader, className, lastModified, properties);
	}

}
