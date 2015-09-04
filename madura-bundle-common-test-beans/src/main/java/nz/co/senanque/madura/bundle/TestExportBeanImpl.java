package nz.co.senanque.madura.bundle;

import org.springframework.stereotype.Component;

@Component("exportBean")
@BundleExport
public class TestExportBeanImpl {
	
	public String toString () {
		return "this is a test export";
	}

}
