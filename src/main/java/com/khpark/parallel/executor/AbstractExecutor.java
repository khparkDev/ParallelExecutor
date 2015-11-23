package com.khpark.parallel.executor;

import static com.khpark.parallel.executor.ParallelExecutorConstants.CLASS_OBJECT;
import static com.khpark.parallel.executor.ParallelExecutorConstants.METHOD_NAME;
import static com.khpark.parallel.executor.ParallelExecutorConstants.PARAMS;
import static com.khpark.parallel.executor.ParallelExecutorConstants.TIMEOUT_SEC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class AbstractExecutor {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecutor.class);
	protected Map<String, Map<String, Object>> taskMap;
	protected Map<String, Future<?>> futureMap;
	protected ExecutorService executor;
	protected Map<String, Object> result;
	protected ExecutorLogger executorLogger;
	protected int index;

	public AbstractExecutor() {
		taskMap = new ConcurrentHashMap<String, Map<String, Object>>();
		futureMap = new HashMap<String, Future<?>>();
		result = new HashMap<String, Object>();
		executorLogger = new ExecutorLogger();
		index = 0;
	}

	protected void executeWorkerThread() {
		executor = Executors.newFixedThreadPool(taskMap.size());
		taskMap.entrySet().parallelStream().forEach(
		        tm -> {
			        futureMap.put(tm.getKey(), executor.submit(new ParallelWorkerThread(taskMap.get(tm.getKey()))));
			        executorLogger.setStartTimeTask(tm.getKey());
		        });
		futureMap.entrySet().parallelStream().forEach(
		        fm -> {
			        try {
				        result.put(fm.getKey(), futureMap.get(fm.getKey()).get(TIMEOUT_SEC, TimeUnit.SECONDS));
				        executorLogger.setEndTimeTask(fm.getKey());
			        } catch (Exception e) {
				        LOGGER.error("# executeParallelTask : ", e);
			        }
		        });
	}

	protected void addTaskMap(String key, Object classObject, String methodName, ParamBuilder parameterBuilder) {
		taskMap.put(key, new ConcurrentHashMap<String, Object>() {
			{
				put(CLASS_OBJECT, classObject);
				put(METHOD_NAME, methodName);
				put(PARAMS, parameterBuilder);
			}
		});
	}

	protected boolean enableStatus() {
		return taskMap.size() > 0 ? true : false;
	}

	protected void shutdownNow() {
		executor.shutdownNow();
		executorLogger.showTaskInfo();
	}

	protected void run() {
		if (enableStatus()) {
			executeWorkerThread();
			shutdownNow();
		}
	}
}
