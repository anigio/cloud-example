package com.anigio.example.cloudapi.ecode;

import com.anigio.common.response.template.ParamResponse;
import com.anigio.common.response.template.Response;
import com.anigio.example.cloudapi.enums.ESysCode;

/**
 * Created by Eric.Shang on 23/8/17.
 */
public final class ESysCodeManage {

    private ESysCodeManage() {
    }

    /**
     * 系统接口错误码
     * @param recv 系统内误码码
     * @return Response
     */
    public static Response syscode(Object recv) {
        if (recv instanceof ESysCode) {
            ESysCode sysCode = (ESysCode) recv;
            switch (sysCode) {
                case RESPONSE:
                    return ParamResponse.from(ECode.E_RESPONSE);
                case PARAMS:
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                case ILLEGAL_REQUEST:
                    return ParamResponse.from(ECode.E_ILLEGAL_REQUEST);
                default:
                    return ParamResponse.from(ECode.E_SYSTEM);
            }
        }
        if (recv instanceof ECode) {
            ECode[] values = ECode.values();
            for (ECode item : values) {
                if (item == recv) {
                    return ParamResponse.from((ECode) recv);
                }
            }
        }
        return ParamResponse.from(ECode.E_RESPONSE);
    }
}
