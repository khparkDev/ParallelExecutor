package com.khpark.parallel.executor;

import java.util.Map;

public class CallbackExecutor extends AbstractExecutor {

	public void addTaskCallback(String key, Object classObject, String methodName, ParamBuilder parameterBuilder) {
		addTaskMap(key, classObject, methodName, parameterBuilder);
	}

	public Map<String, Object> executeParallelTaskCallback() {
		run();
		return result;
	}
}
