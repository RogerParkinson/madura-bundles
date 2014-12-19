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

import java.lang.reflect.Proxy;

/**
 * Helper class to get the proxy set up for the class given.
 * It uses the bean name to find the target while the class describes the interface
 * 
 * @author Roger Parkinson
 * @version $Revision: 1.2 $
 */
public class BundleProxy
{

    public static Object getProxy(BundleManager bundleManager, Class<?> class1, String beanName)
    {
        BundleInvocationHandler bih = new BundleInvocationHandler(bundleManager, beanName);
        return Proxy.newProxyInstance(class1.getClassLoader(),
                new Class[] { class1 },
                bih);
    }

}
