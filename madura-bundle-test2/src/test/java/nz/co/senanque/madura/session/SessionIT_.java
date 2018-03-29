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

import java.lang.reflect.Method;
import java.util.Collection;

import nz.co.senanque.madura.bundle.BundleInterface;
import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.BundleRoot;
import nz.co.senanque.madura.bundle.StringWrapperImpl;
import nz.co.senanque.madura.bundle.spring.DumpBeanFactory;
import nz.co.senanque.madura.testbeans.TestBean;
import nz.co.senanque.madura.testbeans.TestExportBean2;
import nz.co.senanque.madura.testbeans.ValueInjectedBean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Tests multiple sessions and multiple bundles using
 * annotations to configure.
 * Beans can be
 * <p><ol>
 * <li>singleton defined in ap</li>
 * <li>singleton defined in ap exported to bundle</li>
 * <li>singleton defined in bundle</li>
 * <li>session defined in ap</li>
 * <li>session defined in bundle</li>
 * <li>singleton defined in ap</li>
 * <li>session defined in ap exported to bundle</li>
 * </ol>
 * <p>
 * The 1st and 4th are ordinary Spring beans so we don't test those here.
 * Other tests are marked in the comments using numbers eg {@literal #}2
 * 
 * @author Roger Parkinson
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
public class SessionIT_ {

	private Logger m_logger = LoggerFactory.getLogger(this.getClass());

	@Autowired WebApplicationContext wac;
	@Autowired MockHttpServletRequest request;
	@Autowired MockHttpSession session;
	@Autowired MySessionBean mySessionBean;
	@Autowired MyRequestBean myRequestBean;
//	@Autowired @Qualifier("getTestExportBean2") TestExportBean2 sessionBean;
    @Autowired BundleManager bundleManager;
    @Autowired ApplicationContext applicationContext;
	@Autowired Environment env;

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
	/**
	 * This just checks that the beans are being loaded by the classloader we expect.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReflection() throws Exception {
    	String targetBundle = null;
    	Collection<BundleRoot> bundles = bundleManager.getAvailableBundleRoots();
    	assertEquals(2,bundles.size());
    	BundleRoot[] bundleArray = bundles.toArray(new BundleRoot[2]);
    	targetBundle = bundleArray[0].getName();
    	bundleManager.setBundle(targetBundle);
    	ClassLoader classLoader = bundleArray[0].getBundleClassLoader();
		ClassLoader mainCL = TestBean.class.getClassLoader();

		Class<?> TestBeanClassImpl = classLoader.loadClass("nz.co.senanque.madura.testbeans.TestBeanImpl");
		@SuppressWarnings("unused")
		Class<?> TestBeanClass = classLoader.loadClass("nz.co.senanque.madura.testbeans.TestBean");
    	@SuppressWarnings("unused")
		Class<?> TestBeanClassMain = mainCL.loadClass("nz.co.senanque.madura.testbeans.TestBean");
    	ClassLoader foundCL = TestBeanClass.getClassLoader();
    	ClassLoader foundCLMain = TestBeanClassMain.getClassLoader();
    	
		Method method = TestBean.class.getMethod("getContent", new Class<?>[]{});
		TestBean tb = (TestBean) TestBeanClassImpl.newInstance();
		tb.setContent(new StringWrapperImpl("abc"));
		Object o = method.invoke(tb, null);
		assertEquals("abc",o.toString());
	}
	
    /**
     * Test scenarios use two bundles and two sessions.
     * Pick the first bundle, test, then the second bundle, test
     * Then create a new session and do the same tests/bundles
     * The session beans should increment in the first two tests ten, when
     * we change session they should start from 0 again.
     * The exception is the session bean defined in a bundle (test {@literal #}5)
     * which is always 0 because we don't revisit any session/bundle combination
     */
    @Test
    public void testInit2() {
    	String targetBundle = null;
    	DumpBeanFactory.dumpBeans((GenericWebApplicationContext)applicationContext,"main");
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
    	
    	// check we have the right bundle
        BundleRoot n = (BundleRoot)this.applicationContext.getBean("bundleRoot");
        assertEquals(bundleName,n.getName());

        // #3 Singleton defined in bundle
        // Bean is @Bean
        TestBean tb = (TestBean)this.applicationContext.getBean("TestBean");
        Object o = tb.getContent();
        assertEquals("TestBean",o.toString());
        
        // #2 singleton defined in app and exported to bundle. 
        // Bean is @Component, interface is @BundleInterface
        // and bundle injects it into TestBean
        o = tb.getSampleExport();
        assertEquals("this is a test export",o.toString());
        
        Resource resource = tb.getResource();
        assertNotNull(resource);
        
        // #3 singleton defined in bundle, bean is @Component, interface is annotated and value is injected
        ValueInjectedBean valueInjectedBean = this.applicationContext.getBean(ValueInjectedBean.class);
        String value = valueInjectedBean.getValue();
        assertEquals("value from configb.properties",value);
        
        Object s = tb.getSampleExport();
        assertNotNull(s);

        // #6 session defined in ap exported to bundle
        // Bean is @Bean @BundleExport and scoped session
        // Bundle injects it into TestBean and we read the counter
        TestExportBean2 tb2 = tb.getExportBean2();
        assertNotNull(tb2);
        int i = tb2.getCounter();
        assertEquals(expected,i);
        
//        m_logger.debug("{} counter {} TestExportBean2 {}",source, i, System.identityHashCode(tb2));
        // #5 session defined in bundle
        TestExportBean2 bb = (TestExportBean2)n.getApplicationContext().getBean("TestBundleBean");
        i = bb.getCounter();
        assertEquals(0,i);
//        m_logger.debug("{} counter {} TestExportBean2 {}",source, i, System.identityHashCode(bb));
    }
}
