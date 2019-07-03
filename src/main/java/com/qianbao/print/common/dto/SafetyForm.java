package com.qianbao.print.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SafetyForm<T> {

  @ApiModelProperty(hidden = true)
  private String organizationNo;

  @ApiModelProperty(hidden = true)
  private T data;

}
