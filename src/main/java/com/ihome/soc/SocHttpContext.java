/*
 * ihome inc.
 * soc
 */
package com.ihome.soc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ������
 * @author sihai
 *
 */
public class SocHttpContext {

	private HttpServletRequest  request;			//	HTTP ����
    private HttpServletResponse response;			//  HTTP ��Ӧ
    private ServletContext      servletContext; 	//	Servlet������

    public SocHttpContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
    	this.request  = request;
        this.response = response;
        this.servletContext  = servletContext;
    }

    public HttpServletRequest getRequest() {
        return  request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }
}
