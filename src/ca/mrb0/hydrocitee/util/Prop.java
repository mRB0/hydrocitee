package ca.mrb0.hydrocitee.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class Prop<T> {
	T value;
	final ValidityTest<T> test;
	final AtomicBoolean frozen = new AtomicBoolean(false);
	
	public Prop(T initialValue, ValidityTest<T> test) {
		super();
		value = initialValue;
		this.test = test;
	}
	
	public Prop(T initialValue) {
		super();
		value = initialValue;
		test = null;
	}
	
	public Prop() {
		super();
		test = null;
	}
	
	public Prop(Prop<T> copy) {
		value = copy.value;
		test = copy.test;
	}
	
	public void set(T newValue) throws IllegalArgumentException {
		if (frozen.get()) {
			throw new IllegalStateException("Frozen");
		}
		if (test != null && !test.isValid(newValue)) {
			throw new IllegalArgumentException("Failed validation for new value");
		}
		value = newValue;
	}
	
	public T get() {
		return value;
	}
	
	public void freeze() {
		frozen.set(true);
	}
	
	@Override
	public String toString() {
		if (value != null) {
			return String.format("Arg(%s)", value.toString());
		} else {
			return "Arg(null)";
		}
	}

	public static interface ValidityTest<V> {
		public boolean isValid(V newValue);
	}
}
