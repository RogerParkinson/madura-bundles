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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import nz.co.senanque.madura.bundle.BundleInterface;
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
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"BundleManager2Test-bundle.xml"})
public class BundleManager2Test
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
    	Object bean = applicationContext.getBean("TestBean");
        BundleManager bm = getBundleManager();
        bm.setBundle("bundle","3.0");
        testBundleName(bm, "bundle-3.0");
        testBundleFile(bm, "bundle-3.0");
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
        assertEquals(bundleName,bundleRoot.getName());
        TestBean tb = (TestBean)this.applicationContext.getBean("TestBean");
        assertEquals(bundleName,tb.getContent().toString());
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
        assertEquals(bundleName,tb1.getContent().toString());
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
    private Resource[] scanPackageForBundleInterface(String basePackage) throws ClassNotFoundException {
    	
    	ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				true) {
			private String iface = BundleInterface.class.getCanonicalName();
			/**
			 * Check if the class has the right annotation
			 * @param metadataReader the ASM ClassReader for the class
			 * @return whether the class qualifies as a candidate component
			 */
			protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
				AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
				for (String n : metadata.getInterfaceNames()) {
					if (iface.equals(n)) {
						return true;
					}
				}
				return false;
			}

			/**
			 * Determine whether the given bean definition qualifies as candidate.
			 * <p>The default implementation checks whether the class is concrete
			 * (i.e. not abstract and not an interface). Can be overridden in subclasses.
			 * @param beanDefinition the bean definition to check
			 * @return whether the bean definition qualifies as a candidate component
			 */
			protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
				return (beanDefinition.getMetadata().isConcrete() && beanDefinition.getMetadata().isIndependent());
			}
		};
		String r = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage+ "/**/*.class";
		Set<BeanDefinition> l = provider.findCandidateComponents(basePackage);
		Object o = provider.getResourceLoader();
		PathMatchingResourcePatternResolver resolver = (PathMatchingResourcePatternResolver)o;
		Resource[] resources;
		try {
			resources = resolver.getResources(basePackage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return resources;
    }
}
