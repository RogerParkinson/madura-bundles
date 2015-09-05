/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import nz.co.senanque.madura.bundle.aether.AetherHelper;
import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author Roger Parkinson
 *
 */
public class BundleMangerDelegateMaven extends BundleManagerDelegateAbstract {

	private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    private final File m_file;
    private final URL m_url;
    private final String m_name;

	protected BundleMangerDelegateMaven(BundleManagerImpl bundleManagerImpl, File file, URL url) {
		super(bundleManagerImpl);
		m_file = file;
		m_url = url;
		m_name = m_file.getName();
	}

    protected BundleMangerDelegateMaven(BundleManagerImpl bundleManagerImpl,String name, URL url) {
		super(bundleManagerImpl);
		m_file = null;
		m_url = url;
		m_name = name;
	}

	@Override
	public BundleVersion addBundle() {
		try {
			return addBundle(m_file.getName(),new FileInputStream(m_file),m_url);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public BundleVersion addBundle(InputStream is) {
		return addBundle(m_name,is,m_url);
	}
	@Override
	public BundleVersion addBundle(String bundleName, InputStream inputStream, URL url1) {
		BundleVersion bundleVersion = null;
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
			properties.setProperty("bundle.file", m_file.getName());
			properties
					.setProperty("Bundle-Name", elements[elements.length - 3]);
			properties.setProperty("Bundle-Version",
					elements[elements.length - 2]);
			bundleVersion = new BundleVersion(
					properties.getProperty("bundle.file"), 
					properties.getProperty("bundle.name"), 
					properties.getProperty("Bundle-Version")
					);
			
			ClassLoader classLoader = createClassLoader(urls, properties, realBundleName, className, 0L, url1);
			Properties p0 = getPropertiesFromJar(urls.get(0));
			p0.putAll(properties);
			cleanup(classLoader, className, p0, bundleVersion);
			m_logger.info("Added bundle: {}", bundleName);
		} catch (Exception e) {
			if (m_logger.isDebugEnabled()) {
				m_logger.error(e.getMessage(), e);
			}
			m_logger.warn("{} {}", bundleName, e.getMessage());
		}
		return bundleVersion;
	}
	
	private Properties getPropertiesFromJar(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		JarInputStream jarInputStream = new JarInputStream(connection.getInputStream());
		Manifest mf = jarInputStream.getManifest();
		Attributes attributes = mf.getMainAttributes();
		Properties properties = getProperties(attributes);
		return properties;
	}

	private ClassLoader createClassLoader(List<URL> urls, Properties properties,
			String bundleName, String className, long lastModified, URL url)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		URL[] urlArray = urls.toArray(new URL[] {});
		ClassLoader cl = this.getClass().getClassLoader();
		ClassLoader classLoader;
		if (m_bundleManagerImpl.isChildFirst()) {
			classLoader = new BundleClassLoader(true, urlArray, null, cl, url);
		} else {
			classLoader = new URLClassLoader(urlArray, cl); // parent first
		}
		return classLoader;
	}

}
