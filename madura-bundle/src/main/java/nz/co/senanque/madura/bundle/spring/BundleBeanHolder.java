package nz.co.senanque.madura.bundle.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleBeanHolder {

    private Logger m_logger = LoggerFactory.getLogger(this.getClass());
	private final String m_name;
	private Object m_bean;
	private Runnable m_callback;

	public BundleBeanHolder(String name) {
		m_name = name;
	}

	public String getName() {
		return m_name;
	}

	public Object getBean() {
		return m_bean;
	}

	public void setBean(Object bean) {
		m_bean = bean;
	}

	public Runnable getCallback() {
		return m_callback;
	}

	public void setCallback(Runnable callback) {
		m_callback = callback;
	}

	public void cleanup() {
		if (m_callback == null) {
			return;
		}
		try {
			m_callback.run();
		} catch (Exception e) {
			m_logger.warn(e.getMessage(),e);
		}
	}

}
