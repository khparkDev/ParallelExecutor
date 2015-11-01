package com.khpark.model;

public class SampleModel {
	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public SampleModel setName(String name) {
		this.name = name;
		return this;
	}

	public int getAge() {
		return age;
	}

	public SampleModel setAge(int age) {
		this.age = age;
		return this;
	}
}
