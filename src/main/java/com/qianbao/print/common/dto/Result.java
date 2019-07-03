package com.qianbao.print.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qianbao.print.common.exception.ErrorCode;
import com.qianbao.print.common.exception.ErrorField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@ApiModel("Result model")
@Data
public class Result<T> {

    @ApiModelProperty(value = "Error errorCode.",example = "20000xxx")
    private String code;

    @ApiModelProperty(value = "Readable message corresponding to the return code."
            , example = "success.")
    private String message;
    private boolean succeed;

    @ApiModelProperty("Response to client data in JSON format.")
    private T result;

    private List<ErrorField> errors = new ArrayList<>();

    public boolean isSucceed() {
        return this.code != null && this.code.startsWith("20");
    }

    public static <T> Result<T> ok(T data) {
        return ok(ErrorCode.OK.message(), data);
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(String message, T data) {
        return ResultBuilder.builder()
                .errorCode(ErrorCode.OK.code())
                .message(message)
                .data(data)
                .build();
    }

    public static Result error() {
        return error(ErrorCode.ERROR);
    }


    public static Result error(ErrorCode errorCode) {
        return error(errorCode.code(), errorCode.message(), null);
    }

    public static Result error(ErrorCode errorCode, List<ErrorField> errors) {
        return error(errorCode.code(), errorCode.message(), errors);
    }

    public static Result error(String message, List<ErrorField> errors) {
        return error(ErrorCode.ERROR.message(), message, errors);
    }

    public static Result error(String errorCode) {
        return error(errorCode, ErrorCode.ERROR.message(), null);
    }

    public static Result error(String errorCode, String errorMessage) {
        return error(errorCode, errorMessage, null);
    }

    public static Result error(String errorCode, String errorMessage, List<ErrorField> errors) {
        return ResultBuilder.builder().errorCode(errorCode)
                .message(errorMessage)
                .errors(errors)
                .build();
    }


    public static final class ResultBuilder<T> {

        private String code;
        private String message;
        private T result;
        private List<ErrorField> errors;

        private ResultBuilder() {
        }

        public static <T> ResultBuilder<T> builder() {
            return new ResultBuilder<>();
        }

        public ResultBuilder errorCode(String errorCode) {
            this.code = errorCode;
            return this;
        }

        public ResultBuilder message(String errorMessage) {
            this.message = errorMessage;
            return this;
        }

        public ResultBuilder data(T data) {
            this.result = data;
            return this;
        }

        public ResultBuilder errors(List<ErrorField> errors) {
            this.errors = errors;
            return this;
        }

        public Result<T> build() {
            Result<T> result = new Result<>();
            result.result = this.result;
            result.message = this.message;
            result.code = this.code;
            result.errors = this.errors;
            return result;
        }
    }
}