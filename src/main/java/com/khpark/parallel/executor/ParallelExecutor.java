package com.khpark.parallel.executor;

import static com.khpark.parallel.executor.ParallelExecutorConstants.CLASS_OBJECT;
import static com.khpark.parallel.executor.ParallelExecutorConstants.METHOD_NAME;
import static com.khpark.parallel.executor.ParallelExecutorConstants.PARAMS;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParallelExecutor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParallelExecutor.class);
	private ExecutorService executor;
	private Map<String, Map<String, Object>> taskMap;
	private Map<String, Future<?>> futureMap;

	public ParallelExecutor() {
		taskMap = new HashMap<String, Map<String, Object>>();
		futureMap = new HashMap<String, Future<?>>();
	}

	@SuppressWarnings("serial")
	public void addTask(String key, Object classObject, String methodName, List<Object> paramList) {
		taskMap.put(key, new HashMap<String, Object>() {
			{
				put(CLASS_OBJECT, classObject);
				put(METHOD_NAME, methodName);
				put(PARAMS, paramList);
			}
		});
	}

	public Map<String, Object> executeParallelTask() {
		if (taskMap != null && taskMap.size() > 0) {
			executor = Executors.newFixedThreadPool(taskMap.size());
			Map<String, Object> result = new HashMap<String, Object>();
			taskMap.entrySet().parallelStream().forEach(tm -> futureMap.put(tm.getKey(), executor.submit(new ParallelWorkerThread(taskMap.get(tm.getKey())))));
			futureMap.entrySet().parallelStream().forEach(fm -> {
				try {
					result.put(fm.getKey(), futureMap.get(fm.getKey()).get(5, TimeUnit.SECONDS));
				} catch (Exception e) {
					LOGGER.error("executeParallelTask : ", e);
				}
			});

			executor.shutdownNow();

			return result;
		}

		return Collections.emptyMap();
	}
}
