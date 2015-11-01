package com.taru.model;

public class Pair<T1, T2> extends Object{
	
	private T1 first;
	private T2 second;
	
	public Pair() {
	}
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int hashCode() {
		return first.hashCode() + (31 * second.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		Pair<T1,T2> pair = (Pair<T1,T2>)obj; 
		return pair.getFirst().equals(this.getFirst()) && pair.getSecond().equals(this.getSecond());
	}
	
	/**
	 * @return the first
	 */
	public T1 getFirst() {
		return first;
	}
	/**
	 * @param first the first to set
	 */
	public void setFirst(T1 first) {
		this.first = first;
	}
	/**
	 * @return the second
	 */
	public T2 getSecond() {
		return second;
	}
	/**
	 * @param second the second to set
	 */
	public void setSecond(T2 second) {
		this.second = second;
	}
	
	
	
	
}
