package com.anigio.example.cloudapi.enums;

import lombok.Getter;

/**
 * @author: James
 * @date: 2019/1/25 3:24 PM
 * @description:
 */
@Getter
public enum EDataAnalysisType {
    NORMAL(0, "基础"),
    STAND(1, "标准"),
    SIMPLE(2, "精简");

    private int value;
    private String name;

    EDataAnalysisType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 数据解析协议
     * @param value 协议类型
     * @return EDataAnalysisType
     */
    public static EDataAnalysisType get(int value) {
        EDataAnalysisType[] values = EDataAnalysisType.values();
        for (EDataAnalysisType item : values) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return NORMAL;
    }
}
