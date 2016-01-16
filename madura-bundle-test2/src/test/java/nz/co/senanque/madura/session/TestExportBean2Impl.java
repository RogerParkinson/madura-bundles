package nz.co.senanque.madura.session;

import nz.co.senanque.madura.testbeans.TestExportBean2;

public class TestExportBean2Impl implements TestExportBean2 {
	
	private int counter=0;
	
	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.TestExportBean#toString()
	 */
	@Override
	public String toString () {
		return "this is a test export";
	}
	
	public int getCounter() {
		return counter++;
	}

}
