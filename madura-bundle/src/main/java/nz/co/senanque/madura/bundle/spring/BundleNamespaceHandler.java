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

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 
 * This is used to define the custom namespace for spring.
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.1 $
 */
public class BundleNamespaceHandler extends NamespaceHandlerSupport
{
    public void init()
    {
        registerBeanDefinitionParser("manager",
                new ManagerBeanDefinitionParser());
        registerBeanDefinitionParser("bean",
                new BeanBeanDefinitionParser());
    }
}
