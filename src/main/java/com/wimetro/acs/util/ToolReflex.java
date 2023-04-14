package com.wimetro.acs.util;

import com.wimetro.acs.util.ProtocolFiledUtil.CmdProp;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ToolReflex {

    public enum MethodType{
        GET,SET
    }

    //*  通过属性名称获取属性类型 *//*
    public static String getMethodName(MethodType type,String filedName){
        if(filedName == null){
            return null;
        }
        String firstChar = filedName.substring(0,1);
        if(type == MethodType.GET){
            return  "get" + filedName.replaceFirst(firstChar, firstChar.toUpperCase());
        }
        return  "set" + filedName.replaceFirst(firstChar, firstChar.toUpperCase());
    }


    public static Object getFiledValue(Field field, byte[] src) throws InvocationTargetException,NoSuchMethodException,IllegalAccessException{
        CmdProp cmdProp = field.getAnnotation(CmdProp.class);
        String codec = cmdProp.deCodec();

        Method method = ToolConvert.class.getDeclaredMethod(codec, byte[].class); //方法名称，参数
        return method.invoke(null, src);//方法参数
    }


    public static String getFiledHexValue(Field field, Object obj) throws InvocationTargetException,NoSuchMethodException,IllegalAccessException{
        CmdProp cmdProp = field.getAnnotation(CmdProp.class);
        String codec = cmdProp.enCodec();

        Method method = ToolConvert.class.getDeclaredMethod(codec,Integer.class, Integer.class, field.getType()); //方法名称，返回长度,原始数值
        String value = (String)method.invoke(null,cmdProp.sort(), cmdProp.len(),obj);//方法参数
        return value;
    }



}
