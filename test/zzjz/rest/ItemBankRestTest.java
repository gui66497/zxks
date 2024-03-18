package zzjz.rest;

import zzjz.bean.*;
import zzjz.service.ItemBankService;
import zzjz.service.ItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName: ItemBankRestTest
 * @Description: 题库管理rest单元测试类
 * @Author guzhenggen
 * @Date: 2016/7/13 15:29
 */
public class ItemBankRestTest {
    private ItemService itemService;
    private ItemBankRest itemBankRest;
    private ItemBankService itemBankService;
    private HttpServletRequest servletRequest;
    private MultivaluedMap<String, String> headerParams;
    //实例化headers
    HttpHeaders headers = new HttpHeaders() {
        public MultivaluedMap<String, String> getRequestHeaders() {
            return headerParams;
        }

        public List<String> getRequestHeader(String arg0) {
            return null;
        }

        public MediaType getMediaType() {
            return null;
        }

        public Locale getLanguage() {
            return null;
        }

        public Map<String, Cookie> getCookies() {
            return null;
        }

        public List<MediaType> getAcceptableMediaTypes() {
            return null;
        }

        public List<Locale> getAcceptableLanguages() {
            return null;
        }
    };
    @Before
    public void setUp() throws Exception {
        itemBankRest = new ItemBankRest();                                  //初始化itemRest类
        itemService = PowerMockito.mock(ItemService.class);                 //mock itemService类
        itemBankService = PowerMockito.mock(ItemBankService.class);         //mock itemBankService类
        servletRequest = PowerMockito.mock(HttpServletRequest.class);       //mock servletRequest类
        headerParams = PowerMockito.mock(MultivaluedMap.class);             //mock headerParams类
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");    //mock headerParams.getFirst("userId")
        itemBankRest.itemService = itemService;                             //mock的itemService对象赋值给ItemRest
        itemBankRest.itemBankService = itemBankService;                     //mock的itemBankService对象赋值给ItemRest
        itemBankRest.servletRequest = servletRequest;                       //mock的servletRequest对象赋值给ItemRest
    }

    /**
     * 添加分类信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testAddCategory() throws Exception {
        ItemBankCategoryRequest request = new ItemBankCategoryRequest();
        // 断言
        Assert.assertEquals("分类信息为空,异常测试",ResultCode.BAD_REQUEST, itemBankRest.addCategory(request,headers).getResultCode());

        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        request.setItemBankCategory(category);
        // mock获取分类信息结果为category
        Mockito.when(itemBankService.getItemBankCategory((ItemBankCategory)Mockito.anyObject())).thenReturn(category);
        // 断言
        Assert.assertEquals("分类信息存在,异常测试",ResultCode.ERROR, itemBankRest.addCategory(request,headers).getResultCode());

        // mock获取分类信息结果为null
        Mockito.when(itemBankService.getItemBankCategory((ItemBankCategory)Mockito.anyObject())).thenReturn(null);
        // mock添加结果为true
        Mockito.when(itemBankService.addItemBankCategory((ItemBankCategory) Mockito.anyObject())).thenReturn(true);
        // 断言
        Assert.assertEquals("添加成功,正常测试",ResultCode.SUCCESS, itemBankRest.addCategory(request, headers).getResultCode());

        // mock获取分类信息结果为null
        Mockito.when(itemBankService.getItemBankCategory((ItemBankCategory) Mockito.anyObject())).thenReturn(null);
        // mock添加结果为false
        Mockito.when(itemBankService.addItemBankCategory((ItemBankCategory) Mockito.anyObject())).thenReturn(false);
        // 断言
        Assert.assertEquals("添加失败,异常测试",ResultCode.ERROR, itemBankRest.addCategory(request,headers).getResultCode());
    }

    /**
     * 删除分类信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteCategory() throws Exception {
        BaseRequest request = new BaseRequest();
        // 断言
        Assert.assertEquals("分类ID为空,异常测试",ResultCode.BAD_REQUEST, itemBankRest.deleteCategory(request, headers).getResultCode());

        request.setId(1L);
        // mock获取子分类信息结果为new ItemBankCategory()
        Mockito.when(itemBankService.getItemBankCategoryById(Mockito.anyLong())).thenReturn(new ItemBankCategory());
        // 断言
        Assert.assertEquals("分类信息存在子分类,异常测试",ResultCode.ERROR, itemBankRest.deleteCategory(request, headers).getResultCode());

        // mock获取子分类信息结果为null
        Mockito.when(itemBankService.getItemBankCategoryById(Mockito.anyLong())).thenReturn(null);
        List<ItemBank> itemBankList = new ArrayList<ItemBank>();
        ItemBank itemBank = new ItemBank();
        itemBankList.add(itemBank);
        // mock获取分类下题库信息结果为itemBankList
        Mockito.when(itemBankService.getItemBankList(Mockito.anyLong())).thenReturn(itemBankList);
        // 断言
        Assert.assertEquals("分类下存在题库,异常测试",ResultCode.ERROR, itemBankRest.deleteCategory(request, headers).getResultCode());

        // mock获取子分类信息结果为null
        Mockito.when(itemBankService.getItemBankCategoryById(Mockito.anyLong())).thenReturn(null);
        // mock获取分类下题库信息结果为null
        Mockito.when(itemBankService.getItemBankList(Mockito.anyLong())).thenReturn(null);
        // mock删除结果为true
        Mockito.when(itemBankService.deleteItemBankCategory(Mockito.anyLong())).thenReturn(true);
        // 断言
        Assert.assertEquals("删除成功,正常测试",ResultCode.SUCCESS, itemBankRest.deleteCategory(request, headers).getResultCode());

        // mock获取子分类信息结果为null
        Mockito.when(itemBankService.getItemBankCategoryById(Mockito.anyLong())).thenReturn(null);
        // mock获取分类下题库信息结果为null
        Mockito.when(itemBankService.getItemBankList(Mockito.anyLong())).thenReturn(null);
        // mock删除结果为false
        Mockito.when(itemBankService.deleteItemBankCategory(Mockito.anyLong())).thenReturn(false);
        // 断言
        Assert.assertEquals("删除失败,异常测试",ResultCode.ERROR, itemBankRest.deleteCategory(request, headers).getResultCode());

    }

    /**
     * 更新分类信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateCategory() throws Exception {
        ItemBankCategoryRequest request = new ItemBankCategoryRequest();
        Assert.assertEquals("分类信息为空,异常测试",ResultCode.BAD_REQUEST, itemBankRest.updateCategory(request, headers).getResultCode());

        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        request.setItemBankCategory(category);
        Mockito.when(itemBankService.getItemBankCategory((ItemBankCategory)Mockito.anyObject())).thenReturn(category);
        Assert.assertEquals("分类信息已存在,异常测试",ResultCode.ERROR, itemBankRest.updateCategory(request, headers).getResultCode());

        Mockito.when(itemBankService.getItemBankCategory((ItemBankCategory)Mockito.anyObject())).thenReturn(null);
        Mockito.when(itemBankService.updateItemBankCategory((ItemBankCategory) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("更新成功,正常测试",ResultCode.SUCCESS, itemBankRest.updateCategory(request, headers).getResultCode());

        Mockito.when(itemBankService.getItemBankCategory((ItemBankCategory)Mockito.anyObject())).thenReturn(null);
        Mockito.when(itemBankService.updateItemBankCategory((ItemBankCategory) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("更新失败,异常测试",ResultCode.ERROR, itemBankRest.updateCategory(request, headers).getResultCode());

    }

    /**
     * 获取分类信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testGetCategoryList() throws Exception {
        List<ItemBankCategory> itemBankCategoryList = new ArrayList<ItemBankCategory>();
        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        itemBankCategoryList.add(category);
        Mockito.when(itemBankService.getItemBankCategoryList()).thenReturn(itemBankCategoryList);
        Assert.assertEquals("分类信息获取成功,正常测试",ResultCode.SUCCESS, itemBankRest.getCategoryList(headers).getResultCode());

        Mockito.when(itemBankService.getItemBankCategoryList()).thenReturn(null);
        Assert.assertEquals("分类信息为空,异常测试",ResultCode.ERROR, itemBankRest.getCategoryList(headers).getResultCode());

    }

    /**
     * 添加题库信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemBankInfo() throws Exception {
        ItemBankRequest request = new ItemBankRequest();
        Assert.assertEquals("题库信息为空,异常测试",ResultCode.BAD_REQUEST,itemBankRest.addItemBankInfo(request,headers).getResultCode());

        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        request.setItemBank(itemBank);
        Mockito.when(itemBankService.getItemBank((ItemBank)Mockito.anyObject())).thenReturn(new ItemBank());
        Assert.assertEquals("题库信息已存在,异常测试", ResultCode.ERROR, itemBankRest.addItemBankInfo(request, headers).getResultCode());

        Mockito.when(itemBankService.getItemBank((ItemBank)Mockito.anyObject())).thenReturn(null);
        Mockito.when(itemBankService.addItemBank((ItemBank) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("添加成功,正常测试", ResultCode.SUCCESS, itemBankRest.addItemBankInfo(request, headers).getResultCode());

        Mockito.when(itemBankService.getItemBank((ItemBank)Mockito.anyObject())).thenReturn(null);
        Mockito.when(itemBankService.addItemBank((ItemBank) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("添加失败,异常测试", ResultCode.ERROR, itemBankRest.addItemBankInfo(request, headers).getResultCode());
    }

    /**
     * 删除题库信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemBanInfo() throws Exception {
        BaseRequest request = new BaseRequest();
        Assert.assertEquals("题库Id为空,异常测试", ResultCode.BAD_REQUEST, itemBankRest.deleteItemBanInfo(request, headers).getResultCode());

        request.setId(1L);
        Mockito.when(itemService.getItemCount(Mockito.anyLong())).thenReturn(2);
        Assert.assertEquals("题库下存在词条,异常测试", ResultCode.ERROR, itemBankRest.deleteItemBanInfo(request, headers).getResultCode());

        Mockito.when(itemService.getItemCount(Mockito.anyLong())).thenReturn(0);
        Mockito.when(itemBankService.deleteItemBank(Mockito.anyLong())).thenReturn(true);
        Assert.assertEquals("删除成功,正常测试", ResultCode.SUCCESS, itemBankRest.deleteItemBanInfo(request, headers).getResultCode());

        Mockito.when(itemService.getItemCount(Mockito.anyLong())).thenReturn(0);
        Mockito.when(itemBankService.deleteItemBank(Mockito.anyLong())).thenReturn(false);
        Assert.assertEquals("删除失败,异常测试", ResultCode.ERROR, itemBankRest.deleteItemBanInfo(request, headers).getResultCode());
    }

    /**
     * 更新题库信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItemBankInfo() throws Exception {
        ItemBankRequest request = new ItemBankRequest();
        Assert.assertEquals("题库信息为空,异常测试", ResultCode.BAD_REQUEST, itemBankRest.updateItemBankInfo(request, headers).getResultCode());

        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        request.setItemBank(itemBank);
        Mockito.when(itemBankService.getItemBank((ItemBank)Mockito.anyObject())).thenReturn(new ItemBank());
        Assert.assertEquals("题库信息已存在,异常测试", ResultCode.ERROR, itemBankRest.updateItemBankInfo(request, headers).getResultCode());

        Mockito.when(itemBankService.getItemBank((ItemBank)Mockito.anyObject())).thenReturn(null);
        Mockito.when(itemBankService.updateItemBank((ItemBank) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("添加成功,正常测试", ResultCode.SUCCESS, itemBankRest.updateItemBankInfo(request, headers).getResultCode());

        Mockito.when(itemBankService.getItemBank((ItemBank)Mockito.anyObject())).thenReturn(null);
        Mockito.when(itemBankService.updateItemBank((ItemBank) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("添加失败,异常测试", ResultCode.ERROR, itemBankRest.updateItemBankInfo(request, headers).getResultCode());
    }

    /**
     * 获取题库信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBankInfoList() throws Exception {
        Assert.assertEquals("题库分类Id为空,异常测试", ResultCode.BAD_REQUEST, itemBankRest.getItemBankInfoList(headers).getResultCode());

        Mockito.when(servletRequest.getParameter("id")).thenReturn("1001");
        List<ItemBank> itemBankList = new ArrayList<ItemBank>();
        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        itemBankList.add(itemBank);
        Mockito.when(itemBankService.getItemBankList(Mockito.anyLong())).thenReturn(itemBankList);
        Assert.assertEquals("题库信息获取成功,正常测试", ResultCode.SUCCESS, itemBankRest.getItemBankInfoList(headers).getResultCode());

        Mockito.when(servletRequest.getParameter("id")).thenReturn("1001");
        Mockito.when(itemBankService.getItemBankList(Mockito.anyLong())).thenReturn(null);
        Assert.assertEquals("题库信息为空,异常测试", ResultCode.ERROR, itemBankRest.getItemBankInfoList(headers).getResultCode());

        Mockito.when(servletRequest.getParameter("id")).thenReturn("1001");
        Mockito.when(servletRequest.getParameter("searchName")).thenReturn("java");
        List<ItemBank> itemBankList1 = new ArrayList<ItemBank>();
        ItemBank itemBank1 = new ItemBank();
        itemBank1.setItemBankId(1L);
        itemBankList1.add(itemBank1);
        Mockito.when(itemBankService.getItemBankList(Mockito.anyLong(), Mockito.anyString())).thenReturn(itemBankList1);
        Assert.assertEquals("题库信息获取成功,正常测试", ResultCode.SUCCESS, itemBankRest.getItemBankInfoList(headers).getResultCode());

        Mockito.when(servletRequest.getParameter("id")).thenReturn("1001");
        Mockito.when(servletRequest.getParameter("searchName")).thenReturn("java");
        Mockito.when(itemBankService.getItemBankList(Mockito.anyLong(), Mockito.anyString())).thenReturn(null);
        Assert.assertEquals("题库信息为空,异常测试", ResultCode.ERROR, itemBankRest.getItemBankInfoList(headers).getResultCode());
    }
}