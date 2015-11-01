package com.khpark.parallel.executor;

import static com.khpark.parallel.executor.ParallelExecutorConstants.CLASS_OBJECT;
import static com.khpark.parallel.executor.ParallelExecutorConstants.METHOD_NAME;
import static com.khpark.parallel.executor.ParallelExecutorConstants.PARAMS;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelExecutor {
	private ExecutorService executor;
	private Map<String, Map<String, Object>> taskMap;
	private Map<String, Future<?>> futureMap;

	public ParallelExecutor() {
		taskMap = new HashMap<String, Map<String, Object>>();
		futureMap = new HashMap<String, Future<?>>();
	}

	@SuppressWarnings("serial")
	public void addTask(String key, Object classObject, String methodName, Object... params) {
		taskMap.put(key, new HashMap<String, Object>() {
			{
				put(CLASS_OBJECT, classObject);
				put(METHOD_NAME, methodName);
				put(PARAMS, params);
			}
		});
	}

	public Map<String, Object> executeParallelTask() {
		executor = Executors.newFixedThreadPool(taskMap.size());
		Map<String, Object> result = new HashMap<String, Object>();
		taskMap.entrySet().parallelStream().forEach(tm -> futureMap.put(tm.getKey(), executor.submit(new ParallelWorkerThread(taskMap.get(tm.getKey())))));
		futureMap.entrySet().parallelStream().forEach(fm -> {
			try {
				result.put(fm.getKey(), futureMap.get(fm.getKey()).get(5, TimeUnit.SECONDS));
			} catch (Exception e) {
				System.out.println("# executeParallelTask : " + e);
			}
		});

		executor.shutdownNow();

		return result;
	}
}
