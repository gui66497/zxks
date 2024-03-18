package zzjz.rest;

import zzjz.bean.ItemBank;
import zzjz.bean.ItemBankCategory;
import zzjz.bean.ItemBankRequest;
import zzjz.bean.ItemBankCategoryRequest;
import zzjz.bean.BaseRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.service.ItemBankService;
import zzjz.service.ItemService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/5/30 14:34
 * @ClassName: ItemBankRest
 * @Description: 题库相关操作Rest接口类
 */
@Component
@Path("/itemBank")
public class ItemBankRest {
    private static Logger logger = Logger.getLogger(UserRest.class); //日志

    private static String str = "userId";

    private static String strMess = "分类信息为空!";

    private static String messBank = "题库信息为空!";

    /**
     * 注入ItemBankService.
     */
    @Autowired
    public ItemBankService itemBankService;

    /**
     * 注入ItemService.
     */
    @Autowired
    public ItemService itemService;

    @Context
    ServletContext context;

    @Context
    HttpServletRequest servletRequest;

    /**
     * 添加题库分类.
     *
     * @param request 题库实体返回
     * @param headers headers对象
     * @return true or false
     */
    @POST
    @Path("/category")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addCategory(ItemBankCategoryRequest request,
                                            @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = ""; //返回信息
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(str);
        ItemBankCategory itemBankCategory = request.getItemBankCategory(); //获取分类信息

        if (itemBankCategory == null) {
            message = strMess;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        long categoryId = new Date().getTime(); //时间戳，即为分类Id
        itemBankCategory.setCategoryId(categoryId); //设置分类Id
        itemBankCategory.setCreator(Long.parseLong(userId)); //设置用户Id
        ItemBankCategory category = itemBankService.getItemBankCategory(itemBankCategory);

        if (category != null) {
            message = "分类信息已存在!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success = itemBankService.addItemBankCategory(itemBankCategory); //添加题库分类信息
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
     * 删除题库分类.
     *
     * @param request BaseRequest
     * @param headers headers对象
     * @return true or false
     */
    @DELETE
    @Path("/categoryDelete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteCategory(BaseRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = ""; //返回信息
        Long categoryId = request.getId();
        if (categoryId == null) {
            message = strMess;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        ItemBankCategory category = itemBankService.getItemBankCategoryById(categoryId);
        if (category != null) {
            message = "该分类信息存在子分类!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        //分类下存在题库
        List<ItemBank> itemBankList = itemBankService.getItemBankList(categoryId);
        if (itemBankList != null && itemBankList.size() > 0) {
            message = "该分类下存在题库!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success = itemBankService.deleteItemBankCategory(categoryId); //删除题库分类信息
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
     * 更新题库分类信息.
     *
     * @param request 题库分类实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/categoryUpdate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateCategory(ItemBankCategoryRequest request,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = ""; //返回信息
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(str);
        ItemBankCategory itemBankCategory = request.getItemBankCategory(); //获取分类信息
        if (itemBankCategory == null) {
            message = strMess;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        itemBankCategory.setCreator(Long.parseLong(userId)); //设置用户Id
        ItemBankCategory category = itemBankService.getItemBankCategory(itemBankCategory);
        if (category != null) {
            message = "分类信息已存在!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success = itemBankService.updateItemBankCategory(itemBankCategory); //更新题库分类信息
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
     * 获取题库分类列表.
     *
     * @param headers headers对象
     * @return 题库分类列表
     */
    @GET
    @Path("/categoryList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<ItemBankCategory> getCategoryList(@Context HttpHeaders headers) {
        BaseResponse<ItemBankCategory> response = new BaseResponse<ItemBankCategory>();
        //获取分类信息列表
        List<ItemBankCategory> itemBankCategoryList = itemBankService.getItemBankCategoryList();
        if (itemBankCategoryList != null && itemBankCategoryList.size() > 0) {
            logger.debug("分类信息获取成功!");
            response.setData(itemBankCategoryList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(strMess);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 添加题库.
     *
     * @param request 题库实体返回
     * @param headers headers对象
     * @return true or false
     */
    @POST
    @Path("/infoAdd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addItemBankInfo(ItemBankRequest request,
                                                @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = ""; //返回信息
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(str);
        ItemBank itemBank = request.getItemBank(); //获取分类信息
        if (itemBank == null) {
            message = messBank;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        long itemBankId = new Date().getTime(); //时间戳，即为分类Id
        itemBank.setItemBankId(itemBankId); //设置分类Id
        itemBank.setCreator(Long.parseLong(userId)); //设置用户Id
        ItemBank itemBankTemp = itemBankService.getItemBank(itemBank); //获取题库信息
        if (itemBankTemp != null) {
            message = "题库信息已存在!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success = itemBankService.addItemBank(itemBank); //添加题库信息
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
     * 删除题库.
     *
     * @param request BaseRequest
     * @param headers headers对象
     * @return true or false
     */
    @DELETE
    @Path("/infoDelete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteItemBanInfo(BaseRequest request,
                                                  @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message; //返回信息
        Long itemBankId = request.getId();
        if (itemBankId == null) {
            message = "题库Id为空!";
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        //题库下存在题目
        int itemCount = itemService.getItemCount(itemBankId);
        if (itemCount > 0) {
            message = "该题库下存在题目!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success = itemBankService.deleteItemBank(itemBankId); //删除题库信息
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
     * 更新题库信息.
     *
     * @param request 题库实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/infoUpdate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateItemBankInfo(ItemBankRequest request,
                                                   @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String message = ""; //返回信息
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst(str);
        ItemBank itemBank = request.getItemBank(); //获取题库信息
        if (itemBank == null) {
            message = messBank;
            logger.debug(message);
            response.setResultCode(ResultCode.BAD_REQUEST);
            response.setMessage(message);
            return response;
        }
        itemBank.setCreator(Long.parseLong(userId)); //设置用户Id
        ItemBank itemBankTemp = itemBankService.getItemBank(itemBank); //根据题库信息查询改题库信息是否存在
        if (itemBankTemp != null) {
            message = "题库信息已存在!";
            logger.debug(message);
            response.setResultCode(ResultCode.ERROR);
            response.setMessage(message);
            return response;
        }
        boolean success = itemBankService.updateItemBank(itemBank); //更新题库信息
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
     * 获取题库信息列表.
     *
     * @param headers headers对象
     * @return 题库信息列表
     * @throws UnsupportedEncodingException 异常
     */
    @GET
    @Path("/infoList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<ItemBank> getItemBankInfoList(@Context HttpHeaders headers)
            throws UnsupportedEncodingException {
        BaseResponse<ItemBank> response = new BaseResponse<ItemBank>();
        String message = ""; //返回信息
        String categoryId = servletRequest.getParameter("id"); //分类id
        if (StringUtils.isBlank(categoryId)) {
            categoryId = "0";
        }
        //获取题库信息列表
        List<ItemBank> itemBankList;
        String searchName = servletRequest.getParameter("searchName"); //检索条件
        if (StringUtils.isNotBlank(searchName)) {
            searchName = new String(searchName.getBytes("iso-8859-1"), "utf-8"); //检索条件
            searchName = StringUtil.zhuanyi(searchName);
            //根据分类ID和检索条件查询题库信息
            itemBankList = itemBankService.getItemBankList(
                    Long.parseLong(categoryId), searchName);
        } else {
            itemBankList =
                    itemBankService.getItemBankList(Long.parseLong(categoryId)); //根据分类ID查询题库信息
        }
        if (itemBankList != null && itemBankList.size() > 0) {
            logger.debug("题库信息获取成功!");
            response.setData(itemBankList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(messBank);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

}
