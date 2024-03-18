package zzjz.rest;

import com.google.common.collect.Lists;
import zzjz.bean.TestPaper;
import zzjz.bean.TestPaperRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.BaseRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.TestInfo;
import zzjz.bean.Item;
import zzjz.bean.BatchExtract;
import zzjz.bean.ExtractEntity;
import zzjz.bean.TestPaperPart;
import zzjz.bean.ItemBank;

import zzjz.service.TestPaperService;
import zzjz.service.TestPaperPartService;
import zzjz.service.TestInfoService;
import zzjz.service.ItemBankService;
import zzjz.service.ItemService;

import zzjz.util.NumberUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import java.util.Arrays;
import java.util.List;

/**
 * @author guitang.fang
 * @version 2016/6/1 9:21
 * @ClassName: TestPaperRest
 * @Description: 试卷相关操作Rest接口类
 */
@Component
@Path("/testPaper")
public class TestPaperRest {

    private static Logger logger = Logger.getLogger(TestPaperRest.class);

    private static String strTestInfo = "试卷信息不正确！";

    private static String strParam = "参数错误！";

    private static String strInfoSu = "试卷信息获取成功！";

    private static String strInfoFa = "试卷信息获取失败！";

    @Autowired
    TestPaperService testPaperService;

    @Autowired
    TestPaperPartService testPaperPartService;

    @Autowired
    TestInfoService testInfoService;

    @Autowired
    ItemBankService itemBankService;

    @Autowired
    ItemService itemService;

    /**
     * 添加试卷信息.
     *
     * @param request 试卷实体返回
     * @param headers headers对象
     * @return 添加结果
     * @date 2016/5/31 16:06
     * @author guitang.fang
     */
    @POST
    @Path("/paper")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addTestPaper(TestPaperRequest request,
                                             @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        TestPaper testPaper = request.getTestPaper();
        String message = "";
        if (testPaper == null) {
            message = "试卷信息为空！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        testPaper.setCreator(Long.parseLong(userId)); //创建人
        TestPaper testPaperTemp = testPaperService.getTestPaperByNameAndId(testPaper.getTestpaperName(), "" + testPaper.getCategoryId());
        if (testPaperTemp != null) {
            message = "试卷信息已存在";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.RECORD_EXIST);
            return response;
        }
        boolean result = testPaperService.addTestPaper(testPaper);
        if (result) {
            message = "试卷信息添加成功";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "试卷信息添加失败";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

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
    @Path("/paper")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteCategory(BaseRequest request,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        long id = request.getId();
        String message = "";
        if (id < 1) {
            message = "试卷信息Id不正确！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<TestInfo> testInfos = testInfoService.getTestedInfoByPaperId(id);
        if (testInfos.size() > 0) {
            message = "存在相应考试,无法删除！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        boolean res = testPaperService.deleteTestPaperById(id);
        if (res) {
            message = "试卷信息删除成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "试卷信息删除失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 根据试卷ID获取考试信息.
     *
     * @param request 试卷实体返回
     * @param headers headers对象
     * @return 考试信息列表
     */
    @POST
    @Path("/exist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestInfo> existTest(TestPaperRequest request,
                                            @Context HttpHeaders headers) {
        BaseResponse<TestInfo> response = new BaseResponse<TestInfo>();
        long id = request.getId();
        String message = "";
        if (id < 1) {
            message = "试卷信息Id不正确！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<TestInfo> testInfos = testInfoService.getTestedInfoByPaperId(id);
        if (testInfos.size() > 0) {
            message = "获取考试信息成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setData(testInfos);
            response.setResultCode(ResultCode.SUCCESS);
            return response;
        }
        return response;
    }

    /**
     * 修改试卷信息.
     *
     * @param request 试卷实体返回
     * @param headers headers对象
     * @return 修改结果
     * @date 2016/6/2 13:06
     * @author guitang.fang
     */
    @PUT
    @Path("/paper")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateCategory(TestPaperRequest request,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        TestPaper testPaper = request.getTestPaper(); //获取试卷信息
        String message = "";
        if (testPaper == null || !StringUtils.isNotBlank(testPaper.getTestpaperName())) {
            message = strTestInfo;
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        TestPaper originTestPaper = testPaperService.getTestPaperById(testPaper.getId());
        //判断名字是否改了，分情况
        //改了name
        if (!testPaper.getTestpaperName().equals(originTestPaper.getTestpaperName())) {
            TestPaper testPaperTemp =
                    testPaperService.getTestPaperByNameAndId(testPaper.getTestpaperName(), testPaper.getCategoryId() + "");
            if (testPaperTemp != null) {
                message = "试卷信息已存在!";
                logger.debug(message);
                response.setResultCode(ResultCode.ERROR);
                response.setMessage(message);
                return response;
            }
        }
        //更新试卷信息，默认所有值必须传过来，为空的话原始类型默认赋值0
        boolean success = testPaperService.updateTestPaper(testPaper);
        if (success) {
            message = "试卷信息更新成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "试卷信息更新失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取指定考试分类下试卷信息.
     *
     * @param request 试卷实体返回
     * @param headers headers对象
     * @return 试卷列表
     * @date 2016/6/2 10:27
     * @author guitang.fang
     */
    @POST
    @Path("/paperList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestPaper> getTestPaperList(TestPaperRequest request,
                                                    @Context HttpHeaders headers) {
        BaseResponse<TestPaper> response = new BaseResponse<TestPaper>();
        long categoryId = request.getId();
        String message = "";
        if (categoryId < 0) {
            message = strParam;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        List<TestPaper> testPaperList =
                testPaperService.getTestPaperListByCategoryId(request.getId(), request.getPaging());
        message = "获取试卷列表成功";
        logger.debug(message);
        response.setMessage(message);
        int testpaperCount =
                testPaperService.getTestpaperCountByCategory(categoryId, request.getPaging()); //总数
        response.setData(testPaperList);
        response.setiTotalRecords(testpaperCount);
        response.setiTotalDisplayRecords(testpaperCount);
        response.setResultCode(ResultCode.SUCCESS);
        return response;
    }

    /**
     * 批量删除试卷信息.
     * @param request 试卷实体返回
     * @param headers headers对象
     * @return 删除结果
     * @date 2016/5/31 16:06
     * @author guitang.fang
     */
    @DELETE
    @Path("/paperBatch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> batchDeleteCategory(TestPaperRequest request,
                                                    @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        List<Long> ids = request.getTestPaperIdList();
        String message = "";
        if (CollectionUtils.isEmpty(ids)) {
            message = "参数不正确！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<String> hasTestPapers = Lists.newArrayList();
        for (long id : ids) {
            List<TestInfo> testInfos = testInfoService.getTestedInfoByPaperId(id);
            if (testInfos.size() > 0) {
                TestPaper testPaper = testPaperService.getTestPaperById(id);
                hasTestPapers.add(testPaper.getTestpaperName());
            }
        }
        if (hasTestPapers.size() > 0) {
            message = "以下试卷存在相应考试，无法删除！</br>" + StringUtils.join(hasTestPapers, "</br>");
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        boolean res = testPaperService.batchDeleteTestPaperById(ids);
        if (res) {
            message = "试卷信息批量删除成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "试卷信息批量删除失败！";
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
    @Path("/allTestPaperInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestPaperPart> getTestPaperAllInfo(TestPaperRequest request,
                                                           @Context HttpHeaders headers) {
        BaseResponse<TestPaperPart> response = new BaseResponse<TestPaperPart>();
        String message = "";
        long testInfoId = request.getId();
        if (testInfoId < 1) {
            message = strParam;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        List<TestPaperPart> testPaperPartList =
                testPaperPartService.getPartInfoListByTestInfoId(testInfoId);
        if (testPaperPartList != null && testPaperPartList.size() > 0) {
            message = strInfoSu;
            logger.debug(message);
            response.setMessage(message);
            response.setData(testPaperPartList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = strInfoFa;
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        response.setData(testPaperPartList);
        return response;
    }


    /**
     * 抽题.
     *
     * @param batchEntity 批量抽取实体
     * @param headers     headers对象
     * @return 题目列表
     * @Author 房桂堂
     * @Date 2016/6/14 9:45
     */
    @POST
    @Path("/extractItem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Item> extractItem(BatchExtract batchEntity,
                                          @Context HttpHeaders headers) {
        BaseResponse<Item> response = new BaseResponse<Item>();
        String message = "";
        if (batchEntity.getObjList() == null) {
            message = "抽题参数不正确！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<ExtractEntity> extractList = batchEntity.getObjList();
        List<Item> finalItemList = Lists.newLinkedList();
        //循环每个题型
        for (ExtractEntity entity : extractList) {
            ItemBank itemBank = itemBankService.getItemBankById(entity.getItemBankId());
            int needQuantity = entity.getQuantity(); //所需题目数
            List<Item> randomItemList = Lists.newLinkedList();
            List<Item> itemList =
                    itemService.getItemByItemTypeAndItemBank(entity.getItemBankId(), entity.getItemType());
            List<Item> filterItemList = Lists.newLinkedList(); //需要过滤掉的题目
            Long[] existItemIds = batchEntity.getExistItemIds();
            if (existItemIds != null) {
                for (Item item : itemList) {
                    if (Arrays.asList(existItemIds).contains(item.getItemId())) {
                        filterItemList.add(item);
                    }
                }
            }
            itemList.removeAll(filterItemList);
            int itemCount = itemList.size();
            if (entity.getQuantity() > itemCount) {
                message = "题目数不足！";
                logger.debug(message);
                response.setMessage(message);
                response.setResultCode(ResultCode.ERROR);
                return response;
            }
            //随机sql 还要传入需要过滤的题目 速度没有快多少 有空研究
            Integer[] indexArr = NumberUtil.randomCommon(itemCount - 1, needQuantity);
            for (int index : indexArr) {
                randomItemList.add(itemList.get(index));
            }
            for (Item item : randomItemList) {
                item.setItemBankName(itemBank.getItemBankName());
            }
            finalItemList.addAll(randomItemList);
        }
        message = "抽取题目成功！";
        logger.debug(message);
        response.setMessage(message);
        response.setData(finalItemList);
        response.setResultCode(ResultCode.SUCCESS);
        return response;
    }


    /**
     * 创建试卷.
     *
     * @param request 试卷实体返回
     * @param headers headers对象
     * @return true or false
     * @Author 房桂堂
     * @Date 2016/6/14 22:42
     */
    @POST
    @Path("/createTestpaper")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> createTestpaper(TestPaperRequest request,
                                                @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = "";
        TestPaper testPaper = request.getTestPaper(); //试卷基本信息
        if (testPaper == null || !StringUtils.isNotBlank(testPaper.getTestpaperName())) {
            message = strTestInfo;
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        testPaper.setCreator(Long.parseLong(userId));
        TestPaper testPaperTemp =
                testPaperService.getTestPaperByNameAndId(testPaper.getTestpaperName(), testPaper.getCategoryId() + "");
        if (testPaperTemp != null) {
            message = "试卷名称已存在!";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.RECORD_EXIST);
            return response;
        }
        if (testPaper.getTestPaperParts() == null) {
            message = "试卷没有模块信息!";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        boolean res = false;

        res = testPaperService.createTestpaper(testPaper);

        if (res) {
            message = "试卷创建成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "试卷创建失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取试卷信息和每部分的详细信息.
     *
     * @param request 请求
     * @param headers 头信息
     * @return 试卷详细信息
     */
    @POST
    @Path("/getTestPaperWithPart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestPaper> getTestPaperWithPart(TestPaperRequest request,
                                                        @Context HttpHeaders headers) {
        BaseResponse<TestPaper> response = new BaseResponse<TestPaper>();
        List<TestPaper> resultTestPaper = Lists.newArrayList();
        String message = "";
        long testPaperId = request.getId();
        if (testPaperId < 1) {
            message = strParam;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        TestPaper testPaper = testPaperService.getTestPaperById(testPaperId);
        if (testPaper == null) {
            message = strInfoFa;
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        List<TestPaperPart> testPaperPartList =
                testPaperPartService.getPartInfoListByTestPaperId(testPaperId);
        testPaper.setTestPaperPartList(testPaperPartList);
        message = strInfoSu;
        logger.debug(message);
        resultTestPaper.add(testPaper);
        response.setMessage(message);
        response.setData(resultTestPaper);
        response.setResultCode(ResultCode.SUCCESS);
        return response;
    }

    /**
     * 修改试卷.
     *
     * @param request 试卷实体返回
     * @param headers headers对象
     * @return true or false
     * @author 房桂堂
     * @Date 2016/6/15 16:55
     */
    @PUT
    @Path("/updateTestPaper")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateTestPaper(TestPaperRequest request,
                                                @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = "";
        TestPaper testPaper = request.getTestPaper(); //试卷基本信息
        if (testPaper == null || !StringUtils.isNotBlank(testPaper.getTestpaperName())
                || testPaper.getId() < 1) {
            message = strTestInfo;
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        TestPaper originTestPaper = testPaperService.getTestPaperById(testPaper.getId());
        //判断名字是否改了，分情况
        //改了name
        if (!testPaper.getTestpaperName().equals(originTestPaper.getTestpaperName())) {
            TestPaper testPaperTemp =
                    testPaperService.getTestPaperByNameAndId(testPaper.getTestpaperName(), testPaper.getCategoryId() + "");
            if (testPaperTemp != null) {
                message = "试卷名称已存在!";
                logger.debug(message);
                response.setResultCode(ResultCode.RECORD_EXIST);
                response.setMessage(message);
                return response;
            }
        }
        if (testPaper.getTestPaperParts() == null) {
            message = "试卷没有模块信息!";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
            return response;
        }
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        testPaper.setCreator(Long.parseLong(userId));
        boolean res = false;
        res = testPaperService.updateTestpaperAndPart(testPaper);

        if (res) {
            message = "试卷修改成功！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            message = "试卷修改失败！";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }
}
