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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Roger Parkinson
 *
 */

@Configuration
public class BundleRoot4Impl  extends BundleRootImpl {
	
	@Autowired
    Environment env;

	public BundleRoot4Impl() {
		"".toString();
	}
	@Bean(name="bundleFile")
	public StringWrapper bundleFile() {
		StringWrapperImpl ret = new StringWrapperImpl(env.getProperty("bundle.file"));
		return ret;
	}
	@Bean(name="bundleName")
	public StringWrapper bundleName() {
		StringWrapperImpl ret = new StringWrapperImpl(env.getProperty("bundle.name"));
		return ret;
	}
	@Bean(name="TestBean")
	public TestBean testBean() {
		TestBean ret = new TestBeanImpl();
		ret.setResource(new ClassPathResource("classpath:BundleResource4.txt"));
		return ret;
	}

}
