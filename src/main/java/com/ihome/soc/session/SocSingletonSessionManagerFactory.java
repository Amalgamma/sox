/*
 * ihome inc.
 * soc
 */
package com.ihome.soc.session;

/**
 * ����SocSessionManager����
 * @author sihai
 *
 */
public abstract class SocSingletonSessionManagerFactory {
	
	private static SocSessionManager sessionManager;
	
	static {
		sessionManager = new SocDefaultSessionManager();
	}
	
	/**
	 * ���ص�����ʵ��
	 */
	public static SocSessionManager getInstance() {
		return sessionManager;
	}
}
