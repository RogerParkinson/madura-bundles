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

import org.springframework.core.io.Resource;

/**
 * Used in the tests
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.1 $
 */
public class TestBeanImpl implements TestBean
{
    private StringWrapper m_content;
    private Resource m_resource;
    private String m_sampleParent;

    /* (non-Javadoc)
     * @see nz.co.senanque.madura.bundle.TestBean#getContent()
     */
    public StringWrapper getContent()
    {
        return m_content;
    }

    /* (non-Javadoc)
     * @see nz.co.senanque.madura.bundle.TestBean#setContent(nz.co.senanque.madura.bundle.StringWrapper)
     */
    public void setContent(StringWrapper content)
    {
        m_content = content;
    }

    public Resource getResource()
    {
        return m_resource;
    }

    public void setResource(Resource resource)
    {
        m_resource = resource;
    }

    public String getSampleParent()
    {
        return m_sampleParent;
    }

    public void setSampleParent(String sampleParent)
    {
        m_sampleParent = sampleParent;
    }
    

}
