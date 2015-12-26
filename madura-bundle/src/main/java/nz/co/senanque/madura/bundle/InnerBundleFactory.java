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
package nz.co.senanque.madura.bundle;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 
 * This factory is used to deliver exported beans to the bundle beans.
 * 
 * @author Roger Parkinson
 * @version $Revision:$
 */
@Component
public class InnerBundleFactory implements FactoryBean<Object>, BeanNameAware, InitializingBean
{
    private String m_key;
    private String m_beanName;
    private Object m_inheritedBean;

    public void setBeanName(String name)
    {
        m_beanName = name;
    }

    public Object getObject() throws Exception
    {
        return m_inheritedBean;
    }
    
    public void setObject(Object o)
    {
    	m_inheritedBean = o;
    }

    public Class<? extends Object> getObjectType()
    {
        return (m_inheritedBean==null)?null:m_inheritedBean.getClass();
    }

    public boolean isSingleton()
    {
        return true;
    }

    public String getKey()
    {
        return m_key;
    }

    public void setKey(String key)
    {
        m_key = key;
    }

    public void afterPropertiesSet() throws Exception
    {
    }

	public String getBeanName() {
		return m_beanName;
	}
}
