package nz.co.senanque.madura.testbeans;

import nz.co.senanque.madura.bundle.BundleInterface;

@BundleInterface("valueInjectedBean")
public interface ValueInjectedBean {

	public abstract String getValue();

}