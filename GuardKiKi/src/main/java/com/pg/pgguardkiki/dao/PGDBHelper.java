package com.pg.pgguardkiki.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.pg.pgguardkiki.dao.IDatabase;
import com.pg.pgguardkiki.tools.StringIdEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


public class PGDBHelper implements IDatabase {

    private static final String ClassName = "PGDBHelper";

    /**
     * 数据库的路径
     */
    String dbFullPath;
    /**
     * SQLiteDatabase对象
     */
    SQLiteDatabase database;

    /**
     * 创建表SQL语句
     */
    private static final String CREATE_TABLE_PG_Roster = "CREATE TABLE PG_Roster(" +
           // "Roster_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + //ID 自增
            "Roster_Username Varchar(50) ," +                         //用户名也就是手机号码
            "Roster_Jid Varchar(50)," +                               //好友
            "Roster_Nick Varchar(50)" +                               //好友分组
            ")";

    private static final String CREATE_TABLE_PG_User = "CREATE TABLE PG_User(" +
            // "Roster_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + //ID 自增
            "PG_User_Username Varchar(50) ," +                         //用户名
            "PG_User_Logintime Varchar(50)," +                         //登陆日期
            "PRIMARY KEY (PG_User_Username) " +
            ")";

    /**
     * 构造方法
     *
     * @param name    数据库名字
     */
    public PGDBHelper( String name) {
        String dbPath = Tools.getDBPath();
        try {
            dbFullPath = dbPath + "/" + name;
            Log.d(ClassName,"===PGDBHelper====="+dbFullPath);
            File file = new File(dbFullPath);
            boolean flag = file.exists();
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
            if(!flag){
                database.execSQL(CREATE_TABLE_PG_Roster);
                database.execSQL(CREATE_TABLE_PG_User);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String queryUser(String table, String[] columns, String selection, String[] selectionArgs, Class clazz) {
        try {
            Cursor cursor = database.query(table, columns, selection, selectionArgs, null, null, "PG_User_Logintime desc" );
            String uername = "";
            String logintime = "";

            while (cursor.moveToNext()) {
                uername=cursor.getString(0);
                logintime =cursor.getString(1);
                Log.d(ClassName,"uername:"+uername+",logintime:"+logintime);
                break;
            }
            if (cursor != null)
                cursor.close();
            return uername;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 查询方法
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param clazz         数据类型
     * @return 结果集，传入的clazz构成的list结果
     */
    @Override
    public List query(String table, String[] columns, String selection, String[] selectionArgs, Class clazz) {
        try {
            Cursor cursor = database.query(table, columns, selection, selectionArgs, null, null, null);
//            while (cursor.moveToNext()) {
//                String Roster_Nick=cursor.getString(0);
//                Log.d(ClassName,"=======Roster_Nick===111="+Roster_Nick);
//                Log.d(ClassName,"=======Roster_Nick===222="+cursor.getCount());
//            }
            List list = cursor2VOList(cursor, clazz);
            if (cursor != null)
                cursor.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查询方法
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param orderBy       排序规则
     * @param clazz         数据类型
     * @return 结果集，传入的clazz构成的list结果
     */
    @Override
    public List query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy, Class clazz) {
        try {
            Cursor cursor = database.query(table, columns, selection, selectionArgs, null, null, orderBy);
            List list = cursor2VOList(cursor, clazz);
            if (cursor != null)
                cursor.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询方法
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param groupBy       分组规则
     * @param orderBy       排序规则
     * @param clazz         数据类型
     * @return 结果集，传入的clazz构成的list结果
     */
    @Override
    public List query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String orderBy, Class clazz) {
        try {
            Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, null, orderBy);
            List list = cursor2VOList(cursor, clazz);
            if (cursor != null)
                cursor.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询方法
     *
     * @param table         表名
     * @param columns       返回那些列
     * @param selection     条件语句
     * @param selectionArgs 条件参数
     * @param groupBy       分组规则
     * @param having        过滤规则
     * @param orderBy       排序规则
     * @param clazz         数据类型
     * @return 结果集，传入的clazz构成的list结果
     */
    @Override
    public List query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, Class clazz) {
        try {
            Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            List list = cursor2VOList(cursor, clazz);
            if (cursor != null)
                cursor.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询方法
     *
     * @param table         表名
     * @param columns       sql语句
     * @param selection     　条件语句
     * @param selectionArgs 　条件参数
     * @return 结果集，ArrayList<HashMap<String, String>> arrayList里面的每一个元素都是一个map集合，map中对应整个columns每个字段
     */
    @Override
    public ArrayList<HashMap<String, String>> query(String table, String[] columns, String selection, String[] selectionArgs) {
        try {
            Cursor cursor = database.query(table, columns, selection, selectionArgs, null, null, null);
            ArrayList<HashMap<String, String>> list = cursor2MapList(cursor);
            if (cursor != null)
                cursor.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询方法
     *
     * @param sql          sql语句
     * @param selectionArg 　条件参数
     * @return 结果集，ArrayList<HashMap<String, String>> arrayList里面的每一个元素都是一个map集合，map中对应整个columns每个字段
     */
    @Override
    public ArrayList<HashMap<String, String>> rawQuery(String sql, String[] selectionArg) {
        try {
            Cursor cursor = database.rawQuery(sql, selectionArg);
            ArrayList<HashMap<String, String>> list = cursor2MapList(cursor);
            if (cursor != null)
                cursor.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查询方法
     *
     * @param sql          sql语句
     * @param selectionArg 　条件参数
     * @param clazz        　数据类型
     * @return 结果集，传入的clazz构成的list结果
     */
    @Override
    public List rawQuery(String sql, String[] selectionArg, Class clazz) {
        try {
            Cursor cursor = database.rawQuery(sql, selectionArg);
            List list = cursor2VOList(cursor, clazz);
            if (cursor != null)
                cursor.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询方法
     *
     * @param sql          sql语句
     * @param selectionArg 　条件参数
     * @param key          　数据类型
     * @return 结果集，ArrayList<HashMap<String, String>> arrayList里面的每一个元素都是一个map集合，map中对应整个columns每个字段
     */
    @Override
    public ArrayList<String> rawQuery(String sql, String[] selectionArg, String key) {
        try {
            Cursor cursor = database.rawQuery(sql, selectionArg);
            ArrayList<String> list = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(key));
                    list.add(name);
                }
                cursor.close();
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 更新方法，根据传入的contentValues来更新满足whereClause条件的数据
     *
     * @param table       表名
     * @param values      插入列值
     * @param whereClause 条件语句
     * @param whereArgs   条件参数
     * @return 更新操作是否顺利完成 true|false
     */
    @Override
    public boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        try {
            return database.update(table, values, whereClause, whereArgs) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新方法。
     *
     * @param table        表名
     * @param businessList 等待写入的数据集合，集合中每一个元素都是一个HashMap<String, String>
     * @param tableConfig  TableConfig 用以获取table中所有的字段名
     * @param whereClause  条件语句
     * @param whereArgs    补充条件语句中的占位符集合
     * @return 更新操作是否顺利完成 true|false
     */
    public boolean update(String table, List<HashMap<String, String>> businessList, TableConfig tableConfig, String whereClause, String[] whereArgs) {

        try {
            database.beginTransaction();
            int iFiledCount = tableConfig.getFieldList().size();
            String strFiledName;
            String strFiledValue;
            for (HashMap<String, String> map : businessList) {
                ContentValues values = new ContentValues();
                for (int i = 0; i < iFiledCount; i++) {
                    strFiledName = tableConfig.getFieldList().get(i).getName();
                    strFiledValue = map.get(strFiledName);
                    if (strFiledValue != null) {
                        values.put(strFiledName, strFiledValue);
                    }
                }
                database.update(table, values, whereClause, whereArgs);
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 插入方法
     *
     * @param table  表名
     * @param values 插入列值
     * @return 插入操作是否成功。true|false
     */
    @Override
    public boolean insert(String table, ContentValues values) {
        long result;
        try {
            result = database.insert(table, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return result == -1;
    }

    public boolean replace(String table, ContentValues values) {
        long result;
        try {
            result = database.replace(table, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return result == -1;
    }

    /**
     * 插入方法
     *
     * @param table          表名
     * @param values         插入列值
     * @param nullColumnHack optional; may be null. SQL doesn't allow inserting a completely empty row without naming
     *                       at least one column name. If your provided initialValues is empty, no column names are
     *                       known and an empty row can't be inserted. If not set to null, the nullColumnHack parameter
     *                       provides the name of nullable column name to explicitly insert a NULL into in the case
     *                       where your initialValues is empty.
     * @return 插入操作是否成功
     */
    @Override
    public boolean insert(String table, ContentValues values, String nullColumnHack) {
        try {
            return database.insertWithOnConflict(table, nullColumnHack, values, SQLiteDatabase.CONFLICT_REPLACE) != -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除方法
     *
     * @param table       表名
     * @param whereClause 条件语句
     * @param whereArgs   条件参数
     * @return 删除方法是否成功
     */
    @Override
    public boolean delete(String table, String whereClause, String[] whereArgs) {
        try {
            return database.delete(table, whereClause, whereArgs) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除所有。
     * 使用删除table的方法来实现删除所有数据。执行此方法后，传入的table在数据库里被删除
     *
     * @param table 　表名
     */
    @Override
    public void deleteAll(String table) {
        try {
            String sql = "DELETE FROM " + table;
            database.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据库所有的表
     */
    /**
     * 删除数据库中所有的表，包括数据全部清空
     */
    @Override
    public void deleteAllTables() {
        String[] selectionArgs = new String[]{"table"};
        Cursor cursor = database.rawQuery("select name from sqlite_master where type=? ", selectionArgs);
        List<String> tables = new ArrayList<>();
        while (cursor.moveToNext()) {
            tables.add(cursor.getString(0));
        }
        cursor.close();
        for (String table : tables) {
            deleteAll(table);
        }
    }

    /**
     * 执行传入的sql语句
     *
     * @param sql SQL语句
     */
    @Override
    public void execSQL(String sql) {
        try {
            database.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
    public void insertAll(List<HashMap<String, String>> businessList, TableConfig tableConfig) {
        database.beginTransaction();
        int iFiledCount = tableConfig.getFieldList().size();
        String strFiledName;
        String strFiledValue;
        for (HashMap<String, String> map : businessList) {
            ContentValues values = new ContentValues();
            for (int i = 0; i < iFiledCount; i++) {
                strFiledName = tableConfig.getFieldList().get(i).getName();
                strFiledValue = map.get(strFiledName);
                if (strFiledValue != null) {
                    values.put(strFiledName, strFiledValue);
                }
            }
            database.insert(tableConfig.getName(), null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();

    }

    public void insertAllNoSqlStr(List<HashMap<String, String>> businessList, TableConfig tableConfig) {
//        insert into EMI_InventoryTask(ID,EnableImage,PostCode,DeviceCount,LocalStatus,SyncStatus)values(?,?,?,?,?,?,?,?,?,?,?,?)
        StringBuilder sql = new StringBuilder();
        int iFiledCount = tableConfig.getFieldList().size();
        String[] args = null;
        sql.append("insert into " + tableConfig.getName());
        sql.append("(");
        for (TableField tf : tableConfig.getFieldList()) {
            sql.append(tf.getName() + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        sql.append("values(");
        for (int i = 0; i < iFiledCount; i++) {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        SQLiteStatement stat = database.compileStatement(sql.toString());
        database.beginTransaction();
        for (HashMap<String, String> map : businessList) {
            args = new String[iFiledCount];
            for (int i = 0; i < iFiledCount; i++) {
                String str = map.get(tableConfig.getFieldList().get(i).getName());
                if (str == null)
                    str = "";
                args[i] = str;
            }
            stat.bindAllArgsAsStrings(args);
            stat.executeInsert();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
//        for (HashMap map : businessList) {
//            for (Object o : map.keySet()) {
//                cn.sdisframework.utils.LogUtil.i("insert " + tableConfig.getName() + ":" + map.get(o) + "");
//            }
//        }
    }
*/


    /**
     * 插入方法。插入businessList中的所有数据，
     *
     * @param sql          插入方法的sql语句
     * @param businessList 待插入的数据集合，集合元素是HashMap<String, String>
     * @param tableConfig  TableConfig 获取table字段名
     */
    public void insertAll(String sql, List<HashMap<String, String>> businessList, TableConfig tableConfig) {
        SQLiteStatement stat = database.compileStatement(sql);
        database.beginTransaction();
        try {
            int iFiledCount = tableConfig.getFieldList().size();
            String strFiledName;
            String strFiledValue;
            int count;
            for (HashMap<String, String> map : businessList) {
                count = 0;
                for (int i = 0; i < iFiledCount; i++) {
                    strFiledName = tableConfig.getFieldList().get(i).getName();
                    strFiledValue = map.get(strFiledName);
                    if (strFiledValue != null) {
                        stat.bindString(count + 1, map.get(strFiledName));
                        count++;
                    }
                }
                stat.executeInsert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public String getFullPath() {
        return dbFullPath;
    }

    @Override
    public void beginTransaction() {
        database.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        database.endTransaction();
    }

    /**
     * open 重写
     * 打开数据库
     */
    @Override
    public void open() {
        Log.d(ClassName,"------PGDBHelper-------open---------------isOpen: " + database.isOpen());
        if (!database.isOpen()) {
            File file = new File(dbFullPath);
            database = SQLiteDatabase.openOrCreateDatabase(file, null);
        }
    }

    /**
     * close 重写
     * 关闭数据库
     */
    @Override
    public void close() {
        database.close();
    }


    /**
     * 通过Cursor转换成对应的HashMap。
     * 注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param cursor cursor
     * @return List
     */
    private static ArrayList<HashMap<String, String>> cursor2MapList(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                String[] columnNames = cursor.getColumnNames();// 字段数组
                HashMap<String, String> value = new HashMap<>();
                for (String columnName : columnNames) {
                    String str = cursor.getString(cursor.getColumnIndex(columnName));
                    //if value is null,make it to ""
                    if (str == null) {
                        str = "";
                    }
                    value.put(columnName, str);
                }
                list.add(value);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过Cursor转换成对应的VO集合。注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param cursor cursor
     * @param clazz  class
     * @return List
     */
    private static List cursor2VOList(Cursor cursor, Class clazz) {
        if (cursor == null) {
            return null;
        }
        List list = new ArrayList();
        Object obj;
        Class clazzTemp = clazz;
        if (clazz == String.class) {
            clazzTemp = StringIdEntity.class;
        }
        try {
            while (cursor.moveToNext()) {
                obj = setValues2Fields(cursor, clazzTemp);
                if (clazz == String.class) {
                    list.add(((StringIdEntity) obj).getId());
                } else {
                    list.add(obj);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把值设置进类属性里
     *
     * @param cursor cursor
     * @param clazz  class
     * @throws Exception
     */
    private static Object setValues2Fields(Cursor cursor, Class clazz) throws Exception {

        String[] columnNames = cursor.getColumnNames();// 字段数组
        //init a instance from the VO`s class
        Object obj = clazz.newInstance();
        //return a field array from obj`s ALL(include private exclude inherite(from father)) field
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class<?> typeClass = field.getType();// 属性类型
            if (field.isSynthetic() || typeClass == java.util.List.class || typeClass == java.util.ArrayList.class) {
                continue;
            }
            //field`s type
            for (String columnName : columnNames) {
                typeClass = getBasicClass(typeClass);
                //if typeClass is basic class , package.if not,no change
                if (isBasicType(typeClass)) {
                    if (clazz == StringIdEntity.class) {
                        field.setAccessible(true);
                        try {
                            field.set(obj, cursor.getString(0));
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                    } else if (columnName.equalsIgnoreCase(field.getName())) {// 是基本类型
                        String str = cursor.getString(cursor.getColumnIndex(columnName));
                        if (str == null) {
                            break;
                        }
                        //if value is null,make it to ""
                        //use the constructor to init a attribute instance by the value
                        Constructor<?> cons = typeClass.getConstructor(String.class);
                        Object attribute = cons.newInstance(str);
                        field.setAccessible(true);
                        //give the obj the attr
                        field.set(obj, attribute);
                        break;
                    }
                } /*else {
                    Object obj2 = setValues2Fields(cursor, typeClass);// 递归
                    field.set(obj, obj2);
                    break;
                } */
            }
        }
        return obj;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass typeClass
     * @return boolean
     */
    private static boolean isBasicType(Class typeClass) {
        return typeClass.equals(Integer.class)
                || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class);
    }

    /**
     * 获得包装类
     *
     * @param typeClass 类类型
     * @return Class
     */
    public static Class<?> getBasicClass(Class typeClass) {
        Class clazz = basicMap.get(typeClass);
        if (clazz == null)
            clazz = typeClass;
        return clazz;
    }

    /**
     * 基本类型数据表
     */
    private static Map<Class, Class> basicMap = new HashMap<>();

    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }
}
