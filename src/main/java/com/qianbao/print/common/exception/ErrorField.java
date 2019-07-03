package com.qianbao.print.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用错误码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorField {
    private String field;
    private String message;


    @Override
    public String toString() {
        return "ErrorField{" +
                "field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
