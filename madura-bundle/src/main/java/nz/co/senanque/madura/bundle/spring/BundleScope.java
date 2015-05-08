package nz.co.senanque.madura.bundle.spring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nz.co.senanque.madura.bundle.BundleManager;
import nz.co.senanque.madura.bundle.BundleRoot;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.RequestContextHolder;

public class BundleScope implements Scope {
    @Autowired private transient BundleManager m_bundleManager;
	private Map<String, Object> objectMap = Collections
			.synchronizedMap(new HashMap<String, Object>());

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#get(java.lang.String,
	 *      org.springframework.beans.factory.ObjectFactory)
	 */
	public Object get(String name, ObjectFactory<?> objectFactory) {
		String keyName = getConversationId()+'/'+name;
		if (!objectMap.containsKey(keyName)) {
			objectMap.put(keyName, objectFactory.getObject());
		}
		return objectMap.get(keyName);

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#remove(java.lang.String)
	 */
	public Object remove(String name) {
		String keyName = getConversationId()+'/'+name;
		return objectMap.remove(keyName);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#registerDestructionCallback
         *  (java.lang.String, java.lang.Runnable)
	 */
	public void registerDestructionCallback(String name, Runnable callback) {
		String keyName = getConversationId()+'/'+name;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#resolveContextualObject(java.lang.String)
	 */
	public Object resolveContextualObject(String key) {
		return null;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.config.Scope#getConversationId()
	 */
	public String getConversationId() {
		String sessionId = "none";
		try {
			sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		} catch (Exception e) {
			// ignore exceptions and use 'none'
		}
		String bundleId = "none";
		BundleRoot bundleRoot = m_bundleManager.getBundle();
		if (bundleRoot != null) {
			bundleId = bundleRoot.getName();
		}
		return sessionId+'/'+bundleId;
	}

	/**
	 * clear the beans
	 */
	public void clearBean() {
		objectMap.clear();
	}

	public BundleManager getBundleManager() {
		return m_bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		m_bundleManager = bundleManager;
	}
}
