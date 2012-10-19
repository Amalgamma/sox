/*
 * ihome inc.
 * soc
 */
package com.ihome.soc.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ihome.soc.filter.AbstractFilter;

/**
 * ��װ��һ��
 * @author sihai
 *
 */
public class Decoder {
	
	private static final Log logger  = LogFactory.getLog(AbstractFilter.class);
	
	/**
	 * 
	 * @param encoded
	 * @return
	 */
	public static String decode(String encoded) {
		
        String result = null;
        if (StringUtils.isNotBlank(encoded)) {
            try {
                byte[] decoded = Base64.decodeBase64(encoded.getBytes());
                result = new String(decoded);
            } catch (Exception e) {
            	logger.warn(" decode base error", e);
                result = "";
            }
        }

        return result;
    }
}
