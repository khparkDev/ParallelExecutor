package com.khpark.parallel.executor;

public class DefaultExecutor extends AbstractExecutor {

	public void addTask(Object classObject, String methodName) {
		String key = methodName + (index++);
		addTaskMap(key, classObject, methodName, new ParamBuilder());
	}

	public void addTask(Object classObject, String methodName, ParamBuilder parameterBuilder) {
		String key = methodName + (index++);
		addTaskMap(key, classObject, methodName, parameterBuilder);
	}

	public void executeParallelTask() {
		run();
	}
}
