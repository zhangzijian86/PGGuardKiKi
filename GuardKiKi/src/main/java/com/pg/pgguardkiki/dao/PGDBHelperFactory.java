package com.pg.pgguardkiki.dao;


public class PGDBHelperFactory {
    /**
     * 单例对象
     */
    private static PGDBHelper INSTANCE = null;

    /**
     * 数据库文件名
     */
    public static final String DB_NAME = "PGGuardKiKi.db";

    /**
     * 获取PGDBHelper对象
     *
     * @return PGDBHelper
     */
    public static PGDBHelper getDBHelper() {
        INSTANCE = new PGDBHelper (DB_NAME);
        return INSTANCE;
    }
}
