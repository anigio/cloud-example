package com.anigio.example.cloudapi.foundation;


import com.anigio.cloudapi.openapi.ApiService;

import java.util.Map;
import java.util.Set;

/**
 * 开放接口代理类
 * Created by Eric.Shang on 2018/12/10.
 */
public interface CloudOpenApiService {

    /**
     * 获取ApiService实例
     * @return ApiService
     */
    ApiService getApi();

    /**
     * 查询设备在线状态信息
     * @param paramMap 数据内容
     * @return Map<String, Object>
     */
    Map<String, Object> findDeviceOnlineStateInfoWhen(Map<String, Object> paramMap);

    /**
     * 设备在线状态
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDeviceOnlineStateBaseInfo(Map<String, Object> paramMap);

    /**
     * 查询设备在线信息
     * @param dids 设备列表
     * @return Map<String, Object>
     */
    Map<String, Object> findDeviceOnlineInfo(Set<String> dids);

    /**
     * 设备控制消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] devicePropertyControlMessage(Map<String, Object> paramMap);

    /**
     * 查询设备功能状态
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDeviceFuncStateInfo(Map<String, Object> paramMap);

    /**
     * 更新设备回调通知消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] updateDeviceNotifyMessage(Map<String, Object> paramMap);

    /**
     * 查询设备MQTT配置信息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findProductMQTTConfigInfo(Map<String, Object> paramMap);

    /**
     * 设备MQTT配置信息
     * @param did 设备ID
     * @return Object[]
     */
    Object[] findDeviceMQTTConfigInfo(String did);

    /**
     * 查询设备在线升级信息
     * @param did 设备标识
     * @param body 数据内容
     * @return Object[]
     */
    Object[] findDeviceOnlineUpgradeBaseInfo(String did, Map<String, Object> body);

    /**
     * 查询设备数据信息
     * @param did 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDeviceDataBaseInfo(String did, Map<String, Object> paramMap);

    /**
     * 设备属性查询
     * @param did 设备ID
     * @param objectId 对象Id
     * @param instanceId 实例Id
     * @param resourceId 资源Id
     * @return Object[]
     */
    Object[] findDevicePropertyDataInfo(String did, String objectId, String instanceId, String resourceId);

    /**
     * 设备资源控制消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceResourceControlMessage(Map<String, Object> paramMap);

    /**
     * 设备属性上传消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDevicePropertyUploadMessage(Map<String, Object> paramMap);

    /**
     * 设备重新拉取节点消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceNodeDataQueryMessage(Map<String, Object> paramMap);

    /**
     * 设备版本检查消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceUpgradeCheckMessage(Map<String, Object> paramMap);

    /**
     * 设备强制升级消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceUpgradeForceMessage(Map<String, Object> paramMap);

    /**
     * 设备重启指令消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceRebootMessage(Map<String, Object> paramMap);

    /**
     * 设备恢复设置消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceResetMessage(Map<String, Object> paramMap);

    /**
     * 设备日志上报消息
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceLogUploadMessage(Map<String, Object> paramMap);

}
