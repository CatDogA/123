package com.shanghaigm.dms.model;

/**
 * Created by Tom on 2017/5/24.
 */

public class Constant {
    //GET
    public static final String URL_GET_JOB_LIST = Config.URL_BASE + "/appLogin/getJobList";
    public static final String URL_GET_HOME_DATA = Config.URL_BASE + "/appDKXS/getDetailTaskItems";//包含4项功能
    public static final String URL_QUERY_ORDER_SUB_INFO = Config.URL_BASE + "/appDKXS/getOrderInfoPageData";//提交分页查询
    public static final String URL_QUREY_CHANGE_LETTER_SUB_INFO = Config.URL_BASE + "/appDKXS/getChangeLetterPageData";//提交更改函分页查询
    public static final String URL_QUERY_CONTRACT_REVIEW_INFO = Config.URL_BASE + "/appDKXS/getContractAutitInfoPageData";//合同审核查询
    public static final String URL_QUERY_ORDER_REVIEW_INFO = Config.URL_BASE + "/appDKXS/getOrderAutitInfoPageData";//订单审核分页查询
    public static final String URL_QUERY_CHANGE_LETTER_INFO = Config.URL_BASE + "/appDKXS/getChangeLetterAutitInfoPageData";//更改函审核
    public static final String URL_QUERY_CHANGE_BILL_INFO = Config.URL_BASE + "/appDKXS/getChangeSheetAutitInfoPageData";//更改单审核分页查询
    public static final String URL_GET_MODELS = Config.URL_BASE + "/appDKXS/getModelsList";//车型
    public static final String URL_GET_REGION = Config.URL_BASE + "/appDKXS/getRegionList";//
    public static final String URL_ORDER_ADD = Config.URL_BASE + "/appDKXS/addOrderInfo";//新增
    public static final String URL_ORDER_MODIFY = Config.URL_BASE + "/appDKXS/updateOrder";//修改
    public static final String URL_ORDER_COMMIT = Config.URL_BASE + "/appDKXS/commitOrder";//提交
    public static final String URL_GET_PROVINCE_CITY = Config.URL_BASE + "/appDKXS/selectProvincialCity";//省市
    public static final String URL_GET_CHECK_ITEMS_LIST = Config.URL_BASE + "/appDKXS/getCheckItemsList";
    public static final String URL_GET_STATE = Config.URL_BASE + "/appDKXS/getDicByFiled";  //状态
    public static final String URL_GET_AREA = Config.URL_BASE + "/appDKXS/getRegionList";  //区域
    public static final String URL_GET_ORDER_DETAIL_INFO = Config.URL_BASE + "/appDKXS/getOrderInfo";//订单信息
    public static final String URL_GET_ORDER_REVIEW = Config.URL_BASE + "/appDKXS/orderAudit";//订单审核
    public static final String URL_GET_CONTRACT_REVIEW = Config.URL_BASE + "/appDKXS/contractAudit";//合同审核
    public static final String URL_GET_CHANGE_LETTER_REVIEW = Config.URL_BASE + "/appDKXS/changeLetterAudit";//更改单审核
    public static final String URL_GET_CHANGE_BILL_REVIEW = Config.URL_BASE + "/appDKXS/changeSheetAudit";//更改单审核
    public static final String URL_GET_ASSEMBLY_INFO = Config.URL_BASE + "/appDKXS/getCheckItemsList";  //获取总成信息
    public static final String URL_GET_ASSEMBLY_LIST = Config.URL_BASE + "/appDKXS/getmatchingandstandard";//获取总成信息列表
    public static final String URL_GET_ADD_ORDER_NAME = Config.URL_BASE + "/appDKXS/getCustomerList";   //订单新增选择用户列表
    public static final String URL_GET_ORDER_NUMBER = Config.URL_BASE + "/appDKXS/getOrderNumber";  //获取orderNumber

    //POST
    public static final String URL_GET_ACCESS_TOKEN = Config.URL_BASE + "/appLogin/appLogin";

}
