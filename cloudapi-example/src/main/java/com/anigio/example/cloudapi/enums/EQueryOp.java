package com.anigio.example.cloudapi.enums;

import lombok.Getter;

/**
 * Created by Eric.Shang on 30/5/17.
 */
@Getter
public enum EQueryOp {
    FUNCTION_1(1),
    FUNCTION_2(2),
    FUNCTION_3(3),
    FUNCTION_4(4),
    FUNCTION_5(5),
    FUNCTION_6(6),
    FUNCTION_7(7),
    FUNCTION_8(8),
    FUNCTION_9(9),
    FUNCTION_10(10),
    FUNCTION_11(11),
    FUNCTION_12(12),
    FUNCTION_13(13),
    FUNCTION_14(14),
    FUNCTION_15(15),
    FUNCTION_16(16),
    FUNCTION_17(17),
    FUNCTION_18(18),
    FUNCTION_19(19),
    FUNCTION_20(20),
    FUNCTION_21(21),
    FUNCTION_22(22),
    FUNCTION_23(23),
    FUNCTION_24(24),
    UNKNOWN(-1);

    private int value;

    EQueryOp(int value) {
        this.value = value;
    }

    /**
     * 校验枚举合法性
     * @param value 枚举值
     * @return boolean
     */
    public boolean isValid(int value) {
        return (this.getValue() == value);
    }

    /**
     * 枚举对象
     * @param value 枚举值
     * @return EQueryOp
     */
    public static EQueryOp getOp(int value) {
        EQueryOp[] values = EQueryOp.values();
        for (EQueryOp item : values) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return UNKNOWN;
    }
}
