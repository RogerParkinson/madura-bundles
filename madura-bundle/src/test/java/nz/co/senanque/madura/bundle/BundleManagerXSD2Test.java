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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"BundleManagerTest-spring.xml"})
public class BundleManagerXSD2Test
{

    @Autowired ApplicationContext applicationContext;

    @Test
    public void testInit()
    {
        BundleManagerImpl bm = (BundleManagerImpl)this.applicationContext.getBean("bundleManager");
        bm.setBundle("bundle","2.0");
        testBundleName(bm, "bundle-2.0");
    }
    private void testBundleName(BundleManagerImpl bm, String bundleName)
    {
        StringWrapper n = (StringWrapper)this.applicationContext.getBean("bundleName");
        assertTrue(n.toString().equals(bundleName));
        TestBean tb = (TestBean)this.applicationContext.getBean("TestBean");
        assertTrue(tb.getContent().toString().equals(bundleName));
        assertTrue(tb.getSampleParent().toString().equals("example of a parent bean"));
        TestBean tb1 = (TestBean)this.applicationContext.getBean("TestBean1");
        assertTrue(tb1.getContent().toString().equals(bundleName));
    }

}
