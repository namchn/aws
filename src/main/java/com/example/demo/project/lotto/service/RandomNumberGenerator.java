/**
 * 
 */
package com.example.demo.project.lotto.service;

/**
 * 
 */
public class RandomNumberGenerator implements NumberGenerator {

	/**
	 * 
	 */
	//public RandomNumberGenerator() {
		
	//}

	@Override
	public Number generateFrom() {
		double dValue = Math.random();
	return dValue;
	}

}
