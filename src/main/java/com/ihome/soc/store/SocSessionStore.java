/*
 * ihome inc.
 * soc
 */
package com.ihome.soc.store;

import java.util.Map;

import com.ihome.soc.SocHttpContext;

/**
 * �����־û�session attribute�Ĵ洢��
 * @author sihai
 *
 */
public interface SocSessionStore {
	
	String SESSION = "session";
	String CONFIG = "config";
	
	/**
     * ��ʼ��ÿ��STORE�Ļ���,�����������֣�1��ʵʱ���� 2������
     *
     * @param context
     */
    void init(Map<String, Object> context);
    
	/**
     * ���ݵ���KEY����ֵ
     *
     * @param key
     *
     * @return
     */
    Object getAttribute(String key);
    
    /**
     * ��ֵд�ش洢
     * @param httpContext
     */
    void save(SocHttpContext httpContext);

    /**
     * ��ָ����ֵд�ش洢
     * @param httpContext
     * @param key
     */
    void save(SocHttpContext httpContext, String key);
    
    /**
     * ����ʧЧ
     * @param key
     */
    void invalidate(String key);
    
    /**
     * ����ʧЧȫ��
     */
    void invalidate();
}
