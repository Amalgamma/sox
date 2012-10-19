/*
 * ihome inc.
 * soc
 */
package com.ihome.soc.session;

import com.ihome.soc.store.SocSessionStore;
import com.ihome.soc.store.StoreType;


/**
 * Session�Ĺ�����
 * @author sihai
 *
 */
public interface SocSessionManager {
	
	String CONFIG_FILE_NAME = "soc.properties";
	
	String SOC_ATTRIBUTS = "soc.attributes";
	
	String SOC_ATTRIBUT = "soc.attribute";
	
	/**
	 * ��������������session
	 * @param session
	 */
	void setSession(SocSession session);
	
	/**
	 * ȡ��manager�������session
	 * @return
	 */
	SocSession getSession();
	
	/**
	 * ����
	 *
	 */
	void save();
	
	/**
	 * ʹ���ڵ�sessionֵʧЧ
	 *
	 */
	void invalidate();
	
	/**
	 * ����store�����ͷ���ʵ�ֵ�store
	 * @param type
	 * @return
	 */
	SocSessionStore getSessionStore(StoreType type);
	
	/**
	 * ��ȡ����ֵ
	 * @param key
	 * @return
	 */
	Object getAttribute(String key);
	
	
	/**
	 * �ж�Ҫ���key�Ƿ�����������ļ�֮��
	 * @param key
	 * @return
	 */
	boolean isExistKey(String key);
}
