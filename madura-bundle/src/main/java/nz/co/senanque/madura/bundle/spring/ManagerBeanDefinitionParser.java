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

import nz.co.senanque.madura.bundle.BundleManagerImpl;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
/**
 *  
 * @author Roger Parkinson
 * @version $Revision: 1.2 $
 */
public class ManagerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser
{
    protected Class<BundleManagerImpl> getBeanClass(Element element)
    {
        return nz.co.senanque.madura.bundle.BundleManagerImpl.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder bean)
    {
        String directory = element.getAttribute("directory");
        if (StringUtils.hasText(directory))
        {
            bean.addPropertyValue("directory", directory);
        }
        String id = element.getAttribute("id");
        if (!StringUtils.hasText(id))
        {
            bean.addPropertyValue("id", "bundleManager");
        }
        String time = element.getAttribute("time");
        if (StringUtils.hasLength(time))
        {
            bean.addPropertyValue("time", time);
        }
        String export = element.getAttribute("export");
        if (StringUtils.hasLength(export))
        {
            bean.addPropertyValue("export", export);
        }
        String childFirst = element.getAttribute("childFirst");
        if (StringUtils.hasLength(childFirst))
        {
            bean.addPropertyValue("childFirst", childFirst);
        }
    }
}
