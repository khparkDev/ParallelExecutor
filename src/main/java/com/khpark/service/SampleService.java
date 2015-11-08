package com.khpark.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khpark.model.SampleModel;

public class SampleService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleService.class);

	public boolean testService() throws InterruptedException {
		Thread.sleep(100L);
		LOGGER.debug(getMethodName());
		return true;
	}

	public void testService(SampleModel model) throws InterruptedException {
		Thread.sleep((long) (Math.random() * 100 + 100));
		LOGGER.debug(getMethodName() + " = {model.getName() = " + model.getName() + "}");
	}

	public void testService(String msg) throws InterruptedException {
		Thread.sleep(100L);
		LOGGER.debug(getMethodName() + " = {msg = " + msg + "}");
	}

	public void testService(int first, double second, float third) throws InterruptedException {
		Thread.sleep(100L);
		LOGGER.debug(getMethodName() + " = {" + first + ", " + second + "," + third + "}");
	}

	public void testService(boolean first, char second, byte third, short fourth) throws InterruptedException {
		Thread.sleep(100L);
		LOGGER.debug(getMethodName() + " = {" + first + ", " + second + ", " + third + ", " + fourth + "}");
	}

	private String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
}
