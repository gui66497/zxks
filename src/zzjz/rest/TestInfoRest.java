package zzjz.rest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.PagingEntity;
import zzjz.bean.ResultCode;
import zzjz.bean.TestInfo;
import zzjz.bean.TestInfoRequest;
import zzjz.service.TestInfoService;
import zzjz.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 * @author zhuxiaoxiao
 * @version 2016/5/30 16:23
 * @ClassName: TestInfoRest
 * @Description: 考试信息相关操作rest接口类
 */
@Component
@Path("/testInfo")
public class TestInfoRest {
    private static Logger logger = Logger.getLogger(UserRest.class); //日志

    private static String strUserId = "userId";

    private static String strParam = "参数为空!";

    private static String strTest = "tp.TESTPAPER_NAME";

    private static String strEnd = "ti.TEST_END_TIME";

    private static String strStart = "ti.TEST_START_TIME";

    private static String strTestInfo = "考试信息获取成功！";

    private static String strCount = "总页数:";

    private static String strSuccess = "查询成功!";

    private static String strNull = "考试信息为空!";


    /**
     * 注入TestInfoService.
     */
    @Autowired
    public TestInfoService testInfoService;

    @Context
    ServletContext context;

    @Context
    HttpServletRequest servletRequest;

    /**
     * 添加考试信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return true or false
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public BaseResponse<String> addTestInfo(TestInfoRequest testInfoRequest,
                                            @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(strUserId);
        if (StringUtils.isBlank(userId)) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        TestInfo testInfo = testInfoRequest.getTestInfo(); //获取考试信息
        if (testInfo == null) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if ("0".equals(testInfo.getTestId())) {
            logDebugAndSetMessage("试卷不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isBlank(testInfo.getStartTime())) {
            logDebugAndSetMessage("开始时间不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isBlank(testInfo.getEndTime())) {
            logDebugAndSetMessage("结束时间不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (testInfo.getUserIdList() == null || 0 == testInfo.getUserIdList().size()) {
            logDebugAndSetMessage("考生列表不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (0 == testInfo.getTotalUserNo()) {
            logDebugAndSetMessage("参考总人数不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        //唯一性校验
        TestInfo searchResult = testInfoService.getTestInfoByTestIdAndTime(testInfo);
        if (searchResult != null) {
            logDebugAndSetMessage("同考试时段已存在该考试！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        testInfo.setCreator(Long.parseLong(userId));
        testInfo.setTestStatus(1);
        long id = System.currentTimeMillis(); //时间戳，即为考试Id
        testInfo.setId(id);
        logger.debug("正在添加考试信息");
        boolean success = testInfoService.addTestInfo(testInfo);
        if (success) {
            logDebugAndSetMessage("考试信息添加成功！", response);
            logger.debug("正在添加考试和用户关联信息");
            boolean addUsersFlag = testInfoService.addTestInfoAndUserConnection(testInfo);
            if (addUsersFlag) {
                logDebugAndSetMessage("考试信息添加成功！", response);
                response.setResultCode(ResultCode.SUCCESS);
            } else {
                logDebugAndSetMessage("考试信息添加成功,考生添加失败！", response);
                response.setResultCode(ResultCode.ERROR);
            }
        } else {
            logDebugAndSetMessage("考试信息添加失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 更新考试信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return true or false
     */
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //添加考试信息
    public BaseResponse<String> updateTestInfo(TestInfoRequest testInfoRequest,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(strUserId);
        if (StringUtils.isBlank(userId)) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        TestInfo testInfo = testInfoRequest.getTestInfo(); //获取考试信息
        if (testInfo == null) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (0 == testInfo.getId()) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }


        if ("0".equals(testInfo.getTestId())) {
            logDebugAndSetMessage("试卷不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isBlank(testInfo.getStartTime())) {
            logDebugAndSetMessage("开始时间不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isBlank(testInfo.getEndTime())) {
            logDebugAndSetMessage("结束时间不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (testInfo.getUserIdList() == null || 0 == testInfo.getUserIdList().size()) {
            logDebugAndSetMessage("考生列表不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (0 == testInfo.getTotalUserNo()) {
            logDebugAndSetMessage("参考总人数不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        //唯一性校验
        TestInfo searchResult = testInfoService.getTestInfoByTestIdAndTime(testInfo);
        if (searchResult != null && searchResult.getId() != testInfo.getId()) {
            logDebugAndSetMessage("同考试时段已存在该考试！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        boolean success = testInfoService.updateTestInfo(testInfo);
        if (success) {
            logDebugAndSetMessage("考试信息修改成功！", response);
            logger.debug("正在删除之前考试和用户关联信息");
            boolean deleteUsersFlag = testInfoService.deleteTestInfoAndUserConnection(testInfo);
            if (deleteUsersFlag) {
                logDebugAndSetMessage("考试之前和用户关联信息删除成功！", response);
                logger.debug("正在添加考试和用户关联信息");
                boolean addUsersFlag = testInfoService.addTestInfoAndUserConnection(testInfo);
                if (addUsersFlag) {
                    logDebugAndSetMessage("考试信息更新成功！", response);
                    response.setResultCode(ResultCode.SUCCESS);
                } else {
                    logDebugAndSetMessage("考试信息更新成功,考生添加失败！", response);
                    response.setResultCode(ResultCode.ERROR);
                }
            } else {
                logDebugAndSetMessage("考试信息修改成功,清除之前考生失败！", response);
                response.setResultCode(ResultCode.ERROR);
            }

        } else {
            logDebugAndSetMessage("考试信息修改失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 删除考试信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return true or false
     */
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteTestInfo(TestInfoRequest testInfoRequest,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(strUserId);
        if (StringUtils.isBlank(userId)) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        TestInfo testInfo = testInfoRequest.getTestInfo();
        if (!testInfoService.confirmActionById(testInfo.getId())) {
            logDebugAndSetMessage("该操作仅支持在考试开始前执行！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        boolean success = testInfoService.deleteTestInfoById(testInfo.getId()); //删除用户信息
        if (success) {
            logDebugAndSetMessage("考试信息删除成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("考试信息删除失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取筛选过的考试信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return 考试信息对象
     */
    @POST
    @Path("/filterTestInfoList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //查询考试信息
    public BaseResponse<TestInfo> getFilterTestInfo(TestInfoRequest testInfoRequest,
                                                    @Context HttpHeaders headers) {
        BaseResponse<TestInfo> response = new BaseResponse<TestInfo>();
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(strUserId);
        if (StringUtils.isBlank(userId)) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        TestInfo testInfo = testInfoRequest.getTestInfo();
        //定义列名
        String[] cols = {"", strTest, "tiCategory.CATEGORY_NAME", strStart, "", strEnd}; //字段名称，对应页面表格
        PagingEntity pagingEntity = testInfoRequest.getPaging();
        String sortIndex = pagingEntity.getSortColumn(); //排序字段名称对应的位置
        pagingEntity.setSortColumn(cols[Integer.parseInt(sortIndex)]); //修改为排序的字段名称

        List<TestInfo> testInfoList = testInfoService.getFilteredTestInfo(testInfo, pagingEntity);
        if (testInfoList != null) {
            logger.debug(strTestInfo);
            response.setMessage(strTestInfo);
            int count = testInfoService.getFilteredTestInfoCount(testInfo, pagingEntity);
            logger.debug(strCount + count);
            response.setData(testInfoList);
            response.setiTotalRecords(count);
            response.setiTotalDisplayRecords(count);
            logger.debug(strSuccess);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(strNull);
            response.setMessage(strNull);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;

    }

    /**
     * 获取考试信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return 考试信息对象
     */
    @POST
    @Path("/getTestInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //查询考试信息
    public BaseResponse<TestInfo> getTestInfoData(TestInfoRequest testInfoRequest,
                                                  @Context HttpHeaders headers) {
        BaseResponse<TestInfo> response = new BaseResponse<TestInfo>();
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(strUserId);
        if (StringUtils.isBlank(userId)) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        TestInfo testInfo = testInfoRequest.getTestInfo();
        if (testInfo == null) {
            logDebugAndSetMessage(strParam, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<TestInfo> searchResult = testInfoService.getTestInfoById(testInfo.getId());
        if (searchResult != null) {
            logger.debug(strTestInfo);
            response.setMessage(strTestInfo);
            response.setData(searchResult);
            logger.debug(strSuccess);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(strNull);
            response.setMessage(strNull);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;

    }

    /**
     * 根据用户ID获取考试信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return 考试信息列表
     */
    @POST
    @Path("/testInfoList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //查询考试信息
    public BaseResponse<TestInfo> getTestInfo(TestInfoRequest testInfoRequest,
                                              @Context HttpHeaders headers) {
        BaseResponse<TestInfo> response = new BaseResponse<TestInfo>();
        // 获取restful的headers信息
        String uid = (String) servletRequest.getSession().getAttribute(strUserId);
        //定义列名
        String[] cols = {"", strTest, strStart, strEnd, "", "tp.TEST_TIME"}; //字段名称，对应页面表格
        PagingEntity pagingEntity = testInfoRequest.getPaging();
        String sortIndex = pagingEntity.getSortColumn(); //排序字段名称对应的位置
        pagingEntity.setSortColumn(cols[Integer.parseInt(sortIndex)]); //修改为排序的字段名称
        if (StringUtils.isNotBlank(pagingEntity.getSearchValue())) {
            pagingEntity.setSearchValue(StringUtil.zhuanyi(pagingEntity.getSearchValue()));
        }
        List<TestInfo> testInfoList = testInfoService.getTestInfo(uid, pagingEntity);
        int count = testInfoService.getTestInfoCount(uid, pagingEntity);
        logger.debug(strCount + count);
        response.setData(testInfoList);
        response.setiTotalRecords(count);
        response.setiTotalDisplayRecords(count);
        logger.debug(strSuccess);
        response.setResultCode(ResultCode.SUCCESS);
        return response;
    }

    /**
     * 根据用户ID获取成绩信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return 成绩信息列表
     */
    @POST
    @Path("/pointInfoList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //查询考试信息
    public BaseResponse<TestInfo> getPointInfo(TestInfoRequest testInfoRequest,
                                               @Context HttpHeaders headers) {
        BaseResponse<TestInfo> response = new BaseResponse<TestInfo>();
        // 获取restful的headers信息
        String uid = (String) servletRequest.getSession().getAttribute(strUserId);
        //定义列名
        String[] cols = {"", strTest, strStart, strEnd, "tp.TEST_TIME", "tp.MARK_TOTAL", "am.USER_MARK"}; //字段名称，对应页面表格
        PagingEntity pagingEntity = testInfoRequest.getPaging();
        String sortIndex = pagingEntity.getSortColumn(); //排序字段名称对应的位置
        pagingEntity.setSortColumn(cols[Integer.parseInt(sortIndex)]); //修改为排序的字段名称
        if (StringUtils.isNotBlank(pagingEntity.getSearchValue())) {
            pagingEntity.setSearchValue(StringUtil.zhuanyi(pagingEntity.getSearchValue()));
        }
        List<TestInfo> pointInfoList = testInfoService.getPointInfo(uid, pagingEntity);
        if (pointInfoList != null) {
            logger.debug("考试信息查询成功！");
            //获取总条数
            int count = testInfoService.getPointInfoCount(uid, pagingEntity);
            logger.debug(strCount + count);
            response.setData(pointInfoList);
            response.setiTotalRecords(count);
            response.setiTotalDisplayRecords(count);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug("考试信息查询失败！");
            response.setResultCode(ResultCode.ERROR);
        }
        return response;

    }

    /**
     * 判断用户是否参加过考试.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return true or false
     */
    @POST
    @Path("/userAchievement")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestInfo> userAchievement(TestInfoRequest testInfoRequest,
                                                  @Context HttpHeaders headers) {
        BaseResponse<TestInfo> response = new BaseResponse<TestInfo>();
        // 获取restful的headers信息
        String uid = (String) servletRequest.getSession().getAttribute(strUserId);
        TestInfo testInfo = testInfoRequest.getTestInfo();
        boolean count = testInfoService.userAchievement(testInfo.getId() + "", uid);
        if (count) {
            response.setMessage("1");
            logger.debug("您已参加过该场考试！");
            response.setResultCode(ResultCode.BAD_REQUEST);
        } else {
            response.setMessage("2");
            logger.debug("进入考试！");
            response.setResultCode(ResultCode.SUCCESS);
        }
        return response;
    }


    /**
     * 获取参考人员信息.
     *
     * @param testInfoRequest 考试信息实体返回
     * @param headers         headers对象
     * @return 参考人员列表
     */
    @POST
    @Path("/getTestpeopleNames")//获取参考人员信息
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestInfo> getTestpeopleNames(TestInfoRequest testInfoRequest,
                                                     @Context HttpHeaders headers) {
        BaseResponse<TestInfo> response = new BaseResponse<TestInfo>();
        // 获取restful的headers信息
        TestInfo testInfo = testInfoRequest.getTestInfo();

        List<TestInfo> peopleList = testInfoService.getTestpeopleNames(testInfo.getId() + "");
        if (peopleList != null) {
            response.setMessage("获取参考人员信息成功！");
            logger.debug("获取参考人员信息成功！");
            response.setData(peopleList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            response.setMessage("无人参加该场考试！");
            logger.debug("无人参加该场考试！");
            response.setResultCode(ResultCode.BAD_REQUEST);
        }
        return response;
    }

    private void logDebugAndSetMessage(String strMessage, BaseResponse response) {
        logger.debug(strMessage);
        response.setMessage(strMessage);
    }

}
