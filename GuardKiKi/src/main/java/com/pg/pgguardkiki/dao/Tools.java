package com.pg.pgguardkiki.dao;

import android.content.ContentValues;
import android.os.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Tools {

    /**
     * 获取数据库路径
     *
     * @return String
     */
    public static String getDBPath() {
        String dbPath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dbPath = Environment.getExternalStorageDirectory().toString();

        }
        return dbPath;
    }

    public static HashMap<String, String> convertObj2Map(Object obj, Class<?> clazz) {
        try {
            Method methodSend;
            String strFiledName;
            String strValue;
            Field[] fields = clazz.getDeclaredFields();
            HashMap<String, String> value = new HashMap<>();
            for (Field field : fields) {
                if (field.isSynthetic()) {
                    continue;
                }
                methodSend = null;
                strFiledName = field.getName();
                /**
                 * 首字母更改为大写
                 */
                strFiledName = strFiledName.substring(0, 1).toUpperCase() + strFiledName.substring(1);
                try {
                    if (!strFiledName.contains("SerialVersionUID"))
                        methodSend = clazz.getMethod("get" + strFiledName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (methodSend != null) {
                    if (!(methodSend.invoke(obj) instanceof String))
                        continue;
                    strValue = (String) methodSend.invoke(obj);
                    value.put(strFiledName, strValue);
                }
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将字符串列表转换成字符串
     *
     * @param list List<String>
     * @return String
     */
    public static String convertToString(List list) {
        String ids = "";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String obj = (String) list.get(i);
                ids += obj;
                if (i != list.size() - 1) {
                    ids += ",";
                }
            }
        }
        return ids;
    }

    /**
     * 将字符串列表转换成用于sql语句的字符串
     *
     * @param list List<String>
     * @return String
     */
    public static String convertToStringForSql(List list) {
        String ids = "";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String obj = (String) list.get(i);
                ids += "'" + obj + "'";
                if (i != list.size() - 1) {
                    ids += ",";
                }
            }
        }
        return ids;
    }

    /**
     * 转换 HashMap 到 ContentValues
     *
     * @param value HashMap
     * @return ContentValues
     */
    public static ContentValues convert2Values(HashMap<String, String> value) {
        ContentValues contentValues = new ContentValues();
        Iterator<Map.Entry<String, String>> it = value.entrySet().iterator();
        if (it.hasNext()) {
            do {
                Map.Entry<String, String> entry = it.next();
                if(entry.getKey().equals("OrderCode")){
                    contentValues.put(entry.getKey(),entry.getValue().toUpperCase());
                }else {
                    contentValues.put(entry.getKey(), entry.getValue());
                }
            } while (it.hasNext());
        }
        return contentValues;
    }

    public static ContentValues convert2Values(HashMap<String, String> value, TableConfig tableConfig) {
        ContentValues contentValues = new ContentValues();
        int iFiledCount = tableConfig.getFieldList().size();
        String strFiledName;
        String strFiledValue;
        for (int i = 0; i < iFiledCount; i++) {
            strFiledName = tableConfig.getFieldList().get(i).getName();
            strFiledValue = value.get(strFiledName);
            if (strFiledValue != null) {
                contentValues.put(strFiledName, strFiledValue);
            }
        }
        return contentValues;
    }
}
