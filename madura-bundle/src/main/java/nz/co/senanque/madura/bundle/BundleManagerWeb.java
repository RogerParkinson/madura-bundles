/*******************************************************************************
 * Copyright (c)2015 Prometheus Consulting
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

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ServletContextAware;

/**
 * This bundle manager loads bundles from WEB-INF/bundles at startup time if there are any.
 * You can have a scanned directory in as well if you want but this extension is intended to
 * allow bundles to live inside a war file which is useful for on-line demos, but rather makes
 * the idea of bundles a bit redundant. But it does simplify setting up a demo.
 * 
 * @author Roger Parkinson
 *
 */
public class BundleManagerWeb extends BundleManagerImpl implements ServletContextAware{

    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
	private ServletContext m_servletContext;


	@PostConstruct
    public void init() {
    	super.init();
    	scanServletContext();
    }
    
    public void scanServletContext()
    {
        m_logger.debug("Scanning files");
        if (m_servletContext == null) {
        	return;
        }
        Set<String> bundles = m_servletContext.getResourcePaths("/WEB-INF/bundles");
        if (bundles != null) {
	        for (String fileName : bundles) {
	        	int i = fileName.lastIndexOf('.');
	        	if (i <= 0) {
	        		continue;
	        	}
	        	BundleManagerDelegate bundleManagerDelegate = getBundleManagerDelegate(fileName);
	        	if (bundleManagerDelegate == null) continue;
	            // This is a new one so add it
	        	try {
	        		InputStream is = m_servletContext.getResourceAsStream(fileName);
					BundleVersion bundleVersion = bundleManagerDelegate.addBundle(is);
					bundleVersion.increment(); // ensures we never delete these bundles.
				} catch (Exception e) {
					m_logger.warn("Failed to open {}",fileName);
				}
	        }
        }
    }
    private BundleManagerDelegate getBundleManagerDelegate(String fileName) {
    	int i = fileName.lastIndexOf('.');
    	if (i <= 0) {
    		return null;
    	}
    	URL url;
		try {
			url = m_servletContext.getResource(fileName);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
    	String extension = fileName.substring(i);
    	i = fileName.lastIndexOf('/');
    	if (i >= -1) {
    		fileName = fileName.substring(i+1);
    	}
    	if (".jar".equals(extension)) {
			return new BundleMangerDelegateJar(this, fileName, url);
		}
		if (".bundle".equals(extension)) {
			return new BundleMangerDelegateMaven(this, fileName, url);
		}
		return null;
	}
	public void setServletContext(ServletContext arg0) {
		m_servletContext = arg0;
	}
}
