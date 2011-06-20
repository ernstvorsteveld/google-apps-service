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

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import nl.iwelcome.connector.google.domain.GoogleAtom;
import nl.iwelcome.connector.google.domain.GoogleConstants;
import nl.iwelcome.connector.google.domain.GoogleProperty;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class GoogleAppsService {

	private GoogleAppsTemplate googleAppsTemplate;

	private Jaxb2Marshaller marshaller;

	public GoogleAppsService(String email, String password, String loginUrl, String baseUrl, String customerIdUrl,
	        String moveToGroupUrl) {
		this.googleAppsTemplate = new GoogleAppsTemplate(email, password, loginUrl, baseUrl, customerIdUrl,
		        moveToGroupUrl);
		this.marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(GoogleAtom.class, GoogleAtom.class);
	}

	/**
	 * Typical message:
	 * 
	 * <atom:entry xmlns:atom='http://www.w3.org/2005/Atom' xmlns:apps='http://schemas.google.com/apps/2006'>
	 *  ... <apps:property name="name" value="new organization unit's name" />
	 *  	<apps:property name="description" value="new organization description" />
	 *  	<apps:property name="parentOrgUnitPath" value="new parent path/organization unit" />
	 *  	<apps:property name="blockInheritance" value="true or false" />
	 *  	<apps:property name="usersToMove" value="liz@example.com, namrata@example.com, jake@example.com" />
	 * </atom:entry>
	 */
	public boolean moveUserToOrgUnit(String user, String orgUnit) {
		GoogleAtom customerIdResponse = getCustomerId();
		GoogleAtom moveToOrgUnitRequest = getMoveToOrgUnitRequest(user, orgUnit, customerIdResponse.getId());

		byte[] resultBytes = googleAppsTemplate.moveUserToGroup(customerIdResponse.getId(),
		        customerIdResponse.getCustomerId(),
		        orgUnit,
		        moveToOrgUnitRequest);
		return resultBytes != null;
	}

	private GoogleAtom getMoveToOrgUnitRequest(String user, String orgUnit, String customerId) {
		GoogleAtom moveToOrgUnitRequest = new GoogleAtom();
		GoogleProperty[] properties = new GoogleProperty[3];
		GoogleProperty customerIdGoogleProperty = new GoogleProperty(GoogleConstants.CUSTOMER_ID_MESSAGE_FIELD,
		        customerId);
		properties[0] = customerIdGoogleProperty;
		GoogleProperty nameGoogleProperty = new GoogleProperty(GoogleConstants.NAME_MESSAGE_FIELD, orgUnit);
		properties[1] = nameGoogleProperty;
		GoogleProperty usersToMoveGoogleProperty = new GoogleProperty(GoogleConstants.USERS_TO_MOVE_MESSAGE_FIELD, user);
		properties[2] = usersToMoveGoogleProperty;
		moveToOrgUnitRequest.setProperties(properties);
		return moveToOrgUnitRequest;
	}

	private GoogleAtom getCustomerId() {
		String token = googleAppsTemplate.getToken();
		byte[] bytes = googleAppsTemplate.getCustomerId(token);

		Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes));

		Source source = new StreamSource(reader);
		GoogleAtom googleAtom = (GoogleAtom) marshaller.unmarshal(source);
		return googleAtom;
	}
}
