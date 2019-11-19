package com.anigio.example.cloudapi.core.proxy.impl;

import com.alibaba.fastjson.JSON;
import com.anigio.cloudapi.openapi.domain.OpenApi;
import com.anigio.common.enums.EResponse;
import com.anigio.example.cloudapi.enums.MK;
import com.anigio.example.cloudapi.core.proxy.OpenApiProxyService;
import com.anigio.example.cloudapi.enums.ESysCode;
import com.anigio.example.cloudapi.foundation.CloudOpenApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Eric.Shang on 4/2/18.
 */
@Slf4j
@Service
public class OpenApiProxyServiceImpl implements OpenApiProxyService {

    @Autowired
    private CloudOpenApiService cloudOpenApiService;


    /**
     * 发送设备控制消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceControlMessage(String userid, Map<String, Object> paramMap) {

        // 发送设备属性控制消息
        Object[] resp = cloudOpenApiService.devicePropertyControlMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceControlMessage userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        log.info("recv={}", resp[1]);
        return EResponse.response(EResponse.SUCCESS);
    }

    /**
     * 查询功能状态信息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceFuncStateInfo(String userid, Map<String, Object> paramMap) {

        Object[] resp = cloudOpenApiService.findDeviceFuncStateInfo(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("findDeviceFuncStateInfo userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return EResponse.response(EResponse.SUCCESS, resp[1]);
    }

    /**
     * 更新设备回调通知消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] updateDeviceNotifyMessage(String userid, Map<String, Object> paramMap) {
        // 发送设备属性控制消息
        Object[] resp = cloudOpenApiService.updateDeviceNotifyMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("updateDeviceNotifyMessage userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return EResponse.response(EResponse.SUCCESS);
    }

    /**
     * 查询设备属性信息(GET)
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDevicePropertyGetQueryInfo(String userid, Map<String, Object> paramMap) {

        Object[] resp = cloudOpenApiService.findDevicePropertyDataInfo(MapUtils.getString(paramMap, MK.DEVICE_ID),
                MapUtils.getString(paramMap, MK.OBJECT_ID, ""),
                MapUtils.getString(paramMap, MK.INSTANCE_ID, "0"),
                MapUtils.getString(paramMap, MK.RESOURCE_ID, ""));
        if (EResponse.toFail(resp[0])) {
            log.warn("findDevicePropertyGetQueryInfo invalid param, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 查询设备属性消息(POST)
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDevicePropertyQueryInfo(String userid, Map<String, Object> paramMap) {

        String deviceId = MapUtils.getString(paramMap, MK.DEVICE_ID);
        Object[] resp = cloudOpenApiService.findDeviceDataBaseInfo(deviceId, paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("findDevicePropertyQueryInfo invalid req, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备在线状态查询
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceOnlineStateInfo(String userid, Map<String, Object> paramMap) {

        Object[] resp = cloudOpenApiService.findDeviceOnlineStateBaseInfo(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("findDeviceOnlineStateInfo invalid req, map={}", paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备MQTT配置信息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceMQTTConfigInfo(String userid, Map<String, Object> paramMap) {

        Object[] resp = cloudOpenApiService.findDeviceMQTTConfigInfo(MapUtils.getString(paramMap, MK.DEVICE_ID));
        if (EResponse.toFail(resp[0])) {
            log.warn("findDeviceMQTTConfigInfo invalid req, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 产品MQTT配置信息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findProductMQTTConfigInfo(String userid, Map<String, Object> paramMap) {

        Object[] resp = cloudOpenApiService.findProductMQTTConfigInfo(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("findProductMQTTConfigInfo invalid req, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备固件升级检查
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] findDeviceUpgradeConfigInfo(String userid, Map<String, Object> paramMap) {

        OpenApi.DeviceUpgradeMessage message = JSON.parseObject(MapUtils.getString(paramMap, MK.DATA),
                OpenApi.DeviceUpgradeMessage.class);

        String deviceId = MapUtils.getString(paramMap, MK.DEVICE_ID);
        Object[] resp = cloudOpenApiService.findDeviceOnlineUpgradeBaseInfo(deviceId, message.map());
        if (EResponse.toFail(resp[0])) {
            log.warn("findDeviceUpgradeConfigInfo userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备资源控制消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDevicePropertyControlMessage(String userid, Map<String, Object> paramMap) {

        Object[] resp = cloudOpenApiService.sendDeviceResourceControlMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceControlMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备属性重传消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDevicePropertyUploadMessage(String userid, Map<String, Object> paramMap) {
        Object[] resp = cloudOpenApiService.sendDevicePropertyUploadMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDevicePropertyUploadMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备节点数据查询请求
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceNodeDataQueryMessage(String userid, Map<String, Object> paramMap) {
        Object[] resp = cloudOpenApiService.sendDeviceNodeDataQueryMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceNodeDataQueryMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备版本检查消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceUpgradeCheckMessage(String userid, Map<String, Object> paramMap) {
        Object[] resp = cloudOpenApiService.sendDeviceUpgradeCheckMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceUpgradeCheckMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备强制升级消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceUpgradeForceMessage(String userid, Map<String, Object> paramMap) {
        Object[] resp = cloudOpenApiService.sendDeviceUpgradeForceMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceUpgradeForceMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备重启消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceRebootControlMessage(String userid, Map<String, Object> paramMap) {
        Object[] resp = cloudOpenApiService.sendDeviceRebootMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceRebootControlMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备恢复重置消息
     *
     * @param userid   用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceResetControlMessage(String userid, Map<String, Object> paramMap) {
        Object[] resp = cloudOpenApiService.sendDeviceResetMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceResetControlMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

    /**
     * 设备日志上传消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    @Override
    public Object[] sendDeviceLogUploadMessage(String userid, Map<String, Object> paramMap) {
        Object[] resp = cloudOpenApiService.sendDeviceLogUploadMessage(paramMap);
        if (EResponse.toFail(resp[0])) {
            log.warn("sendDeviceLogUploadMessage invalid send, userid={}, map={}", userid, paramMap);
            return EResponse.response(EResponse.FAILURE, ESysCode.RESPONSE);
        }
        return resp;
    }

}
