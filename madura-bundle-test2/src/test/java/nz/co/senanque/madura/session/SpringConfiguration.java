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
package nz.co.senanque.madura.session;

import java.util.HashMap;
import java.util.Map;

import nz.co.senanque.madura.bundle.BundleExport;
import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.TestExportBean;
import nz.co.senanque.madura.bundle.TestExportBean2;
import nz.co.senanque.madura.bundle.TestExportBean2Impl;
import nz.co.senanque.madura.bundle.TestExportBeanImpl;
import nz.co.senanque.madura.bundle.spring.EnableBundles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.core.env.Environment;

/**
 * @author Roger Parkinson
 *
 */

@Configuration
@EnableBundles
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
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	@Bean
	public CustomScopeConfigurer getCustomScopeConfigurer() {
		CustomScopeConfigurer ret = new CustomScopeConfigurer();
		Map<String,Object> map = new HashMap<>();
		map.put("session", new SimpleThreadScope());
		ret.setScopes(map);
		return ret;
	}
	@Bean
	public TestExportBean getTestExportBean() {
		return new TestExportBeanImpl();
	}
	@Bean
	@BundleExport
	public TestExportBean2 getTestExportBean2() {
		return new TestExportBean2Impl();
	}
	public BundleManager getBundleManager() {
		return m_bundleManager;
	}
	public void setBundleManager(BundleManager bundleManager) {
		m_bundleManager = bundleManager;
	}

}
