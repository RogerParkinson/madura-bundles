/**
 * 
 */
package nz.co.senanque.madura.bundle.spring;

import nz.co.senanque.madura.bundle.BundleManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Roger Parkinson
 *
 */
@Component
public class BundleScheduler {
	
	@Autowired BundleManager m_bundleManager;

	@Scheduled(fixedDelayString="${nz.co.senanque.madura.bundle.spring.BundleScheduler.scan:10000}")
	public void scan() throws Exception {
		m_bundleManager.scan();
	}
}