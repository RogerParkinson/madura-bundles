package nz.co.senanque.madura.testbeans;

public class BundleSessionBeanImpl implements TestExportBean2 {
	
	private int counter=0;

	public String toString() {
		return "";
	}
	
	public int getCounter() {
		return counter++;
	}
}
