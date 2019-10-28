package com.anigio.example.cloudapi.enums;

import lombok.Getter;

/**
 * Created by Eric.Shang on 2018/9/10.
 */
@Getter
public enum EDataMethod {

    POST("POST"),
    PUT("PUT"),
    GET("GET"),
    DEL("DEL");

    private String name;

    EDataMethod(String name) {
        this.name = name;
    }

    /**
     * 校验数据操作合法性
     * @param name 数据操作
     * @return boolean
     */
    public static boolean invalid(String name) {
        EDataMethod[] values = EDataMethod.values();
        for (EDataMethod item : values) {
            if (item.getName().equals(name.toUpperCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数据操作枚举
     * @param name 数据操作
     * @return EDataMethod
     */
    public static EDataMethod get(String name) {
        EDataMethod[] values = EDataMethod.values();
        for (EDataMethod item : values) {
            if (item.getName().equals(name.toUpperCase())) {
                return item;
            }
        }
        return POST;
    }
}
