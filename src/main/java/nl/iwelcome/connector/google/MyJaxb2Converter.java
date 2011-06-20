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
package nl.iwelcome.connector.google;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import nl.iwelcome.connector.google.domain.GoogleAtom;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;

public class MyJaxb2Converter extends Jaxb2RootElementHttpMessageConverter {

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Arrays.asList(MediaType.APPLICATION_ATOM_XML);
	}

	@Override
    protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws IOException {
	    // TODO Auto-generated method stub
	    return super.readFromSource(clazz, headers, source);
    }

	@Override
    protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException {
	    // TODO Auto-generated method stub
	    super.writeToResult(o, headers, result);
    }

	@Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
	    // TODO Auto-generated method stub
	    return super.canRead(clazz, mediaType);
    }

	@Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
	    // TODO Auto-generated method stub
	    return super.canWrite(clazz, mediaType);
    }

	@Override
    protected boolean supports(Class<?> clazz) {
		return clazz.getName().equals(GoogleAtom.class);
    }
	
	

}
