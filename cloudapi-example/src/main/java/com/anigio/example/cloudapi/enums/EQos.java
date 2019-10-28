package com.anigio.example.cloudapi.enums;

import lombok.Data;
import lombok.Getter;

/**
 * Created by Eric.Shang on 14/8/17.
 */
@Getter
public enum EQos {
    S0(0, "0"),
    S1(1, "1"),
    S2(2, "2");

    private int value;
    private String name;

    EQos(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 合法性校验
     * @param type 类型
     * @return boolean
     */
    public static boolean invalid(int type) {
        EQos[] values = EQos.values();
        for (EQos item : values) {
            if (item.getValue() == type) {
                return false;
            }
        }
        return true;
    }
}
