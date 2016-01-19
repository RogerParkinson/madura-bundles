/*******************************************************************************
 * Copyright (c)2016 Prometheus Consulting
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nz.co.senanque.madura.bundle.spring;

import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Roger Parkinson
 *
 */
public class SessionIdProviderImpl implements SessionIdProvider {

	/* (non-Javadoc)
	 * @see nz.co.senanque.madura.bundle.spring.SessionIdProvider#getSessionId()
	 */
	@Override
	public String getSessionId() {
		String sessionId = "none";
		try {
			sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		} catch (Exception e) {
			// ignore exceptions and use 'none'
		}
		return sessionId;
	}

}
