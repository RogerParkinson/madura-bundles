package nz.co.senanque.madura.bundle.aether;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.junit.Ignore;
import org.junit.Test;

public class AetherHelperTest {

	@Test// @Ignore
	public void test() throws DependencyCollectionException, DependencyResolutionException {

		Properties properties = System.getProperties();
//		properties.list(System.out);
        List<URL> urls = AetherHelper.extractURLClassPath("org.apache.maven:maven-profile:2.2.1");
        assertEquals(7,urls.size());
//        urls = AetherHelper.extractURLClassPath("nz.co.senanque:madura-bundle-maven:3.9.4-SNAPSHOT");
//        assertEquals(1,urls.size());
	}
}
