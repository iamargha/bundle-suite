package com.mhe.bundle.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.mhe.bundle.common.Logger;

@Provider
@Consumes("application/json")
public class JSONBodyReader implements MessageBodyReader<Object> {

	private static  Logger logger = Logger.getInstance(JSONBodyReader.class);
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2,
			MediaType arg3) {
		logger.debug("Inside isReadable of JSONBodyReader");
		return true;
	}

	public Object readFrom(Class<Object> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		String jSonString = readStream(entityStream);
		Gson gson = new Gson();	
		return gson.fromJson(jSonString,type);	
		
	}

	private static String readStream(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);

		byte[] buffer = new byte[1024];
		int read;
		StringBuffer sb = new StringBuffer();
		while ((read = bis.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, read));
		}

		return sb.toString();
	}
}
