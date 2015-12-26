package nz.co.senanque.madura.bundlemap;

public class BundleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BundleNotFoundException() {
	}

	public BundleNotFoundException(String message) {
		super(message);
	}

	public BundleNotFoundException(Throwable cause) {
		super(cause);
	}

	public BundleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BundleNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
