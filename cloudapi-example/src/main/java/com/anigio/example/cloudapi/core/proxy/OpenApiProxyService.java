package com.anigio.example.cloudapi.core.proxy;

import java.util.Map;

/**
 * Created by Eric.Shang on 4/2/18.
 */
public interface OpenApiProxyService {

    /**
     * 发送设备控制消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceControlMessage(String userid, Map<String, Object> paramMap);

    /**
     * 查询功能状态信息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDeviceFuncStateInfo(String userid, Map<String, Object> paramMap);

    /**
     * 更新设备回调通知消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] updateDeviceNotifyMessage(String userid, Map<String, Object> paramMap);

    /**
     * 查询设备属性信息(GET)
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDevicePropertyGetQueryInfo(String userid, Map<String, Object> paramMap);

    /**
     * 查询设备属性消息(POST)
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDevicePropertyQueryInfo(String userid, Map<String, Object> paramMap);

    /**
     * 设备在线状态查询
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDeviceOnlineStateInfo(String userid, Map<String, Object> paramMap);

    /**
     * 设备MQTT配置信息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDeviceMQTTConfigInfo(String userid, Map<String, Object> paramMap);

    /**
     * 产品MQTT配置信息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findProductMQTTConfigInfo(String userid, Map<String, Object> paramMap);

    /**
     * 设备固件升级检查
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findDeviceUpgradeConfigInfo(String userid, Map<String, Object> paramMap);

    /**
     * 设备资源控制消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDevicePropertyControlMessage(String userid, Map<String, Object> paramMap);

    /**
     * 设备属性上传消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDevicePropertyUploadMessage(String userid, Map<String, Object> paramMap);

    /**
     * 设备重新拉取节点消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceNodeDataQueryMessage(String userid, Map<String, Object> paramMap);

    /**
     * 设备版本检查消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceUpgradeCheckMessage(String userid, Map<String, Object> paramMap);

    /**
     * 设备强制升级消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceUpgradeForceMessage(String userid, Map<String, Object> paramMap);

    /**
     * 设备重启指令消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceRebootControlMessage(String userid, Map<String, Object> paramMap);

    /**
     * 设备恢复设置消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceResetControlMessage(String userid, Map<String, Object> paramMap);

    /**
     * 设备日志上报消息
     * @param userid 用户标识
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] sendDeviceLogUploadMessage(String userid, Map<String, Object> paramMap);

    /**
     * 商户根空间信息
     * @param userid 用户ID
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findMerchantRootSpaceInfo(String userid, Map<String, Object> paramMap);

    /**
     * 商户子空间信息
     * @param userid 用户ID
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findMerchantSpaceInfo(String userid, Map<String, Object> paramMap);

    /**
     * 空间设备列表
     * @param userid 用户ID
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findSpaceDeviceListInfo(String userid, Map<String, Object> paramMap);

    /**
     * 空间设备详情信息
     * @param userid 用户ID
     * @param paramMap 数据内容
     * @return Object[]
     */
    Object[] findSpaceVirtualDeviceDetailInfo(String userid, Map<String, Object> paramMap);
}
