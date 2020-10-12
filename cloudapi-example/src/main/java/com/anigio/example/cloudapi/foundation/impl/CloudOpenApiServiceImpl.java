package com.anigio.example.cloudapi.foundation.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.anigio.cloudapi.openapi.ApiService;
import com.anigio.cloudapi.openapi.domain.ApiResult;
import com.anigio.cloudapi.openapi.exception.ApiException;
import com.anigio.common.enums.EResponse;
import com.anigio.common.json.FluentMap;
import com.anigio.common.json.ali.AliJSONFilter;
import com.anigio.common.json.ali.AliJSONMap;
import com.anigio.example.cloudapi.enums.MK;
import com.anigio.example.cloudapi.foundation.CloudOpenApiService;
import com.anigio.example.cloudapi.foundation.message.DeviceOnlineBody;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Eric.Shang on 2018/12/10.
 */
@Service
@Slf4j
public class CloudOpenApiServiceImpl implements CloudOpenApiService {

    @Autowired
    private ApiService apiService;

    /**
     * 获取ApiService实例
     *
     * @return ApiService
     */
    @Override
    public ApiService getApi() {
        return apiService;
    }

    /**
     * 查询设备在线状态信息
     *
     * @param paramMap 数据内容
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> findDeviceOnlineStateInfoWhen(Map<String, Object> paramMap) {
        Map<String, Object> retmap = Maps.newHashMap();
        try {
            // 组装消息体Body内容
            String ret = getDeviceOnlineStateInfo(paramMap);

            List<DeviceOnlineBody.OnlineBean> retlist = JSONArray.parseArray(ApiResult.fromJson(ret).getData().toString(),
                    DeviceOnlineBody.OnlineBean.class);
            if (CollectionUtils.isEmpty(retlist)) {
                retlist = Lists.newArrayList();
            }
            for (DeviceOnlineBody.OnlineBean item : retlist) {
                if (item != null && StringUtils.isNotEmpty(item.getDid())) {
                    retmap.put(item.getDid(), item.getOnline());
                }
            }

        } catch (ApiException e) {
            log.error("findDeviceOnlineStateInfoWhen map={}, resp={}", paramMap, e.getResult());
        }
        return retmap;
    }

    /**
     * 查询设备在线状态
     * @param paramMap 数据内容
     * @return String
     * @throws ApiException ApiException
     */
    private String getDeviceOnlineStateInfo(Map<String, Object> paramMap) throws ApiException {
        List<String> devices = JSONArray.parseArray(MapUtils.getString(paramMap, MK.DATA), String.class);
        DeviceOnlineBody body = DeviceOnlineBody.builder().devices(devices).build();

        String appid = apiService.getConfigStorage().getAppId();
        return apiService.findDeviceOnlineInfoWhen(appid, body.bodymap());
    }

    /**
     * 设备在线状态
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceOnlineStateBaseInfo(Map<String, Object> paramMap) {
        try {
            // 组装消息体Body内容
            ApiResult ret = ApiResult.fromJson(getDeviceOnlineStateInfo(paramMap));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(data(ret)));
        } catch (ApiException e) {
            log.error("findDeviceOnlineStateBaseInfo map={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 查询设备在线信息
     *
     * @param dids 设备列表
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> findDeviceOnlineInfo(Set<String> dids) {
        Map<String, Object> onlineMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(dids)) {
            List<String> retlist = Lists.newArrayList(dids);
            FluentMap fmap;
            int limit = dids.size() / 10;
            int idx;
            for (idx = 0; idx < limit; idx++) {
                fmap = FluentMap.builder().add(MK.DATA, retlist.subList(idx * 10, (idx + 1) * 10));
                onlineMap.putAll(findDeviceOnlineStateInfoWhen(fmap.map()));
            }
            // 非10整数倍情况，查询剩余设备在线状态
            if ((idx * 10) < retlist.size()) {
                fmap = FluentMap.builder().add(MK.DATA, retlist.subList(idx * 10, retlist.size()));
                onlineMap.putAll(findDeviceOnlineStateInfoWhen(fmap.map()));
            }
        }

        return onlineMap;
    }

    /**
     * 设备控制消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] devicePropertyControlMessage(Map<String, Object> paramMap) {

        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.devicePropertyControlMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSONObject.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("devicePropertyControlMessage resp={}", e.getResult());
        }
        return EResponse.response(EResponse.FAILURE);
    }

    /**
     * 查询设备功能状态
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceFuncStateInfo(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.findDeviceFuncStateInfo(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSONObject.toJSONString(data(ret)));
        } catch (ApiException e) {
            log.error("findDeviceFuncStateInfo resp={}", e.getResult());
        }
        return EResponse.response(EResponse.FAILURE);
    }

    /**
     * 更新设备回调通知消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] updateDeviceNotifyMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.updateDeviceNotifyMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSONObject.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("findDeviceFuncStateInfo resp={}", e.getResult());
        }
        return EResponse.response(EResponse.FAILURE);
    }

    /**
     * 数据JSON格式化
     * @param ret 数据内容
     * @return Object
     */
    private Object data(ApiResult ret) {
        if (ret.getData() instanceof String) {
            return JSON.parse((String) ret.getData());
        }
        return ret.getData();
    }

    /**
     * 查询设备MQTT配置信息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findProductMQTTConfigInfo(Map<String, Object> paramMap) {
        try {
            String appid = apiService.getConfigStorage().getAppId();

            Map<String, Object> query = AliJSONMap.tomap(MapUtils.getString(paramMap, MK.DATA));
            ApiResult ret = ApiResult.fromJson(apiService.findProductMQTTConfig(appid, query));

            SimplePropertyPreFilter filter = AliJSONFilter.builder().exclude("heartbeat").filter();
            return EResponse.response(EResponse.SUCCESS, JSONArray.toJSONString(data(ret), filter));
        } catch (ApiException e) {
            log.error("findProductMQTTConfigInfo map={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备MQTT配置信息
     *
     * @param did 设备ID
     * @return Object[]
     */
    @Override
    public Object[] findDeviceMQTTConfigInfo(String did) {
        try {
            ApiResult ret = ApiResult.fromJson(apiService.findDeviceMQTTConfigInfo(did, null));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(data(ret)));
        } catch (ApiException e) {
            log.error("findDeviceMQTTConfigInfo did={}, resp={}", did, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 查询设备在线升级信息
     *
     * @param did  设备标识
     * @param body 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceOnlineUpgradeBaseInfo(String did, Map<String, Object> body) {
        try {
            ApiResult ret = ApiResult.fromJson(apiService.deviceOnlineUpgrade(did, body));

            return EResponse.response(EResponse.SUCCESS, JSONObject.toJSONString(data(ret)));
        } catch (ApiException e) {
            log.error("findDeviceOnlineUpgradeBaseInfo did={}, body={}, resp={}", did, body, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 查询设备数据信息
     *
     * @param did      用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceDataBaseInfo(String did, Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = AliJSONMap.tomap(MapUtils.getString(paramMap, MK.DATA));
            ApiResult ret = ApiResult.fromJson(apiService.findDeviceDataInfo(did, body));

            return EResponse.response(EResponse.SUCCESS, JSONArray.toJSONString(data(ret)));
        } catch (ApiException e) {
            log.error("findDeviceDataBaseInfo uid={}, body={}, resp={}", did, paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备属性查询
     *
     * @param did        设备ID
     * @param objectId   对象Id
     * @param instanceId 实例Id
     * @param resourceId 资源Id
     * @return Object[]
     */
    @Override
    public Object[] findDevicePropertyDataInfo(String did, String objectId, String instanceId, String resourceId) {
        try {
            if (objectId == null) {
                objectId = "";
            }
            if (StringUtils.isNotEmpty(instanceId)) {
                objectId += "/" + instanceId;
            }
            if (StringUtils.isNoneEmpty(instanceId, resourceId)) {
                objectId += "/" + resourceId;
            }
            ApiResult ret = ApiResult.fromJson(apiService.findDevicePropertyInfo(did, objectId));

            return EResponse.response(EResponse.SUCCESS, JSONArray.toJSONString(data(ret)));
        } catch (ApiException e) {
            log.error("findDevicePropertyDataInfo uid={}, oid={}, iid={}, rid={}, resp={}", did,
                    objectId, instanceId, resourceId, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备资源控制消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceResourceControlMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDeviceResourceControlMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDeviceResourceControlMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 控制消息内容
     * @param paramMap 数据内容
     * @return Map<String, Object>
     */
    private Map<String, Object> getBodyMessageMap(Map<String, Object> paramMap) {
        Map<String, Object> body = AliJSONMap.tomap(MapUtils.getString(paramMap, MK.DATA));
        if (paramMap.containsKey(MK.MESSAGE)) {
            // 重新设置属性控制消息体内容
            body.put(MK.DATA, JSON.parse(MapUtils.getString(paramMap, MK.MESSAGE)));
        }
        return body;
    }

    /**
     * 设备属性上传消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDevicePropertyUploadMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDevicePropertyUploadMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDevicePropertyUploadMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备重新拉取节点消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceNodeDataQueryMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDeviceNodeQueryMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDeviceNodeDataQueryMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备版本检查消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceUpgradeCheckMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDeviceUpgradeCheckMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDeviceUpgradeCheckMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备强制升级消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceUpgradeForceMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDeviceUpgradeForceMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDeviceUpgradeForceMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备重启指令消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceRebootMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDeviceRebootMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDeviceRebootMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备恢复设置消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceResetMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDeviceResetMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDeviceResetMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 设备日志上报消息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceLogUploadMessage(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.userDeviceLogUploadMessage(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("sendDeviceLogUploadMessage body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 商户根空间信息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findMerchantRootSpaceInfo(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.merchantRootSpaceInfo(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("findMerchantRootSpaceInfo body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 商户子空间信息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findMerchantNormalSpaceInfo(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.merchantSpaceInfo(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("findMerchantNormalSpaceInfo body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 空间设备列表
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findSpaceDeviceListInfo(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.merchantSpaceDeviceListInfo(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("findSpaceDeviceListInfo body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

    /**
     * 空间设备详情信息
     *
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findSpaceVirtualDeviceInfo(Map<String, Object> paramMap) {
        try {
            Map<String, Object> body = getBodyMessageMap(paramMap);

            String appid = apiService.getConfigStorage().getAppId();
            ApiResult ret = ApiResult.fromJson(apiService.merchantSpaceDeviceInfo(appid, body));

            return EResponse.response(EResponse.SUCCESS, JSON.toJSONString(ret.getData()));
        } catch (ApiException e) {
            log.error("findSpaceVirtualDeviceDetailInfo body={}, resp={}", paramMap, e.getResult());
        }
        return EResponse.ret();
    }

}
