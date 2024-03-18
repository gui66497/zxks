package zzjz.rest;

import zzjz.bean.TestPaperCategory;
import zzjz.bean.TestPaperCategoryRequest;
import zzjz.bean.BaseRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.TestPaper;

import zzjz.service.TestPaperCategoryService;
import zzjz.service.TestPaperService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 * @author guitang.fang
 * @version 2016/5/31 13:46
 * @Description: 考试类别相关操作Rest接口类
 * @ClassName: TestPaperCategoryRest
 */
@Component
@Path("/testPaperCategory")
public class TestPaperCategoryRest {

    private static Logger logger = Logger.getLogger(TestPaperCategoryRest.class);

    /**
     * 注入TestPaperCategoryService.
     */
    @Autowired
    public TestPaperCategoryService testPaperCategoryService;

    /**
     * 注入TestPaperService.
     */
    @Autowired
    public TestPaperService testPaperService;

    /**
     * 添加考试类别信息.
     *
     * @param request 试卷分类实体返回
     * @param headers headers对象
     * @return 添加结果
     * @author guitang.fang
     * @date 2016/5/31 15:25
     */
    @POST
    @Path("/category")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addCategory(TestPaperCategoryRequest request,
                                            @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        TestPaperCategory testPaperCategory = request.getTestPaperCategory();
        String message = "";
        if (testPaperCategory == null) {
            message = "考试类别为空！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        testPaperCategory.setCreator(Long.parseLong(userId));
        TestPaperCategory category =
                testPaperCategoryService.getTestPaperCategory(testPaperCategory);
        if (category != null) {
            message = "考试类别已存在！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        //考试分类最多支持2级分类 所以这里判断下
        if (testPaperCategory.getParentId() != 0) {
            TestPaperCategory fatherTestPaperCategory =
                    testPaperCategoryService.getFatherTestPaperCategory(
                            testPaperCategory.getParentId());
            if (fatherTestPaperCategory != null) {
                if (fatherTestPaperCategory.getParentId() != 0) {
                    message = "考试类别只支持最多二级分类！";
                    logger.debug(message);
                    response.setMessage(message);
                    response.setResultCode(ResultCode.ERROR);
                    return response;
                }
            }
        }
        boolean success =
                testPaperCategoryService.addTestPaperCategory(testPaperCategory); //添加考试类别信息
        if (success) {
            message = "考试类别添加成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "考试类别添加失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 删除考试类别信息.
     *
     * @return 删除结果
     * @date 2016/5/31 16:06
     * @author guitang.fang
     */
    /**
     * 删除考试类别信息.
     *
     * @param request BaseRequest
     * @param headers headers对象
     * @return 删除结果
     * @date 2016/5/31 16:06
     * @author guitang.fang
     */
    @DELETE
    @Path("/category")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteCategory(BaseRequest request,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        long categoryId = request.getId();
        String message = "";
        if (categoryId == 0) {
            message = "考试类别Id为空！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<TestPaperCategory> childCategoryList =
                testPaperCategoryService.getChildTestPaperCategoryById(categoryId);
        if (childCategoryList != null && childCategoryList.size() > 0) {
            message = "该考试类别存在子类别！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        List<TestPaper> testPaperList = testPaperService.getTestPaperByCategoryId(categoryId);
        if (CollectionUtils.isNotEmpty(testPaperList)) { //存在试卷
            message = "该考试类别下存在试卷！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        boolean res = testPaperCategoryService.deleteTestPaperCategory(categoryId);
        if (res) {
            message = "考试类别删除成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "考试类别删除失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 修改考试类别信息.
     *
     * @param request 试卷分类实体返回
     * @param headers headers对象
     * @return 修改结果
     * @date 2016/5/31 16:42
     * @author guitang.fang
     */
    @PUT
    @Path("/category")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateCategory(TestPaperCategoryRequest request,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        // 获取restful的headers信息
        TestPaperCategory testPaperCategory = request.getTestPaperCategory(); //获取考试类别信息
        String message = "";
        if (testPaperCategory == null) {
            message = "考试类别为空！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        TestPaperCategory category =
                testPaperCategoryService.getTestPaperCategory(testPaperCategory);
        if (category != null) {
            message = "考试类别已存在!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success =
                testPaperCategoryService.updateTestPaperCategory(testPaperCategory); //更新考试类别信息
        if (success) {
            message = "考试类别更新成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "考试类别更新失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取考试类别信息.
     *
     * @param headers headers对象
     * @return 考试类别实体类集合
     * @author guitang.fang
     * @date 2016/5/31 15:37
     */
    @GET
    @Path("/categoryList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestPaperCategory> getCategoryList(@Context HttpHeaders headers) {
        BaseResponse<TestPaperCategory> response = new BaseResponse<TestPaperCategory>();
        List<TestPaperCategory> testPaperCategoryList =
                testPaperCategoryService.getTestPaperCategoryList();
        //获取考试类别信息
        String message = "";
        if (testPaperCategoryList != null && testPaperCategoryList.size() > 0) {
            message = "试类别信息获取成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setData(testPaperCategoryList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "考试类别信息为空！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

}
