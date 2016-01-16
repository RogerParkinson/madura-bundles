package nz.co.senanque.madura.testbeans;

import nz.co.senanque.madura.bundle.BundleExport;

import org.springframework.stereotype.Component;

@Component("exportBean")
@BundleExport
public class TestExportBeanImpl implements TestExportBean {
	
	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.TestExportBean#toString()
	 */
	@Override
	public String toString () {
		return "this is a test export";
	}

}
