package com.android.zd112.ui.view.picker.entity;

import com.android.zd112.utils.LogUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by etongdai on 2018/3/9.
 */

public class JavaBean implements Serializable{
    /**
     * 反射出所有字段值
     */
    @Override
    public String toString() {
        ArrayList<Field> list = new ArrayList<>();
        Class<?> clazz = getClass();
        list.addAll(Arrays.asList(clazz.getDeclaredFields()));//得到自身的所有字段
        StringBuilder sb = new StringBuilder();
        while (clazz != Object.class) {
            clazz = clazz.getSuperclass();//得到继承自父类的字段
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                int modifier = field.getModifiers();
                if (Modifier.isPublic(modifier) || Modifier.isProtected(modifier)) {
                    list.add(field);
                }
            }
        }
        Field[] fields = list.toArray(new Field[list.size()]);
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                Object obj = field.get(this);
                sb.append(fieldName);
                sb.append("=");
                sb.append(obj);
                sb.append("\n");
            } catch (IllegalAccessException e) {
                LogUtils.e(e);
            }
        }
        LogUtils.e("------------------sb:",sb.toString());
        return sb.toString();
    }
}
