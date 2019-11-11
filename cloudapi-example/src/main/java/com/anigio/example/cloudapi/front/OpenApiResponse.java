package com.anigio.example.cloudapi.front;

import com.alibaba.fastjson.JSON;
import com.anigio.common.json.ali.AliJSONWrapper;
import com.anigio.example.cloudapi.ecode.ECode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Eric.Shang on 2019-11-11.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiResponse implements Serializable {

    private String message;
    private Integer code;
    private Object data;

    public String jsonString() {
        return JSON.toJSONString(this);
    }

    public String getMessage() {
        return Optional.ofNullable(message).orElse(ECode.E_OK.message());
    }

    public int getCode() {
        return Optional.ofNullable(code).orElse(ECode.E_OK.code());
    }

    public Object getData() {
        return Optional.ofNullable(data).orElse(AliJSONWrapper.builder().jsonObject());
    }
}
