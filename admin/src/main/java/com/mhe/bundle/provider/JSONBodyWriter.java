package com.mhe.bundle.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mhe.bundle.common.Logger;

/**
 * 
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JSONBodyWriter implements MessageBodyWriter<Object> {
	private static Logger logger = Logger.getInstance(JSONBodyWriter.class);

	public long getSize(Object t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return true;
	}


	public void writeTo(Object t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		GsonBuilder gsonBuilder = new GsonBuilder();
		
		Gson gson = gsonBuilder.create();
		OutputStreamWriter osw = new OutputStreamWriter(entityStream);		
		String responseJson = gson.toJson(t);
		osw.write(responseJson);
		osw.flush();
		
		logger.debug(" Set response string::{} into thread local", new Object[] { responseJson });
	}

}
