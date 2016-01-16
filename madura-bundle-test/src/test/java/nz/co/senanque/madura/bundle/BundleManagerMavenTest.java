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
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import nz.co.senanque.madura.testbeans.TestBean;

import org.junit.Test;
import org.junit.runner.RunWith;
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
@ContextConfiguration(locations={"/BundleManagerMaven-bundle.xml"})
public class BundleManagerMavenTest {

//    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    @Autowired ApplicationContext applicationContext;
    @Autowired BundleManager bundleManager;
    @Autowired Properties b;
    
    @Test
    public void testInit()
    {
    	String currentVersion = b.getProperty("current.version");
    	BundleManager bm = (BundleManager)this.applicationContext.getBean("bundleManager");
        bm.setBundle("madura-bundle-maven-"+currentVersion);
        testBundleName(bm, "madura-bundle-maven-"+currentVersion);
    }
    private void testBundleName(BundleManager bm, String bundleName)
    {
        StringWrapper n = (StringWrapper)this.applicationContext.getBean("bundleName");
        assertTrue(n.toString().equals(bundleName));
        TestBean tb = (TestBean)this.applicationContext.getBean("TestBean");
        StringWrapper sw = tb.getContent();
        assertTrue(tb.getContent().toString().equals(bundleName));
        assertEquals("this is a test export",tb.getSampleExport().toString());
        Resource resource = tb.getResource();
        assertNotNull(resource);
    }
}
