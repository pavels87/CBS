package com.cbs.service.helpers;


import com.cbs.controller.dto.common.OperationResult;
import org.apache.commons.lang.Validate;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class HttpCodeHandlerMethodProcessor extends RequestResponseBodyMethodProcessor {

    public HttpCodeHandlerMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    public HttpCodeHandlerMethodProcessor(List<HttpMessageConverter<?>> messageConverters, ContentNegotiationManager contentNegotiationManager) {
        super(messageConverters, contentNegotiationManager);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return OperationResult.class.isAssignableFrom(returnType.getParameterType());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
        Object response = webRequest.getNativeResponse();
        Validate.isTrue(returnValue instanceof OperationResult, "Method return type is expected to be OperationResult type or one of its subclasses");
        Validate.isTrue(response instanceof HttpServletResponse, "Response type is expected to be HttpServletResponse type or one of its subclasses");
        ((HttpServletResponse) response).setStatus(((OperationResult) returnValue).getCode().getHttpCode());
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
