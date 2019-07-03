package com.qianbao.print.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 🐝 King 👉 wangwei2@qianbao.com
 * @create 2018/9/27
 * @copyright Copyright © qianbao.com 2018. All Rights Reserved
 */
@ApiModel("报文体加密表单数据模型")
@EqualsAndHashCode(callSuper = true)
@Data
public class BodyForm<T> extends SafetyForm<T> {

  @NotEmpty(message = "报文不允许为空")
  @ApiModelProperty("加密后的报文体")
  private String reqData;

}
