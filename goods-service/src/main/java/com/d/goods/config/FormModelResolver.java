package com.d.goods.config;

import java.beans.ConstructorProperties;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;

import com.di.kit.ClassUtil;
import com.di.kit.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.*;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;

public class FormModelResolver implements HandlerMethodArgumentResolver {
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FormModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String name = ModelFactory.getNameForParameter(parameter);
        ModelAttribute ann = parameter.getParameterAnnotation(ModelAttribute.class);
        if (ann != null) {
            mavContainer.setBinding(name, ann.binding());
        }

        Object attribute = null;
        BindingResult bindingResult = null;

        if (mavContainer.containsAttribute(name)) {
            attribute = mavContainer.getModel().get(name);
        } else {
            // Create attribute instance
            try {
                attribute = createAttribute(name, parameter, binderFactory, webRequest);
            } catch (BindException ex) {
                if (isBindExceptionRequired(parameter)) {
                    // No BindingResult parameter -> fail with BindException
                    throw ex;
                }
                // Otherwise, expose null/empty value and associated BindingResult
                if (parameter.getParameterType() == Optional.class) {
                    attribute = Optional.empty();
                }
                bindingResult = ex.getBindingResult();
            }
        }

        if (bindingResult == null) {
            // Bean property binding and validation;
            // skipped in case of binding failure on construction.
            ServletRequest request = (ServletRequest) webRequest.getNativeRequest();
            WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
            if (binder.getTarget() != null) {
                if (!mavContainer.isBindingDisabled(name)) {
                    bindRequestParameters(binder, webRequest);
                }
                validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }
            // Value type adaptation, also covering java.util.Optional
            if (!parameter.getParameterType().isInstance(attribute)) {
                attribute = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
            }
            bindingResult = binder.getBindingResult();
        }

        // Add resolved attribute and BindingResult at the end of the model
        Map<String, Object> bindingResultModel = bindingResult.getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);

        return attribute;
    }

    protected Object createAttribute(String attributeName, MethodParameter parameter,
                                     WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {

        MethodParameter nestedParameter = parameter.nestedIfOptional();
        Class<?> clazz = nestedParameter.getNestedParameterType();

        Constructor<?> ctor = BeanUtils.findPrimaryConstructor(clazz);
        if (ctor == null) {
            Constructor<?>[] ctors = clazz.getConstructors();
            if (ctors.length == 1) {
                ctor = ctors[0];
            } else {
                try {
                    ctor = clazz.getDeclaredConstructor();
                } catch (NoSuchMethodException ex) {
                    throw new IllegalStateException("No primary or default constructor found for " + clazz, ex);
                }
            }
        }

        Object attribute = constructAttribute(ctor, attributeName, binderFactory, webRequest);
        if (parameter != nestedParameter) {
            attribute = Optional.of(attribute);
        }
        return attribute;
    }

    protected Object constructAttribute(Constructor<?> ctor, String attributeName,
                                        WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {
        if (ctor.getParameterCount() == 0) {
            // A single default constructor -> clearly a standard JavaBeans arrangement.
            return BeanUtils.instantiateClass(ctor);
        }

        // A single data class constructor -> resolve constructor arguments from request parameters.
        ConstructorProperties cp = ctor.getAnnotation(ConstructorProperties.class);
        String[] paramNames = (cp != null ? cp.value() : parameterNameDiscoverer.getParameterNames(ctor));
        Assert.state(paramNames != null, () -> "Cannot resolve parameter names for constructor " + ctor);
        Class<?>[] paramTypes = ctor.getParameterTypes();
        Assert.state(paramNames.length == paramTypes.length,
                () -> "Invalid number of parameter names: " + paramNames.length + " for constructor " + ctor);

        Object[] args = new Object[paramTypes.length];
        WebDataBinder binder = binderFactory.createBinder(webRequest, null, attributeName);
        String fieldDefaultPrefix = binder.getFieldDefaultPrefix();
        String fieldMarkerPrefix = binder.getFieldMarkerPrefix();
        boolean bindingFailure = false;

        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            Class<?> paramType = paramTypes[i];
            Object value = webRequest.getParameterValues(StringUtil.snakeCase(paramName));
            if (value == null) {
                if (fieldDefaultPrefix != null) {
                    value = webRequest.getParameter(fieldDefaultPrefix + paramName);
                }
                if (value == null && fieldMarkerPrefix != null) {
                    if (webRequest.getParameter(fieldMarkerPrefix + paramName) != null) {
                        value = binder.getEmptyValue(paramType);
                    }
                }
            }
            try {
                MethodParameter methodParam = new MethodParameter(ctor, i);
                if (value == null && methodParam.isOptional()) {
                    args[i] = (methodParam.getParameterType() == Optional.class ? Optional.empty() : null);
                } else {
                    args[i] = binder.convertIfNecessary(value, paramType, methodParam);
                }
            } catch (TypeMismatchException ex) {
                ex.initPropertyName(paramName);
                binder.getBindingErrorProcessor().processPropertyAccessException(ex, binder.getBindingResult());
                bindingFailure = true;
                args[i] = value;
            }
        }

        if (bindingFailure) {
            if (binder.getBindingResult() instanceof AbstractBindingResult) {
                AbstractBindingResult result = (AbstractBindingResult) binder.getBindingResult();
                for (int i = 0; i < paramNames.length; i++) {
                    result.recordFieldValue(paramNames[i], paramTypes[i], args[i]);
                }
            }
            throw new BindException(binder.getBindingResult());
        }

        return BeanUtils.instantiateClass(ctor, args);
    }

    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) throws UnsupportedEncodingException {
        // 将key-value封装为map，传给bind方法进行参数值绑定
        Map<String, String> map = new HashMap<>();
        Map<String, String[]> params = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String name = entry.getKey();
            // 执行urldecode
            String value = entry.getValue()[0];
            try {
                value = URLDecoder.decode(entry.getValue()[0], "UTF-8");
            } catch (Exception e) {
            }
            map.put(StringUtil.camelCase(name), value);
        }
        PropertyValues propertyValues = new MutablePropertyValues(map);
        // 将K-V绑定到binder.target属性上
        binder.bind(propertyValues);
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
                binder.validate(validationHints);
                validate(binder, parameter);
                break;
            }
        }
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        return isBindExceptionRequired(parameter);
    }

    protected boolean isBindExceptionRequired(MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }

    /**
     * 下划线转驼峰
     */
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FormModel {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LargeThan {
        String name() default "";

        long largeValue() default 0L;

        String message() default "";
    }

    /**
     * 自定义校验
     *
     * @param binder
     */
    private void validate(WebDataBinder binder, MethodParameter parameter) {
        BindingResult result = binder.getBindingResult();
        Object target = binder.getTarget();
        if (target == null) return;
        List<Field> fields = new ArrayList<>();
        Map<String, Object> fieldVal = new HashMap<>();
        for (Field f : ClassUtil.getDeclaredFields(target.getClass())) {
            if (!f.isAccessible()) f.setAccessible(true);
            try {
                Object o = f.get(target);
                if (o == null) continue;
                if (f.isAnnotationPresent(LargeThan.class)) {
                    if (f.isAccessible()) f.setAccessible(true);
                    fields.add(f);
                }
                fieldVal.put(target.getClass().getName() + "." + f.getName(), f.get(target));
            } catch (IllegalAccessException e) {
            }
        }
        for (Field f : fields) {
            if (f.isAnnotationPresent(LargeThan.class)) {
                LargeThan largeThan = f.getAnnotation(LargeThan.class);
                Object o = fieldVal.get(target.getClass().getName() + "." + f.getName());
                if (o == null) continue;
                Object o1 = fieldVal.get(target.getClass().getName() + "." + largeThan.name());
                if (o1 == null) continue;
                if ((o instanceof Date) && (o1 instanceof Date)) {
                    Date d1 = (Date) o;
                    Date d2 = (Date) o1;
                    if (largeThan.largeValue() == 0L && d1.before(d2)) {
                        ObjectError error = new ObjectError("", largeThan.message().isEmpty() ? (f.getName() + "比" + largeThan.name() + "大") : largeThan.message());
                        result.addError(error);
                    } else if (largeThan.largeValue() > 0L && (d1.getTime() - largeThan.largeValue()) <= d2.getTime()) {
                        ObjectError error = new ObjectError("", largeThan.message().isEmpty() ? (f.getName() + "比" + largeThan.name() + "大" + largeThan.largeValue() + "秒") : largeThan.message());
                        result.addError(error);
                    }
                } else if ((o instanceof Short) && (o1 instanceof Short) || (o instanceof Integer) && (o1 instanceof Integer)
                        || (o instanceof Long) && (o1 instanceof Long) || (o instanceof Double) && (o1 instanceof Double)
                        || (o instanceof Float) && (o1 instanceof Float)) {
                    if ((Double.valueOf(o + "") - largeThan.largeValue()) <= Double.valueOf(o1 + "")) {
                        ObjectError error = new ObjectError("", largeThan.message().isEmpty() ? (f.getName() + "比" + largeThan.name() + "大" + largeThan.largeValue()) : largeThan.message());
                        result.addError(error);
                    }
                }
            }
        }
    }
}
