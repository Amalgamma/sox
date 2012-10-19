/*
 * ihome inc.
 * soc
 */
package com.ihome.soc.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ihome.soc.store.SocSessionStore;
import com.ihome.soc.store.StoreType;

/**
 * Ĭ�ϵ�SocSessionManager��ʵ��
 * @author sihai
 *
 */
public class SocDefaultSessionManager implements SocSessionManager {

	private final Log logger  = LogFactory.getLog(getClass());
	
	// session�洢������
    private Map<StoreType, SocSessionStore> storeMap = new HashMap<StoreType, SocSessionStore>();
    
    // ����ֵ������
    private Map<String, SessionAttributeConfig> sessionAttributeConfigMap = new HashMap<String, SessionAttributeConfig>(); //�����ɶ���������ļ�
    
	// ��ǰ������session
	ThreadLocal<SocSession> threadLocal = new ThreadLocal<SocSession>();
	
	/**
	 * ʹ��Ĭ�ϵ�<code>CONFIG_FILE_NAME</code>��ʼ��
	 */
	public void init() {
		init(CONFIG_FILE_NAME);
	}
	
	/**
	 * ʹ��ָ���������ļ���ʼ��, �����ļ�λ��classpath��
	 * @param configFileName
	 */
	public void init(String configFileName) {
		try {
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName));
			init(properties);
		} catch (IOException e) {
			throw new IllegalArgumentException("SOC init failed", e);
		}
	}
	
	/**
	 * ʹ���ƶ���Properties��ʼ��
	 * @param properties
	 */
	public void init(Properties properties) {
		
		// �õ����е��ֶ�
		String key = null;
		String value = properties.getProperty(SOC_ATTRIBUTS);
		if(StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(String.format("Please config %s property, or if you no need, please not use soc", SOC_ATTRIBUTS));
		}
		
		String[] attributes = StringUtils.trim(value).split(",");
		if(0 == attributes.length) {
			throw new IllegalArgumentException(String.format("Please config %s property, or if you no need, please not use soc", SOC_ATTRIBUTS));
		}
		
		for(String attribute : attributes) {
			SessionAttributeConfig config = new SessionAttributeConfig();
			config.setName(attribute);
			
			// alias Ĭ��ʹ�� name
			config.setAlias(attribute);
			value = properties.getProperty(String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.ALIAS));
			if(StringUtils.isNotBlank(value)) {
				config.setAlias(StringUtils.trim(value));
			}
			
			// storeTypeĬ��cookie
			key = String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.STORE_TYPE);
			value = properties.getProperty(key);
			if(StringUtils.isNotBlank(value)) {
				StoreType type = StoreType.valueOf(StringUtils.upperCase(StringUtils.trim(value)));
				if(null == type) {
					StringBuilder sb = new StringBuilder();
					for(StoreType s : StoreType.values()) {
						sb.append(StringUtils.lowerCase(s.toString()));
						sb.append(" or ");
					}
					throw new IllegalArgumentException(String.format("Value of %s property only allow: %s", key, sb.toString()));
				}
				config.setStoreType(type);
			}
						
			// isBase64 Ĭ��false
			key = String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.IS_BASE64);
			value = properties.getProperty(key);
			if(StringUtils.isNotBlank(value)) {
				if(StringUtils.trim(value).equals(Boolean.TRUE.toString())) {
					config.setBase64(true);
				} else if(StringUtils.trim(value).equals(Boolean.FALSE.toString())) {
					config.setBase64(false);
				} else {
					throw new IllegalArgumentException(String.format("Value of %s property only allow: %s or %s", key, Boolean.TRUE, Boolean.FALSE));
				}
			}
			
			// isEncryptĬ��false
			key = String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.IS_ENCRYPT);
			value = properties.getProperty(key);
			if(StringUtils.isNotBlank(value)) {
				if(StringUtils.trim(value).equals(Boolean.TRUE.toString())) {
					config.setEncrypt(true);
				} else if(StringUtils.trim(value).equals(Boolean.FALSE.toString())) {
					config.setEncrypt(false);
				} else {
					throw new IllegalArgumentException(String.format("Value of %s property only allow: %s or %s", key, Boolean.TRUE, Boolean.FALSE));
				}
			}
			
			// lifeTime
			key = String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.LIFE_TIME);
			value = properties.getProperty(key);
			if(StringUtils.isNotBlank(value)) {
				try {
					config.setLifeTime(Integer.valueOf(StringUtils.trim(value)));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(String.format("Value of %s property only allow big than 0 integer", key));
				}
			}
			
			// domain used by cookie store
			key = String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.DOMAIN);
			value = properties.getProperty(key);
			if(StringUtils.isNotBlank(value)) {
				config.setDomain(StringUtils.trim(value));
			}
			
			// cookie path used by cookie store
			key = String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.COOKIE_PATH);
			value = properties.getProperty(key);
			if(StringUtils.isNotBlank(value)) {
				config.setCookiePath(StringUtils.trim(value));
			}
			
			// isHttpOnly used by cookie store
			key = String.format("%s.%s.%s", SOC_ATTRIBUT, attribute, SessionAttributeConfig.IS_HTTP_ONLY);
			value = properties.getProperty(key);
			if(StringUtils.isNotBlank(value)) {
				if(StringUtils.trim(value).equals(Boolean.TRUE.toString())) {
					config.setHttpOnly(true);
				} else if(StringUtils.trim(value).equals(Boolean.FALSE.toString())) {
					config.setHttpOnly(false);
				} else {
					throw new IllegalArgumentException(String.format("Value of %s property only allow: %s or %s", key, Boolean.TRUE, Boolean.FALSE));
				}
			}
			
			sessionAttributeConfigMap.put(config.getName(), config);
		}
	}
	
	@Override
	public void setSession(SocSession session) {
		threadLocal.set(session);
	}

	@Override
	public SocSession getSession() {
		return threadLocal.get();
	}

	@Override
	public void save() {
		
		logger.debug("start save session attribute!");
		SocSession session = getSession();
		Map<String, Boolean> change = session.getChangedMarkMap();		
		
		for(Iterator<Entry<String, Boolean>> iterator = change.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Boolean> e = iterator.next();
			String key = e.getKey();
			if(e.getValue().booleanValue()) {
				//ȡ������STORE, ������
				SessionAttributeConfig config = sessionAttributeConfigMap.get(key);
				
				if (null == config) {
					continue;
				}
				
				//ȡ�ø�KEY���õ�STORE        �˴����ֹû��ȡ��STORE
		        StoreType type = config.getStoreType();
		        SocSessionStore store = (SocSessionStore)getSession().getSessionStore(type);

		        store.save(this.getSession().getHttpContext(), key);
			}			
		}		
	}

	@Override
	public void invalidate() {
		// �������ã���ת�������STORE����
		for (Iterator<Entry<String, SessionAttributeConfig>> iterator = sessionAttributeConfigMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, SessionAttributeConfig> e = iterator.next();
			SessionAttributeConfig config = e.getValue();
			if (config.getLifeTime() <= 0 && null != config.getStoreType()) { // ˵����Ҫ����
				getStore(config.getStoreType()).invalidate(e.getKey());
			}
		}	
	}

	@Override
	public SocSessionStore getSessionStore(StoreType type) {
		return storeMap.get(type);
	}
	
	@Override
	public Object getAttribute(String key) {
		//ȡ�ø�key���õ�store
        SessionAttributeConfig config = (SessionAttributeConfig)sessionAttributeConfigMap.get(key);
        StoreType storeType = config.getStoreType();
        SocSessionStore store = getStore(storeType);

        return store.getAttribute(key);
	}

	@Override
	public boolean isExistKey(String key) {
		if(sessionAttributeConfigMap.containsKey(key)) {
            return true;
        }

        return false;
	}
	
	/**
	 * 
	 * @param storeKey
	 * @return
	 */
	private SocSessionStore getStore(StoreType storeType) {
		SocSessionStore store = this.getSession().getSessionStore(storeType);

        //�����ǰ������������û��STORE�����½�һ����
        if(null == store) {
            store = (SocSessionStore)SocSessionStoreFactory.newInstance(storeType);
            Map<String, Object> context = new HashMap<String, Object>();
            context.put(SocSessionStore.SESSION, this.getSession());
            context.put(SocSessionStore.CONFIG, sessionAttributeConfigMap);
            store.init(context);
            getSession().setSessionStore(storeType, store);
        }
		return store;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SocDefaultSessionManager sm = new SocDefaultSessionManager();
		sm.init();
		System.out.println(sm);
	}
}