package com.khpark.parallel.executor;

import java.util.Formatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorLogger {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorLogger.class);
	private Map<String, Long> timer = new ConcurrentHashMap<String, Long>();
	private static final int FIXED_LENGTH = 38;

	public void setStartTimeTask(String key) {
		timer.put(key, System.currentTimeMillis());
	}

	public void setEndTimeTask(String key) {
		long endTime = System.currentTimeMillis();
		timer.put(key, endTime - timer.get(key).longValue());
	}

	public void showTaskInfo() {
		prettyPrint();
	}

	@SuppressWarnings("resource")
	private void prettyPrint() {
		StringBuffer sb = new StringBuffer();
		Formatter f = new Formatter(sb);

		int maxKeyLength = timer.entrySet().stream().max((x, y) -> Integer.compare(x.getKey().length(), y.getKey().length())).get().getKey().length();
		int columnCount = maxKeyLength + FIXED_LENGTH;
		String line = "";

		for (int i = 0; i < columnCount; i++) {
			line += "-";
		}

		f.format("%n%s%n", line);

		for (String keyName : timer.keySet()) {
			f.format("%s ", "taskName = " + keyName + addEmptySpace(keyName, maxKeyLength));
			f.format("%s %n", ", execute time = " + timer.get(keyName) + " ms");
		}

		long longestTime = timer.entrySet().stream().max((x, y) -> Long.compare(x.getValue(), y.getValue())).get().getValue();
		f.format("%s%n", line);
		f.format("%s %n", " Total Execute time = " + longestTime + " ms");
		f.format("%s%n", line);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(f.toString());
		}
	}

	private String addEmptySpace(String keyName, int maxKeyLength) {
		int emptypSpaceLength = maxKeyLength - keyName.length();
		String space = "";

		for (int i = 0; i < emptypSpaceLength; i++) {
			space += " ";
		}
		return space;
	}
}
