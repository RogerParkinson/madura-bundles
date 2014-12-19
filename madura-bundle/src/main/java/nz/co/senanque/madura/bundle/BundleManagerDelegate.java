package nz.co.senanque.madura.bundle;

import java.io.InputStream;

public interface BundleManagerDelegate {

	void addBundle(String bundleName, InputStream is);

}
