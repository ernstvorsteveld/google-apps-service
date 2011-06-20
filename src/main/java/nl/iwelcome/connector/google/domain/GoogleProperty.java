/**
 * Copyright (C) 2009-2010 Ernst Vorsteveld
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
package nl.iwelcome.connector.google.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * GoogleProperty is the java representation of a property element in a 
 * GoogleAtom mesasge.
 * 
 * The xml representation is typically:
 * <apps:property name="name" value="new organization unit's name" />
 * where apps is namespace: xmlns:apps='http://schemas.google.com/apps/2006'
 * 
 */
@XmlRootElement(name = GoogleConstants.GOOGLE_NAMESPACE_NAME, namespace = GoogleConstants.GOOGLE_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class GoogleProperty {

	@XmlAttribute(name = GoogleConstants.NAME_MESSAGE_FIELD)
	String name;

	@XmlAttribute(name = GoogleConstants.VALUE_MESSAGE_FIELD)
	String value;

	public GoogleProperty() {
	}

	public GoogleProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString() {
	    return "GoogleProperty [name=" + name + ", value=" + value + "]";
    }
	
	

}
