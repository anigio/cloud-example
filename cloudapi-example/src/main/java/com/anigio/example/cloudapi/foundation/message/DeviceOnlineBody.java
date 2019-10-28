package com.anigio.example.cloudapi.foundation.message;

import com.alibaba.fastjson.JSONObject;
import com.anigio.common.json.ali.AliJSONMap;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric.Shang on 8/8/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceOnlineBody implements Serializable {

    private List<String> devices;

    /**
     * 数据内容
     * @return List<DataBean>
     */
    public List<String> getDevices() {
        if (this.devices == null) {
            setDevices(Lists.newArrayList());
        }
        return devices;
    }

    /**
     * body转称Map对象
     * @return Map<String, Object>
     */
    public Map<String, Object> bodymap() {
        return AliJSONMap.tomap(JSONObject.toJSONString(this));
    }


    /**
     * 设备在线模型
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OnlineBean {
        private String did;
        private Boolean online;
        private Long timestamp;
    }
}
