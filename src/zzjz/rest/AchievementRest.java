package zzjz.rest;


import zzjz.bean.Answer;
import zzjz.bean.TestPaperPart;
import zzjz.service.AchievementService;
import zzjz.service.TestPaperPartService;
import zzjz.service.TestResultService;
import org.apache.log4j.Logger;
import zzjz.bean.BaseRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.AchievementRequest;
import zzjz.bean.Achievement;
import zzjz.bean.ResultCode;
import zzjz.bean.TestPaperRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 * @author guitang.fang
 * @version 2016/6/3 17:02
 * @ClassName: AchievementRest
 * @Description: 成绩管理rest
 */
@Component
@Path("/achievement")
public class AchievementRest {

    private static Logger logger = Logger.getLogger(AchievementRest.class);

    @Autowired
    AchievementService achievementService;

    @Autowired
    TestPaperPartService testPaperPartService;

    @Autowired
    TestResultService testResultService;

    /**
     * 添加成绩信息.
     *
     * @param request 接收对象
     * @param headers headers对象
     * @return 添加结果
     * @author guitang.fang
     * @date 2016/6/3 17:42
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addAchievement(AchievementRequest request,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        Achievement achievement = request.getAchievement();
        String message = "";
        if (achievement == null) {
            message = "成绩信息为空！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        achievement.setUserId(Long.parseLong(userId)); //用户
        achievement.setCreator(Long.parseLong(userId)); //创建者
        Achievement achievementTemp = achievementService.getAchievement(achievement);
        if (achievementTemp != null) {
            message = "成绩信息已存在！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.RECORD_EXIST);
            return response;
        }
        boolean success = achievementService.addAchievement(achievement); //添加考试类别信息
        if (success) {
            testResultService.updateTestUserNum(achievement.getTestInfoId());
            message = "添加成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "添加失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 删除成绩信息.
     *
     * @param request 接收对象
     * @param headers headers对象
     * @return 删除结果
     * @date 2016/6/6 10:06
     * @author guitang.fang
     */
    @DELETE
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteAchievement(BaseRequest request,
                                                  @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        long id = request.getId();
        String message = "";
        final int num = 1;
        if (id < num) {
            message = "成绩信息Id不正确！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        boolean res = achievementService.deleteAchievementById(id);
        if (res) {
            message = "成绩信息删除成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "成绩信息删除失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 修改成绩信息.
     *
     * @param achievement 成绩实体
     * @param headers     headers对象
     * @return 修改结果
     * @date 2016/6/2 13:06
     * @author guitang.fang
     */
    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateAchievement(Achievement achievement,
                                                  @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = "";
        if (achievement == null) {
            message = "该成绩信息为空！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        //更新成绩信息，默认所有值必须传过来，为空的话原始类型默认赋值0
        boolean success = achievementService.updateAchievement(achievement);
        if (success) {
            message = "成绩信息更新成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "成绩信息更新失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取指定考试的所有成绩信息.
     *
     * @param testInfoId 考试信息id
     * @param headers    header头
     * @return 成绩信息列表
     * @date 2016/6/3 17:07
     * @author 房桂堂
     */
    @GET
    @Path("/list/{testInfoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Achievement> getAchievement(@PathParam("testInfoId") long testInfoId,
                                                    @Context HttpHeaders headers) {
        BaseResponse<Achievement> response = new BaseResponse<Achievement>();
        String message = "";
        final int numObj = 1;
        if (testInfoId < numObj) {
            message = "参数不正确！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<Achievement> achievementList
                = achievementService.getAchievementListByTestInfoId(testInfoId);
        if (achievementList != null && achievementList.size() > 0) {
            message = "成绩信息获取成功";
            logger.debug(message);
            response.setMessage(message);
            response.setData(achievementList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "成绩信息获取失败";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取试卷每部分的详细信息，包括试卷信息和题目信息.
     *
     * @param request 请求
     * @param headers 头信息
     * @return 试卷详细信息
     */
    @POST
    @Path("/detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestPaperPart> getAchievementDetail(TestPaperRequest request,
                                                            @Context HttpHeaders headers) {
        BaseResponse<TestPaperPart> response = new BaseResponse<TestPaperPart>();
        String message = "";
        long achievementId = request.getId();
        final int numObj = 1;
        if (achievementId < numObj) {
            message = "参数错误!";
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        List<TestPaperPart> testPaperPartList =
                testPaperPartService.getPartInfoListByAchieveMentId(achievementId);
        if (testPaperPartList != null && testPaperPartList.size() > 0) {
            message = "试卷信息获取成功";
            logger.debug(message);
            response.setMessage(message);
            response.setData(testPaperPartList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "试卷信息获取失败";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        response.setData(testPaperPartList);
        return response;
    }

    /**
     * 计算总分
     * @param request 成绩
     * @param headers headers
     * @return
     */
    @POST
    @Path("/mark")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Answer> calMark(AchievementRequest request,@Context HttpHeaders headers) {
        BaseResponse<Answer> response = new BaseResponse<Answer>();
        if (request.getAchievement() == null) {
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<Answer> answerList = request.getAchievement().getAnswers();
        if (answerList == null) {
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        //List<Word> words = WordSegmenter.seg("杨尚川是APPDPlat应用级产品开发平台的作者");
        //System.out.println(words);
        List<Answer> answerResultList = achievementService.calMark(answerList);
        response.setData(answerResultList);
        response.setMessage("计算总分成功！");
        response.setResultCode(ResultCode.SUCCESS);
        return response;
    }
}
