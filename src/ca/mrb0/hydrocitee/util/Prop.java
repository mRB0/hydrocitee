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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Prop<?> other = (Prop<?>) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
	
}
