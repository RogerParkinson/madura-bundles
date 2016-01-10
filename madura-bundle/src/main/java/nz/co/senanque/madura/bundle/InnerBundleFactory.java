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
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
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
    private DefaultListableBeanFactory m_owner;
    private Class<?> m_type;

    public void setBeanName(String name)
    {
        m_beanName = name;
    }

    public Object getObject() throws Exception
    {
    	if (m_inheritedBean == null) {
    		return m_owner.getBean(m_beanName);
    	}
        return m_inheritedBean;
    }
    
    public void setObject(Object o)
    {
    	m_inheritedBean = o;
    }

    public Class<? extends Object> getObjectType()
    {
    	if (m_type != null) {
    		return m_type;
    	}
    	if (m_inheritedBean != null) {
    		return m_inheritedBean.getClass();
    	}
    	if (m_beanName == null || m_owner == null) {
    		return null;
    	}
    	try {
    		BeanDefinition bd = m_owner.getBeanDefinition(m_beanName);
    		String beanClassName = bd.getBeanClassName();
			return Class.forName(beanClassName);
		} catch (Exception e) {
			return null;
		}
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

	public DefaultListableBeanFactory getOwner() {
		return m_owner;
	}

	public void setOwner(DefaultListableBeanFactory owner) {
		m_owner = owner;
	}

	public Class<?> getType() {
		return m_type;
	}

	public void setType(Class<?> type) {
		m_type = type;
	}

}
