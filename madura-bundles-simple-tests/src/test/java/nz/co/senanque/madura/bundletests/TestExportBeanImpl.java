package nz.co.senanque.madura.bundletests;

import nz.co.senanque.madura.bundle.BundleExport;
import nz.co.senanque.madura.testbeans.TestExportBean;

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
