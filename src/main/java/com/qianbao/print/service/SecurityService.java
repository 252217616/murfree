package com.qianbao.print.service;

import com.qianbao.print.common.exception.BusinessException;
import com.qianbao.print.common.exception.ErrorCode;
import com.qianbao.print.common.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Slf4j
@Validated
@RequiredArgsConstructor
@Service
public class SecurityService {

  final SecurityConfigService securityConfigService;

  public String decrypt (@NotEmpty String organizationNo, @NotEmpty String data) {
    String aesKey = securityConfigService.getAesKey(organizationNo);
    Assert.notNull(aesKey, ErrorCode.INVALID_ENCRYPT.message());
    try {
      return AESUtil.decryptData(aesKey, new String(Base64.decode(data)));
    } catch (Exception e) {
      log.warn("decrypt request body cause an exception:{}", e.getCause());
      throw new BusinessException(ErrorCode.INVALID_ENCRYPT);
    }
  }


  public String encrypt (@NotEmpty String organizationNo, @NotEmpty String data) {
    String aesKey = securityConfigService.getAesKey(organizationNo);
    Assert.notNull(aesKey, ErrorCode.INVALID_ENCRYPT.message());
    try {
      return new String(Base64.encode(AESUtil.encryptData(aesKey,data).getBytes()));
    } catch (Exception e) {
      log.warn("encrypt request body cause an exception:{}", e.getCause());
      throw new BusinessException(ErrorCode.INVALID_ENCRYPT);
    }
  }
  
}
