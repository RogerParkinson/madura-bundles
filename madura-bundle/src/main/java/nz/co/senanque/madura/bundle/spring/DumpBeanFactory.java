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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Used for debugging. It dumps the beans for the given bean factory.
 * 
 * @author Roger Parkinson
 *
 */
public class DumpBeanFactory {

	private static Logger m_logger = LoggerFactory.getLogger(DumpBeanFactory.class);
	
	public static void dumpBeans(GenericApplicationContext context, String text) {
    	dumpBeans((ConfigurableListableBeanFactory)context.getBeanFactory(),text);
	}

	public static void dumpBeans(GenericWebApplicationContext context, String text) {
    	dumpBeans((ConfigurableListableBeanFactory)context.getBeanFactory(),text);
	}

	public static void dumpBeans(ConfigurableListableBeanFactory beanFactory, String text) {
		if (!m_logger.isDebugEnabled()) {
			return;
		}
		StringBuilder sb = new StringBuilder("\n**");
		sb.append(text);
		sb.append("**\n");
		String[] beanNames = beanFactory.getBeanDefinitionNames();
		for (String beanName: beanNames) {
			if (beanName.startsWith("org.")) {
				// Ignore processors
				continue;
			}
			BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
			String beanType = bd.getBeanClassName();
			if (StringUtils.hasText(beanType)) {
				int i = beanType.indexOf("$$");
				if (i > -1) {
					beanType = beanType.substring(0, i);
				}
			}
			String scope = bd.getScope();
			if (!StringUtils.hasText(scope) && "nz.co.senanque.madura.bundle.InnerBundleFactory".equals(beanType)) {
				scope = "exported";
			}
			sb.append(figureMessage("{} type: {} scope: {}",new Object[]{beanName, beanType, scope}));
			sb.append("\n");
		}
		m_logger.debug("{}\n",sb);
	}
	private static String figureMessage(String message, Object... parameters) {
		return org.slf4j.helpers.MessageFormatter.arrayFormat(message, parameters).getMessage();
	}

}
