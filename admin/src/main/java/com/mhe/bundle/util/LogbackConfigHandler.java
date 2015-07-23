package com.mhe.bundle.util;

import java.io.ByteArrayInputStream;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

@Service("logbackConfigHandler")
public class LogbackConfigHandler {

	public void changeLevel(String loggerName, String logLevel) {
		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		Logger lglogger = context.getLogger(loggerName);
		lglogger.setLevel(Level.toLevel(logLevel));
	}
	public static void setErrorLogLevel() {
		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		for(Logger logger:context.getLoggerList()){
			logger.setLevel(Level.ERROR);
		}
	}
	public void configure(byte[] configFileBytes) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure(new ByteArrayInputStream(configFileBytes));
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printIfErrorsOccured(lc);
	}
}
