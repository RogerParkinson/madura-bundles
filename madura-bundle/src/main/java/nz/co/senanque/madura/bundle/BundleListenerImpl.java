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

import nz.co.senanque.madura.bundlemap.BundleVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * Simple example implementation of a bundle listener
 * 
 * @author Roger Parkinson
 * @version $Revision:$
 */
@Component
public class BundleListenerImpl implements BundleListener
{
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());

    /* (non-Javadoc)
     * @see nz.co.senanque.madura.bundle.BundleListener#add(java.lang.String, nz.co.senanque.madura.bundle.BundleRoot)
     */
    public void add(BundleVersion bundleVersion)
    {
        m_logger.debug("added bundle {}",bundleVersion.getId());        
    }

    public void remove(BundleVersion bundleVersion)
    {
        m_logger.debug("removed bundle {}",bundleVersion.getId());        
    }

}
