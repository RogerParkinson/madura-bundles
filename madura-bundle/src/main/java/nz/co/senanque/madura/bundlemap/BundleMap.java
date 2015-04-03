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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nz.co.senanque.madura.bundle.BundleRoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the bundles by keeping them in a map with name and version and id.
 * The id is normally the file name concatenated with the version and extension but it
 * might be different.
 * New bundles are added to the map and bundles are searched for by checking the map.
 * If an earlier version of a bundle is not present it will return a later one if it can. 
 * 
 * @author Roger Parkinson
 *
 */
public class BundleMap {
	
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
	private Map<String,Set<BundleVersion>> m_map = new HashMap<String,Set<BundleVersion>>();
	private Comparator<BundleVersion> m_comparator = new Comparator<BundleVersion>(){

		@Override
		public int compare(BundleVersion o1, BundleVersion o2) {
//			m_logger.debug("o2.getVersion(): {} o1.getVersion(): {} result {}",o2.getVersion(),o1.getVersion(),o2.getVersion().compareTo(o1.getVersion()));
            if ( o2.getVersion().equals(o1.getVersion()) ) 
                return 0;
			return o2.getVersion().compareTo(o1.getVersion());
		}};
	
	/**
	 * We get a bundle name and version so we return the latest version for that bundle,
	 * assuming any versions of the bundle exists, else we throw exception.
	 * The bundle name must not contain version information in the string.
	 * @param bundleName
	 * @return a BundleVersion
	 */
	public BundleVersion selectBestBundle(String bundleName) {
		Set<BundleVersion> bundleVersions = m_map.get(bundleName);
		if (bundleVersions == null || bundleVersions.isEmpty()) {
			throw new BundleNotFoundException("Bundle "+bundleName+" not found");
		}
		return bundleVersions.iterator().next(); // gets the first one in the set
	}

	/**
	 * Find the specific bundle using the bundle name and version.
	 * Throw exception if not found.
	 * @param bundleName
	 * @param version
	 * @return bundleVersion
	 */
	public BundleVersion selectBestBundle(String bundleName, String version) {
		Set<BundleVersion> bundleVersions = m_map.get(bundleName);
		if (bundleVersions == null || bundleVersions.isEmpty()) {
			return null;
		}
		for (BundleVersion bundleVersion: bundleVersions) {
			if (bundleVersion.getVersion().equals(version)) {
				return bundleVersion;
			}
		}
		throw new BundleNotFoundException("Bundle "+bundleName+":"+version+" not found");
	}

	/**
	 * Find the specific bundle using its unique key.
	 * This is usually the file name that contains the bundle.
	 * Return null if the id is not found.
	 * @param id
	 * @return bundleVersion
	 */
	public BundleVersion findById(String id) {
		for (Set<BundleVersion> bundleVersions: m_map.values()) {
			for (BundleVersion bundleVersion: bundleVersions) {
				if (bundleVersion.getId().equals(id)) {
					return bundleVersion;
				}
			}
		}
		return null;
	}
	public void shutdown(BundleVersion bundleVersion) {
		bundleVersion.getRoot().shutdown();
		Set<BundleVersion> ret = m_map.get(bundleVersion.getId());
		ret.remove(bundleVersion);
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

	public void addBundle(BundleVersion bundleVersion, BundleRoot root) {
		
		String name = bundleVersion.getName();
		if (name == null || bundleVersion.getVersion() == null) {
			throw new RuntimeException("bundle name ["+bundleVersion+"] does not conform to standard format (aaaaaa-n.n.n)");			
		}
		bundleVersion.setRoot(root);
		Set<BundleVersion> bundles = find(name);
		if (bundles == null) {
			// No bundles of this name yet to create new name
			bundles = new TreeSet<BundleVersion>(m_comparator);
			bundles.add(bundleVersion);
			m_map.put(name, bundles);
		} else {
			// we do have a bundle of this name so add this version
			// it is a Set so if it is already there nothing happens.
			bundles.add(bundleVersion);
		}
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

	// These are only used for testing
	public Map<String, Set<BundleVersion>> getMap() {
		return m_map;
	}

	public void setMap(Map<String, Set<BundleVersion>> map) {
		m_map = map;
	}

}
