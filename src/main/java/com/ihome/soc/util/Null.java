/*
 * ihome inc.
 * soc
 */
package com.ihome.soc.util;

/**
 * ��ʾnul�Ķ���
 * @author sihai
 *
 */
public class Null {
	
	private static Null instance = new Null();
	
	/**
	 * 
	 */
	private Null(){}
	
	/**
	 * 
	 * @return
	 */
	public static Null getInstance() {
		return instance;
	}
}
