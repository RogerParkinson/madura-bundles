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

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import nz.co.senanque.madura.bundle.spring.BundleScope;
import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * This class manages multiple bundles. Each bundle is a jar file loaded under a different classloader.
 * The different bundles each contain a BundleRoot, which is all we know about them apart from their file name.
 * There is an injected directory that is scanned on startup and at other times for jar files.
 * Any new bundles found are loaded. Any that were found earlier and are gone are shut down.
 * When the manager is shut down all bundles are shut down as well.
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.5 $
 */
public class BundleManagerImpl extends AbstractBundleManager
{
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    private String m_directory;
    private long m_time = -1; // optional scan timer
    
    @Override
    public void init() {
    	super.init();
        if (getTime() != -1)
        {
            // we have a timer so launch it now
            TimerTask t = new TimerTask(){

                @Override
                public void run() {
                    scan();
                    
                }};
            Timer timer = new Timer();
            timer.schedule(t,m_time,m_time);
        }
        else
        {
            scan();
        }
    }
    private BundleManagerDelegate getBundleManagerDelegate(File file) {
    	String fileName = file.getName().toLowerCase();
    	int i = fileName.lastIndexOf('.');
    	if (i <= 0) {
    		return null;
    	}
        URL url;
		try {
			url = new URL("file://"+file.getAbsolutePath());
		} catch (MalformedURLException e1) {
			m_logger.warn("Failed to get a URL for {}",file.getAbsolutePath());
			return null;
		}
    	String extension = fileName.substring(i);
    	if (".jar".equals(extension)) {
			return new BundleMangerDelegateJar(this, file, url);
		}
		if (".bundle".equals(extension)) {
			return new BundleMangerDelegateMaven(this, file, url);
		}
		return null;
	}
    public synchronized void scan()
    {
        m_logger.debug("Scanning files");
        if (m_directory == null) {
        	return;
        }
        
        // Find all the relevant files that are not already loaded.
        File dir = new File(m_directory);
        File[] files = dir.listFiles(new FilenameFilter(){

            public boolean accept(File arg0, String arg1)
            {
            	// extension is 'bundle' or 'jar'
                if (arg1.toUpperCase().endsWith("JAR") || arg1.toUpperCase().endsWith("BUNDLE")) {
                	// is it already loaded?
                	if (m_bundleMap.findById(arg1) == null) {
                		return true;
                	}
                }
                return false;
            }});
        if (files==null)
        {
            throw new RuntimeException("Could not access directory: "+m_directory);
        }
        File[] allFiles = dir.listFiles(new FilenameFilter(){

            public boolean accept(File arg0, String arg1)
            {
            	// extension is 'bundle' or 'jar'
                if (arg1.toUpperCase().endsWith("JAR") || arg1.toUpperCase().endsWith("BUNDLE")) {
                		return true;
                	}
                return false;
            }});
        
        m_lock = Thread.currentThread();
        // Scan all the files in the directory
        for (File file:files)
        {
        	BundleManagerDelegate bundleManagerDelegate = getBundleManagerDelegate(file);
        	if (bundleManagerDelegate == null) continue;

            // This is a new one so add it
        	try {
				bundleManagerDelegate.addBundle();
			} catch (Exception e) {
				m_logger.warn("Failed to open {}",file.getAbsolutePath());
			}
        }
        for (BundleVersion bundleVersion: m_bundleMap.getAvailableBundles()) {
        	if (bundleVersion.isInUse()) {
        		continue;
        	}
        	boolean found = false;
        	for (File file: allFiles) {
        		String fileName = file.getName();
        		if (bundleVersion.getId().equals(fileName)) {
        			found = true;
        			break;
        		}
        	}
        	if (!found) {
        		// delete the bundle
        		m_bundleMap.shutdown(bundleVersion);
        		for (BundleListener bundleListener : m_bundleListeners) {
        			bundleListener.remove(bundleVersion);
        		}
         		m_logger.info("Shutdown bundle: {}", bundleVersion.getId());
        	}
        }
        m_lock = null;
    }
    
    public void shutdown()
    {
    	for (BundleRoot bundleRoot :m_bundleMap.getAvailableBundleRoots()) {
    		AbstractApplicationContext context = (AbstractApplicationContext)bundleRoot.getApplicationContext();
    		context.close();
    	}
    	m_bundleMap.clear();
    }
    public String getDirectory()
    {
        return m_directory;
    }
    public void setDirectory(String directory)
    {
        m_directory = directory;
    }
    public long getTime()
    {
        return m_time;
    }
    public void setTime(long time)
    {
        m_time = time;
    }
}
