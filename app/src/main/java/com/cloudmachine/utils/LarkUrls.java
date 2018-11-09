package com.cloudmachine.utils;

import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.contract.AuthPersonalInfoContract;

/**
 * Created by xiaojw on 2018/9/12.
 * Cloudm3分拆Lark
 */

public interface LarkUrls {
    String REGISTER_URL=ApiConstants.LARK_HOST+"registerNew";
    String LOGIN_URL = ApiConstants.LARK_HOST + "login";//登录
    String PSW_MODIFY_URL=ApiConstants.LARK_HOST+"member/modifyPwd";//修改密码
    String INFO_MODIFY_URL=ApiConstants.LARK_HOST+"member/updateMemberInfo";
    String LOCUS=ApiConstants.LARK_HOST+"device/locus";
    String MEMBER_SEARCH_URL=ApiConstants.LARK_HOST+"member/detail";//搜索成员
    String MEMBER_PERMISSION_URL=ApiConstants.LARK_HOST+"device/givePermissionNew";
    String UPDATE_DEVICEINFO_URL=ApiConstants.LARK_HOST+"device/updateDeviceInfo";
    String CONFIRM_PAY_URL=ApiConstants.LARK_HOST+"salary/confirmPay";
    String GET_CODE_URL=ApiConstants.LARK_HOST+"member/getCode";
    //获取工单支付sign
    String GET_PAY_SIGN=ApiConstants.LARK_HOST+"pay/getPaySign";
    //提交评价
    String SAVE_EVALUATE_URL=ApiConstants.LARK_HOST+"evaluate/saveEvaluate";
    //获取评价标签
    String GET_EVALUATE_URL=ApiConstants.LARK_HOST+"evaluate/getEvaluateTag";
    //获取评价详情
    String GET_EVALUATE_INFO=ApiConstants.LARK_HOST+"evaluate/getEvaluateInfo";

    //工作时长统计
    String GET_MOTH_DATA=ApiConstants.LARK_HOST+"device/workTime/getDataStatisticMonth";
    //工时统计（近28天）
    String GET_DAY_DATA=ApiConstants.LARK_HOST+"device/workTime/getDataStatisticDay";
    //提交报修工单
    String SAVE_REPAIR_ORDER=ApiConstants.LARK_HOST+"device/repair/saveRepairOrder";
    //工时明细
    String DAILY_WORK_DETAIL=ApiConstants.LARK_HOST+"device/workTime/dailyWorkDetails";

    String OPERATOR_CODE_VALID=ApiConstants.LARK_HOST+"loan/verification/operatorCodeValid";
    String CONTRAST_FACE=ApiConstants.LARK_HOST+"loan/verification/contrastFace";
    String FORGET_PWD=ApiConstants.LARK_HOST+"member/forgetPwd";




}
