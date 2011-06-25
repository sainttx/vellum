/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */
package bizmon.util;

import common.exception.Exceptions;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author evanx
 */
public class Beans {

    public static Map<String, Field> getFieldMap(Class beanClass) {
        Map<String, Field> map = new HashMap();
        for (Field field : beanClass.getDeclaredFields()) {
            map.put(field.getName(), field);
        }
        return map;
    }

    public static Map<String, PropertyDescriptor> getPropertyMap(Class beanClass) {
        Map<String, PropertyDescriptor> map = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                map.put(property.getName(), property);
            }
            return map;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static List<PropertyDescriptor> getPropertyList(Class beanClass) {
        List<PropertyDescriptor> list = new ArrayList();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                list.add(property);
            }
            return list;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static void parse(Object bean, PropertyDescriptor property, String string) {
        Object value = Types.parse(property.getPropertyType(), string);
        try {
            property.getWriteMethod().invoke(bean, value);
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static void convert(Object bean, PropertyDescriptor property, Object value) {
        value = Types.convert(property.getPropertyType(), value);
        try {
            property.getWriteMethod().invoke(bean, value);
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

}
