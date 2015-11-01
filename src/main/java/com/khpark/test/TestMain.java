package com.khpark.test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.khpark.model.SampleModel;
import com.khpark.parallel.executor.ParallelExecutor;
import com.khpark.service.SampleService;

public class TestMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, ClassNotFoundException {
		SampleService service = new SampleService();
		SampleModel model = new SampleModel().setName("testName").setAge(20);

		long st = System.currentTimeMillis();
		nonParallelExecute(service, model);
		long et1 = System.currentTimeMillis();
		parallelExecute(service, model);
		long et2 = System.currentTimeMillis();

		System.out.println("# nonParallelExecute time : " + (et1 - st) + " ms");
		System.out.println("# ParallelExecute time : " + (et2 - et1) + " ms");
	}

	private static void nonParallelExecute(SampleService service, SampleModel model) throws InterruptedException {
		service.testService();
		service.testService(model);
		service.testService("message test!");
		service.testService(10, 12.2, 345.123F);
		service.testService((Boolean) true, (char) 'c', (byte) 1, (short) 1);
	}

	private static void parallelExecute(SampleService service, SampleModel model) {
		ParallelExecutor pe = new ParallelExecutor();
		pe.addTask("service1", service, "testService");
		pe.addTask("service2", service, "testService", model);
		pe.addTask("service3", service, "testService", "message test!");
		pe.addTask("service4", service, "testService", 10, 12.2, 345.123F);
		pe.addTask("service5", service, "testService", (Boolean) true, (char) 'C', (byte) 1, (short) 1);
		Map<String, Object> resultMap = pe.executeParallelTask();

		resultMap.get("service1");
		resultMap.get("service2");
		resultMap.get("service3");
		resultMap.get("service4");
		resultMap.get("service5");
	}
}