package com.khpark.test;

import java.util.ArrayList;
import java.util.List;
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

	@SuppressWarnings("serial")
	private static void parallelExecute(SampleService service, SampleModel model) {
		List<Object> paramList1 = new ArrayList<Object>();
		List<Object> paramList2 = new ArrayList<Object>() {
			{
				add(model);
			}
		};
		List<Object> paramList3 = new ArrayList<Object>() {
			{
				add("message test!");
			}
		};
		List<Object> paramList4 = new ArrayList<Object>() {
			{
				add(10);
				add(12.2);
				add(345.123F);
			}
		};
		List<Object> paramList5 = new ArrayList<Object>() {
			{
				add((Boolean) true);
				add((char) 'C');
				add((byte) 1);
				add((short) 1);
			}
		};

		ParallelExecutor pe = new ParallelExecutor();
		pe.addTask("service1", service, "testService", paramList1);
		pe.addTask("service2", service, "testService", paramList2);
		pe.addTask("service3", service, "testService", paramList3);
		pe.addTask("service4", service, "testService", paramList4);
		pe.addTask("service5", service, "testService", paramList5);
		Map<String, Object> resultMap = pe.executeParallelTask();

		resultMap.get("service1");
		resultMap.get("service2");
		resultMap.get("service3");
		resultMap.get("service4");
		resultMap.get("service5");
	}
}