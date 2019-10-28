package com.anigio.example.cloudapi.ecode;

import com.anigio.tool.enums.FieldConfig;
import com.anigio.tool.enums.FieldConfigUtils;
import com.anigio.tool.enums.IECode;

/**
 * Created by Eric.Shang on 8/3/17.
 */
public enum ECode implements IECode {
    @FieldConfig(code = 5401, msg = "invalid param")
    E_INVALID_PARAM,
    @FieldConfig(code = 5402, msg = "illegal request")
    E_ILLEGAL_REQUEST,
    @FieldConfig(code = 5403, msg = "request refresh")
    E_REQ_REFRESH,
    @FieldConfig(code = 5404, msg = "response fail")
    E_RESPONSE,
    @FieldConfig(code = 200, msg = "ok")
    E_OK,
    @FieldConfig(code = -1, msg = "system error")
    E_SYSTEM;

    /**
     * 错误码描述
     * @return String
     */
    public String message() {
        String msg;
        try {
            msg = FieldConfigUtils.getFieldConfig(this).msg();
        } catch (Exception e) {
            msg = "Unknown Exception";
        }
        return msg;
    }

    /**
     * 错误码
     * @return Integer
     */
    public Integer code() {
        Integer code;
        try {
            code = FieldConfigUtils.getFieldConfig(this).code();
        } catch (Exception e) {
            code = E_SYSTEM.code();
        }
        return code;
    }

    /**
     * 错误码枚举类
     * @param code 错误码
     * @return ECode
     */
    public static ECode getCode(int code) {

        ECode[] values = ECode.values();
        for (ECode item : values) {
            if (item.code() == code) {
                return item;
            }
        }

        return E_RESPONSE;
    }
}
