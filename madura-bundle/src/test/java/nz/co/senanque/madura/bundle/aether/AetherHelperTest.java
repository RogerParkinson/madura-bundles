package nz.co.senanque.madura.bundle.aether;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.junit.Ignore;
import org.junit.Test;

public class AetherHelperTest {

	@Test @Ignore
	public void extractURLClassPathTest() throws DependencyCollectionException, DependencyResolutionException {

        List<URL> urls = AetherHelper.extractURLClassPath("org.apache.maven:maven-profile:2.2.1");
        assertEquals(7,urls.size());
        File file = new File(urls.get(0).getFile());
        try {
			URL url = new URL("jar:file://"+file.getAbsolutePath()+"!/META-INF/DEPENDENCIES");
	        assertNotNull(url);
	        InputStream is = url.openStream();
	        is.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//        urls = AetherHelper.extractURLClassPath("nz.co.senanque:madura-bundle-maven:3.9.4-SNAPSHOT");
//        assertEquals(1,urls.size());
	}
	@Test @Ignore
	public void artifactTest() throws DependencyCollectionException, DependencyResolutionException {

		Artifact artifact = new DefaultArtifact("nz.co.senanque:order-workflow:1.0.0-SNAPSHOT.jar");
//		String baseVersion = artifact.getBaseVersion();
		String version = artifact.getVersion();
		version.toString();
	}
}
