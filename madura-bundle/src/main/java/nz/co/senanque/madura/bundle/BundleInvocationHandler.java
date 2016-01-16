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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bundle proxies use this to map to the current bean
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.1 $
 */
public class BundleInvocationHandler implements InvocationHandler
{
    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    private final BundleManager m_bundleManager;
    private final String m_beanName;

    public BundleInvocationHandler(BundleManager bundleManager,String beanName)
    {
        m_bundleManager = bundleManager;
        m_beanName = beanName;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable
    {
        BundleRoot br =  m_bundleManager.getBundle();
        if (br == null)
            return null;
//    	m_logger.debug("bundle: {} bean: {} method: {}",new Object[]{br.getName(),m_beanName,method.getName()});
        Object target = br.getApplicationContext().getBean(m_beanName);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
        	ClassLoader cl = br.getBundleClassLoader();
        	Thread.currentThread().setContextClassLoader(cl);
        }
        catch (Error e1) {
        	m_logger.info(e1.toString());
        }
        try
        {
            return method.invoke(target, args);
        }
        catch (InvocationTargetException e)
        {
//        	m_logger.debug("error bundle: {} bean: {} method: {} error: {}",br.getName(),m_beanName,method.getName(),e.getTargetException().getMessage());
            throw e.getTargetException();

        }
        catch (IllegalArgumentException e)
        {
//        	m_logger.debug("error bundle: {} bean: {} method: {} error: {}",br.getName(),m_beanName,method.getName(),e.getTargetException().getMessage());
            throw e;

        }
        finally {
        	Thread.currentThread().setContextClassLoader(classLoader);
//        	m_logger.debug("finished bundle: {} bean: {} method: {}",br.getName(),m_beanName,method.getName());
        }
    }

}
