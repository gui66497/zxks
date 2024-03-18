package zzjz.rest;

import zzjz.bean.*;
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
 * @ClassName: ItemRestTest
 * @Description: 题目管理单元测试类
 * @Author guzhenggen
 * @Date: 2016/7/12 11:15
 */
public class ItemRestTest  {
    private ItemRest itemRest;
    private ItemService itemService;
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
        itemRest = new ItemRest();                                          //初始化itemRest类
        itemService = PowerMockito.mock(ItemService.class);                 //mock itemService类
        servletRequest = PowerMockito.mock(HttpServletRequest.class);       //mock servletRequest类
        headerParams = PowerMockito.mock(MultivaluedMap.class);             //mock headerParams类
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");    //mock headerParams.getFirst("userId")
        itemRest.itemService = itemService;                                 //mock的itemService对象赋值给ItemRest
        itemRest.servletRequest = servletRequest;                           //mock的servletRequest对象赋值给ItemRest
    }

    /**
     * 添加题目rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testAddItem() throws Exception {
        ItemRequest request = new ItemRequest();
        // 断言
        Assert.assertEquals("题目信息为空,异常测试", ResultCode.BAD_REQUEST, itemRest.addItem(request, headers).getResultCode());

        Item item = new Item();
        item.setItemId(1L);
        request.setItem(item);
        // mock添加结果为true
        Mockito.when(itemService.addItem(item)).thenReturn(true);
        // 断言
        Assert.assertEquals("添加成功,正常测试", ResultCode.SUCCESS, itemRest.addItem(request, headers).getResultCode());

        // mock添加结果false
        Mockito.when(itemService.addItem(item)).thenReturn(false);
        // 断言
        Assert.assertEquals("添加失败,正常测试", ResultCode.ERROR, itemRest.addItem(request, headers).getResultCode());
    }

    /**
     * 获取题目列表rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemList() throws Exception {
        ItemRequest request = new ItemRequest();
        Item item = new Item();
        item.setItemId(1L);
        request.setItem(item);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSortColumn("1");
        request.setPaging(pagingEntity);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        // mock获取题目列表为itemList
        Mockito.when(itemService.getItemList(Mockito.anyLong(), (PagingEntity)Mockito.anyObject())).thenReturn(itemList);
        // mock添加题目总数为10
        Mockito.when(itemService.getItemCount(Mockito.anyLong(), (PagingEntity)Mockito.anyObject())).thenReturn(10);
        // 断言
        Assert.assertEquals("分类信息获取成功,正常测试", ResultCode.SUCCESS, itemRest.getItemList(request, headers).getResultCode());

        PagingEntity pagingEntity1 = new PagingEntity();
        pagingEntity1.setSortColumn("1");
        request.setPaging(pagingEntity1);
        // mock获取题目列表为null
        Mockito.when(itemService.getItemList(Mockito.anyLong(), (PagingEntity)Mockito.anyObject())).thenReturn(null);
        // 断言
        Assert.assertEquals("分类信息为空,异常测试", ResultCode.ERROR, itemRest.getItemList(request, headers).getResultCode());
    }

    /**
     * 删除题目rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItem() throws Exception {
        BaseRequest request = new BaseRequest();
        // 断言
        Assert.assertEquals("题目Id为空,异常测试", ResultCode.BAD_REQUEST, itemRest.deleteItem(request, headers).getResultCode());

        request.setId(1L);
        // mock删除结果true
        Mockito.when(itemService.deleteItem(Mockito.anyLong())).thenReturn(true);
        // 断言
        Assert.assertEquals("删除成功,正常测试", ResultCode.SUCCESS, itemRest.deleteItem(request, headers).getResultCode());

        // mock删除结果false
        Mockito.when(itemService.deleteItem(Mockito.anyLong())).thenReturn(false);
        // 断言
        Assert.assertEquals("删除失败,异常测试", ResultCode.ERROR, itemRest.deleteItem(request, headers).getResultCode());
    }

    /**
     * 批量删除题目rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testBatchDeleteItem() throws Exception {
        ItemRequest request = new ItemRequest();
        // 断言
        Assert.assertEquals("题目Id为空,异常测试", ResultCode.BAD_REQUEST, itemRest.batchDeleteItem(request, headers).getResultCode());

        List<Long> itemIDList = new ArrayList<Long>();
        itemIDList.add(1L);
        itemIDList.add(2L);
        request.setItemIDList(itemIDList);
        // mock批量删除结果true
        Mockito.when(itemService.deleteItemByItemIDList((List<Long>)Mockito.anyObject())).thenReturn(true);
        // 断言
        Assert.assertEquals("删除成功,正常测试", ResultCode.SUCCESS, itemRest.batchDeleteItem(request, headers).getResultCode());

        // mock批量删除结果false
        Mockito.when(itemService.deleteItemByItemIDList((List<Long>)Mockito.anyObject())).thenReturn(false);
        // 断言
        Assert.assertEquals("删除失败,异常测试", ResultCode.ERROR, itemRest.batchDeleteItem(request, headers).getResultCode());
    }

    /**
     * 更新题目rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItemInfo() throws Exception {
        ItemRequest request = new ItemRequest();
        // 断言
        Assert.assertEquals("题目Id为空,异常测试", ResultCode.BAD_REQUEST, itemRest.updateItemInfo(request, headers).getResultCode());

        Item item = new Item();
        item.setItemId(1L);
        request.setItem(item);
        // mock更新结果true
        Mockito.when(itemService.updateItem((Item) Mockito.anyObject())).thenReturn(true);
        // 断言
        Assert.assertEquals("更新成功,正常测试", ResultCode.SUCCESS, itemRest.updateItemInfo(request, headers).getResultCode());

        // mock更新结果false
        Mockito.when(itemService.updateItem((Item) Mockito.anyObject())).thenReturn(false);
        // 断言
        Assert.assertEquals("更新失败,异常测试", ResultCode.ERROR, itemRest.updateItemInfo(request, headers).getResultCode());
    }

    /**
     * 获取题目信息rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemInfo() throws Exception {
        BaseRequest request = new BaseRequest();
        // 断言
        Assert.assertEquals("题目Id为空,异常测试", ResultCode.BAD_REQUEST, itemRest.getItemInfo(request, headers).getResultCode());

        request.setId(1L);
        // mock获取结果为new Item()
        Mockito.when(itemService.getItem(Mockito.anyLong())).thenReturn(new Item());
        // 断言
        Assert.assertEquals("获取题目信息成功,正常测试", ResultCode.SUCCESS, itemRest.getItemInfo(request, headers).getResultCode());

        // mock获取结果为null
        Mockito.when(itemService.getItem(Mockito.anyLong())).thenReturn(null);
        // 断言
        Assert.assertEquals("题目信息为空,异常测试", ResultCode.ERROR, itemRest.getItemInfo(request, headers).getResultCode());
    }

    /**
     * 根据题目类型和题库id获取题目rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemByItemTypeAndItemBank() throws Exception {
        // 断言
        Assert.assertEquals("题目Id为空,异常测试", ResultCode.BAD_REQUEST, itemRest.getItemByItemTypeAndItemBank("", headers).getResultCode());

        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item());
        // mock获取题目信息列表为itemList
        Mockito.when(itemService.getItemByItemTypeAndItemBank(Mockito.anyLong(), Mockito.anyInt())).thenReturn(itemList);
        // 断言
        Assert.assertEquals("题目信息获取成功,异常测试", ResultCode.SUCCESS, itemRest.getItemByItemTypeAndItemBank("1_2", headers).getResultCode());
    }

    /**
     * 根据题目类型和题库id获取题目数量rest接口单元测试
     * @throws Exception
     */
    @Test
    public void testCountItemByItemTypeAndItemBank() throws Exception {
        // 断言
        Assert.assertEquals("题目Id为空,异常测试", ResultCode.BAD_REQUEST, itemRest.countItemByItemTypeAndItemBank("", headers).getResultCode());

        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item());
        // mock获取题目数量为1
        Mockito.when(itemService.countItemByItemTypeAndItemBank(Mockito.anyLong(), Mockito.anyInt())).thenReturn(1);
        // 断言
        Assert.assertEquals("题目数量获取成功,异常测试", ResultCode.SUCCESS, itemRest.countItemByItemTypeAndItemBank("1_2", headers).getResultCode());
    }

}