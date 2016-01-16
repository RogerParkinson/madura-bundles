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
package nz.co.senanque.madura.b3;

import nz.co.senanque.madura.testbeans.ValueInjectedBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Roger Parkinson
 *
 */
@Component("valueInjectedBean")
public class ValueInjectedBeanImpl implements ValueInjectedBean {

    @Value("${nz.co.senanque.madura.bundle.TestBeanImpl.m_value}")
    private transient String m_value;
	public ValueInjectedBeanImpl() {
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.ValueInjectedBean#getValue()
	 */
	@Override
	public String getValue() {
		return m_value;
	}
	public void setValue(String value) {
		m_value = value;
	}

}
