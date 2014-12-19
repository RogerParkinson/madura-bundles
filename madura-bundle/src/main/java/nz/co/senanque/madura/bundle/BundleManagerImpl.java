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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

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
 * Bundles may be loaded at startup time from the WEB-INF/bundles dir if there are any, and you can combine the two, or
 * omit wiring the directory in and just get the ones from WEB-INF/bundles, or vv.
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.5 $
 */
public class BundleManagerImpl extends AbstractBundleManager
{
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    private String m_directory;
    private long m_time = -1; // optional scan timer
	private ServletContext m_servletContext;
    
    @Override
    public void init() {
    	super.init();
        scanServletContext();
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
    public void scanServletContext()
    {
        m_logger.debug("Scanning files");
        if (m_servletContext == null) {
        	return;
        }
        Set<String> bundles = m_servletContext.getResourcePaths("/WEB-INF/bundles");
        for (String fileName : bundles) {
        	int i = fileName.lastIndexOf('.');
        	if (i <= 0) {
        		continue;
        	}
        	BundleManagerDelegate bundleManagerDelegate = getBundleManagerDelegate(fileName.substring(i));
        	if (bundleManagerDelegate != null) {
            	int j = fileName.lastIndexOf('/');
            	int j1 = fileName.lastIndexOf('-');
            	String bundleName = fileName.substring(j+1,j1);
            	InputStream is = m_servletContext.getResourceAsStream(fileName);
            	bundleManagerDelegate.addBundle(bundleName,is);
            	try {
    				is.close();
    			} catch (IOException e) {
    				m_logger.warn("{} {}",fileName,e.getMessage());
    			}
            	continue;
        	}
        }
    }
    private BundleManagerDelegate getBundleManagerDelegate(String extension) {
		if (".jar".equals(extension)) {
			return new BundleMangerDelegateJar(this);
		}
		if (".bundle".equals(extension)) {
			return new BundleMangerDelegateMaven(this);
		}
		return null;
	}
    public synchronized void scan()
    {
        m_logger.debug("Scanning files");
        if (m_directory == null) {
        	return;
        }
        File dir = new File(m_directory);
        File[] files = dir.listFiles(new FilenameFilter(){

            public boolean accept(File arg0, String arg1)
            {
                if (arg1.toUpperCase().endsWith("JAR")) return true;
                if (arg1.toUpperCase().endsWith("BUNDLE")) return true;
                return false;
            }});
        if (files==null)
        {
            throw new RuntimeException("Could not access directory: "+m_directory);
        }
        
        // Put all the names in the list of delete candidates
        // we will remove the ones we want to keep
        List<String> deleteCandidates = new ArrayList<String>();
        for (BundleVersion bv: m_bundleMap.getAvailableBundles()) {
        	deleteCandidates.add(bv.getFullVersion());
        }
        
        m_lock = Thread.currentThread();
        // Scan all the files in the directory
        for (File file:files)
        {
        	String fileName = file.getName().toLowerCase();
        	int i = fileName.lastIndexOf('.');
        	if (i <= 0) {
        		continue;
        	}
        	BundleManagerDelegate bundleManagerDelegate = getBundleManagerDelegate(fileName.substring(i));
        	if (bundleManagerDelegate == null) continue;

            String bundleName = fileName.substring(0,i);
            BundleRoot root = m_bundleMap.getBundleRoot(bundleName);
            long lastModified = file.lastModified();
            if (root == null)
            {
                // This is a new one so add it
            	try {
					bundleManagerDelegate.addBundle(bundleName, new FileInputStream(file));
				} catch (FileNotFoundException e) {
					m_logger.warn("Failed to open {}",file.getAbsolutePath());
				}
            }
            else
            {
                // Make sure we don't delete (shutdown) this
                deleteCandidates.remove(bundleName);
                if (root.getDate() != lastModified)
                {
                    m_bundleMap.deleteBundle(bundleName);
                    try {
						bundleManagerDelegate.addBundle(bundleName, new FileInputStream(file));
					} catch (FileNotFoundException e) {
						m_logger.warn("Failed to open {}",file.getAbsolutePath());
					}
                }
            }
        }
        for (String bundleName:deleteCandidates)
        {
        	m_bundleMap.deleteBundle(bundleName);
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
	public void setServletContext(ServletContext arg0) {
		m_servletContext = arg0;
	}
}
