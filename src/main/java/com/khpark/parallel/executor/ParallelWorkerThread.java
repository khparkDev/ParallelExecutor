package com.khpark.parallel.executor;

import static com.khpark.parallel.executor.ParallelExecutorConstants.CLASS_OBJECT;
import static com.khpark.parallel.executor.ParallelExecutorConstants.EMTPY_OBJECT;
import static com.khpark.parallel.executor.ParallelExecutorConstants.METHOD_NAME;
import static com.khpark.parallel.executor.ParallelExecutorConstants.PARAMS;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelWorkerThread implements Callable<Object> {
	private Object classObject;
	private String methodName;
	private Object[] taskParams;

	public ParallelWorkerThread(Map<String, Object> task) {
		classObject = task.get(CLASS_OBJECT);
		methodName = (String) task.get(METHOD_NAME);
		taskParams = (Object[]) task.get(PARAMS);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object call() throws Exception {
		Method[] classMethods = classObject.getClass().getDeclaredMethods();
		List<Method> methodList = Stream.of(classMethods).parallel()
		        .filter(method -> method.getName().contains(methodName) && method.getParameterTypes().length == taskParams.length).collect(Collectors.toList());

		for (Method targetMethod : methodList) {
			Class[] methodParams = targetMethod.getParameterTypes();
			boolean isSameMethod = true;

			for (int i = 0; i < methodParams.length; i++) {
				String taskParamTypeName = taskParams[i].getClass().getSimpleName().toLowerCase();
				String methodParamTypeName = methodParams[i].getSimpleName().toLowerCase();

				if (!taskParamTypeName.equals(methodParamTypeName) && !taskParamTypeName.contains(methodParamTypeName)) {
					isSameMethod = false;
					break;
				}
			}

			if (isSameMethod) {
				return methodParams.length > 0 ? targetMethod.invoke(classObject, taskParams) : targetMethod.invoke(classObject);
			}
		}

		return EMTPY_OBJECT;
	}
}
