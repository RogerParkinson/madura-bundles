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

import nz.co.senanque.madura.bundle.BundleRoot;

/**
 * @author Roger Parkinson
 *
 */
public class BundleVersion implements Comparable<BundleVersion> {
	
	private final String m_name;
	private final String m_version;
	private final String m_id;
	private BundleRoot m_root;
	private int inuse;
	
	public BundleVersion(String id, String name, String version) {
		m_name = name;
		m_version = version;
		m_id = id;
	}

	public String getName() {
		return m_name;
	}

	public String getVersion() {
		return m_version;
	}
	
	public String getFullVersion() {
		return m_name+((m_version==null)?"":"-"+m_version);
	}

	public BundleRoot getRoot() {
		return m_root;
	}


	@Override
	public int compareTo(BundleVersion bundleVersion) {
		
		return getId().compareTo(bundleVersion.getId());
	}
	public String toString() {
		return getFullVersion();
	}

	public String getId() {
		return m_id;
	}

	public void setRoot(BundleRoot root) {
		m_root = root;
	}

	public void increment() {
		inuse++;
	}

	public void decrement() {
		inuse--;
	}

	public boolean isInUse() {
		return inuse!=0;
	}

}
