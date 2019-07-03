package com.qianbao.print.common.mapper;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

/**
 * 通用 Mapper 接口
 *
 * @author Jun
 * @create 2018/4/4
 */

/**
 * 通用 Mapper 接口
 *
 * @author Shaojun Liu <liushaojun@qianbao.com>
 * @create 2018/4/4
 */
public interface BaseMapper<T> extends Mapper<T>
        , SeqInsertMapper<T>
        , BatchInsertMapper<T>
        , InsertUseGeneratedKeysMapper<T>
        , IdsMapper<T> {

    /**
     * Deletion with version number.
     * @param entity The deleted entity.
     * @return The number of influence.
     */
    default int deleteWithVersion(T entity) {
        return delete(entity);
    }

    /**
     * Update by primary key with version number.
     *
     * @param entity The updated entity.
     * @return Returns to True successfully or fail.
     */
    default boolean updateByPrimaryKeyWithVersion(T entity) {
        return updateByPrimaryKey(entity) >0;
    }

}
