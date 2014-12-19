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
package nz.co.senanque.madura.bundlemap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import nz.co.senanque.madura.bundle.BundleRoot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/nz/co/senanque/madura/bundlemap/BundleMapTest-bundle.xml"})
public class BundleMapTest {
	
	@Autowired private BundleMap m_bundleMap;

	@Test
	public void testSelectBestBundleString() {
		boolean exception = false;
		Collection<BundleRoot> roots = m_bundleMap.getAvailableBundleRoots();
		assertEquals(6,roots.size());
		Collection<BundleVersion> bundleVersions = m_bundleMap.getAvailableBundles();
		assertEquals(6,bundleVersions.size());
		BundleVersion bv = m_bundleMap.getBundleVersion("bundle1");
		assertEquals("bundle1",bv.getName());
		assertNull(bv.getVersion());
		bv = m_bundleMap.getBundleVersion("bundle1-1.0.0");
		assertEquals("1.0.0",bv.getVersion());
		bv = m_bundleMap.getBundleVersion("bundle1-2.0.0");
		assertEquals("2.0.0",bv.getVersion());
		
		bv = m_bundleMap.selectBestBundle("bundle1");
		assertEquals("2.1.0",bv.getVersion());
		bv = m_bundleMap.selectBestBundle("bundle1-1.0.0");
		assertEquals("1.0.0",bv.getVersion());
		bv = m_bundleMap.selectBestBundle("bundle1-2.0.0");
		assertEquals("2.1.0",bv.getVersion());

		m_bundleMap.deleteBundle("bundle1-1.0.0");
		try {
			bv = m_bundleMap.findBundleVersion("bundle1-1.0.0");
		} catch (Exception e) {
			exception = true;
		}
		assertFalse(exception);
		assertTrue(bv.getRoot().isShutdown());
		bv = m_bundleMap.selectBestBundle("bundle1-1.0.0");
		assertEquals("1.1.0",bv.getVersion());
		BundleRoot root = m_bundleMap.getBundleRoot("bundle1-1.1.0");
		assertNotNull(root);
	}

}
