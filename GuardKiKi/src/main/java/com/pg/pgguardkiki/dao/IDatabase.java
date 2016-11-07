package com.pg.pgguardkiki.dao;

import java.util.List;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IDatabase {
    /**
     * 查询记录
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param clazz         数据类型
     * @return 对象列表
     */
    List query(String table, String[] columns, String selection,
               String[] selectionArgs, Class clazz);

    /**
     * 查询记录
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param orderBy       排序规则
     * @param clazz         数据类型
     * @return 对象列表
     */
    List query(String table, String[] columns, String selection,
               String[] selectionArgs, String orderBy, Class clazz);

    /**
     * 查询记录
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param groupBy       分组规则
     * @param orderBy       排序规则
     * @param clazz         数据类型
     * @return 对象列表
     */
    List query(String table, String[] columns, String selection,
               String[] selectionArgs, String groupBy, String orderBy, Class clazz);

    /**
     * 查询记录
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param groupBy       分组规则
     * @param having        过滤规则
     * @param orderBy       排序规则
     * @param clazz         数据类型
     * @return 对象列表
     */
    List query(String table, String[] columns, String selection,
               String[] selectionArgs, String groupBy, String having,
               String orderBy, Class clazz);

    /**
     * 查询记录
     *
     * @param table         表名
     * @param columns       sql语句
     * @param selection     　条件语句
     * @param selectionArgs 　条件参数
     * @return HashMap列表
     */
    ArrayList<HashMap<String, String>> query(String table, String[] columns, String selection, String[] selectionArgs);

    /**
     * 查询记录
     *
     * @param sql          sql语句
     * @param selectionArg 　条件参数
     * @param clazz        　数据类型
     * @return 对象列表
     */
    List rawQuery(String sql, String[] selectionArg, Class clazz);


    /**
     * 查询记录
     *
     * @param sql          sql语句
     * @param selectionArg 　条件参数
     * @return HashMap列表
     */
    List<HashMap<String, String>> rawQuery(String sql, String[] selectionArg);

    /**
     * 查询记录
     *
     * @param sql          sql语句
     * @param selectionArg 　条件参数
     * @param key          　数据类型
     * @return 对象列表
     */
    ArrayList<String> rawQuery(String sql, String[] selectionArg, String key);

    /**
     * 更新记录
     *
     * @param table       表名
     * @param values      插入列值
     * @param whereClause 条件语句
     * @param whereArgs   条件参数
     * @return 返回值　true:成功, false:失败
     */
    boolean update(String table, ContentValues values, String whereClause, String[] whereArgs);

    /**
     * 插入记录
     *
     * @param table  表名
     * @param values 插入列值
     * @return 返回值　true:成功, false:失败
     */
    boolean insert(String table, ContentValues values);

    boolean insert(String table, ContentValues values, String nullColumnHack);

    /**
     * 删除记录
     *
     * @param table       表名
     * @param whereClause 条件语句
     * @param whereArgs   条件参数
     * @return 返回值　true:成功, false:失败
     */
    boolean delete(String table, String whereClause, String[] whereArgs);

    /**
     * 删除所有记录记录
     *
     * @param table 　表名
     */
    void deleteAll(String table);

    /**
     * 删除所有表
     */
    void deleteAllTables();

    /**
     * 执行sql语句
     *
     * @param sql SQL语句
     */
    void execSQL(String sql);


    /**
     * 获取数据文件路径
     */
    String getFullPath();


    /**
     * 开始事务
     */
    void beginTransaction();


    /**
     * 提交事务
     */
    void setTransactionSuccessful();

    /**
     * 结束事务
     */
    void endTransaction();


    /**
     * 打开数据库
     */
    void open();

    /**
     * 关闭数据库
     */
    void close();
}
