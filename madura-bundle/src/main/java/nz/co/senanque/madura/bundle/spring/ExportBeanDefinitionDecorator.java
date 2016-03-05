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
package nz.co.senanque.madura.bundle.spring;

import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Node;

/**
 * @author Roger Parkinson
 *
 */
public class ExportBeanDefinitionDecorator implements BeanDefinitionDecorator {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.BeanDefinitionDecorator#decorate(org.w3c.dom.Node, org.springframework.beans.factory.config.BeanDefinitionHolder, org.springframework.beans.factory.xml.ParserContext)
	 */
	@Override
	public BeanDefinitionHolder decorate(Node node,
			BeanDefinitionHolder holder, ParserContext parserContext) {
		AbstractBeanDefinition definition = ((AbstractBeanDefinition) holder.getBeanDefinition());
		BeanMetadataAttribute att = new BeanMetadataAttribute("export",true);
		definition.addMetadataAttribute(att);
		return holder;
	}

}
