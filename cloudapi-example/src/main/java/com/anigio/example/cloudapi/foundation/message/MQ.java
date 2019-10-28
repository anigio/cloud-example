package com.anigio.example.cloudapi.foundation.message;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Eric.Shang on 7/5/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MQ implements Serializable {

    private int qos;
    private int ttl;

    /**
     * 消息等级
     * @param qos MQTT等级
     * @return MQ
     */
    public MQ qos(int qos) {
        this.qos = qos;
        return this;
    }

    /**
     * 心跳存活时间
     * @param ttl 心跳时间
     * @return MQ
     */
    public MQ ttl(int ttl) {
        this.ttl = ttl;
        return this;
    }

    /**
     * 构建器对象
     * @return MQ
     */
    public static MQ build() {
        return MQ.builder().build().qos(0).ttl(1);
    }

    /**
     * 数据JSON转换
     * @return JSONObject
     */
    public JSONObject jsonObject() {
        return JSONObject.parseObject(this.jsonString());
    }

    /**
     * 数据转换JSON字符串
     * @return String
     */
    public String jsonString() {
        return JSONObject.toJSONString(this);
    }
}
