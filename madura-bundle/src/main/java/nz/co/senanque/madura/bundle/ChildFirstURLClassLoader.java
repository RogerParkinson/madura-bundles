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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Grabbed this code from the last entry in 
 * http://stackoverflow.com/questions/5445511/how-do-i-create-a-parent-last-child-first-classloader-in-java-or-how-to-overri
 * some modifications from http://svn.apache.org/repos/asf/openejb/branches/openejb0/src/main/org/openejb/util/MemoryClassLoader.java
 * 
 * @author http://stackoverflow.com/users/36071/yoni
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @author Roger Parkinson
 * @version $Revision:$
 * @deprecated superseded by {$link BundleClassLoader}
 */
public class ChildFirstURLClassLoader extends URLClassLoader {

    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
    private final static int BUFFER_SIZE = 1024;
    private HashMap<String,byte[]> classes = new HashMap<String,byte[]>();
    private HashMap<String,byte[]> others = new HashMap<String,byte[]>();

    private ClassLoader system;

    public ChildFirstURLClassLoader(URL[] classpath, ClassLoader parent) {
        super(classpath, parent);
        system = getSystemClassLoader();
    }

    public ChildFirstURLClassLoader(JarInputStream[] classpath, ClassLoader parent) {
        super(null, parent);
        system = getSystemClassLoader();
        for (JarInputStream jarInputStream : classpath)
        {
        	addJar(jarInputStream);
        }
        
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class<?> c = findLoadedClass(name);
        if (c == null || c.getClassLoader() == getParent()) {
            if (system != null) {
                try {
                    // checking system: jvm classes, endorsed, cmd classpath, etc.
                    c = system.loadClass(name);
                }
                catch (ClassNotFoundException ignored) {
                }
            }
            if (c == null) {
                try {
                    // checking local
                    c = findClass(name);
                    m_logger.debug("Loaded class from bundle: {}",name);
                } catch (ClassNotFoundException e) {
                    // checking parent
                    // This call to loadClass may eventually call findClass again, in case the parent doesn't find anything.
                    c = super.loadClass(name, resolve);
                    m_logger.debug("Loaded class from parent: {}",name);
                }
            }
        }
        else
        {
            m_logger.debug("Loaded class already: {}",name);
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    public URL getResource(String name) {
        URL url = null;
        if (system != null) {
            url = system.getResource(name); 
        }
        if (url == null) {
            url = findResource(name);
            if (url == null && name.startsWith("/"))
            {
            	url = findResource(name.substring(1));
            }
            if (url == null) {
                // This call to getResource may eventually call findResource again, in case the parent doesn't find anything.
                url = super.getResource(name);
            }
        }
        return url;
    }

    public Enumeration<URL> getResources(String name) throws IOException {
        /**
        * Similar to super, but local resources are enumerated before parent resources
        */
        Enumeration<URL> systemUrls = null;
        if (system != null) {
            systemUrls = system.getResources(name);
        }
        Enumeration<URL> localUrls = findResources(name);
        Enumeration<URL> parentUrls = null;
        if (getParent() != null) {
            parentUrls = getParent().getResources(name);
        }
        final List<URL> urls = new ArrayList<URL>();
        if (systemUrls != null) {
            while(systemUrls.hasMoreElements()) {
                urls.add(systemUrls.nextElement());
            }
        }
        if (localUrls != null) {
            while (localUrls.hasMoreElements()) {
                urls.add(localUrls.nextElement());
            }
        }
        if (parentUrls != null) {
            while (parentUrls.hasMoreElements()) {
                urls.add(parentUrls.nextElement());
            }
        }
        return new Enumeration<URL>() {
            Iterator<URL> iter = urls.iterator();

            public boolean hasMoreElements() {
                return iter.hasNext(); 
            }
            public URL nextElement() {
                return iter.next();
            }
        };
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = findClassData(name);
        if(data != null) {
            return defineClass(name, data, 0, data.length);
        } else {
            return super.findClass(name);
        }
    }
    private byte[] findClassData(String name) {
        return (byte[])classes.remove(name);
    }
    public InputStream getResourceAsStream(String name) {
        InputStream stream = getParent().getResourceAsStream(name);
        if(stream == null) {
            byte[] buf = (byte[])others.get(name);
            if(buf != null) {
                stream = new ByteArrayInputStream(buf);
            }
        }
        return stream;
    }
    public void addJar(JarInputStream stream) {
        byte[] buf = new byte[BUFFER_SIZE];
        int count;
        try {
            while(true) {
                JarEntry entry = stream.getNextJarEntry();
                if(entry == null)
                    break;
                String name = entry.getName();
                int size = (int)entry.getSize();
                ByteArrayOutputStream out =
                    size >= 0 ? new ByteArrayOutputStream(size)
                              : new ByteArrayOutputStream(BUFFER_SIZE);
                while((count = stream.read(buf)) > -1)
                    out.write(buf, 0, count);
                out.close();
                if(name.endsWith(".class")) {
                    classes.put(getClassName(name), out.toByteArray());
                } else {
                    others.put(name, out.toByteArray());
                }
                out.toByteArray();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private static String getClassName(String fileName) {
        return fileName.substring(0, fileName.length()-6).replace('/','.');
    }

}
