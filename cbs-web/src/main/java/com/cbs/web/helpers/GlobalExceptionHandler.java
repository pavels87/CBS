package com.cbs.web.helpers;

import com.cbs.core.converters.ConstraintViolationToValidationErrorConverter;
import com.cbs.core.dto.common.CategorizedHintError;
import com.cbs.core.dto.common.ErrorType;
import com.cbs.core.dto.common.OperationResult;
import com.cbs.core.dto.common.SaveResult;
import com.cbs.core.dto.enums.ResultCode;
import com.cbs.core.utils.exception.ParseJsonException;
import com.cbs.core.utils.helpers.ValidationErrorContainer;
import com.cbs.web.utils.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cbs.core.dto.enums.ResultCode.*;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ConstraintViolationToValidationErrorConverter<?> violationConverter;

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public OperationResult handleException(HttpServletRequest request, Throwable t) {
        if (!(t instanceof MethodArgumentNotValidException)) {
            logRequestAndException(request, t);
        }
        return translateToResult(request, t);
    }

    private void logRequestAndException(HttpServletRequest request, Throwable t) {
        LOG.error("Exception upon request:\n" + HttpRequestUtils.toString(request), t);
    }

    public OperationResult translateToResult(HttpServletRequest request, Throwable ex) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            if (statusCode == HttpServletResponse.SC_NOT_FOUND) {
                return notFound(ex);
            } else if (statusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
                return unclassifiedError(ex);
            }
        }

        if (ex instanceof NoSuchRequestHandlingMethodException) {//404
            return notFound(ex);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {//405
            return badRequest(ex);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {//415
            return badRequest(ex);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {//406
            return badRequest(ex);
        } else if (ex instanceof MissingServletRequestParameterException) {//400
            return badRequest(ex);
        } else if (ex instanceof ServletRequestBindingException) {//400
            return badRequest(ex);
        } else if (ex instanceof ConversionNotSupportedException) {//500
            return unclassifiedError(ex);
        } else if (ex instanceof TypeMismatchException) {//400
            return badRequest(ex);
        } else if (ex instanceof HttpMessageNotReadableException) {//400
            if (((HttpMessageNotReadableException) ex).getRootCause() instanceof ParseJsonException) {
                return parseJsonException((ParseJsonException) ((HttpMessageNotReadableException) ex).getRootCause());
            } else {
                return badRequest(ex);
            }
        } else if (ex instanceof HttpMessageNotWritableException) {//500
            return unclassifiedError(ex);
        } else if (ex instanceof MethodArgumentNotValidException) {//400
            return validationError((MethodArgumentNotValidException) ex);
        } else if (ex instanceof MissingServletRequestPartException) {//400
            return badRequest(ex);
        } else if (ex instanceof BindException) {//400
            return badRequest(ex);
        } else if (ex instanceof ParseJsonException) { //400
            return parseJsonException((ParseJsonException) ex);
        }
        return unclassifiedError(ex);
    }

    private OperationResult<SaveResult> validationError(MethodArgumentNotValidException ex) {
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();
        ex.getBindingResult().getFieldErrors()
                .stream()
                .forEach(error -> {
                    validationErrorContainer.addError(error.getField(), CategorizedHintError.message(error.getDefaultMessage()));
                });

        return new OperationResult<>(ResultCode.VALIDATION_ERROR, new SaveResult<>(null, validationErrorContainer.getValidationErrorsAsList()));
    }

    private OperationResult<SaveResult> parseJsonException(ParseJsonException e) {
        ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();
        validationErrorContainer.addError(e.getFieldName(), CategorizedHintError.code(e.getCode(), ErrorType.ERROR, e.getHint()));
        return new OperationResult<>(ResultCode.VALIDATION_ERROR, new SaveResult<>(null, validationErrorContainer.getValidationErrorsAsList()));
    }

    private OperationResult<String> unclassifiedError(Throwable t) {
        return new OperationResult<>(UNCLASSIFIED_ERROR, t.getMessage());
    }

    private OperationResult<String> badRequest(Throwable t) {
        return new OperationResult<>(BAD_REQUEST, t.getMessage());
    }

    private OperationResult<String> notFound(Throwable t) {
        return new OperationResult<>(PAGE_NOT_FOUND, t.getMessage());
    }
}