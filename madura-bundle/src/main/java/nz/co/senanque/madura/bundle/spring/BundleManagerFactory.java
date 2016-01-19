/*******************************************************************************
 * Copyright (c)2015 Prometheus Consulting
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
package nz.co.senanque.madura.bundle.spring;

import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.BundleManagerImpl;
import nz.co.senanque.madura.bundle.BundleManagerWeb;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory that creates a configured BundleManager
 * 
 * @author Roger Parkinson
 *
 */
@Component("bundleManager")
public class BundleManagerFactory implements FactoryBean<BundleManager>, BeanFactoryAware {
	
    @Value("${nz.co.senanque.madura.bundle.spring.BundleManagerFactory.type:impl}")
    private String m_type;
    @Value("${nz.co.senanque.madura.bundle.spring.BundleManagerFactory.directory:}")
    private String m_directory;
    @Value("${nz.co.senanque.madura.bundle.spring.BundleManagerFactory.time:-1}")
    private long m_time; // optional scan timer
	private BeanFactory m_beanFactory;
	
	private BundleManagerImpl m_bundleManager;

	@Override
	public BundleManager getObject() throws Exception {
		if (m_bundleManager == null) {
			if (m_type.equals("impl")) {
				m_bundleManager = new BundleManagerImpl();
			} else {
				m_bundleManager = new BundleManagerWeb();
			}
			m_bundleManager.setDirectory(m_directory);
			m_bundleManager.setTime(m_time);
			m_bundleManager.setBeanFactory(m_beanFactory);
			m_bundleManager.init();
		}
		return m_bundleManager;
	}

	@Override
	public Class<?> getObjectType() {
		return BundleManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getType() {
		return m_type;
	}

	public void setType(String type) {
		m_type = type;
	}

	public String getDirectory() {
		return m_directory;
	}

	public void setDirectory(String directory) {
		m_directory = directory;
	}

	public long getTime() {
		return m_time;
	}

	public void setTime(long time) {
		m_time = time;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		m_beanFactory = beanFactory;
	}

}
