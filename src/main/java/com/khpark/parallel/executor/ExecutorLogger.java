package com.khpark.parallel.executor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		int columnCount = firstKeyLength > 13 ? 25 + firstKeyLength : 25;
		String line = "";
		String emptySpace = "";

		for (int i = 0; i < columnCount; i++) {
			line += "-";
		}

		for (int i = 0; i < columnCount / 10; i++) {
			emptySpace += " ";
		}

		sb.append("\n");
		sb.append(line + "\n");
		sb.append(" taskName " + emptySpace + " elasedTime (ms)\n");
		sb.append(line + "\n");

		for (String keyName : timer.keySet()) {
			sb.append(" ").append(keyName).append(emptySpace);
			sb.append(timer.get(keyName)).append("\n");
		}

		//timer.entrySet().stream().mapToLong()
		
		sb.append(line + "\n");
//		sb.append(" Execute time = " + timer.entrySet().stream().filter( + "\n");
		sb.append(line + "\n");

		LOGGER.info(sb.toString());
	}
}
