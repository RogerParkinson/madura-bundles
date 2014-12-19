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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nz.co.senanque.madura.bundle.BundleRoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the bundles by keeping them in a map with name and version.
 * New bundles are added to the map and bundles are searched for by checking the map.
 * If an earlier version of a bundle is not present it will return a later one if it can. 
 * 
 * @author Roger Parkinson
 *
 */
public class BundleMap {
	
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
	private Map<String,Set<BundleVersion>> m_map = new HashMap<String,Set<BundleVersion>>();
	
	public static BundleVersion getBundleVersion(String bundleName) {
		int i = bundleName.indexOf('.');
		if (i == -1) {
			// must be no version name
			return new BundleVersion(bundleName,null);
		}
		String name = bundleName.substring(0, i);
		int i1 = name.lastIndexOf('-');
		if (i1 == -1) {
			throw new RuntimeException("bundle name "+bundleName+" does not conform to standard format (aaaaaa-n.n.n)");
		}
		name = name.substring(0,i1);
		String version = bundleName.substring(i1+1);
		return new BundleVersion(name,version);
	}

	public BundleVersion selectBestBundle(String bundleName) {
		return selectBestBundle(getBundleVersion(bundleName));
	}
	public BundleVersion selectBestBundle(String bundleName, String version) {
		return selectBestBundle(new BundleVersion(bundleName,version));
	}
	public BundleVersion selectBestBundle(BundleVersion bundleVersion) {
		Set<BundleVersion> bundles = find(bundleVersion.getName());
		if (bundles == null) {
			throw new RuntimeException("bundle name "+bundleVersion.getFullVersion()+" not found");
		}
		BundleVersion lastVersion=null;
		for (BundleVersion bv: bundles) {
			if (bv.getRoot().isShutdown()) {
				continue;
			}
			if (bundleVersion.getVersion() != null) {
				int compare = (bv.getVersion().compareTo(bundleVersion.getVersion()));
				if (compare >= 0) {
					return bv;
				}
			}
			lastVersion = bv;
		}
		if (bundleVersion.getVersion() == null && lastVersion != null) {
			return lastVersion;
		}
		throw new RuntimeException("bundle name "+bundleVersion.getFullVersion()+" not found");
	}
	private Set<BundleVersion> find(String key) {
		Set<BundleVersion> ret = m_map.get(key);
		return ret;
	}

	public Collection<BundleRoot> getAvailableBundleRoots() {
		List<BundleRoot> ret = new ArrayList<BundleRoot>();
		for (Set<BundleVersion> value: m_map.values()) {
			for (BundleVersion bv: value) {
				ret.add(bv.getRoot());
			}
		}
		return ret;
	}
	public Collection<BundleVersion> getAvailableBundles() {
		List<BundleVersion> ret = new ArrayList<BundleVersion>();
		for (Set<BundleVersion> value: m_map.values()) {
			for (BundleVersion bv: value) {
				ret.add(bv);
			}
		}
		return ret;
	}

	public BundleVersion findBundleVersion(String bundleName) {
		BundleVersion ret = getBundleVersion(bundleName);
		return findBundleVersion(ret);
	}
	private BundleVersion findBundleVersion(BundleVersion bundleVersion) {
		String name = bundleVersion.getName();
		if (name == null || bundleVersion.getVersion() == null) {
			throw new RuntimeException("bundle name "+bundleVersion.getFullVersion()+" does not conform to standard format (aaaaaa-n.n.n)");			
		}
		Set<BundleVersion> bundles = find(name);
		if (bundles == null) {
			throw new RuntimeException("Bundle "+bundleVersion.getFullVersion()+" not found");
		}
		for (BundleVersion bv: bundles) {
			if (bv.getVersion().equals(bundleVersion.getVersion())) {
				return bv;
			}
		}
		throw new RuntimeException("Bundle "+bundleVersion.getFullVersion()+" not found");
	}
	
	public void deleteBundle(String bundleName) {
		BundleVersion bundleVersion = findBundleVersion(bundleName);
		deleteBundle(bundleVersion);
	}
	
	public void deleteBundle(BundleVersion bundleVersion) {
		
		bundleVersion.getRoot().shutdown();
//		Set<BundleVersion> bundles = find(bundleVersion.getName());
//		bundles.remove(bundleVersion);
//		if (bundles.size() == 0) {
//			m_map.remove(bundleVersion.getName());
//		}
        m_logger.info("Removed bundle: {}",bundleVersion.getFullVersion());
    }

	public void addBundle(String bundleName, BundleRoot root) {
		
		BundleVersion bundleVersion = getBundleVersion(bundleName);
		String name = bundleVersion.getName();
		if (name == null || bundleVersion.getVersion() == null) {
			throw new RuntimeException("bundle name "+bundleName+" does not conform to standard format (aaaaaa-n.n.n)");			
		}
		bundleVersion.setRoot(root);
		Set<BundleVersion> bundles = find(name);
		if (bundles == null) {
			// No bundles of this name yet to create new name
			bundles = new TreeSet<BundleVersion>();
			bundles.add(bundleVersion);
			m_map.put(name, bundles);
		} else {
			// we do have a bundle of this name so add this version
			// it is a Set so if it is already there nothing happens.
			bundles.add(bundleVersion);
		}
	}

	public BundleRoot getBundleRoot(String bundleName) {
		BundleVersion bundleVersion;
		try {
			bundleVersion = findBundleVersion(bundleName);
		} catch (Exception e) {
			return null;
		}
		return bundleVersion.getRoot();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (BundleVersion bundleVersion: getAvailableBundles()) {
			sb.append(bundleVersion.toString());
			sb.append('\n');
		}
		sb.append('\n');
		return sb.toString();
	}
	public void clear() {
		m_map.clear();
	}

	public Map<String, Set<BundleVersion>> getMap() {
		return m_map;
	}

	public void setMap(Map<String, Set<BundleVersion>> map) {
		m_map = map;
	}

}
