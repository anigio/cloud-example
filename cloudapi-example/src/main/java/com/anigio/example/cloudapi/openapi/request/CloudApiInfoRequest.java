package com.anigio.example.cloudapi.openapi.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anigio.cloudapi.openapi.domain.OpenApi;
import com.anigio.common.json.FluentMap;
import com.anigio.common.json.ali.AliJSONArrayWrapper;
import com.anigio.common.json.ali.AliJSONWrapper;
import com.anigio.example.cloudapi.domain.CloudData;
import com.anigio.example.cloudapi.enums.EDataMethod;
import com.anigio.example.cloudapi.enums.EDataType;
import com.anigio.example.cloudapi.enums.EDataAnalysisType;
import com.anigio.example.cloudapi.enums.MK;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by Eric.Shang on 5/2/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudApiInfoRequest implements Serializable {

    private String userid; // 用户标识
    private Integer op; // 功能标识

    private String deviceId; // 设备DID

    // 设备属性控制复杂协议
    private String command; // 控制消息指令
    private Object data; // 控制消息
    private Object args; // 设备拓展参数

    private CloudData.MQReq mq; // 控制消息内容
    private Object meta; // 控制拓展数据


    // 设备属性控制简单协议
    private Object objectId; // 对象ID
    private Integer instanceId; // 对象实例ID
    private Object resourceId; // 资源ID
    private Integer dataAnalysis; // 数据解析协议
    private Object value; // 设备属性值

    public Map<String, Object> deviceSimplePropControlMap() {

        // objectId 为整型数据或者非整型数据，整型数据与instanceId进行计算
        OpenApi.ControlMessage.DataReq dataReq = OpenApi.ControlMessage.DataReq.builder().id(getObjectId())
                .v(getValue()).build();
        if (getObjectId() instanceof Integer) {
            dataReq.id((Integer) getObjectId() | getInstanceId() << 16);
        }

        // 属性解析协议格式
        EDataAnalysisType analysisType = EDataAnalysisType.get(getDataAnalysis());
        switch (analysisType) {
            case STAND: // 标准
                if (getResourceId() != null) {
                    dataReq.v(Lists.newArrayList(OpenApi.ControlMessage.DataReq.Resource.builder()
                            .r(getResourceId()).v(getValue()).build()));
                }
                break;
            case SIMPLE: // 精简
                if (getResourceId() != null) {
                    dataReq.v(Lists.newArrayList(AliJSONWrapper.builder().put(String.valueOf(getResourceId()), getValue())
                            .jsonObject()));
                }
                break;
            default:
                break;
        }

        // 设备属性控制消息
        OpenApi.ControlMessage controlMessage = OpenApi.ControlMessage.builder().dataType(OpenApi.EControlDataType.PROP.getName())
                .to(getDeviceId()).cmd(OpenApi.ENotify.PROP_PUT.getName()).data(dataReq).args(getArgs()).build();
        return FluentMap.builder().add(MK.DATA, controlMessage.jsonString()).add(MK.DEVICE_ID, getDeviceId()).map();
    }

    private Integer getInstanceId() {
        if (this.instanceId == null) {
            setInstanceId(0);
        }
        return instanceId;
    }

    private Integer getDataAnalysis() {
        if (this.dataAnalysis == null) {
            setDataAnalysis(EDataAnalysisType.STAND.getValue());
        }
        return dataAnalysis;
    }

    // 设备功能控制
    private String funcIdentity; // 功能惟一标识
    private Object funcValue; // 功能内容

    public Map<String, Object> deviceFuncControlMap() {
        // 设备属性控制消息
        OpenApi.ControlFuncMessage controlMessage = OpenApi.ControlFuncMessage.builder().dataType(OpenApi.EControlDataType.FUNC.getName())
                .to(getDeviceId()).funcIdentity(getFuncIdentity()).funcValue(getFuncValue()).build();
        return FluentMap.builder().add(MK.DATA, controlMessage.jsonString()).add(MK.DEVICE_ID, getDeviceId()).map();
    }

    // 批量功能控制
    public boolean invalidBatchControlParam() {
        if (getData() == null) {
            return true;
        }
        String retInfo = String.valueOf(getData());
        if (StringUtils.isEmpty(retInfo) || !Pattern.compile(MK.EXP_JSON_A).matcher(retInfo).find()) {
            return true;
        }
        List<OpenApi.BatchControlFuncMessage.FuncData> funcData = JSON.parseArray(JSON.toJSONString(getData()),
                OpenApi.BatchControlFuncMessage.FuncData.class);
        for (OpenApi.BatchControlFuncMessage.FuncData item : funcData) {
            if (StringUtils.isAnyBlank(item.getDeviceId(), item.getFuncIdentity()) || item.getFuncValue() == null) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> batchControlFuncMessgaeMap() {
        List<OpenApi.BatchControlFuncMessage.FuncData> funcData = JSON.parseArray(JSON.toJSONString(getData()),
                OpenApi.BatchControlFuncMessage.FuncData.class);
        for (OpenApi.BatchControlFuncMessage.FuncData item : funcData) {
            item.to(item.getDeviceId()).setDeviceId(null); // 转换批量控制协议
        }
        // 批量功能控制MQTT消息
        OpenApi.BatchControlFuncMessage message = OpenApi.BatchControlFuncMessage.builder()
                .dataType(OpenApi.EControlDataType.BATCH.getName()).data(funcData).build();
        return FluentMap.builder().add(MK.DATA, message.jsonString()).map();
    }

    // 设备属性查询
    public boolean invalidPropQueryParam() {
        if (getData() != null) {
            String message = JSONObject.toJSONString(getData());
            return !(Pattern.compile(MK.EXP_JSON_A).matcher(message).find()
                    || Pattern.compile(MK.EXP_JSON_O).matcher(message).find());
        }
        return StringUtils.isAnyBlank(getDeviceId());
    }

    public Map<String, Object> devicePropQueryMap() {
        OpenApi.PropQueryMessage queryMessage = OpenApi.PropQueryMessage.builder().name(EDataType.PROPERTY.getName())
                .method(EDataMethod.GET.getName()).data(getData()).build();
        return FluentMap.builder().add(MK.DATA, queryMessage.jsonString()).add(MK.DEVICE_ID, getDeviceId()).map();
    }

    // 设备在线状态查询
    private List<String> devices;

    public Map<String, Object> deviceOnlineStateMap() {
        return FluentMap.builder().add(MK.DATA, getDevices()).map();
    }

    public List<String> getDevices() {
        if (this.devices == null) {
            setDevices(Lists.newArrayList());
        }
        return devices;
    }

    // 产品MQTT配置
    private List<String> prodIds;

    public Map<String, Object> productMQTTConfigMap() {
        return FluentMap.builder().add(MK.DATA, AliJSONWrapper.builder().put("pids",
                String.join(",", getProdIds())).jsonString()).map();
    }

    public List<String> getProdIds() {
        if (this.prodIds == null) {
            setProdIds(Lists.newArrayList());
        }
        return prodIds;
    }

    // 设备检查升级
    private String version;

    public boolean invalidDeviceUpgradeParam() {
        return StringUtils.isAnyBlank(getDeviceId(), getCommand(), getVersion());
    }

    public Map<String, Object> deviceUpgradeMap() {
        OpenApi.DeviceUpgradeMessage message = OpenApi.DeviceUpgradeMessage.builder().cmd(getCommand())
                .version(getVersion()).build();

        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备资源控制或设备属性重传
    public Map<String, Object> devicePropControlMessageMap() {
        if (getData() == null) {
            setData(AliJSONArrayWrapper.builder().jsonArray());
        }
        OpenApi.ControlMessage message = OpenApi.ControlMessage.builder().to(getDeviceId()).data(getData()).build();

        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备节点查询
    private String nid;

    public Map<String, Object> deviceNodeDataQueryMessageMap() {
        if (getData() == null) {
            setData(AliJSONArrayWrapper.builder().jsonArray());
        }
        OpenApi.ControlMessage message = OpenApi.ControlMessage.builder().to(getDeviceId()).nid(getNid()).build();
        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备版本检查
    private Integer check;

    public Integer getCheck() {
        if (this.check == null) {
            setCheck(0);
        }
        return check;
    }

    public Map<String, Object> deviceUpgradeCheckMessageMap() {
        OpenApi.UpgradeCheckMessage message = OpenApi.UpgradeCheckMessage.builder().to(getDeviceId())
                .check(getCheck()).build();
        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备强制升级
    private String file;
    private String checksum;

    public Map<String, Object> deviceUpgradeForceMessageMap() {
        OpenApi.UpgradeForceMessage message = OpenApi.UpgradeForceMessage.builder().to(getDeviceId())
                .file(getFile()).version(getVersion()).checksum(getChecksum()).build();
        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备重启
    public Map<String, Object> deviceRebootMessageMap() {
        OpenApi.ControlMessage message = OpenApi.ControlMessage.builder().to(getDeviceId()).build();
        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备恢复重置
    private Integer reset;

    public Integer getReset() {
        if (this.reset == null) {
            setReset(0);
        }
        return reset;
    }

    public Map<String, Object> deviceResetMessageMap() {
        OpenApi.DeviceResetMessage message = OpenApi.DeviceResetMessage.builder().to(getDeviceId())
                .reset(getReset()).build();
        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备日志上报
    public Map<String, Object> deviceLogUploadMessageMap() {
        OpenApi.ControlMessage message = OpenApi.ControlMessage.builder().to(getDeviceId()).build();
        return FluentMap.builder().add(MK.DEVICE_ID, getDeviceId()).add(MK.DATA, message.jsonString()).map();
    }

    // 设备功能状态查询
    private List<OpenApi.DeviceFuncStateInfo.FunctionInfo> functions;

    /**
     * 设备功能状态参数
     * @return boolean
     */
    public boolean invalidDeviceFuncParam() {
        if (CollectionUtils.isEmpty(getFunctions()) || getFunctions().size() > 20) {
            return true;
        }
        for (OpenApi.DeviceFuncStateInfo.FunctionInfo item : getFunctions()) {
            if (StringUtils.isAnyEmpty(item.getDeviceId(), item.getFuncIdentity())) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> deviceFuncMap() {
        OpenApi.DeviceFuncStateInfo stateInfo = OpenApi.DeviceFuncStateInfo.builder()
                .functions(getFunctions()).build();
        return FluentMap.builder().add(MK.DATA, stateInfo.jsonString()).map();
    }

    // 设备回调通知消息

    public boolean invalidNotifyParam() {
        return StringUtils.isEmpty(getDeviceId()) || getData() == null
                || Pattern.compile(MK.EXP_JSON_O).matcher(getData().toString()).find();
    }

    public Map<String, Object> notifyMessageMap() {

        OpenApi.DeviceNotifyMessage notifyMessage = OpenApi.DeviceNotifyMessage.builder()
                .deviceId(getDeviceId()).data(getData()).build();
        return FluentMap.builder().add(MK.DATA, notifyMessage.jsonString()).map();
    }

    private Integer page;
    private Integer limit;

    public Integer getPage() {
        return Optional.ofNullable(page).orElse(1);
    }

    public Integer getLimit() {
        return Optional.ofNullable(limit).orElse(10);
    }

    // 商户根空间信息
    public Map<String, Object> merchantRoomSpaceInfoMap() {
        return FluentMap.builder().add(MK.LIMIT, getLimit())
                .add(MK.PAGE, (getPage() - 1) * getLimit()).map();
    }

    // 商户子空间信息
    private String spaceId;

    public boolean invalidMerchantSpaceInfoParam() {
        return StringUtils.isEmpty(getSpaceId());
    }

    public Map<String, Object> merchantSpaceInfoMap() {
        return FluentMap.builder().add2L(MK.SPACE_ID, getSpaceId())
                .add(MK.LIMIT, getLimit()).add(MK.PAGE, (getPage() - 1) * getLimit()).map();
    }

    // 空间设备列表
    public boolean invalidSpaceDeviceQueryParam() {
        return StringUtils.isEmpty(getSpaceId());
    }

    public Map<String, Object> spaceDeviceQueryMap() {
        return FluentMap.builder().add2L(MK.SPACE_ID, getSpaceId())
                .add(MK.LIMIT, getLimit()).add(MK.PAGE, (getPage() - 1) * getLimit()).map();
    }

    // 空间设备信息
    private String id;

    public boolean invalidDeviceInfoQueryParam() {
        return StringUtils.isEmpty(getId());
    }

    public Map<String, Object> deviceInfoQueryMap() {
        return FluentMap.builder().add2L(MK.ID, getId()).map();
    }


    // Setting Method

    public CloudApiInfoRequest userid(String userid) {
        this.userid = userid;
        return this;
    }

    public CloudApiInfoRequest op(Integer op) {
        if (this.op == null) {
            this.op = op;
        }
        return this;
    }
}
