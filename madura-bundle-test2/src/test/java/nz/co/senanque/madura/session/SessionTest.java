/*******************************************************************************
 * Copyright (c)2016 Prometheus Consulting
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

import java.util.Collection;

import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.BundleRoot;
import nz.co.senanque.madura.bundle.TestBean;
import nz.co.senanque.madura.bundle.TestExportBean2;
import nz.co.senanque.madura.bundle.ValueInjectedBean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Roger Parkinson
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public class SessionTest {

	private Logger m_logger = LoggerFactory.getLogger(this.getClass());

	@Autowired WebApplicationContext wac;
	@Autowired MockHttpServletRequest request;
	@Autowired MockHttpSession session;
	@Autowired MySessionBean mySessionBean;
	@Autowired MyRequestBean myRequestBean;
	@Autowired @Qualifier("getTestExportBean2") TestExportBean2 sessionBean;
    @Autowired BundleManager bundleManager;
    @Autowired ApplicationContext applicationContext;

	protected void startRequest() {
		request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
				request));
	}

	protected void endRequest() {
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.requestCompleted();
		RequestContextHolder.resetRequestAttributes();
		request = null;
	}

	protected void startSession() {
		session = new MockHttpSession();
	}

	protected void endSession() {
		session.clearAttributes();
		session = null;
	}

	@Before
	public void constructSession() {
		startRequest();
		startSession();
	}

	@After
	public void sessionClean() {
		endRequest();
		endSession();
	}

	private void displayIds(String source) {
//		m_logger.debug("{} session: {} mySessionBean: {} sessionBean: {}",
//				source,
//				System.identityHashCode(session),
//				System.identityHashCode(mySessionBean),
//				System.identityHashCode(sessionBean));
	}

    @Test
    public void testInit2() {
    	String targetBundle = null;
    	Collection<BundleRoot> bundles = bundleManager.getAvailableBundleRoots();
    	assertEquals(2,bundles.size());
    	BundleRoot[] bundleArray = bundles.toArray(new BundleRoot[2]);

    	targetBundle = bundleArray[0].getName();
    	bundleManager.setBundle(targetBundle);
        testBundleName(bundleManager, targetBundle,targetBundle+" T1", 0);
        
        targetBundle = bundleArray[1].getName();
    	bundleManager.setBundle(targetBundle);
        testBundleName(bundleManager, targetBundle,targetBundle+" T1", 1);
        sessionClean();
        constructSession();

    	targetBundle = bundleArray[0].getName();
    	bundleManager.setBundle(targetBundle);
        testBundleName(bundleManager, targetBundle,targetBundle+" T2", 0);
        
        targetBundle = bundleArray[1].getName();
    	bundleManager.setBundle(targetBundle);
        testBundleName(bundleManager, targetBundle,targetBundle+" T2", 1);
    }
    private void testBundleName(BundleManager bm, String bundleName, String source, int expected) {
    	displayIds(source);
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
        int i = tb2.getCounter();
        assertEquals(expected,i);
//        m_logger.debug("{} counter {} TestExportBean2 {}",source, i, System.identityHashCode(tb2));
    }
}
