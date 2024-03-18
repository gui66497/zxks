package zzjz.rest;

import com.google.common.collect.Lists;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import zzjz.bean.*;
import zzjz.service.ItemService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.service.TestPaperService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/6/3 16:00
 * @ClassName: ItemRest
 * @Description: 题目相关操作Rest接口类
 */
@Component
@Path("/item")
public class ItemRest {
    private static Logger logger = Logger.getLogger(UserRest.class); //日志

    private static String messItem = "题目Id为空!";

    private static String messItemInfo = "题目信息为空!";

    /**
     * 注入ItemService.
     */
    @Autowired
    public ItemService itemService;

    /**
     * 注入TestPaperService.
     */
    @Autowired
    public TestPaperService testPaperService;

    @Context
    ServletContext context;

    @Context
    HttpServletRequest servletRequest;

    @Context
    HttpServletResponse servletResponse;

    /**
     * 添加题目.
     *
     * @param request 题目实体返回
     * @param headers headers对象
     * @return true or false
     */
    @POST
    @Path("/itemAdd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addItem(ItemRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = ""; //返回信息
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        Item item = request.getItem(); //获取分类信息
        if (item == null) {
            message = messItemInfo;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        item.setCreator(Long.parseLong(userId)); //设置用户Id
        boolean success = itemService.addItem(item); //添加题目信息
        if (success) {
            message = "添加成功!";
            logger.debug(message);
            response.setResultCode(ResultCode.SUCCESS);
            response.setMessage(message);
            return response;
        } else {
            message = "添加失败!";
            logger.debug(message);
            response.setMessage(message);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取题目列表.
     *
     * @param request 题目实体返回
     * @param headers headers对象
     * @return 题目列表
     */
    @POST
    @Path("/itemList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Item> getItemList(ItemRequest request, @Context HttpHeaders headers) {
        BaseResponse<Item> response = new BaseResponse<Item>();
        long itemBankId = request.getItem().getItemBankId();
        //定义列名
        String[] cols = {"", "", "ITEM_TITLE", "ITEM_TYPE", "UPDATE_TIME", ""}; //字段名称，对应页面表格
        PagingEntity pagingEntity = request.getPaging();
        String sortIndex = pagingEntity.getSortColumn(); //排序字段名称对应的位置
        pagingEntity.setSortColumn(cols[Integer.parseInt(sortIndex)]); //修改为排序的字段名称
        //获取分类信息列表
        List<Item> itemList = itemService.getItemList(itemBankId, pagingEntity);
        int count = 0;
        if (itemList != null && itemList.size() > 0) {
            logger.debug("分类信息获取成功!");
            //获取总条数
            count = itemService.getItemCount(itemBankId, pagingEntity);
            logger.debug("总页数:" + count);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug("分类信息为空!");
            response.setResultCode(ResultCode.ERROR);
        }
        response.setData(itemList);
        response.setiTotalRecords(count);
        response.setiTotalDisplayRecords(count);
        return response;
    }

    /**
     * 删除题目.
     *
     * @param request BaseRequest
     * @param headers headers对象
     * @return true or false
     */
    @DELETE
    @Path("/itemDelete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteItem(BaseRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message; //返回信息
        Long itemId = request.getId();
        if (itemId == null) {
            message = messItem;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        List<TestPaper> relatedTestPapers = testPaperService.gettestpaperByItemId(itemId);
        if (relatedTestPapers.size() > 0) {
            List<String> testPaperNames = Lists.newArrayList();
            for (TestPaper testPaper : relatedTestPapers) {
                testPaperNames.add(testPaper.getTestpaperName());
            }
            message = "该题目已被试卷引用，无法删除，请核实！";
            response.setData(testPaperNames);
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success = itemService.deleteItem(itemId); //删除题目信息
        if (success) {
            message = "删除成功!";
            logger.debug(message);
            response.setResultCode(ResultCode.SUCCESS);
            response.setMessage(message);
            return response;
        } else {
            message = "删除失败!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
        }
        return response;
    }

    /**
     * 题目批量删除.
     *
     * @param request 题目实体返回
     * @param headers headers对象
     * @return true or false
     */
    @DELETE
    @Path("/itemBatchDelete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> batchDeleteItem(ItemRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message; //返回信息
        List<Long> itemIDList = request.getItemIDList();
        if (itemIDList == null || itemIDList.size() == 0) {
            message = messItem;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        List<String> itemNames = Lists.newArrayList();
        for (Long itemId : itemIDList) {
            List<TestPaper> relatedTestPapers = testPaperService.gettestpaperByItemId(itemId);
            if (relatedTestPapers.size() > 0) {
                Item item = itemService.getItem(itemId);
                itemNames.add(item.getItemTitle());
            }
        }
        if (itemNames.size() > 0) {
            message = "以下题目被试卷引用，无法删除！</br>";
            message += StringUtils.join(itemNames, "</br></br>");
            logger.debug(message);
            response.setData(itemNames);
            response.setResultCode(ResultCode.RELATED);
            response.setMessage(message);
            return response;
        }

        boolean success = itemService.deleteItemByItemIDList(itemIDList);
        //删除题目信息
        if (success) {
            message = "删除成功!";
            logger.debug(message);
            response.setResultCode(ResultCode.SUCCESS);
            response.setMessage(message);
            return response;
        } else {
            message = "删除失败!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
        }
        return response;
    }

    /**
     * 更新题目.
     *
     * @param request 题目实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/itemUpdate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateItemInfo(ItemRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = ""; //返回信息
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        Item item = request.getItem(); //获取题库信息
        if (item == null) {
            message = messItemInfo;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        List<TestPaper> relatedTestPapers = testPaperService.gettestpaperByItemId(item.getItemId());
        if (relatedTestPapers.size() > 0) {
            message = "该题目已被试卷引用，无法修改，请核实！";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        item.setCreator(Long.parseLong(userId)); //设置用户Id
        boolean success = itemService.updateItem(item); //更新题目信息
        if (success) {
            message = "更新成功!";
            logger.debug(message);
            response.setResultCode(ResultCode.SUCCESS);
            response.setMessage(message);
            return response;
        } else {
            message = "更新失败!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
        }
        return response;
    }

    /**
     * 根据题目id获取关联的试卷列表.
     * @param request request
     * @param headers headers
     * @return 试卷信息
     */
    @POST
    @Path("/getTestPaperByItemId")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<TestPaper> getTestPaperByItemId(BaseRequest request, @Context HttpHeaders headers) {
        BaseResponse<TestPaper> response = new BaseResponse<TestPaper>();
        String message; //返回信息
        Long itemId = request.getId();
        if (itemId == null) {
            message = messItem;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        List<TestPaper> testPapers = testPaperService.gettestpaperByItemId(itemId);
        response.setMessage("获取试卷信息成功!");
        response.setData(testPapers);
        response.setResultCode(ResultCode.SUCCESS);
        return response;
    }

    /**
     * 获取题目信息.
     *
     * @param request BaseRequest
     * @param headers headers对象
     * @return 题目信息对象
     */
    @POST
    @Path("/itemInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Item> getItemInfo(BaseRequest request, @Context HttpHeaders headers) {
        BaseResponse<Item> response = new BaseResponse<Item>();
        String message; //返回信息
        Long itemId = request.getId();
        if (itemId == null) {
            message = messItem;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        Item item = itemService.getItem(itemId);
        if (item != null) {
            List<Item> itemList = new ArrayList<Item>();
            itemList.add(item);
            logger.debug("获取题目信息成功!");
            response.setData(itemList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(messItemInfo);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 根据题目类型和题库id获取题目.
     *
     * @param param   参数
     * @param headers headers对象
     * @return 题目信息列表
     * @author 房桂堂
     * @Date 2016/6/12 16:14
     */

    @GET
    @Path("/getItemByItemTypeAndItemBank/{param}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Item> getItemByItemTypeAndItemBank(@PathParam("param") String param,
                                                           @Context HttpHeaders headers) {
        BaseResponse<Item> response = new BaseResponse<Item>();
        String message; //返回信息
        if (!StringUtils.isNotBlank(param)) {
            message = messItem;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        String[] params = param.split("_");
        Long itemBankId = Long.parseLong(params[0]);
        Integer itemBankType = Integer.parseInt(params[1]);

        List<Item> itemList = itemService.getItemByItemTypeAndItemBank(itemBankId, itemBankType);
        message = "题目信息获取成功";
        logger.debug(message);
        response.setResultCode(ResultCode.SUCCESS);
        response.setData(itemList);
        response.setMessage(message);
        return response;
    }


    /**
     * 根据题目类型和题库id获取题目.
     *
     * @param itemRequest 参数
     * @param headers     headers对象
     * @return 题目信息列表
     * @author 房桂堂
     * @Date 2016/6/12 16:14
     */

    @POST
    @Path("/getItemByItemTypeAndItemBankIds")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Item> getItemByItemTypeAndItemBankIds(ItemRequest itemRequest,
                                                              @Context HttpHeaders headers) {
        BaseResponse<Item> response = new BaseResponse<Item>();
        String message; //返回信息
        if (itemRequest == null) {
            message = messItem;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }

        String[] cols = {"t1.ITEM_ID", "t1.ITEM_TYPE", "t1.ANSWER"}; //字段名称，对应页面表格
        PagingEntity pagingEntity = itemRequest.getPaging();
        String sortIndex = pagingEntity.getSortColumn(); //排序字段名称对应的位置
        if (StringUtils.isNotBlank(sortIndex)) {
            pagingEntity.setSortColumn(cols[Integer.parseInt(sortIndex)]); //修改为排序的字段名称
        }
        String itemBankIds = itemRequest.getItem().getItemBankIds();
        int itemBankType = itemRequest.getItem().getItemType();

        List<Item> itemList = itemService.getItemByItemTypeAndItemBankIds(itemBankIds, itemBankType, pagingEntity);
        int count = itemService.countByItemTypeAndItemBankIds(itemBankIds, itemBankType, pagingEntity);
        message = "题目信息获取成功";
        logger.debug(message);
        response.setResultCode(ResultCode.SUCCESS);
        response.setiTotalRecords(count);
        response.setiTotalDisplayRecords(count);
        response.setData(itemList);
        response.setMessage(message);
        return response;
    }

    /**
     * 根据题目类型和题库id获取题目数量.
     *
     * @param param   参数
     * @param headers headers对象
     * @return list对象
     * @Author 房桂堂
     * @Date 2016/6/12 16:14
     */
    @GET
    @Path("/countItemByItemTypeAndItemBank/{param}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Integer> countItemByItemTypeAndItemBank(@PathParam("param") String param,
                                                                @Context HttpHeaders headers) {
        BaseResponse<Integer> response = new BaseResponse<Integer>();
        String message; //返回信息
        if (!StringUtils.isNotBlank(param)) {
            message = messItem;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        String[] params = param.split("_");
        Long itemBankId = Long.parseLong(params[0]);
        Integer itemBankType = Integer.parseInt(params[1]);

        Integer itemCount = itemService.countItemByItemTypeAndItemBank(itemBankId, itemBankType);
        List<Integer> resList = Lists.newArrayList();
        resList.add(itemCount);
        message = "题目数量获取成功";
        logger.debug(message);
        response.setResultCode(ResultCode.SUCCESS);
        response.setData(resList);
        response.setMessage(message);
        return response;
    }

    /**
     * 获取题目总数列表.
     *
     * @param request 题目实体对象
     * @param headers headers对象
     * @return 题目列表
     */
    @POST
    @Path("/itemCountList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Item> getItemCountList(ItemRequest request, @Context HttpHeaders headers) {
        BaseResponse<Item> response = new BaseResponse<Item>();
        long itemBankId = request.getItem().getItemBankId();
        List<Item> itemList = itemService.getItemListByItemBankId(itemBankId);
        if (itemList != null) {
            response.setMessage("获取题目列表成功！");
            response.setData(itemList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            response.setMessage("获取题目列表失败！");
            response.setResultCode(ResultCode.BAD_REQUEST);
        }
        return response;
    }

    /**
     * 一次上传单个文件到服务器和数据库
     * @param fileInputStream 输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @param userId 用户
     * @param bankId 题库ID
     * @return 结果
     */
    @POST
    @Path("/{userId}/{bankId}/batchUpload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> batchUpload(@FormDataParam("file") InputStream fileInputStream,
                                            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
                                            @PathParam("userId") Long userId,@PathParam("bankId") Long bankId) {
        BaseResponse<String> response = new BaseResponse<String>();

        ImportParams importParams = new ImportParams();
        List<ItemExcel> res = null;
        try{
            res = ExcelImportUtil.importExcel(fileInputStream, ItemExcel.class, importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (ItemExcel itemExcel : res){
            if(itemExcel.getType()==null||itemExcel.getOptionA()==null||itemExcel.getOptionB()==null||itemExcel.getAnswer()==null){
                continue;
            }
            Item item = new Item();
            Long id = new Date().getTime();
            item.setItemId(id);
            item.setCreator(userId);
            item.setItemBankId(bankId);
            item.setItemTitle(itemExcel.getTitle()==null?"":itemExcel.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;"));
            item.setItemCode(itemExcel.getCode()==null?"":itemExcel.getCode().replaceAll("<","&lt;").replaceAll(">","&gt;"));
            String answer = itemExcel.getAnswer().replaceAll(" ","")
                    .replaceAll("A","1")
                    .replaceAll("B","2")
                    .replaceAll("C","3")
                    .replaceAll("D","4")
                    .replaceAll("E","5")
                    .replaceAll("F","6")
                    .replaceAll("G","7")
                    .replaceAll("H","8");
            StringBuffer sb = new StringBuffer("");
            if(answer.length()>1){
                for (int x = 0; x < answer.length(); x++) {
                    char ch = answer.charAt(x);
                    sb.append(ch).append(",");
                }
                answer = sb.toString().substring(0,sb.toString().length()-1);
            }
            item.setAnswer(answer);
            item.setAnswerResolution(itemExcel.getAnalysis()==null?"":itemExcel.getAnalysis());
            item.setItemType(Integer.parseInt(itemExcel.getType()));
            List<ItemOption> list = new ArrayList<ItemOption>();
            String [] arr = {itemExcel.getOptionA(),itemExcel.getOptionB(),itemExcel.getOptionC(),itemExcel.getOptionD(),itemExcel.getOptionE(),itemExcel.getOptionF(),itemExcel.getOptionG(),itemExcel.getOptionH()};
            ItemOption itemOption = null;
            int optionId = 0;
            for(int i=0; i<arr.length; i++){
                if(arr[i]!=null){
                    itemOption = new ItemOption();
                    optionId ++;
                    itemOption.setItemId(id);
                    itemOption.setOptionId(optionId);
                    itemOption.setContent(arr[i].replaceAll("<","&lt;").replaceAll(">","&gt;"));
                    itemOption.setCreator(userId);
                    list.add(itemOption);
                }
            }
            ItemOption[] options = new ItemOption[optionId];
            for(int i=0;i<optionId;i++){
                options[i] = list.get(i);
            }
            item.setOptions(options);
            itemService.addItem(item);
        }

        response.setMessage("success");
        return response;
    }

}
