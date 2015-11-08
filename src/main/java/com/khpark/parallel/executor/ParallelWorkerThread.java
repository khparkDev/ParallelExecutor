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

@SuppressWarnings("rawtypes")
public class ParallelWorkerThread implements Callable<Object> {
	private Object classObject;
	private String methodName;
	private Object[] taskParam;
	private Class[] taskParamClassType;

	public ParallelWorkerThread(Map<String, Object> task) {
		classObject = task.get(CLASS_OBJECT);
		methodName = (String) task.get(METHOD_NAME);
		List<Object> paramList = ((ParamBuilder) task.get(PARAMS)).getParameterList();
		taskParam = paramList.toArray();
		taskParamClassType = new Class[paramList.size()];

		for (int i = 0; i < paramList.size(); i++) {
			taskParamClassType[i] = paramList.get(i).getClass();
		}
	}

	@Override
	public Object call() throws Exception {
		Method[] classMethods = classObject.getClass().getDeclaredMethods();
		List<Method> methodList = Stream.of(classMethods).parallel()
		        .filter(method -> method.getName().contains(methodName) && method.getParameterTypes().length == taskParamClassType.length)
		        .collect(Collectors.toList());

		if (taskParamClassType.length == 0) {
			return methodList.get(0).invoke(classObject);
		}

		for (Method targetMethod : methodList) {
			Class[] methodParams = targetMethod.getParameterTypes();

			for (int i = 0; i < taskParamClassType.length; i++) {
				String taskParamTypeName = taskParamClassType[i].getSimpleName().toLowerCase();
				String methodParamTypeName = methodParams[i].getSimpleName().toLowerCase();

				if (taskParamTypeName.contains(methodParamTypeName)) {
					targetMethod.setAccessible(true);
					return targetMethod.invoke(classObject, taskParam);

				}
			}
		}

		return EMTPY_OBJECT;
	}
}
