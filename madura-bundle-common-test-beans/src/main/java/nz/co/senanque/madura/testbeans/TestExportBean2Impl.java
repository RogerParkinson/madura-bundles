package nz.co.senanque.madura.testbeans;

import javax.annotation.PostConstruct;


public class TestExportBean2Impl implements TestExportBean2 {
	
	private int counter=0;
	
	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.TestExportBean#toString()
	 */
	@Override
	public String toString () {
		return "this is a test export";
	}
	
    @PostConstruct
    public void init() {
    }

	public int getCounter() {
		return counter++;
	}

}
