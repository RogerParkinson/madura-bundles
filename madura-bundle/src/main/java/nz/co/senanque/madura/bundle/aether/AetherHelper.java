/*******************************************************************************
 * Copyright (c) 2010, 2014 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation
 *******************************************************************************/
package nz.co.senanque.madura.bundle.aether;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper to boot the repository system and a repository system session.
 * You can configure the repositories it looks for using two system properties.
 * madura.maven.local.repo defaults to ~/.m2/repository and madura.maven.remote.repo
 * contains a list of external repositories. Each comma separated repo has three comma separated fields.
 * central,default,http://repo1.maven.org/maven2/\n
 * is the default value (name,type,url)
 */
public class AetherHelper
{
    private static Logger m_logger = LoggerFactory.getLogger(AetherHelper.class);

	private static File localUserRepo = getLocalRepoLocation();
	private static List<RemoteRepository> remoteUserRepos = getRemoteRepoLocations();
	
	private static File getLocalRepoLocation() {
		String repo = System.getProperty("madura.maven.local.repo");
		if (repo == null) {
			repo = System.getProperty("user.home") + "/.m2/repository";
		}
		m_logger.debug("repo={}",repo);
		File ret = new File(repo);
		assert(ret != null);
		assert(ret.exists());
		return ret;
	}

	private static List<RemoteRepository> getRemoteRepoLocations() {
		String repo = System.getProperty("madura.maven.remote.repo");
		if (repo == null) {
			repo = "central,default,http://repo1.maven.org/maven2/";
		}
		m_logger.debug("repos={}",repo);
		List<RemoteRepository> ret = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(repo,",");
		while (st.hasMoreTokens()) {
			String id = st.nextToken();
			String type = st.nextToken();
			String url = st.nextToken();
	        RemoteRepository r = new RemoteRepository.Builder( id,type,url ).build();
			ret.add(r);
		}
		return ret;
	}

    public static RepositorySystem newRepositorySystem()
    {
        return ManualRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.guice.GuiceRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.sisu.SisuRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.plexus.PlexusRepositorySystemFactory.newRepositorySystem();
    }

    public static DefaultRepositorySystemSession newRepositorySystemSession( RepositorySystem system )
    {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository( localUserRepo );
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

//        session.setTransferListener( new ConsoleTransferListener() );
//        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public static List<URL> extractURLClassPath(String artifact) throws DependencyCollectionException, DependencyResolutionException {

    	RepositorySystem repoSystem = AetherHelper.newRepositorySystem();
        
        RepositorySystemSession session = AetherHelper.newRepositorySystemSession( repoSystem );
 
        Dependency dependency =
            new Dependency( new DefaultArtifact( artifact ), "compile" );
 
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( dependency );
        collectRequest.setRepositories(remoteUserRepos);
        DependencyNode node = repoSystem.collectDependencies( session, collectRequest ).getRoot();
 
        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot( node );
 
        repoSystem.resolveDependencies( session, dependencyRequest  );
 
        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        node.accept( nlg );
        List<URL> urls = extractDependencies(node);
        return urls;
	
    }
	private static List<URL> extractDependencies(DependencyNode dependencyNode) {
		List<URL> urls = new ArrayList<URL>();
		URL url = getURL(dependencyNode);
		if (url != null) {
			urls.add(url);
		}
		for (DependencyNode child :dependencyNode.getChildren()) {
			urls.addAll(extractDependencies(child));
		}
		return urls;
	}
	private static URL getURL(DependencyNode dependencyNode) {
    	File f = dependencyNode.getArtifact().getFile();
    	URL url = null;
		try {
			url = f.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		return url;
	}

}
