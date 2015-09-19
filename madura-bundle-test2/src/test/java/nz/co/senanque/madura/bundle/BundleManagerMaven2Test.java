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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Roger Parkinson
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/BundleManagerMaven2-bundle.xml"})
public class BundleManagerMaven2Test {

    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    @Autowired ApplicationContext applicationContext;
    @Autowired BundleManager bundleManager;
    @Autowired Properties b;
    
    @Test
    public void testInit()
    {
    	BundleManager bm = (BundleManager)this.applicationContext.getBean("bundleManager");
    	String targetBundle = null;
    	for (BundleRoot br: bm.getAvailableBundleRoots()) {
    		targetBundle = br.getName();
    	}
    	assertNotNull(targetBundle);
        bm.setBundle(targetBundle);
        testBundleName(bm, targetBundle);
    }
    private void testBundleName(BundleManager bm, String bundleName)
    {
        BundleRoot n = (BundleRoot)this.applicationContext.getBean("bundleRoot");
        assertEquals(bundleName,n.getName());
        TestBean tb = (TestBean)this.applicationContext.getBean("TestBean");
        assertEquals("TestBean",tb.getContent().toString());
        assertEquals("this is a test export",tb.getSampleExport().toString());
        Resource resource = tb.getResource();
        assertNotNull(resource);
    }
}
