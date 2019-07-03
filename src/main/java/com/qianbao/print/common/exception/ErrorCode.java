package com.qianbao.print.common.exception;


import lombok.RequiredArgsConstructor;

/**
 * @create 2018/1/18
 */
@RequiredArgsConstructor
public enum ErrorCode {
    OK("20000653", "成功."),
    CREATED("20100653", "已创建"),
    ACCEPTED("20200653", "已接受，等待处理"),
    NO_CONTENT("20400653", "服务器已处理，没有响应的内容"),
    RESET_CONTENT("20500653", "重置表单"),

    //400 参数错误或非法请求
    INVALID("40001653", "{}无效"),
    INVALID_PARAMETER("40002653", "参数{}无效,请检查参数是否正确"),
    MISSING_PARAMETER("40003653", "参数{}缺失"),
    NOT_EFFECTIVE("40004653", "{}未生效"),
    EXPIRED("40005653", "{}已过期"),
    USED("40006653", "{}已使用."),
    BLANCE_NOT_ENOUGH("40007653", "{}不足."),
    USEING("40009653", "{}正在处理中."),
    CHANGED("40010653", "{}已变更."),
    OFFLINE("40011653", "{}已下架."),
    REALNAME_AUTH_FAIL("40012653", "实名认证失败."),
    SMSCODE_ERROR("40013653", "短信验证码错误."),
    OCR_FAIL("40014653", "OCR识别失败.请重试"),

    INVALID_TOKEN("40100653", "token无效或已过期"),
    INVALID_ENCRYPT("40101653", "加密错误或密钥无效"),
    INVALID_GET_TOKEN("40101654", "暂时无法获取登录令牌请稍后重试"),
    FORBIDDEN("40300653", "{}受限"),
    LIMIT_UPPER("40301653", "{}已达上限"),
    LIMIT_FLOWER("40302653", "{}已达下限"),
    STATUS_ERROR("40303653", "订单状态异常"),
    NOT_FOUND("40400653", "{}不存在或已删除"),

    // 调用第三方服务用 408
    TIMEOUT("40800653", "访问超时"),

    // 接口限流用 408
    MANY_REQUEST("40801653", "请求太频繁，已经限制访问，稍后重试"),
    // 扩展业务用 ...

    LOCKED("42300653", "{}已锁定."),

    REMOTE_API_ERROR("50300653", "请求远程服务接口异常"),
    TRADE_CANCEL_ERROR("50001653", "交易未完成或交易超时"),

    //system base code from 01 to 09
    XML_PARSE_ERROR("50001653", "XML解析异常"),
    ERROR("50099653", "服务器繁忙，请稍后重试！");
    final String status;
    final String message;

    public String code() {
        return this.status;
    }

    public String message() {
        return this.message;
    }
}