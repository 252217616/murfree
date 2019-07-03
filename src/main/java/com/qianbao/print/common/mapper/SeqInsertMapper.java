package com.qianbao.print.common.mapper;

import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;

public interface SeqInsertMapper<T> {

    @InsertProvider(
            type = SeqSpecialProvider.class,
            method = "dynamicSQL"
    )
    int insertList(List<T> recordList);
}
