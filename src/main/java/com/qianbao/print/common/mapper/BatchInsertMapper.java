package com.qianbao.print.common.mapper;

import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;

public interface BatchInsertMapper<T> {

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等
     * <p>
     * 不支持主键策略，插入前需要设置好主键的值
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = BatchInsertProvider.class, method = "dynamicSQL")
    int batchInsert(List<T> recordList);

}