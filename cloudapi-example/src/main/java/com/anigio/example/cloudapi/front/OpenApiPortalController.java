package com.anigio.example.cloudapi.front;

import com.anigio.cloudapi.openapi.utils.crypto.ApiSHA1;
import com.anigio.example.cloudapi.ecode.ECode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Eric.Shang on 2019-11-11.
 */
@RestController
@RequestMapping("/")
@Slf4j
public class OpenApiPortalController {
    private static final String NOTIFY_TOKEN = "XXX"; // 回调接口设置密钥

    /**
     * 接口回声测试，确认接口有效性
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 响应内容
     * @return String
     */
    @GetMapping(value = "callback", produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(name = "signature", required = false, defaultValue = "") String signature,
                          @RequestParam(name = "timestamp", required = false, defaultValue = "") String timestamp,
                          @RequestParam(name = "nonce", required = false, defaultValue = "") String nonce,
                          @RequestParam(name = "echostr", required = false, defaultValue = "") String echostr) {
        // 计算校验签名
        if (StringUtils.isNoneBlank(signature, timestamp, nonce, echostr)
                && signature.equals(ApiSHA1.genWithSorted(NOTIFY_TOKEN, nonce, timestamp))) {
            return echostr;
        }
        return "invalid signature";
    }

    /**
     * 空间功能状态通知消息
     * @param requestBody 数据内容
     * @param signature 签名
     * @param encType 加密类型
     * @param msgSignature 消息签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return String
     */
    @PostMapping(value = "callback", produces = "application/json; charset=UTF-8")
    public String wxMessage(@RequestBody String requestBody,
                              @RequestParam(name = "signature", defaultValue = "") String signature,
                              @RequestParam(name = "encryptType", required = false, defaultValue = "") String encType,
                              @RequestParam(name = "msgSignature", required = false, defaultValue = "") String msgSignature,
                              @RequestParam(name = "timestamp", defaultValue = "") String timestamp,
                              @RequestParam(name = "nonce", defaultValue = "") String nonce) {

        // 计算校验签名
        if (StringUtils.isNoneBlank(signature, timestamp, nonce)
                &&signature.equals(ApiSHA1.genWithSorted(NOTIFY_TOKEN, nonce, timestamp))) {
            log.info("requestBody={}", requestBody);
            return OpenApiResponse.builder().build().jsonString();
        }

        return OpenApiResponse.builder().message(ECode.E_INVALID_PARAM.message())
                .code(ECode.E_INVALID_PARAM.code()).build().jsonString();
    }
}
