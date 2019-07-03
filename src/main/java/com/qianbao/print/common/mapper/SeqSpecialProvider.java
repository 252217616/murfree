package com.qianbao.print.common.mapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Iterator;
import java.util.Set;

/**
 * 数据Sequence 批量mapper
 *
 * @author Shaojun Liu <liushaojun@qianbao.com>
 * @create 2018/4/13
 */
public class SeqSpecialProvider extends MapperTemplate {

    public SeqSpecialProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertList(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator var5 = columnList.iterator();

        while (var5.hasNext()) {
            EntityColumn column = (EntityColumn) var5.next();
            if(column.isId()) {
                if (StringUtils.isNotBlank(column.getGenerator()) && !column.isUuid()) {
                    String seq = column.getGenerator().replaceAll("SELECT|select", "");
                    sql.append(seq).append(",");
                } else {
                    sql.append(column.getColumnHolder("record")).append(",");
                }
            } else if (column.isInsertable()) {
                sql.append(column.getColumnHolder("record")).append(",");
            }
        }

        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

}
