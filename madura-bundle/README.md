madura-bundle
============

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nz.co.senanque/madura-bundles/badge.svg)](http://mvnrepository.com/artifact/nz.co.senanque/madura-bundles)

[![build_status](https://travis-ci.org/RogerParkinson/madura-bundles.svg?branch=master)](https://travis-ci.org/RogerParkinson/madura-bundles)

Here's the kind of problem we're solving. Say we have a bunch of resources and code which relates to a specific set of products and we have application code which calls on the product code. We want to be able to change the products without dropping the server. Using Madura Bundle we bundle the products resources and code into a jar file. We can then arrange for that jar file to be loaded dynamically.

The application code, when it wants to access the product information and code just specifies what bundle it wants to use (of the several that might be active). After that the application code doesn't know or care that it is accessing a bundle. It looks like normal code and normal resources. The bundled resources and code are actually injected into the application classes using Spring, so apart from selecting the bundle, the application code knows nothing about the bundles.

Yes, you can do something like this with OSGi, but not quite all of it. I found that to implement OSGi in our existing software I would have to repackage all of the existing jar files and resources, including in-house and 3rd party ones. The key difference between this and OSGi is that it will let you access things on the classpath of the calling application.

So all our existing jar files need *no change whatsoever*. To be fair to OSGi it does offer a bunch of things that Madura Bundle doesn't, such as events and security. In an attempt to maintain some compatibility with OSGi the manifest details used by Madura Bundle is designed to be compatible with OSGi. Migrating from Madura Bundle to OSGi has not been tested.

A second key difference between this and OSGi is that the bundles can be loaded from Maven (again, dynamically).

There are two general ways to use Madura Bundles. You can implement a bundle listener. In this you write a listener that will be called whenever a new bundle arrives or is removed from the system. Your listener then locates the relevant beans in the bundle and puts them in some structure you define such as a list. Your application then scans this list for functions. You might use this in the following situations: 

 * You have a list of validation operations that your application needs to call at a certain stage and you want to vary them dynamically. By deploying them in bundles with a bundle listener your
application can register new validation operations as they are added (and remove them if they are deleted).

 * You have various UI components you want to register in a container application. The components might be menu items, with the code to run if they are picked, forms to appear etc. These can be
delivered to the application as bundles which, as they register themselves with the application, add their various components to the UI.

Rather than write a bundle listener you can, in simpler cases, just query the bundle manager for beans of a given type. All beans of that type in all the current bundles will be returned.
The second way to use Madura Bundles is dynamic proxying. In this case you can inject proxied beans from the bundles into your application using Spring. You application is unaware that what was injected was not the actual bean but a proxy. When it calls the bean the proxy maps to the currently selected bundle (there can be only one) transparently. Of course your application must have selected the current bundle before the call takes place.

This is useful where you have a section of an application which is likely to vary over time but you want sessions that were started to keep running the same code. For example if the application is order entry you might want existing orders to keep using the order entry system they started with and new orders to use the newly deployed system. So you would record the bundle name when you save the order and when the order is fetched for further processing you can select the bundle it was saved with. New orders select the latest bundle.


