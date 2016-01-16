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
package nz.co.senanque.madura.bundletests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.BundleRoot;
import nz.co.senanque.madura.bundle.StringWrapper;
import nz.co.senanque.madura.bundle0.TestBean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"BundleManagerTest-bundle.xml"})
public class BundleManagerTest
{
    
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    @Autowired ApplicationContext applicationContext;
    @Autowired BundleManager bundleManager;
    @Autowired BundleRoot bundleRoot;

    @Before
    public void init() {
//    	s_bundleManager = bundleManager;
    }
	@After
	public void destroy() {
		bundleManager.shutdown();
	}
    @Test
    public void testInit()
    {
        BundleManager bm = getBundleManager();
        bm.setBundle("bundle","1.0");
        testBundleName(bm, "bundle-1.0");
        testBundleFile(bm, "bundle-1.0");
        String bundleName = bundleRoot.getName();
        Object o=null;
		try {
			o = bm.getBundle().getApplicationContext().getBean("JDOMFactory");
		} catch (BeansException e) {
			o=null;
		}
        assertNull(o);
        bm.setBundle("bundle","2.0");
        testBundleName(bm, "bundle-2.0");
        for (BundleRoot br:bm.getAvailableBundleRoots())
        {
            for (Map.Entry<Object,Object> p: br.getProperties().entrySet())
            {
                m_logger.debug("property {} {}",p.getKey(),p.getValue());
            }
        }
        Map<?,BundleRoot> beans = bm.getBeansOfType(TestBean.class);
        assertEquals(3,beans.size());
        o = bm.getBundle().getApplicationContext().getBean("JDOMFactory");
        o.toString();
    }
    private void testBundleName(BundleManager bm, String bundleName)
    {
        StringWrapper n = (StringWrapper)this.applicationContext.getBean("bundleName");
        assertTrue(n.toString().equals(bundleName));
        TestBean tb = (TestBean)this.applicationContext.getBean("TestBean");
        assertTrue(tb.getContent().toString().equals(bundleName));
        try
        {
        	Resource resource = tb.getResource();
            InputStream is = resource.getInputStream();
            assertNotNull(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            assertEquals(bundleName,line);
            br.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        TestBean tb1 = (TestBean)this.applicationContext.getBean("TestBean1");
        assertTrue(tb1.getContent().toString().equals(bundleName));
    }
    private void testBundleFile(BundleManager bm, String bundleFile)
    {
        StringWrapper n = (StringWrapper)this.applicationContext.getBean("bundleFile");
    }
    public BundleManager getBundleManager()
    {
        return bundleManager;
    }
    public void setBundleManager(BundleManager bundleManager)
    {
        this.bundleManager = bundleManager;
    }
}
