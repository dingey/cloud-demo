package com.d.goods.config;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;

import com.di.kit.ClassUtil;
import com.di.kit.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.*;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;

public class FormModelResolver extends HandlerMethodArgumentResolverComposite {
    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FormModel.class);
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
                log.error(e.getMessage());
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
                validate(binder);
                break;
            }
        }
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
     * @param binder binder
     */
    private void validate(WebDataBinder binder) {
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
            } catch (IllegalAccessException ignored) {
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
