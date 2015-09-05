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


/**
 * Used to wrap strings. We need this so that we can put an interface around a String and
 * an implementation class (this is the implementation). We need to do this when we want to
 * expose a String from a bundle because every bean in the bundle must have an interface and
 * implementation.
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.1 $
 */
public class StringWrapperImpl implements StringWrapper
{
    private String m_string;
    
    public StringWrapperImpl(String string)
    {
        m_string = string;
    }

    /* (non-Javadoc)
     * @see nz.co.senanque.madura.bundle.StringWrapper#getString()
     */
    public String toString()
    {
        return m_string;
    }

}
