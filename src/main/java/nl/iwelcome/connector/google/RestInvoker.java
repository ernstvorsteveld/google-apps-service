/**
 * Copyright (C) 2009-2010 Wilfred Springer
 * Changed by Ernst Vorsteveld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.iwelcome.connector.google;

import nl.iwelcome.connector.google.domain.GoogleAtom;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * A simple wrapper around RestTemplate, providing the illusion of a more fluent
 * interface. Typically used like this:
 * 
 * <pre>
 * RestInvoker.preparePostTo(uri)
 *            .expecting(String.class)
 *            .withParam(&quot;foo&quot;, &quot;bar&quot;)
 *            .execute();
 * </pre>
 * <p/>
 * <p>
 * For now, mostly useful for doing POSTs.
 * </p>
 */
public class RestInvoker {

	private final HttpMethod method;
	private final String uri;
	private final RestOperations operations;

	private RestInvoker(HttpMethod method, String uri) {
		this(method, uri, new RestTemplate());
	}

	private RestInvoker(HttpMethod method, String uri, RestOperations operations) {
		this.method = method;
		this.uri = uri;
		this.operations = operations;
	}

	public static RestInvoker preparePostTo(String uri) {
		return new RestInvoker(HttpMethod.POST, uri);
	}

	public static RestInvoker prepareGetTo(String uri) {
		return new RestInvoker(HttpMethod.GET, uri);
	}

	public static RestInvoker preparePutTo(String uri) {
		return new RestInvoker(HttpMethod.PUT, uri);
	}

	/**
	 * Offers the option of using an alternative implementation of
	 * {@link org.springframework.web.client.RestOperations}.
	 */
	public RestInvoker using(RestOperations operations) {
		return new RestInvoker(method, uri, operations);
	}

	/**
	 * Sets the expected type of result.
	 */
	public <T> TypedRestInvoker<T> expecting(Class<T> type) {
		return new TypedRestInvoker<T>(type);
	}

	public class TypedRestInvoker<T> {

		private final Class<T> type;
		private MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		private GoogleAtom objectToSend;

		private TypedRestInvoker(Class<T> type) {
			this.type = type;
		}

		/**
		 * Sets the given parameter to the given value.
		 */
		public TypedRestInvoker<T> withParam(String name, Object value) {
			parameters.add(name, value);
			return this;
		}

		/**
		 * Executes the request.
		 */
		public T execute() {
			switch (method) {
			case POST: {
				return operations.postForObject(uri, parameters, type);
			}
			case GET: {
				return operations.getForObject(uri, type, parameters);
			}
			case PUT: {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_ATOM_XML);
				HttpEntity<GoogleAtom> entity = new HttpEntity<GoogleAtom>(objectToSend, headers);
				return operations.exchange(uri, HttpMethod.PUT, entity, type).getBody();
			}
			default: {
				throw new UnsupportedOperationException();
			}
			}
		}

		public TypedRestInvoker<T> withObject(GoogleAtom usersToMoveMessage) {
			this.objectToSend = usersToMoveMessage;
			return this;
		}
	}
}
