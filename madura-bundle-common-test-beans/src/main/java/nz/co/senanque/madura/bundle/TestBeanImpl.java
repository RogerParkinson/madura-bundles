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

import org.springframework.beans.factory.annotation.Value;
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
    private Object m_sampleExport;
    @Value("${nz.co.senanque.madura.bundle.TestBeanImpl.m_value:}")
    private transient String m_value;
	private TestExportBean2 m_exportBean2;

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

    public Object getSampleExport()
    {
        return m_sampleExport;
    }

    public void setSampleExport(Object sampleExport)
    {
        m_sampleExport = sampleExport;
    }

	public String getValue() {
		return m_value;
	}

	public void setValue(String value) {
		m_value = value;
	}

	public void setSampleExport2(TestExportBean2 exportBean2) {
		setExportBean2(exportBean2);
	}

	public TestExportBean2 getExportBean2() {
		return m_exportBean2;
	}

	public void setExportBean2(TestExportBean2 exportBean2) {
		m_exportBean2 = exportBean2;
	}
    

}
