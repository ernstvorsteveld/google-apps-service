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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import nl.iwelcome.connector.google.domain.GoogleAtom;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * An implementation of {@link nl.flotsam.greader.GoogleAppsOperations} managing
 * authentication for its clients.
 * 
 * http://code.google.com/apis/accounts/docs/AuthForInstalledApps.html#Request
 */
public class GoogleAppsTemplate implements GoogleAppsOperations {

	private final String email;
	private final String password;
	private final RestTemplate restTemplate;
	private final String loginUrl;
	private final String baseUrl;
	private final String customerIdUrl;
	private final String moveToGroupUrl;

	private final AtomicReference<String> auth = new AtomicReference<String>();

	/**
	 * Constructs a new instance.
	 * 
	 * @param email
	 *            The login the account to manage.
	 * @param password
	 *            The password.
	 * @param tracing
	 *            A boolean indicating if tracing should be enabled.
	 */
	public GoogleAppsTemplate(String email, String password, String loginUrl, String baseUrl, String customerIdUrl,
	        String moveToGroupUrl, boolean tracing) {
		this.loginUrl = loginUrl;
		this.baseUrl = baseUrl;
		this.customerIdUrl = customerIdUrl;
		this.moveToGroupUrl = moveToGroupUrl;
		this.password = password;
		this.email = email;
		restTemplate = new AuthenticatingRestTemplate();

		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(
		        restTemplate.getMessageConverters());
		converters.add(new PropertiesHttpMessageConverter());
		converters.add(new XmlMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new ByteArrayHttpMessageConverter());
		restTemplate.setMessageConverters(converters);

		if (tracing) {
			restTemplate.setRequestFactory(new TracingClientHttpRequestFactory(restTemplate.getRequestFactory()));
		}
	}

	/**
	 * Constructs a new instance.
	 * 
	 * @param email
	 *            The email address of the account to manage.
	 * @param password
	 *            The password.
	 */
	public GoogleAppsTemplate(String email, String password, String loginUrl, String baseUrl, String customerIdUrl,
	        String moveToGroupUrl) {
		this(email, password, loginUrl, baseUrl, customerIdUrl, moveToGroupUrl, false);
	}

	@Override
	public <T> T doWithCallback(ReaderCallback<T> callback) {
		if (auth.get() == null) {
			updateAuth();
		}
		return callback.execute(restTemplate);
	}

	@Override
	public String getToken() {
		return doWithCallback(new ReaderCallback<String>() {
			@Override
			public String execute(RestOperations operations) {
				return operations.getForObject(baseUrl, String.class);
			}
		});
	}

	private void updateAuth() {
		String current = auth.get();
		String replacement = authenticate();
		auth.compareAndSet(current, replacement);
	}

	private String authenticate() {
		Properties result = RestInvoker.preparePostTo(loginUrl)
		        .using(restTemplate)
		        .expecting(Properties.class)
		        .withParam("accountType", "HOSTED")
		        .withParam("Email", email)
		        .withParam("Passwd", password)
		        .withParam("service", "apps")
		        .execute();
		return (String) result.get("Auth");
	}

	private class AuthenticatingRestTemplate extends RestTemplate {
		@Override
		protected <T> T doExecute(URI url, HttpMethod method, final RequestCallback requestCallback,
		        ResponseExtractor<T> responseExtractor) throws RestClientException {
			return super.doExecute(url, method, new RequestCallback() {
				@Override
				public void doWithRequest(ClientHttpRequest request) throws IOException {
					String auth = GoogleAppsTemplate.this.auth.get();
					if (auth != null) {
						request.getHeaders().set("Authorization", "GoogleLogin auth=" + auth);
					}
					requestCallback.doWithRequest(request);
				}
			}, responseExtractor);
		}
	}

	public byte[] getUser(final String token, final String name) {
		return doWithCallback(new ReaderCallback<byte[]>() {
			@Override
			public byte[] execute(RestOperations operations) {
				byte[] result = RestInvoker
				                .prepareGetTo(baseUrl + name)
				                .using(operations)
				                .expecting(byte[].class)
				                .withParam("T", token)
				                .execute();
				return result;
			}
		});
	}

	public byte[] getAllUser(final String token) {
		return doWithCallback(new ReaderCallback<byte[]>() {
			@Override
			public byte[] execute(RestOperations operations) {
				byte[] result = RestInvoker
				        .prepareGetTo(baseUrl)
				        .using(operations)
				        .expecting(byte[].class)
				        .withParam("T", token)
				        .execute();
				return result;
			}
		});
	}

	public byte[] getCustomerId(final String token) {
		return doWithCallback(new ReaderCallback<byte[]>() {
			@Override
			public byte[] execute(RestOperations operations) {
				byte[] result = RestInvoker
				        .prepareGetTo(customerIdUrl)
				        .using(operations)
				        .expecting(byte[].class)
				        .withParam("T", token)
				        .execute();
				return result;
			}
		});
	}

	// https://apps-apis.google.com/a/feeds/orgunit/2.0/the customerId/full
	// organization unit's path
	// the customerId/the organization user's email
	public byte[] moveUserToGroup(final String token, final String customerId, final String orgUnit,
	        final GoogleAtom usersToMoveMessage) {
		return doWithCallback(new ReaderCallback<byte[]>() {
			@Override
			public byte[] execute(RestOperations operations) {
				byte[] result = RestInvoker
				        .preparePutTo(moveToGroupUrl + customerId + "/" + orgUnit)
				        .using(operations)
				        .expecting(byte[].class)
				        .withParam("T", token)
				        .withObject(usersToMoveMessage)
				        .execute();
				return result;
			}
		});
	}

}
