package com.khpark.parallel.executor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorLogger {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorLogger.class);
	private Map<String, Long> timer = new HashMap<String, Long>();

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

	private void prettyPrint() {
		StringBuilder sb = new StringBuilder();
		int firstKeyLength = timer.keySet().stream().findFirst().toString().length();
		int columnCount = firstKeyLength + 30;
		String line = "";

		for (int i = 0; i < columnCount; i++) {
			line += "-";
		}

		sb.append("\n" + line + "\n");

		for (String keyName : timer.keySet()) {
			sb.append(" taskName = ").append(keyName);
			sb.append(", execute time =  ");
			sb.append(timer.get(keyName));
			sb.append(" ms\n");
		}

		long longestTime = timer.entrySet().stream().max((x, y) -> Long.compare(x.getValue(), y.getValue())).get().getValue();
		sb.append(line + "\n");
		sb.append(" Total Execute time = " + longestTime + " ms\n");
		sb.append(line + "\n");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(sb.toString());
		}
	}
}
