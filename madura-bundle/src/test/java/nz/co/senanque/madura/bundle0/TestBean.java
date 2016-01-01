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
package nz.co.senanque.madura.bundle0;

import nz.co.senanque.madura.bundle.BundleInterface;
import nz.co.senanque.madura.bundle.StringWrapper;

import org.springframework.core.io.Resource;

@BundleInterface("TestBean")
public interface TestBean
{

    public abstract StringWrapper getContent();

    public abstract void setContent(StringWrapper content);

    public abstract void setResource(Resource resource);

    public abstract Resource getResource();
    
    public abstract Object getSampleExport();
    
}
