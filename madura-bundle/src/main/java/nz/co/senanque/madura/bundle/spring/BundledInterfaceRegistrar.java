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
package nz.co.senanque.madura.bundle.spring;

import java.util.Map;

import nz.co.senanque.madura.bundle.BundleInterface;
import nz.co.senanque.madura.bundle.BundleRoot;
import nz.co.senanque.madura.bundle.BundledSpringFactoryBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author Roger Parkinson
 *
 */
public class BundledInterfaceRegistrar implements ImportBeanDefinitionRegistrar/*,ResourceLoaderAware, BeanClassLoaderAware,BeanFactoryAware,EnvironmentAware*/  {
	
	private Logger m_logger = LoggerFactory.getLogger(this.getClass());
	private MetadataReaderFactory m_metadataReaderFactory = new CachingMetadataReaderFactory();
	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(basePackage);
	}
	
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {

		Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes("org.springframework.context.annotation.ComponentScan");
		String[] packageSearchPaths = (String[])attributes.get("basePackages");
		for (String basePackage : packageSearchPaths) {
			m_logger.debug("{}",basePackage);
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			try {
				Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader metadataReader = m_metadataReaderFactory.getMetadataReader(resource);
						if (metadataReader.getAnnotationMetadata().hasAnnotation("nz.co.senanque.madura.bundle.BundleInterface")) {
							String className = metadataReader.getClassMetadata().getClassName();
							Class<?> clazz = Class.forName(className);
							String beanName = clazz.getAnnotation(BundleInterface.class).value();
							if (StringUtils.isEmpty(beanName)) {
								beanName = className;
							}
							BeanDefinitionHolder definitionHolder = createBeanDefinition(className, beanName, registry);
							registerBeanDefinition(definitionHolder, registry);
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		BeanDefinitionHolder definitionHolder =createBeanDefinition(BundleRoot.class.getName(), "bundleRoot", registry);
		registerBeanDefinition(definitionHolder, registry);
	}

	private BeanDefinitionHolder createBeanDefinition(String className, String beanName,BeanDefinitionRegistry registry) {
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BundledSpringFactoryBean.class);
    	beanDefinitionBuilder.addPropertyReference("bundleManager", "bundleManager"); // TODO: maybe more flexible?
    	beanDefinitionBuilder.addPropertyValue("interface", className);
    	

		BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
		BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
		return definitionHolder;
	}
	protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
		BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
	}
}
