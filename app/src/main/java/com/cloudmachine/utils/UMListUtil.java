package com.cloudmachine.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2016/11/28 下午8:21
 * 修改人：shixionglu
 * 修改时间：2016/11/28 下午8:21
 * 修改备注：
 */

public class UMListUtil {

    private static UMListUtil instance;
    private  Set<Map.Entry<String, String>> sStru1Entry;
    private  Set<Map.Entry<String, String>> sStru2Entry;

    public UMListUtil() {
        initCustomList();
        initStru1Map();
        initStru2Map();
    }

    public static UMListUtil getUMListUtil() {
        if (null == instance) {
            instance = new UMListUtil();
        }

        return instance;
    }

    public  HashMap<String, String> customMap = new HashMap<>();
    public  HashMap<String, String> stru1Map = new HashMap<>();
    public  HashMap<String, String> stru2Map = new HashMap<>();
    public  List<String> stru1 = new ArrayList<String>();
    public  List<String> stru2 = new ArrayList<>();

    private  void initCustomList() {
        customMap.put("MainMapActivity", "time_machine_map");
        customMap.put("DeviceMcActivity", "time_machine_detection");
        customMap.put("FaultDitailsActivity", "time_machine_report");
        customMap.put("FaultDitailsListActivity", "time_machine_report_warning");
        customMap.put("AddDeviceActivity", "time_machine_info");
        customMap.put("DeviceMcMemberActivity", "time_machine_member");
        customMap.put("OilAmountActivity", "time_machine_waterlevel");
        customMap.put("WorkTimeActivity", "time_machine_worktime");
        customMap.put("MapViewActivity", "time_machine_location");
        customMap.put("MapOneActivity", "time_machine_fence");
        customMap.put("HistoricalTrackActivity", "time_machine_historylocus");
        customMap.put("SearchActivity", "time_search");
        customMap.put("BeginnerGuideActivity", "time_machine_experience");
        customMap.put("RepairActivity", "time_repair_history");
        customMap.put("NewRepairActivity", "time_repair_create");
        customMap.put("EditLayoutActivity", "time_repair_create_attribute");
        customMap.put("CheckMachineActivity", "time_repair_create_device");
        customMap.put("CityActivity", "time_repair_create_location_city");
        customMap.put("RepairBasicInfomationActivity", "time_repair_detail");
        customMap.put("EvaluationActivity", "time_repair_comment");
        customMap.put("MessageActivity", "time_message_list");
        customMap.put("MessageContentActivity", "time_message_detail");
        customMap.put("LoginActivity", "time_login");
        customMap.put("UpdatePwdActivity", "time_changepassword");
        customMap.put("UpdateInfoActivity", "time_profile");
        customMap.put("EditPersonalActivity", "time_profile_edit");
        customMap.put("SuggestBackActivity", "time_setting_feedback");
        customMap.put("QrCodeActivity", "time_setting_rqcode");
        customMap.put("MapOneActivity", "电子围栏");
        customMap.put("WorkTimeActivity", "工作时长");
        customMap.put("AboutCloudActivity", "time_about_and_help");
        customMap.put("UseHelpActivity", "time_use_and_help");
    }

    private void initStru1Map() {
        stru1Map.put("Main1FM", "stru_001");
        stru1Map.put("BeginnerGuideActivity", "新手引导");
        stru1Map.put("DeviceMcActivity", "机器内页");
        stru1Map.put("OilAmountActivity", "油位");
        stru1Map.put("DeviceMcMemberActivity", "成员组成");
        stru1Map.put("SearchActivity", "搜索");
        stru1Map.put("MainMapActivity", "所有机器地图页面");
        stru1Map.put("AddDeviceActivity", "机器基本信息");
        stru1Map.put("FaultDitailsActivity", "机器检测报告");
        stru1Map.put("MapViewActivity", "当前位置");
        stru1Map.put("HistoricalTrackActivity", "历史轨迹");
        stru1Map.put("MapOneActivity", "电子围栏");
        stru1Map.put("WorkTimeActivity", "工作时长");
        stru1Map.put("stru_002", "报修历史");
        // stru1Map.put("stru_002","报修历史");
        stru1Map.put("AddMemberSuccess", "添加成功");
        stru1Map.put("WorkTimeFragment", "工作时长展示");
        stru1Map.put("StatisticsFragment", "工作时长统计");

    }

    private void initStru2Map() {
        stru2Map.put("RepairActivity", "stru_002");
        stru2Map.put("NewRepairActivity", "新增报修");
        stru2Map.put("time_repair_create_location", "选择报修位置");
        stru2Map.put("EditLayoutActivity", "新增报修属性选择");
    }


    public void sendStruEvent(String key,Context context) {
        sStru1Entry = stru1Map.entrySet();
        sStru2Entry = stru2Map.entrySet();
        for (Map.Entry<String, String> stru1entry : sStru1Entry) {
            if (key.equals(stru1entry.getKey())) {
                stru1.add(stru1entry.getValue());
                MobclickAgent.onEvent(context,stru1,1,"");
            }
        }

        for (Map.Entry<String, String> stru2entry : sStru2Entry
                ) {
            if (key.equals(stru2entry.getKey())) {
                stru2.add(stru2entry.getValue());
                MobclickAgent.onEvent(context,stru2,1,"");
            }
        }

    }


    public void startCustomEvent(Activity activity) {

        Set<Map.Entry<String, String>> entrySet = customMap.entrySet();
        for (Map.Entry<String,String> entry: entrySet
                ) {
            if (entry.getKey().equals(activity.getClass().getSimpleName())) {
                MobclickAgent.onPageStart(entry.getValue());
            }
        }
    }

    public void startCustomEvent(Fragment fragment) {
        Set<Map.Entry<String, String>> entrySet = customMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals(fragment.getClass().getSimpleName())) {
                MobclickAgent.onPageStart(entry.getValue());
            }
        }
    }

    public void endCustomEvent(Fragment fragment) {
        Set<Map.Entry<String, String>> entrySet = customMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals(fragment.getClass().getSimpleName())) {
                MobclickAgent.onPageEnd(entry.getValue());
            }
        }
    }

    public void endCustomEvent(Activity activity) {
        Set<Map.Entry<String, String>> entrySet = customMap.entrySet();
        for (Map.Entry<String,String> entry: entrySet
                ) {
            if (entry.getKey().equals(activity.getClass().getSimpleName())) {
                MobclickAgent.onPageEnd(entry.getValue());
            }
        }
    }

    public void removeList(String key) {
        for (Map.Entry<String, String> stru1entry : sStru1Entry) {
            if (key.equals(stru1entry.getKey())) {
                stru1.remove(stru1entry.getValue());
            }
        }

        for (Map.Entry<String, String> stru2entry : sStru2Entry
                ) {
            if (key.equals(stru2entry.getKey())) {
                stru2.remove(stru2entry.getValue());
            }
        }
    }
}
