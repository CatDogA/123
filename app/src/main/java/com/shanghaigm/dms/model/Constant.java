package com.shanghaigm.dms.model;

/**
 * Created by Tom on 2017/5/24.
 */

public class Constant {
    //GET
    //订单、合同、更改函、更改单
    public static final String URL_GET_VERSION = Config.URL_BASE + "/appLogin/getDictionaries";   //获取版本号
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
    public static final String URL_ORDER_DELETE = Config.URL_BASE + "/appDKXS/deleteOrderInfo";  //删除订单
    public static final String URL_QUERY_CHANGE_LETTER_ID_IFNO = Config.URL_BASE + "/appDKXS/changeLetterContractList";    //获取更改函号相关信息
    public static final String URL_ADD_CHANGE_LETTER = Config.URL_BASE + "/appDKXS/addChangeLetter";   //新增/修改更改函
    public static final String URL_SUB_CHANGE_LETTER = Config.URL_BASE + "/appDKXS/commitChangeLetter";  //提交更改函
    public static final String URL_DELETE_CHANGE_LETTER = Config.URL_BASE + "/appDKXS/deleteChangeLetterInfo";  //删除更改函
    public static final String URL_GET_CHANGE_LETTER_INFO = Config.URL_BASE + "/appDKXS/getchangeLetterInfo";
    public static final String URL_COMMIT_ORDER = Config.URL_BASE + "/appDKXS/commitOrder";   //订单提交
    public static final String URL_GET_BATTERY_INFO = Config.URL_BASE + "/appDKXS/selectBattery";   //订单提交
    //日报
    public static final String URL_GET_QUERY_REPORT_LIST = Config.URL_BASE + "/appDKSH/getAftersaleDailyPageData";    //查询日报列表
    public static final String URL_GET_REPORT_DETAIL = Config.URL_BASE + "/appDKSH/getAftersaleDailyDetail";      //日报明细
    public static final String URL_GET_ADD_REPORT = Config.URL_BASE + "/appDKSH/addDaily";     //日报保存
    public static final String URL_GET_SUB_REPORT = Config.URL_BASE + "/appDKSH/updateDaily";  //日报提交
    public static final String URL_GET_CAR_NO = Config.URL_BASE + "/appDKSH/getCarNoAll";
    public static final String URL_GET_FOUR_CAR_INFO = Config.URL_BASE + "/appDKSH/getCarDetail";
    public static final String URL_GET_PICTURE_VIDEO_FILE = Config.URL_BASE2 + "/appFileUploadController/fileUpload";
    public static final String URL_GET_SUB_PIC_VIDEO = Config.URL_BASE2 + "/appFileUploadController/updateListFileInfo";
    public static final String URL_GET_FILE_INFO = Config.URL_BASE2 + "/appFileUploadController/getFilePathById";  //获取文件信息
    public static final String URL_DOWNLOAD_FILE = Config.URL_BASE2 + "/appFileUploadController/download";
    public static final String URL_REPORT_DELETE_FILE = Config.URL_BASE2 + "/appFileUploadController/delFilePathById";
    public static final String URL_REPORT_DELETE = Config.URL_BASE + "/appDKSH/deleteDaily";     //日报删除
    //POST
    public static final String URL_GET_ACCESS_TOKEN = Config.URL_BASE + "/appLogin/appLogin";
    //客户文档
    public static final String URL_CUSTOM_LIST = Config.URL_BASE2 + "/app/crmCustomerController/getCrmCustomerInfoAll";//潜客列表
    public static final String URL_CUSTOM_INFO = Config.URL_BASE2 + "/app/crmCustomerController/getDicByFiled";//客户信息
    public static final String URL_MODIFY_CUSTOM_INFO = Config.URL_BASE2 + "/app/crmCustomerController/updateCrmCustomerInfo";//修改客户信息
    public static final String URL_ADD_CUTOM = Config.URL_BASE2 + "/app/crmCustomerController/addCrmCustomer";//新增客户
    public static final String URL_ADDRESS = Config.URL_BASE2 + "/app/crmCustomerController/selectchina";//省市县
    public static final String URL_LINK_MAN_ADD = Config.URL_BASE2 + "/app/crmCustomerController/addCrmCustomerLinkman";//单位联系人新增
    public static final String URL_LINK_MAN_UPDATE = Config.URL_BASE2 + "/app/crmCustomerController/updateCrmCustomerLinkman";//单位联系人修改
    public static final String URL_LINK_LIST = Config.URL_BASE2 + "/app/crmCustomerController/getCrmCustomerLinkman";//单位联系人列表
    public static final String URL_LINK_MAN_TYEP = Config.URL_BASE2 + "/app/crmCustomerController/getDicByFiled";  //联系人类型
    public static final String URL_CAR_TYPE = Config.URL_BASE2 + "/app/crmCustomerController/getDicByFiled"; //客车类型
    public static final String URL_ADD_HOLDINGS = Config.URL_BASE2 + "/app/crmCustomerController/addCrmTenureAmount"; //新增保有量
    public static final String URL_MODIFY_HOLDINGS = Config.URL_BASE2 + "/app/crmCustomerController/updateCrmTenureAmount"; //新增保有量
    public static final String URL_HOLDINGS_LIST = Config.URL_BASE2 + "/app/crmCustomerController/getCrmTenureAmount"; //保有量列表
    public static final String URL_CAR_LENGTH_LIST = Config.URL_BASE2 + "/app/crmCustomerController/getDicByFiled"; //客车长度列表
    public static final String URL_ADD_HISTORY = Config.URL_BASE2 + "/app/crmCustomerController/addCrmBuyHistory";//新增购买历史
    public static final String URL_MODIFY_HISTORY = Config.URL_BASE2 + "/app/crmCustomerController/updateCrmBuyHistory";//新增购买历史
    public static final String URL_HISTORY_LIST = Config.URL_BASE2 + "/app/crmCustomerController/getCrmBuyHistory";//购买历史列表
    public static final String URL_ADD_PRODUCT = Config.URL_BASE2 + "/app/crmCustomerController/addCrmProductDemand";//新增产品需求
    public static final String URL_MODIFY_PRODUCT = Config.URL_BASE2 + "/app/crmCustomerController/updateCrmProductDemand";//修改产品需求
    public static final String URL_PRODUCT_LIST = Config.URL_BASE2 + "/app/crmCustomerController/getCrmProductDemand";//产品列表
    public static final String URL_GET_CUSTOM_INFO = Config.URL_BASE2 + "/app/crmCustomerController/getCrmCustomerDetail";//获取客户信息
    public static final String URL_DELETE_LINK_MAN = Config.URL_BASE2 + "/app/crmCustomerController/deleteCustomerLinkman";//删除联系人
    public static final String URL_DELETE_HOLDINGS = Config.URL_BASE2 + "/app/crmCustomerController/deleteTenureAmount";//删除保有量
    public static final String URL_DELETE_PRODUCTS = Config.URL_BASE2 + "/app/crmCustomerController/deleteProductDemand";//删除产品需求
    public static final String URL_DELETE_HISTORY = Config.URL_BASE2 + "/app/crmCustomerController/deleteBuyHistory";//删除收购历史
    //区域竞品
    public static final String URL_AREA_COM_LIST = Config.URL_BASE2 + "/app/crmRegionController/getCrmRegionCompetitor";//区域竞品列表
    public static final String URL_AREA_COM_GET_CODE = Config.URL_BASE2 + "/app/crmRegionController/getCode";//获取编号
    public static final String URL_BUY_CAR_FOR = Config.URL_BASE2 + "/app/crmRegionController/getPurpose";//购车用途
    public static final String URL_AREA_COM_SUB = Config.URL_BASE2 + "/app/crmRegionController/addCrmRegionCompetitor";//竞品新增
    public static final String URL_AREA_COM_CUSTOM_INFO_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getCrmCustomerInfoYx";//区域竞品客户列表(非日报1)
    public static final String URL_AREA_COM_CUSTOM_INFO_LIST_DAY = Config.URL_BASE2 + "/app/crmDayReportController/getCrmCustomerInfo";
    public static final String URL_AREA_COM_CUSTOM_DETAIL_INFO = Config.URL_BASE2 + "/app/crmRegionController/getCrmRegionCompetitorDetail";//区域竞品明细
    public static final String URL_MODIFY_AREA_COM = Config.URL_BASE2 + "/app/crmRegionController/updateCrmBuyHistory";//区域竞品修改
    public static final String URL_AREA_COM_SUB2 = Config.URL_BASE2 + "/app/crmRegionController/submitCrmRegionCompetitor";//区域竞品提交
    public static final String URL_AREA_COM_DELETE = Config.URL_BASE2 + "/app/crmRegionController/deleteCrmRegionCompetitor";//区域竞品删除
    public static final String URL_AREA_COM_REVIEW = Config.URL_BASE2 + "/app/crmRegionController/getCrmRegionCompetitorOem";//区域竞品审核查询
    public static final String URL_AREA_COM_CANCEL = Config.URL_BASE2 + "/app/crmRegionController/updateState";//区域竞品审核作废
    //月报管理
    public static final String URL_MONTH_REPORT_LIST = Config.URL_BASE2 + "/app/crmMonthController/getPagelistDLR";//月报管理
    public static final String URL_YEAR_SOLDS = Config.URL_BASE2 + "/app/crmMonthController/getSalesVolumesByYear";//月报年度台数
    public static final String URL_LAST_MONTH_TARGET = Config.URL_BASE2 + "/app/crmMonthController/getIndexInfo";//上月指标
    public static final String URL_MONTH_REPORT_ADD = Config.URL_BASE2 + "/app/crmMonthController/addMonthlyReport";//月报新增
    public static final String URL_MONTH_SITUATION = Config.URL_BASE2 + "/app/crmMonthController/addThisMonthWorkContent";//本月情况新增
    public static final String URL_MONTH_SITUATION_MODIFY = Config.URL_BASE2 + "/app/crmMonthController/updateThisMonthWorkContent";//本月情况修改
    public static final String URL_NEXT_MONTH_SITUATION = Config.URL_BASE2 + "/app/crmMonthController/addNextMonthWorkContent";//下月情况新增
    public static final String URL_NEXT_MONTH_SITUATION_MODIFY = Config.URL_BASE2 + "/app/crmMonthController/updateNextMonthWorkContent";//下月情况修改
    public static final String URL_LAST_MONTH_PLAN = Config.URL_BASE2 + "/app/crmMonthController/getLastMonth";//上月计划
    public static final String URL_GET_WORK_LIST = Config.URL_BASE2 + "/app/crmMonthController/monthWorkContentDetailed";//获取本月列表
    public static final String URL_GET_NEXT_LIST = Config.URL_BASE2 + "/app/crmMonthController/nextMonthWorkContentDetailed";//获取下月列表
    public static final String URL_DELETE_THIS_LIST = Config.URL_BASE2 + "/app/crmMonthController/deleteThisMonthWorkContent";//删除本月列表
    public static final String URL_DELETE_NEXT_LIST = Config.URL_BASE2 + "/app/crmMonthController/deleteNextMonthWorkContent";//删除下月列表
    public static final String URL_OTHER_WORK_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getDetailOtherWork";//其他工作列表(通用)
    public static final String URL_DELETE_OTHER_WORK_LIST = Config.URL_BASE2 + "/app/crmDayReportController/deleteOtherWork";//删除其他(通用)
    public static final String URL_ADD_OTHER_LIST = Config.URL_BASE2 + "/app/crmDayReportController/addOtherWork";//新增其他(通用)
    public static final String URL_REVIEW_SCAN = Config.URL_BASE2 + "/app/crmMonthController/getPagelistOEM";//审核查询列表
    public static final String URL_MAIN_INFO = Config.URL_BASE2 + "/app/crmMonthController/selectMonthlyReport";//主表信息
    public static final String URL_MONTH_SUB = Config.URL_BASE2 + "/app/crmMonthController/submit";//月报提交
    public static final String URL_MONTH_DELETE = Config.URL_BASE2 + "/app/crmMonthController/delect";//月报删除
    public static final String URL_MAIN_INFO_MODIFY = Config.URL_BASE2 + "/app/crmMonthController/updateMonthlyReport";//主表修改
    public static final String URL_MONTH_REPORT_REVIEW = Config.URL_BASE2 + "/app/crmMonthController/auditing";//月报审核
    //周报管理
    public static final String URL_WEEK_REPORT_ADD = Config.URL_BASE2 + "/app/crmWeekReportController/addWeekReport";//周报添加
    public static final String URL_WEEK_REPORT_WORK_PLAN_ADD = Config.URL_BASE2 + "/app/crmWeekReportController/addNextWeekWork";//周报工作计划添加
    public static final String URL_WEEK_REPORT_UPDATE = Config.URL_BASE2 + "/app/crmWeekReportController/updateWeekReport";//周报修改
    public static final String URL_WEEK_REPORT_WORK_PALN_UPDATE = Config.URL_BASE2 + "/app/crmWeekReportController/updateNextWeekWork";//修改周工作计划
    public static final String URL_WEEK_REPORT_MAIN_INFO = Config.URL_BASE2 + "/app/crmWeekReportController/getWeekReport";//周报主表
    public static final String URL_WEEK_REPORT_WORK_PLAN_QUERY = Config.URL_BASE2 + "/app/crmWeekReportController/getNextWeekWork";//周工作计划查询
    public static final String URL_WEEK_REPORT_DLR_QUERY = Config.URL_BASE2 + "/app/crmWeekReportController/getPageListDLR";//周报分页查询DLR
    public static final String URL_WEEK_REPORT_OEM_QUERY = Config.URL_BASE2 + "/app/crmWeekReportController/getPageListOEM";//周报分页查询OEM
    public static final String URL_WEEK_REPORT_DELETE = Config.URL_BASE2 + "/app/crmWeekReportController/deleteWeekReport";//删除周报
    public static final String URL_WEEK_REPORT_WORK_PLAN_DELETE = Config.URL_BASE2 + "/app/crmWeekReportController/deleteNextWeekWork";//删除周报计划
    public static final String URL_WEEK_REPORT_SUB = Config.URL_BASE2 + "/app/crmWeekReportController/submit";//周报提交
    public static final String URL_WEEK_REPORT_REVIEW = Config.URL_BASE2 + "/app/crmWeekReportController/auditing";//周报审核
    //日报管理
    public static final String URL_DAY_REPORT_SUB_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getCrmDayReportList";//日报提交列表
    public static final String URL_VISIT_INTENT = Config.URL_BASE2 + "/app/crmCustomerController/getDicByFiled";//拜访意图下拉
    public static final String URL_CUSTOM_INFOS = Config.URL_BASE2 + "/app/crmDayReportController/getCrmCustomerInfo";//选取客户信息
    public static final String URL_LINK_MEN = Config.URL_BASE2 + "/app/crmDayReportController/getLinkmanList";//根据客户ID获取联系人
    public static final String URL_CUSTOM_ADD = Config.URL_BASE2 + "/app/crmDayReportController/addDayCustomer";//走访客户新增保存
    public static final String URL_MODEL_CHOOSE = Config.URL_BASE2 + "/app/crmDayReportController/getModelsListByMap";//填写意向订单信息选取车型
    public static final String URL_CUSTOM_INFO_CHOOSE = Config.URL_BASE2 + "/app/crmDayReportController/getCrmCustomerInfoYx";//意向订单选取客户信息
    public static final String URL_INTENT_ORDER_ADD = Config.URL_BASE2 + "/app/crmDayReportController/addCrmDayIntentionCustomer";//新增意向订单保存
    public static final String URL_DAY_REPOET_DELETE = Config.URL_BASE2 + "/app/crmDayReportController/deleteCrmDayReport";//日报删除
    public static final String URL_VISIT_CUSTOM_INFO_DELETE = Config.URL_BASE2 + "/app/crmDayReportController/deleteDayCustomer";//删除走访客户信息
    public static final String URL_INTENT_ORDER_DELETE = Config.URL_BASE2 + "/app/crmDayReportController/deleteCrmDayIntentionCustomer";//删除意向订单信息
    public static final String URL_VISTIT_INFO_UPDATE = Config.URL_BASE2 + "/app/crmDayReportController/updateDayCustomer";//修改走访客户信息
    public static final String URL_INTENT_ORDER_UPDATE = Config.URL_BASE2 + "/app/crmDayReportController/updateCrmDayIntentionCustomer";//修改意向订单信息
    public static final String URL_VISIT_CUSTOM_DETAIL_INFO = Config.URL_BASE2 + "/app/crmDayReportController/selectCrmDayCustomer";//走访客户明细，单条
    public static final String URL_INTENT_ORDER_DETAIL_INFO = Config.URL_BASE2 + "/app/crmDayReportController/selectCrmDayIntentionCustomer";//意向订单明细，单条
    public static final String URL_VISIT_CUSTOM_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getDetailCrmDayCustomer";//获取走访客户列表
    public static final String URL_INTENT_ORDER_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getDetailCrmDayIntentionCustomer";//意向订单列表
    public static final String URL_DAY_REPORT_SUB = Config.URL_BASE2 + "/app/crmDayReportController/submitCrmDayReport";//日报提交
    public static final String URL_DAY_REPORT_REVIEW_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getCrmDayReportListOem";//日报信息审核查询(厂)
    public static final String URL_DAY_REPORT_QUERY_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getCrmDayReportListSearch";//日报查询（厂）
    public static final String URL_REVIEW_NOTES = Config.URL_BASE2 + "/app/crmDayReportController/selectFlowdetails";//获取审核记录(通用)
    public static final String URL_DAY_REPORT_REVIEW = Config.URL_BASE2 + "/app/crmDayReportController/CrmDayReportAudit";//日报信息审核
    public static final String URL_AREA_LIST = Config.URL_BASE2 + "/app/crmDayReportController/getReg";//获取区域下拉
}
