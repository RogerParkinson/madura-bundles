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

import java.util.HashSet;
import java.util.Set;

import nz.co.senanque.madura.bundle.BundleInterface;
import nz.co.senanque.madura.bundle.BundleRoot;
import nz.co.senanque.madura.bundle.BundledSpringFactoryBean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
/**
 *  
 * @author Roger Parkinson
 * @version $Revision: 1.2 $
 */
public class BeanScanDefinitionParser implements BeanDefinitionParser
{
	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;


	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String[] basePackages = StringUtils.tokenizeToStringArray(element.getAttribute(BASE_PACKAGE_ATTRIBUTE),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		XmlReaderContext readerContext = parserContext.getReaderContext();
		ResourcePatternResolver resourceLoader = ResourcePatternUtils.getResourcePatternResolver(readerContext.getResourceLoader());
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
		Environment environment = parserContext.getDelegate().getEnvironment();
		Set<BeanDefinitionHolder> beanDefinitions = new HashSet<BeanDefinitionHolder>();
		BeanDefinitionRegistry registry = readerContext.getRegistry();
		
		for (String basePackage: basePackages) {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(environment, basePackage) + "/" + this.resourcePattern;
			try {
				Resource[] resources = resourceLoader.getResources(packageSearchPath);
				for (Resource resource: resources) {
					if (resource.isReadable()) {
						MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
						if (metadataReader.getAnnotationMetadata().hasAnnotation("nz.co.senanque.madura.bundle.BundleInterface")) {
							// TODO: register a bean
							String className = metadataReader.getClassMetadata().getClassName();
							Class<?> clazz = Class.forName(className);
							String beanName = clazz.getAnnotation(BundleInterface.class).value();
							if (StringUtils.isEmpty(beanName)) {
								beanName = className;
							}
//				        	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BundledSpringFactoryBean.class);
//				        	beanDefinitionBuilder.addPropertyReference("bundleManager", "bundleManager"); // TODO: maybe more flexible?
//				        	beanDefinitionBuilder.addPropertyValue("interface", className);
//				        	
//
//							BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
//							BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
//							registerBeanDefinition(definitionHolder, parserContext.getRegistry());
//							if (shouldFireEvents()) {
//								BeanComponentDefinition componentDefinition = new BeanComponentDefinition(definitionHolder);
//								postProcessComponentDefinition(componentDefinition);
//								parserContext.registerComponent(componentDefinition);
//							}
							BeanDefinitionHolder definitionHolder = createBeanDefinition(className, beanName, parserContext);
							beanDefinitions.add(definitionHolder);
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		beanDefinitions.add(createBeanDefinition(BundleRoot.class.getName(), "bundleRoot", parserContext));
//		registerComponents(parserContext.getReaderContext(), beanDefinitions, element);

		return null;
	}
	private BeanDefinitionHolder createBeanDefinition(String className, String beanName,ParserContext parserContext) {
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BundledSpringFactoryBean.class);
    	beanDefinitionBuilder.addPropertyReference("bundleManager", "bundleManager"); // TODO: maybe more flexible?
    	beanDefinitionBuilder.addPropertyValue("interface", className);
    	

		BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
		BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
		registerBeanDefinition(definitionHolder, parserContext.getRegistry());
		if (shouldFireEvents()) {
			BeanComponentDefinition componentDefinition = new BeanComponentDefinition(definitionHolder);
			postProcessComponentDefinition(componentDefinition);
			parserContext.registerComponent(componentDefinition);
		}
		return definitionHolder;
	}
	protected boolean shouldFireEvents() {
		return true;
	}
	protected void postProcessComponentDefinition(BeanComponentDefinition componentDefinition) {
	}
	protected void registerBeanDefinition(BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
		BeanDefinitionReaderUtils.registerBeanDefinition(definition, registry);
	}
	protected String resolveBasePackage(Environment environment, String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(basePackage));
	}

	protected void registerComponents(
			XmlReaderContext readerContext, Set<BeanDefinitionHolder> beanDefinitions, Element element) {

		Object source = readerContext.extractSource(element);
		CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);

		for (BeanDefinitionHolder beanDefHolder : beanDefinitions) {
			compositeDef.addNestedComponent(new BeanComponentDefinition(beanDefHolder));
		}
		readerContext.fireComponentRegistered(compositeDef);
	}

}
