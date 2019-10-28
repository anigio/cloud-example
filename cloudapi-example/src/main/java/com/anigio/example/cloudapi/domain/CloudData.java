package com.anigio.example.cloudapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * Created by Eric.Shang on 2018/9/7.
 */
public class CloudData implements Serializable {

    /**
     * 数据存储格式
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataReq implements Serializable {
        private String id; // 存储标识，HBase存储
        private String key; // 设备标识
    }

    /**
     * 数据存储格式
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataScheme implements Serializable {
        private String key; // 存储标识
        private Map<String, Object> data;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StandardScheme implements Serializable {
            private Object r;
            private Object k;
        }
    }

    /**
     * 控制消息MQ
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MQReq implements Serializable {
        private Integer qos; // 消息QoS级别
        private Integer ttl; // 可以忽略, 默认300

        /**
         * QoS
         * @return Integer
         */
        public Integer getQos() {
            if (this.qos == null) {
                setQos(0);
            }
            return qos;
        }

        /**
         * 消息有效时间
         * @return Integer
         */
        public Integer getTtl() {
            if (this.ttl == null) {
                setTtl(300);
            }
            return ttl;
        }
    }
}
