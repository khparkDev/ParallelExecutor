package com.khpark.parallel.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Formatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExecutorLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorLogger.class);
    private Map<String, Long> timer = new ConcurrentHashMap<>();
    private static final int FIXED_LENGTH = 38;

    public void setStartTimeTask(String key) {
        timer.put(key, System.currentTimeMillis());
    }

    public void setEndTimeTask(String key) {
        long endTime = System.currentTimeMillis();
        timer.put(key, endTime - timer.get(key));
    }

    public void showTaskInfo() {
        prettyPrint();
    }

    private void prettyPrint() {
        StringBuffer sb = new StringBuffer();
        Formatter f = new Formatter(sb);

        int maxKeyLength = timer.entrySet().stream().max(Comparator.comparingInt(x -> x.getKey().length())).get().getKey().length();
        int columnCount = maxKeyLength + FIXED_LENGTH;
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < columnCount; i++) {
            line.append("-");
        }

        f.format("%n%s%n", line.toString());

        for (String keyName : timer.keySet()) {
            f.format("%s ", "taskName = " + keyName + addEmptySpace(keyName, maxKeyLength));
            f.format("%s %n", ", execute time = " + timer.get(keyName) + " ms");
        }

        long longestTime = timer.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue)).get().getValue();
        f.format("%s%n", line.toString());
        f.format("%s %n", " Total Execute time = " + longestTime + " ms");
        f.format("%s%n", line.toString());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(f.toString());
        }
    }

    private String addEmptySpace(String keyName, int maxKeyLength) {
        int emptypSpaceLength = maxKeyLength - keyName.length();
        StringBuilder space = new StringBuilder();

        for (int i = 0; i < emptypSpaceLength; i++) {
            space.append(" ");
        }
        return space.toString();
    }
}
