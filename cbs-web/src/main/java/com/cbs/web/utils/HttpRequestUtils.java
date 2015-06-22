package com.cbs.web.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class HttpRequestUtils {

    private HttpRequestUtils() {
    }

    public static String toString(HttpServletRequest request) {
        StringBuilder requestInfo = new StringBuilder();
        requestInfo.append(String.format("Request: %s %s", request.getMethod(), request.getRequestURI()));
        String queryString = request.getQueryString();
        if (!StringUtils.isBlank(queryString)) {
            requestInfo.append(String.format("?%s", queryString));
        }
        requestInfo.append(String.format("%n"));
        requestInfo.append(String.format("User: %s%n", request.getRemoteUser()));
        requestInfo.append(String.format("Session: id: %s, created: %s%n", request.getRequestedSessionId(), String.valueOf(request.getSession(false) != null)));
        Enumeration parameterNames = request.getParameterNames();
        requestInfo.append("\nParameters: \n");
        while (parameterNames.hasMoreElements()) {
            String parameterName = String.valueOf(parameterNames.nextElement());
            String[] parameterValues = request.getParameterValues(parameterName);
            if (parameterValues != null) {
                for (String parameterValue : parameterValues) {
                    requestInfo.append(String.format("%s: %s%n", parameterName, getValueStr(parameterValue)));
                }
            }
        }

        requestInfo.append(String.format("%nRequest headers: %n"));
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = String.valueOf(headerNames.nextElement());
            Enumeration headers = request.getHeaders(headerName);
            if (headers != null) {
                while (headers.hasMoreElements()) {
                    requestInfo.append(String.format("%s: %s%n", headerName, getValueStr(headers.nextElement())));
                }
            }
        }
        return requestInfo.toString();
    }

    private static String getValueStr(Object value) {
        return String.valueOf(value);
    }
}
