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
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class XmlMessageConverter extends AbstractHttpMessageConverter<String> {

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Arrays.asList(MediaType.APPLICATION_XML);
	}
	

	@Override
	protected String readInternal(Class<? extends String> arg0, HttpInputMessage arg1) throws IOException,
	        HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void writeInternal(String arg0, HttpOutputMessage arg1) throws IOException,
	        HttpMessageNotWritableException {
		// TODO Auto-generated method stub

	}

}
