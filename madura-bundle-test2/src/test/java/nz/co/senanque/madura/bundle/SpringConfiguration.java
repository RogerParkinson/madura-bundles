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
package nz.co.senanque.madura.bundle;

import nz.co.senanque.madura.bundle.spring.BundledInterfaceRegistrar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

/**
 * @author Roger Parkinson
 *
 */

@Configuration
@Import(BundledInterfaceRegistrar.class)
@ComponentScan(basePackages = {
		"nz.co.senanque.madura.bundle"})
@PropertySource("classpath:config.properties")
public class SpringConfiguration {
	
	@Autowired
    Environment env;

	@Autowired BundleManager m_bundleManager;
	public SpringConfiguration() {
		"".toString();
	}
//	@Bean
//	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
//		PropertyPlaceholderConfigurer ret = new PropertyPlaceholderConfigurer();
//		ret.setLocation(new ClassPathResource("config.properties"));
//		return ret;
//	}
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	public BundleManager getBundleManager() {
		return m_bundleManager;
	}
	public void setBundleManager(BundleManager bundleManager) {
		m_bundleManager = bundleManager;
	}

}
