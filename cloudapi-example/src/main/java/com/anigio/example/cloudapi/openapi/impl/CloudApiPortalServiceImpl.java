package com.anigio.example.cloudapi.openapi.impl;

import com.anigio.common.enums.EBoolType;
import com.anigio.common.enums.EResponse;
import com.anigio.common.json.FluentMap;
import com.anigio.common.response.DefaultResponse;
import com.anigio.common.response.JSONArrayResponse;
import com.anigio.common.response.JSONObjectResponse;
import com.anigio.common.response.PagingQueryResponse;
import com.anigio.common.response.template.ParamResponse;
import com.anigio.common.response.template.Response;
import com.anigio.example.cloudapi.core.proxy.OpenApiProxyService;
import com.anigio.example.cloudapi.ecode.ECode;
import com.anigio.example.cloudapi.ecode.ESysCodeManage;
import com.anigio.example.cloudapi.enums.EQueryOp;
import com.anigio.example.cloudapi.enums.MK;
import com.anigio.example.cloudapi.openapi.request.CloudApiInfoRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Eric.Shang on 5/2/18.
 */
@RestController
@RequestMapping(value = "cloudapi", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CloudApiPortalServiceImpl {

    @Autowired
    private OpenApiProxyService openApiProxyService;


    /**
     * 控制代理请求
     *
     * @param userid 用户标识
     * @param op     功能标识
     * @param info   数据内容
     * @return Response
     */
    @PostMapping(value = "{UID}/proxy")
    public Response cloudApiProxyRequestInfo(@PathVariable("UID") String userid,
                                             @RequestParam(required = false, defaultValue = "1") Integer op,
                                             @RequestBody CloudApiInfoRequest info) {
        EQueryOp queryOp = EQueryOp.getOp(info.userid(userid).op(op).getOp());
        Object[] resp;
        switch (queryOp) {
            case FUNCTION_2: // TODO: 设备属性控制(简单)
                if (info.getObjectId() == null || info.getValue() == null || StringUtils.isEmpty(info.getDeviceId())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceControlMessage(userid, info.deviceSimplePropControlMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_3: // TODO: 设备功能控制
                if (StringUtils.isAnyBlank(info.getDeviceId(), info.getFuncIdentity()) || info.getFuncValue() == null) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceControlMessage(userid, info.deviceFuncControlMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_4: // TODO: 设备属性查询(复杂)
                if (info.invalidPropQueryParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findDevicePropertyQueryInfo(userid, info.devicePropQueryMap());
                if (EResponse.toBool(resp[0])) {
                    return JSONArrayResponse.from(resp[1]);
                }
                break;
            case FUNCTION_5: // TODO: 设备在线状态
                if (info.getDevices().isEmpty()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findDeviceOnlineStateInfo(userid, info.deviceOnlineStateMap());
                if (EResponse.toBool(resp[0])) {
                    return JSONArrayResponse.from(resp[1]);
                }
                break;
            case FUNCTION_6: // TODO: 设备MQTT配置
                if (StringUtils.isEmpty(info.getDeviceId())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findDeviceMQTTConfigInfo(userid, FluentMap.builder()
                        .add(MK.DEVICE_ID, info.getDeviceId()).map());
                if (EResponse.toBool(resp[0])) {
                    return JSONObjectResponse.from(resp[1]);
                }
                break;
            case FUNCTION_7: // TODO: 产品MQTT配置
                if (CollectionUtils.isEmpty(info.getProdIds())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findProductMQTTConfigInfo(userid, info.productMQTTConfigMap());
                if (EResponse.toBool(resp[0])) {
                    return JSONArrayResponse.from(resp[1]);
                }
                break;
            case FUNCTION_8: // TODO: 设备升级检查
                if (info.invalidDeviceUpgradeParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findDeviceUpgradeConfigInfo(userid, info.deviceUpgradeMap());
                if (EResponse.toBool(resp[0])) {
                    return JSONObjectResponse.from(resp[1]);
                }
                break;
            case FUNCTION_9: // TODO: 设备资源控制消息
                if (StringUtils.isEmpty(info.getDeviceId())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDevicePropertyControlMessage(userid, info.devicePropControlMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_10: // TODO: 设备属性重传消息
                if (StringUtils.isEmpty(info.getDeviceId())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDevicePropertyUploadMessage(userid, info.devicePropControlMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_11: // TODO:  设备节点数据查询消息
                if (StringUtils.isAnyBlank(info.getDeviceId(), info.getNid())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceNodeDataQueryMessage(userid, info.deviceNodeDataQueryMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_12: // TODO: 设备版本检查
                if (StringUtils.isEmpty(info.getDeviceId()) || EBoolType.invalid(info.getCheck())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceUpgradeCheckMessage(userid, info.deviceUpgradeCheckMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_13: // TODO: 设备强制升级
                if (StringUtils.isAnyBlank(info.getDeviceId(), info.getFile(), info.getVersion(), info.getChecksum())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceUpgradeForceMessage(userid, info.deviceUpgradeForceMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_14: // TODO: 设备重启消息
                if (StringUtils.isEmpty(info.getDeviceId())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceRebootControlMessage(userid, info.deviceRebootMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_15: // TODO: 设备恢复重置消息
                if (StringUtils.isEmpty(info.getDeviceId()) || EBoolType.invalid(info.getReset())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceResetControlMessage(userid, info.deviceResetMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_16: // TODO: 设备日志上报
                if (StringUtils.isEmpty(info.getDeviceId())) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceLogUploadMessage(userid, info.deviceLogUploadMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_18: // TODO: 批量功能控制
                if (info.invalidBatchControlParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.sendDeviceControlMessage(userid, info.batchControlFuncMessgaeMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_19: // TODO: 设备功能状态查询
                if (info.invalidDeviceFuncParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findDeviceFuncStateInfo(userid, info.deviceFuncMap());
                if (EResponse.toBool(resp[0])) {
                    return JSONArrayResponse.from(resp[1]);
                }
                break;
            case FUNCTION_20: // TODO: 更新设备回调通知消息
                if (info.invalidNotifyParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.updateDeviceNotifyMessage(userid, info.notifyMessageMap());
                if (EResponse.toBool(resp[0])) {
                    return DefaultResponse.response();
                }
                break;
            case FUNCTION_21: // TODO: 商户根空间信息
                resp = openApiProxyService.findMerchantRootSpaceInfo(userid, info.merchantRoomSpaceInfoMap());
                if (EResponse.toBool(resp[0])) {
                    return PagingQueryResponse.build().list(resp[1]).total(resp[2]).page(info.getPage());
                }
                break;
            case FUNCTION_22: // TODO: 商户子空间信息
                if (info.invalidMerchantSpaceInfoParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findMerchantSpaceInfo(userid, info.merchantSpaceInfoMap());
                if (EResponse.toBool(resp[0])) {
                    return PagingQueryResponse.build().list(resp[1]).total(resp[2]).page(info.getPage());
                }
                break;
            case FUNCTION_23: // TODO: 空间设备列表
                if (info.invalidSpaceDeviceQueryParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findSpaceDeviceListInfo(userid, info.spaceDeviceQueryMap());
                if (EResponse.toBool(resp[0])) {
                    return PagingQueryResponse.build().list(resp[1]).total(resp[2]).page(info.getPage());
                }
                break;
            case FUNCTION_24: // TODO: 空间设备信息
                if (info.invalidDeviceInfoQueryParam()) {
                    return ParamResponse.from(ECode.E_INVALID_PARAM);
                }
                resp = openApiProxyService.findSpaceVirtualDeviceDetailInfo(userid, info.deviceInfoQueryMap());
                if (EResponse.toBool(resp[0])) {
                    return JSONObjectResponse.from(resp[1]);
                }
                break;
            default:
                return ParamResponse.from(ECode.E_ILLEGAL_REQUEST);
        }
        return ESysCodeManage.syscode(resp[1]);
    }
}
