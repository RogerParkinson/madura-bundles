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

import java.util.Map;
import java.util.Properties;

import nz.co.senanque.madura.bundle.BundleRoot;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Roger Parkinson
 *
 */
public class BundleRootMock implements BundleRoot {
	
	private boolean m_shutdown = false;
	String m_name;

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleRoot#shutdown()
	 */
	@Override
	public void shutdown() {
		m_shutdown = true;
	}

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleRoot#init(org.springframework.beans.factory.support.DefaultListableBeanFactory, java.util.Properties, java.lang.ClassLoader, java.util.Map)
	 */
	@Override
	public void init(DefaultListableBeanFactory ownerBeanFactory,
			Properties properties, ClassLoader cl,
			Map<String, Object> inheritableBeans) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleRoot#getApplicationContext()
	 */
	@Override
	public ApplicationContext getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleRoot#getProperties()
	 */
	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleRoot#getBundleClassLoader()
	 */
	@Override
	public ClassLoader getBundleClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleRoot#getName()
	 */
	@Override
	public String getName() {
		return m_name;
	}

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.BundleRoot#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		m_name = name;
	}

	@Override
	public boolean isShutdown() {
		return m_shutdown;
	}
	
	public String toString() {
		return m_name;
	}

}
