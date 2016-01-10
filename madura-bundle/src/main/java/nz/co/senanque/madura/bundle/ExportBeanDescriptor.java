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
package nz.co.senanque.madura.bundle;

import org.springframework.util.StringUtils;

/**
 * @author Roger Parkinson
 *
 */
public class ExportBeanDescriptor {

	private final String m_beanName;
	private final String m_ownerBeanName;
	private final Class<?> m_type;
	public ExportBeanDescriptor(String beanName, BundleExport a,
			Class<?> type) {
		m_ownerBeanName = beanName;
		m_type = type;
		if (StringUtils.hasText(a.value())) {
			m_beanName = a.value();
		} else {
			m_beanName = m_ownerBeanName;
		}
	}
	public String getBeanName() {
		return m_beanName;
	}
	public Class<?> getType() {
		return m_type;
	}
	public String getOwnerBeanName() {
		return m_ownerBeanName;
	}

}
