package model;

import java.io.Serializable;

public class Counter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int counter=0;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	
}
