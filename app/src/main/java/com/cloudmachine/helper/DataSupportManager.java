package com.cloudmachine.helper;

import com.cloudmachine.chart.utils.AppLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/12/23.
 * <p>
 * litePal库bug修复
 */

public class DataSupportManager {
    public static <T> T findFirst(Class<T> modelClass) {
        T t = null;
        try {
            t = DataSupport.findFirst(modelClass);
        } catch (Exception e) {
            AppLog.print("findFirst__execption__" + e.getMessage());
        }
        return t;
    }

    public static <T> List<T> findAll(Class<T> modelClass) {
        List<T> itemList = null;
        try {
            itemList = DataSupport.findAll(modelClass);
        } catch (Exception e) {
            itemList = new ArrayList<>();
            AppLog.print("findAll__execption__" + e.getMessage());
        }
        return itemList;
    }

    public static void deleteAll(Class<?> modelClass) {
        try {
            DataSupport.deleteAll(modelClass);
         } catch (Exception e) {
            AppLog.print("delte ALL exception__");
        }
    }


}
