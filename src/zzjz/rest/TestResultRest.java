package zzjz.rest;

import zzjz.bean.TestResultEntity;
import zzjz.bean.TestInfoRequest;
import zzjz.bean.BaseRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.TestInfo;
import zzjz.bean.ResultCode;
import zzjz.bean.UserScoreEntity;
import zzjz.bean.User;
import zzjz.service.TestResultService;
import zzjz.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 梅宏振
 * @version 2015年6月16日 下午7:32:29
 * @ClassName: TestResultRest
 * @Description: 考试成绩管理操作Rest接口类
 */
@Component
@Path("/testResult")
public class TestResultRest {

    private static Logger logger = Logger.getLogger(TestResultRest.class);

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private UserService userService;

    /**
     * 获取考试成绩信息.
     *
     * @param request 考试信息实体
     * @param headers headers对象
     * @return 成绩列表
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestResultEntity> getTestResultList(TestInfoRequest request,
                                                            @Context HttpHeaders headers) {
        BaseResponse<TestResultEntity> response = new BaseResponse<TestResultEntity>();
        List<TestResultEntity> testResultEntityList;
        String message = "查询成功！";
        TestInfo testInfo = request.getTestInfo();
        //try {
        testResultEntityList =
                testResultService.getTestReusltList(testInfo, request.getPaging());
        int count = testResultService.getTestReusltCount(testInfo, request.getPaging());
        response.setData(testResultEntityList);
        response.setiTotalRecords(count);
        response.setiTotalDisplayRecords(count);
        response.setResultCode(ResultCode.SUCCESS);
        response.setMessage(message);
        /*} catch (Exception e) {
            logger.debug("");
            response.setResultCode(ResultCode.ERROR);
            response.setMessage("获取成绩查询列表出现异常！");
            return response;
        }*/

        return response;
    }

    /**
     * 获取用户得分列表.
     *
     * @param baseRequest BaseRequest
     * @param headers     headers对象
     * @return 查询结果
     */
    @POST
    @Path("/userScoreList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<UserScoreEntity> getTestUserScoreList(BaseRequest baseRequest,
                                                              @Context HttpHeaders headers) {
        BaseResponse<UserScoreEntity> response = new BaseResponse<UserScoreEntity>();
        List<UserScoreEntity> testResultEntityList;
        String message = "查询成功！";

        testResultEntityList =
                testResultService.getTestUserScoreList(baseRequest.getId(),
                        baseRequest.getPaging());
        int count =
                testResultService.getTestUserScoreCount(baseRequest.getId(),
                        baseRequest.getPaging());
        response.setData(testResultEntityList);
        response.setiTotalRecords(count);
        response.setiTotalDisplayRecords(count);
        response.setResultCode(ResultCode.SUCCESS);
        response.setMessage(message);


        return response;
    }

    /**
     * 获取用户列表.
     *
     * @param headers headers对象
     * @return 查询结果
     */
    @GET
    @Path("/list2")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> getUserList(@Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        List<User> userList = userService.getUserList(); //获取用户列表
        if (userList != null) {
            logger.debug("用户信息获取成功！");
            response.setMessage("用户信息获取成功！");
            response.setData(userList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug("用户信息为空！");
            response.setMessage("用户信息为空！");
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

}
