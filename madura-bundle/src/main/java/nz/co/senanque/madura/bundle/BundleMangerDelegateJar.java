/**
 * 
 */
package nz.co.senanque.madura.bundle;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author Roger Parkinson
 *
 */
public class BundleMangerDelegateJar extends BundleManagerDelegateAbstract {

    private Logger m_logger = LoggerFactory.getLogger(this.getClass());

    protected BundleMangerDelegateJar(BundleManagerImpl bundleManagerImpl) {
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
			JarInputStream jarInputStream = new JarInputStream(inputStream);
			Manifest mf = jarInputStream.getManifest();
			Attributes attributes = mf.getMainAttributes();
			Properties properties = getProperties(attributes);
			properties.setProperty("bundle.file",
					properties.getProperty("bundle.name"));
			String className = attributes.getValue("Bundle-Activator");
            String classPath = attributes.getValue("Class-Path");
			List<URL> urls = new ArrayList<URL>();
            if (!StringUtils.isEmpty(classPath)) {
            	addJarsToClasspath(classPath,urls);
            }
			createClassLoader(urls, properties, bundleName, className, 0L,
					new JarInputStream[] { jarInputStream });
			m_logger.info("Added bundle: {}", bundleName);
		} catch (Exception e) {
			if (m_logger.isDebugEnabled()) {
				m_logger.error(e.getMessage(), e);
			}
			m_logger.warn("{} {}", bundleName, e.getMessage());
		}
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
	private void createClassLoader(List<URL> urls, Properties properties,
			String bundleName, String className, long lastModified,
			JarInputStream[] jarClasspath) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		URL[] urlArray = urls.toArray(new URL[] {});
		ClassLoader cl = this.getClass().getClassLoader();
		ClassLoader classLoader;
		if (m_bundleManagerImpl.isChildFirst()) {
			classLoader = new BundleClassLoader(true, urlArray, jarClasspath,
					cl);
		} else {
			classLoader = new URLClassLoader(urlArray, cl); // parent first
		}
		cleanup(classLoader, className, lastModified, properties);
	}

}
