package nz.co.senanque.madura.bundle;

import java.io.InputStream;
import java.net.URL;

import nz.co.senanque.madura.bundlemap.BundleVersion;

public interface BundleManagerDelegate {

	BundleVersion addBundle(String bundleName, InputStream is, URL url);

	BundleVersion addBundle();

	BundleVersion addBundle(InputStream is);

}
