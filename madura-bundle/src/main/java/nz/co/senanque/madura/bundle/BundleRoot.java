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
package nz.co.senanque.madura.bundle;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

public interface BundleRoot
{

    public abstract void shutdown();

    public abstract void init(DefaultListableBeanFactory ownerBeanFactory, Properties properties, ClassLoader cl, Map<String, Object> inheritableBeans);

    public abstract ApplicationContext getApplicationContext();

    public abstract Properties getProperties();

	public ClassLoader getBundleClassLoader();

	public String getName();
	
	public void setName(String name);

	public boolean isShutdown();

}
