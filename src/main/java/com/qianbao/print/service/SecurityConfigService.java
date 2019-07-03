package com.qianbao.print.service;


import javax.validation.constraints.NotEmpty;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@RequiredArgsConstructor
@Service
  public class SecurityConfigService {
    //TODO 查库找秘钥
//  final OrganizationBaseInfoMapper organizationBaseInfoMapper;

  public String getAesKey (@NotEmpty(message = "合作机构不能为空") String organizationNo) {
//    return Optional.ofNullable(organizationBaseInfoMapper.selectByPrimaryKey(organizationNo))
//        .orElse(new OrganizationBaseInfo()).getAesKey();
      return "";
  }


}
