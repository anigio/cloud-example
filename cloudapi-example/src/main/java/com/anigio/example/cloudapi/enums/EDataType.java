package com.anigio.example.cloudapi.enums;

import lombok.Getter;

/**
 * Created by Eric.Shang on 2018/9/10.
 */
@Getter
public enum EDataType {

    PROPERTY("PROP"),
    SETTING("SETTING"),
    SCHEDULE("TASK"),
    NODE("NODE");

    private String name;

    EDataType(String name) {
        this.name = name;
    }

    /**
     * 校验参数合法性
     * @param name 数据类型
     * @return boolean
     */
    public static boolean invalid(String name) {
        EDataType[] values = EDataType.values();
        for (EDataType item : values) {
            if (item.getName().equals(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设备数据请求
     * @param name 数据类型
     * @return boolean
     */
    public static boolean isDataRequest(String name) {
        EDataType[] values = new EDataType[]{PROPERTY, SETTING, SCHEDULE};
        for (EDataType item : values) {
            if (item.getName().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 节点数据请求
     * @param name 数据类型
     * @return boolean
     */
    public static boolean isNodeRequest(String name) {
        EDataType[] values = new EDataType[]{NODE};
        for (EDataType item : values) {
            if (item.getName().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
