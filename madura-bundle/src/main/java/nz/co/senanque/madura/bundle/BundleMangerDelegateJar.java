/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author Roger Parkinson
 *
 */
public class BundleMangerDelegateJar extends BundleManagerDelegateAbstract {

    private final Logger m_logger = LoggerFactory.getLogger(this.getClass());
    private final File m_file;
    private final URL m_url;
    private final String m_name;

    protected BundleMangerDelegateJar(BundleManagerImpl bundleManagerImpl, File file, URL url) {
		super(bundleManagerImpl);
		m_file = file;
		m_url = url;
		m_name = m_file.getName();
	}

    protected BundleMangerDelegateJar(BundleManagerImpl bundleManagerImpl,String name, URL url) {
		super(bundleManagerImpl);
		m_file = null;
		m_url = url;
		m_name = name;
	}

	@Override
	public BundleVersion addBundle() {
		try {
			return addBundle(m_name,new FileInputStream(m_file),m_url);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BundleVersion addBundle(InputStream is) {
		return addBundle(m_name,is,m_url);
	}

	@Override
	public BundleVersion addBundle(String bundleName, InputStream inputStream, URL url) {
		BundleVersion bundleVersion = null;
		try {
			JarInputStream jarInputStream = new JarInputStream(inputStream);
			Manifest mf = jarInputStream.getManifest();
			Attributes attributes = mf.getMainAttributes();
			Properties properties = getProperties(attributes);
			properties.setProperty("bundle.file",bundleName);
			properties.setProperty("bundle.name",StringUtils.stripFilenameExtension(bundleName));
			String className = attributes.getValue("Bundle-Activator");
            String classPath = attributes.getValue("Class-Path");
			List<URL> urls = new ArrayList<URL>();
            if (!StringUtils.isEmpty(classPath)) {
            	addJarsToClasspath(classPath,urls);
            }
    		bundleVersion = new BundleVersion(
    				properties.getProperty("bundle.file"), 
    				properties.getProperty("Bundle-Name"), 
    				properties.getProperty("Bundle-Version")
    				);
    		ClassLoader classLoader = createClassLoader(urls, properties, bundleName, className, 0L,
					new JarInputStream[] { jarInputStream }, url);
			cleanup(classLoader, className, properties, bundleVersion);
			m_logger.info("Added bundle: {}", bundleName);
		} catch (Exception e) {
			if (m_logger.isDebugEnabled()) {
				m_logger.error(e.getMessage(), e);
			}
			m_logger.warn("{} {}", bundleName, e.getMessage());
		}
		return bundleVersion;
	}
	
	private void addJarsToClasspath(String classPath, List<URL> urls) throws MalformedURLException {
        if (classPath != null && classPath.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(classPath," ");
            while (st.hasMoreTokens())
            {
                String pathElement = m_bundleManagerImpl.getDirectory()+File.separator+st.nextToken();
                File f = new File(pathElement);
                if (!f.canRead())
                {
                    throw new RuntimeException("Cannot read file: "+pathElement);
                }
                urls.add(f.toURI().toURL());
            }
        }
	}

	private ClassLoader createClassLoader(List<URL> urls, Properties properties,
			String bundleName, String className, long lastModified,
			JarInputStream[] jarClasspath, URL url) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		URL[] urlArray = urls.toArray(new URL[] {});
		ClassLoader cl = this.getClass().getClassLoader();
		ClassLoader classLoader;
		if (m_bundleManagerImpl.isChildFirst()) {
			classLoader = new BundleClassLoader(true, urlArray, jarClasspath, cl, url);
		} else {
			classLoader = new URLClassLoader(urlArray, cl); // parent first
		}
		return classLoader;
	}

}
