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
package nz.co.senanque.madura.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.BundleRoot;
import nz.co.senanque.madura.bundle.TestBean;
import nz.co.senanque.madura.bundle.TestExportBean2;
import nz.co.senanque.madura.bundle.ValueInjectedBean;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;

/**
 * @author Roger Parkinson
 *
 */
public class SessionBundleTest {

    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    @Autowired AnnotationConfigApplicationContext applicationContext;
    @Autowired BundleManager bundleManager;
    @Autowired Properties b;
    
    @Test
    public void testInit()
    {
    	applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    	bundleManager = (BundleManager)this.applicationContext.getBean("bundleManager");
    	String targetBundle = null;
    	for (BundleRoot br: bundleManager.getAvailableBundleRoots()) {
    		targetBundle = br.getName();
    	}
    	assertNotNull(targetBundle);
    	bundleManager.setBundle(targetBundle);
        testBundleName(bundleManager, targetBundle);
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
        ValueInjectedBean valueInjectedBean = this.applicationContext.getBean(ValueInjectedBean.class);
        String value = valueInjectedBean.getValue();
        assertEquals("value from configb.properties",value);
        Object s = tb.getSampleExport();
        assertNotNull(s);
        TestExportBean2 tb2 = tb.getExportBean2();
        assertNotNull(tb2);
    }
}
