package nl.iwelcome.connector.google.domain;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * GoogleAtom is the java representation of a Google Atom message.
 * 
 * A typical message could be:
 * <atom:entry xmlns:atom='http://www.w3.org/2005/Atom' xmlns:apps='http://schemas.google.com/apps/2006'>
 *   <id>https://apps-apis.google.com/a/feeds/customer/2.0/C03az79cb</id>
 *   ...
 *   <apps:property name="name" value="new organization unit's name" />
 *   <apps:property name="description" value="new organization description" />
 *   <apps:property name="parentOrgUnitPath" value="new parent path/organization unit" />
 *   <apps:property name="blockInheritance" value="true or false" />
 *   <apps:property name="usersToMove" value="liz@example.com, namrata@example.com, jake@example.com" />
 * </atom:entry>
 * 
 * The message is annotated with JAXB XML annotations.
 * 
 */
@XmlRootElement(name = GoogleConstants.ATOM_NAMESPACE_NAME, namespace = GoogleConstants.ATOM_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class GoogleAtom {

	@XmlElement(namespace=GoogleConstants.ATOM_NAMESPACE)
	private String id;

	@XmlElementRef(namespace=GoogleConstants.GOOGLE_NAMESPACE)
	private GoogleProperty[] properties;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GoogleProperty[] getProperties() {
		return properties;
	}

	public void setProperties(GoogleProperty[] properties) {
		this.properties = properties;
	}
	
	public String getCustomerId() {
		for(GoogleProperty current: properties) {
			if(current.getName().equals(GoogleConstants.CUSTOMER_ID_MESSAGE_FIELD)) {
				return current.getValue();
			}
		}
		return null;
	}

	@Override
    public String toString() {
	    return "GoogleAtom [id=" + id + ", properties=" + Arrays.toString(properties) + "]";
    }
	
}
