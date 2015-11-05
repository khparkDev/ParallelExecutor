package com.khpark.test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.khpark.model.SampleModel;
import com.khpark.parallel.executor.ParallelExecutor;
import com.khpark.parallel.executor.ParamBuilder;
import com.khpark.service.SampleService;

public class TestMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, ClassNotFoundException {
		SampleService service = new SampleService();
		SampleModel model = new SampleModel().setName("testName").setAge(20);
		nonParallelExecute(service, model);
		parallelExecuteCallback(service, model);
		parallelExecute(service, model);
	}

	private static void nonParallelExecute(SampleService service, SampleModel model) throws InterruptedException {
		long st = System.currentTimeMillis();
		service.testService();
		service.testService(model);
		service.testService("message test!");
		service.testService(10, 12.2, 345.123F);
		service.testService((Boolean) true, (char) 'c', (byte) 1, (short) 1);
		long et1 = System.currentTimeMillis();
		System.out.println("# nonParallelExecute time : " + (et1 - st) + " ms");
	}

	private static void parallelExecuteCallback(SampleService service, SampleModel model) {
		ParallelExecutor pe = new ParallelExecutor();
		pe.addTaskCallback("service1", service, "testService", new ParamBuilder());
		pe.addTaskCallback("service2", service, "testService", new ParamBuilder().add(model));
		pe.addTaskCallback("service3", service, "testService", new ParamBuilder().add("message test!"));
		pe.addTaskCallback("service4", service, "testService", new ParamBuilder().add(10).add(12.2).add(345.123F));
		pe.addTaskCallback("service5", service, "testService", new ParamBuilder().add((Boolean) true).add((char) 'C').add((byte) 1).add((short) 1));
		Map<String, Object> resultMap = pe.executeParallelTaskCallback();
		System.out.println(resultMap.toString());
	}

	private static void parallelExecute(SampleService service, SampleModel model) {
		ParallelExecutor pe = new ParallelExecutor();
		pe.addTask(service, "testService", new ParamBuilder());
		pe.addTask(service, "testService", new ParamBuilder().add(model));
		pe.addTask(service, "testService", new ParamBuilder().add("message test!"));
		pe.addTask(service, "testService", new ParamBuilder().add(10).add(12.2).add(345.123F));
		pe.addTask(service, "testService", new ParamBuilder().add((Boolean) true).add((char) 'C').add((byte) 1).add((short) 1));
		pe.executeParallelTask();
	}
}