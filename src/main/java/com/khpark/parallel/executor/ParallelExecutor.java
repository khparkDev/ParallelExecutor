package com.khpark.parallel.executor;

import static com.khpark.parallel.executor.ParallelExecutorConstants.CLASS_OBJECT;
import static com.khpark.parallel.executor.ParallelExecutorConstants.METHOD_NAME;
import static com.khpark.parallel.executor.ParallelExecutorConstants.PARAMS;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class ParallelExecutor extends AbstractExecutor {

	public void addTaskCallback(String key, Object classObject, String methodName, ParamBuilder parameterBuilder) {
		addTaskMap(key, classObject, methodName, parameterBuilder);
	}

	public Map<String, Object> executeParallelTaskCallback() {
		executeParallelTask();
		return result;
	}

	@Override
	public void addTask(Object classObject, String methodName, ParamBuilder parameterBuilder) {
		String key = methodName + (index++);
		addTaskMap(key, classObject, methodName, parameterBuilder);
	}

	public void executeParallelTask() {
		if (enableStatus()) {
			execute();
			shutdownNow();
		}
	}

	private void addTaskMap(String key, Object classObject, String methodName, ParamBuilder parameterBuilder) {
		taskMap.put(key, new HashMap<String, Object>() {
			{
				put(CLASS_OBJECT, classObject);
				put(METHOD_NAME, methodName);
				put(PARAMS, parameterBuilder);
			}
		});
	}
}
