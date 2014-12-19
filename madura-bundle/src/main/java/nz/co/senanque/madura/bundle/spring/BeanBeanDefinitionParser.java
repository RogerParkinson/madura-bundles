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
package nz.co.senanque.madura.bundle.spring;

import nz.co.senanque.madura.bundle.BundledSpringFactoryBean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
/**
 * Parses a workflow client bean definition:
 * &lt;client&gt;
 *  sessionFactory="hibernateSessionFactory"
 *  [interval="1000"]
 *  [accessDecisionManager="accessDecisionManager"] 
 *  [defaultExecutor="autoExecutor"]/&gt;>
 *  
 * @author Roger Parkinson
 * @version $Revision: 1.2 $
 */
public class BeanBeanDefinitionParser extends AbstractSingleBeanDefinitionParser
{
    protected Class<BundledSpringFactoryBean> getBeanClass(Element element)
    {
        return BundledSpringFactoryBean.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder bean)
    {
        String bundleManager = element.getAttribute("bundleManager");
        if (!StringUtils.hasText(bundleManager))
            bundleManager = "bundleManager";
        bean.addPropertyReference("bundleManager", bundleManager);

        String interfaceName = element.getAttribute("interface");
        if (StringUtils.hasText(interfaceName))
        {
            bean.addPropertyValue("interface", interfaceName);
        }
    }
}
