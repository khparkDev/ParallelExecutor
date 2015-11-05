package com.khpark.parallel.executor;

import static com.khpark.parallel.executor.ParallelExecutorConstants.TIMEOUT_SEC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExecutor {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ParallelExecutor.class);
	protected Map<String, Map<String, Object>> taskMap;
	protected Map<String, Future<?>> futureMap;
	protected ExecutorService executor;
	protected long startTime;
	protected long endTime;
	protected Map<String, Object> result;
	protected int index;

	public AbstractExecutor() {
		taskMap = new HashMap<String, Map<String, Object>>();
		futureMap = new HashMap<String, Future<?>>();
		result = new HashMap<String, Object>();
		index = 0;
	}

	protected boolean enableStatus() {
		return taskMap.size() > 0 ? true : false;
	}

	protected void execute() {
		startTime = System.currentTimeMillis();
		executor = Executors.newFixedThreadPool(taskMap.size());
		taskMap.entrySet().parallelStream()
		        .forEach(tm -> futureMap.put(tm.getKey(), executor.submit(new ParallelWorkerThread(taskMap.get(tm.getKey())))));
		futureMap.entrySet().parallelStream().forEach(fm -> {
			try {
				result.put(fm.getKey(), futureMap.get(fm.getKey()).get(TIMEOUT_SEC, TimeUnit.SECONDS));
			} catch (Exception e) {
				LOGGER.error("# executeParallelTask : ", e);
			}
		});
	}

	protected void shutdownNow() {
		executor.shutdownNow();
		endTime = System.currentTimeMillis();
		LOGGER.info("# Execution Time = " + (endTime - startTime) + " ms");
	}

	public abstract void addTaskCallback(String key, Object classObject, String methodName, ParamBuilder parameterBuilder);

	public abstract void addTask(Object classObject, String methodName, ParamBuilder parameterBuilder);

	public abstract Map<String, Object> executeParallelTaskCallback();

	public abstract void executeParallelTask();
}
