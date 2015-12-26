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

/**
 * 
 * Returns a bean from a bundle
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.5 $
 */
public class BundledSpringFactoryBean implements FactoryBean<Object>, BeanNameAware
{
    private BundleManager m_bundleManager;
    private String m_beanName;
    private String m_bundleName;
    private String m_version;
    private String m_interfaceName;
    private Class<?> m_class;

    public Object getObject() throws Exception
    {
        return BundleProxy.getProxy(m_bundleManager,m_class,m_beanName);
    }

    public Class<?> getObjectType()
    {
        return m_class;
    }

    public boolean isSingleton()
    {
        return true;
    }


    public BundleManager getBundleManager()
    {
        return m_bundleManager;
    }

    public void setBundleManager(BundleManager bundleManager)
    {
        m_bundleManager = bundleManager;
    }

    public String getBeanName()
    {
        return m_beanName;
    }

    public void setBeanName(String beanName)
    {
        m_beanName = beanName;
    }

    public String getBundleName()
    {
        return m_bundleName;
    }

    public void setBundleName(String bundleName)
    {
        m_bundleName = bundleName;
    }

    public String getVersion()
    {
        return m_version;
    }

    public void setVersion(String version)
    {
        m_version = version;
    }

    public void setInterface(String class1)
    {
        m_interfaceName = class1;
        try
        {
            m_class = Class.forName(class1);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getInterface()
    {
        return m_interfaceName;
    }

}
