package com.khpark.test;

import com.khpark.model.SampleModel;
import com.khpark.parallel.executor.CallbackExecutor;
import com.khpark.parallel.executor.DefaultExecutor;
import com.khpark.parallel.executor.ExecutorLogger;
import com.khpark.parallel.executor.ParamBuilder;
import com.khpark.service.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorLogger.class);

    public static void main(String[] args) throws InterruptedException {
        SampleService service = new SampleService();
        SampleModel model = new SampleModel().setName("testName").setAge(20);
        nonParallelExecute(service, model);
        LOGGER.debug("");
        parallelExecuteCallback(service, model);
        LOGGER.debug("");
        parallelExecute(service, model);
    }

    private static void nonParallelExecute(SampleService service, SampleModel model) throws InterruptedException {
        long st = System.currentTimeMillis();
        service.testService();
        service.testService(model);
        service.testService("message test!");
        service.testService(10, 12.2, 345.123F);
        service.testService(true, 'c', (byte) 1, (short) 1);
        long et1 = System.currentTimeMillis();
        LOGGER.debug("# nonParallelExecute time : " + (et1 - st) + " ms");
    }

    private static void parallelExecuteCallback(SampleService service, SampleModel model) {
        CallbackExecutor ce = new CallbackExecutor();
        ce.addTaskCallback("service1NameTest", service, "testService");
        ce.addTaskCallback("service2", service, "testService", new ParamBuilder().add(model));
        ce.addTaskCallback("service3", service, "testService", new ParamBuilder().add("message test!"));
        ce.addTaskCallback("service4", service, "testService", new ParamBuilder().add(10).add(12.2).add(345.123F));
        ce.addTaskCallback("service5service5service5", service, "testService", new ParamBuilder().add(true).add('C').add((byte) 1).add((short) 1));
        ce.executeParallelTaskCallback();
    }

    private static void parallelExecute(SampleService service, SampleModel model) {
        DefaultExecutor de = new DefaultExecutor();
        de.addTask(service, "testService");
        de.addTask(service, "testService", new ParamBuilder().add(model));
        de.addTask(service, "testService", new ParamBuilder().add("message test!"));
        de.addTask(service, "testService", new ParamBuilder().add(10).add(12.2).add(345.123F));
        de.addTask(service, "testService", new ParamBuilder().add(true).add('C').add((byte) 1).add((short) 1));
        de.executeParallelTask();
    }
}