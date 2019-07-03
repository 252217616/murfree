package com.qianbao.print.common.exception;

import com.qianbao.print.common.utils.Strs;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.NestedRuntimeException;

/**
 * @author Brook 😈
 * @since 2018/9/19
 */
@Getter
public class BusinessException extends NestedRuntimeException {

    private ErrorCode errorCode;
    /**
     * The sub biz error code.
     */
    @Setter
    private String subCode;
    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String msgTempl, Object ...args) {
        this(Strs.format(msgTempl, args));
    }
    public BusinessException(ErrorCode code, Object... args) {
        super(Strs.format(code.message(), args));
        this.errorCode = code;
    }

    public BusinessException(ErrorCode code, String msg) {
        this(msg);
        this.errorCode = code;
    }

    public BusinessException(ErrorCode code, String msgTempl, Object ... args) {
        this(msgTempl, args);
        this.errorCode = code;
    }
    public BusinessException(ErrorCode errorCode) {
        this(Strs.format(errorCode.message()));
        this.errorCode = errorCode;
    }

    public static BusinessException of(String subCode, String message){
        BusinessException re = new BusinessException(ErrorCode.REMOTE_API_ERROR,message);
        re.setSubCode(subCode);
        return re;
    }

    public static BusinessException of(ErrorCode code){
        return new BusinessException(code);
    }

    /**
     * Example:
     * <pre>
     *   of(ErrorCode.INVALID, "Invalid username.");
     *   of(ErrorCode.INVALID, "Invalid username {}.","tom");
     * </pre>
     * @param code The {@code ErrorCode} .
     * @param msgTempl message template.
     * @param args The arguments for template.
     * @return RestException
     */
    public static BusinessException of(ErrorCode code, String msgTempl, Object ...args){
        return new BusinessException(code, msgTempl, args);
    }
}
