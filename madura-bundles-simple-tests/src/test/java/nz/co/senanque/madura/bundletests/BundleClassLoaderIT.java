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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarInputStream;

import nz.co.senanque.madura.bundle.BundleClassLoader;
import nz.co.senanque.madura.bundle.BundleRoot;

import org.junit.Test;

/**
 * @author Roger Parkinson
 *
 */
public class BundleClassLoaderIT {

	@Test
	public void test() throws Exception {
		JarInputStream is = new JarInputStream(new FileInputStream("target/bundles/bundle-1.0.jar"));
		File file = new File("target/bundles/bundle-1.0.jar");
		URL url1 = new URL("file://"+file.getAbsolutePath());
        ClassLoader cl = this.getClass().getClassLoader();
        BundleClassLoader classLoader = new BundleClassLoader(true, new URL[]{}, new JarInputStream[]{is}, cl, url1);
        is.close();
        @SuppressWarnings("unchecked")
		Class<BundleRoot> clazz = (Class<BundleRoot>)classLoader.loadClass("nz.co.senanque.madura.bundle.BundleRootImpl");
        ClassLoader actual = clazz.getClassLoader();
        assertEquals(actual,classLoader);
        @SuppressWarnings("unused")
		BundleRoot root = (BundleRoot)clazz.newInstance();
        URL url = classLoader.getResource("BundleResource.txt");
        assertNotNull(url);
        InputStream is2 = url.openStream();
        is2.close();
        InputStream is1 = classLoader.getResourceAsStream("BundleResource.txt");
        byte[] bytes = new byte[10]; // this size trims the last \n byte.
        @SuppressWarnings("unused")
		int i = is1.read(bytes);
        String bundleResource = new String(bytes);
        assertEquals("bundle-1.0",bundleResource);
        Enumeration<URL> e = classLoader.getResources("BundleResource.txt");
        while (e.hasMoreElements())
        {
        	url = e.nextElement();
        	url.toString();
        }
		URL url4 = classLoader.getResource("mistake.txt");
		assertNull(url4);
		InputStream iis = classLoader.getResourceAsStream("nz/co/senanque/madura/bundle/BundleRootImpl.class");
		assertNotNull(iis);
	}

}
