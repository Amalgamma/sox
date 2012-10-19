/*
 * ihome inc.
 * soc
 */
package com.ihome.soc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * ����filter�Ļ��ࡣ
 * @author sihai
 *
 */
public abstract class AbstractFilter implements Filter {
	
	protected static final Log logger  = LogFactory.getLog(AbstractFilter.class);
	
	private FilterConfig config;		// 
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
		init();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// ���������filter��������exception��
        // ��weblogic�У�servlet forward��jspʱ��jsp�Ի���ô�filter����jsp�׳����쳣�ͻᱻ��filter����
        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)
                    || null != (request.getAttribute(getClass().getName()))) {
            chain.doFilter(request, response);
            return;
        }

        // ��ֹ����.
        request.setAttribute(getClass().getName(), Boolean.TRUE);

        try {
            // ִ�������doFilter
            HttpServletRequest  req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            doFilter(req, res, chain);
        } catch (ServletException e) {
        	throw e;
        }
	}
	
	/**
	 * ��ʼ��filter��
	 * @throws ServletException
	 */
	protected abstract void init() throws ServletException;
	
	/**
     * ִ��filter.
     *
     * @param request HTTP����
     * @param response HTTP��Ӧ
     * @param chain filter��
     *
     * @throws IOException ����filter��ʱ���������������
     * @throws ServletException ����filter��ʱ������һ�����
     */
    public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;
	
    /**
	 * �ͷ���Դ
	 */
	protected abstract void releaseResource();
	
	
	@Override
	public void destroy() {
		releaseResource();
		this.config = null;
	}
	
	/**
     * ȡ��filter��������Ϣ��
     *
     * @return <code>FilterConfig</code>����
     */
    public FilterConfig getFilterConfig() {
        return config;
    }
    
	/**
     * ȡ��servlet��������������Ϣ��
     *
     * @return <code>ServletContext</code>����
     */
    public ServletContext getServletContext() {
        return getFilterConfig().getServletContext();
    }
    
    /**
     * ����ָ����filter��ʼ��������������˳��
     * 
     * <ol>
     * <li>
     * ����filter�����<code>init-param</code>
     * </li>
     * <li>
     * ����webӦ��ȫ�ֵ�<code>init-param</code>
     * </li>
     * <li>
     * ʹ��ָ��Ĭ��ֵ��
     * </li>
     * </ol>
     * 
     *
     * @param parameterName ��ʼ��������
     * @param defaultValue Ĭ��ֵ
     *
     * @return ָ����������Ӧ�ĳ�ʼ������ֵ�����δ��������ֵΪ�գ��򷵻�<code>null</code>��
     */
    public String getInitParameter(String parameterName, String defaultValue) {
        // ȡfilter����
        String value = trimToNull(getFilterConfig().getInitParameter(parameterName));

        // ���δȡ������ȡȫ�ֲ���
        if (value == null) {
            value = trimToNull(getServletContext().getInitParameter(parameterName));
        }

        // ���δȡ������ȡĬ��ֵ
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }
    
    /**
     * ȡ��request������(HTTP����, URI)
     *
     * @param request HTTP����
     *
     * @return �ַ���
     */
    protected String dumpRequest(HttpServletRequest request) {
        String queryString = trimToNull(request.getQueryString());
        return String.format("%s %s, %s%s", request.getMethod(), request.getRequestURI(), null == queryString ? "" : "?" + queryString);
    }
    
    /**
     * ���ַ���trim������ַ���Ϊ�հף��򷵻�<code>null</code>��
     *
     * @param str �����ַ���
     *
     * @return ����ַ�������������ַ���Ϊ�հף��򷵻�<code>null</code>
     */
    protected String trimToNull(String str) {
        if (str != null) {
            str = str.trim();
            if (str.length() == 0) {
                str = null;
            }
        }
        return str;
    }
    
    
}
