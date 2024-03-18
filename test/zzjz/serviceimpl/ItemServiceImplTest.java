package zzjz.serviceimpl;

import zzjz.bean.Item;
import zzjz.bean.ItemOption;
import zzjz.bean.PagingEntity;
import zzjz.bean.ResultInfo;
import zzjz.service.impl.ItemServiceImpl;
import zzjz.util.MyJdbcTemplate;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: ItemServiceImplTest
 * @Description: 题目管理Service单元测试
 * @Author guzhenggen
 * @Date: 2016/7/12 11:15
 */
public class ItemServiceImplTest {
    private ItemServiceImpl itemService;
    private MyJdbcTemplate myJdbcTemplate;
    private MyJdbcTemplate myJdbcTemplateMock;

    @Before
    public void setUp() throws Exception {
        itemService = new ItemServiceImpl();                            //初始化itemRest类
        myJdbcTemplate = PowerMockito.spy(new MyJdbcTemplate());        //mock myJdbcTemplate类,用于mock无返回值的方法
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);   //mock myJdbcTemplate类,用于mock有返回值的方法
    }

    /**
     * 添加题目接口单元测试
     * @throws Exception
     */
    @Test
    public void testAddItem() throws Exception {
        Item item = new Item();
        item.setItemId(1466387389099L);
        //myJdbcTemplate赋给itemService类
        itemService.myJdbcTemplate = myJdbcTemplate;
        //模拟muJdbcTemplate execute方法
        PowerMockito.doNothing().when(myJdbcTemplate,"execute", Mockito.anyString());
        //断言
        Assert.assertEquals("添加题目",false, itemService.addItem(item));
    }

    /**
     * 添加题目接口,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemThrowException() throws Exception {
        Item item = new Item();
        item.setItemId(1466387389099L);
        itemService.myJdbcTemplate = myJdbcTemplate;
        Mockito.doThrow(Exception.class).when(myJdbcTemplate).execute(Mockito.anyString());
        Assert.assertEquals("添加题目,异常测试",false,itemService.addItem(item));
    }

    /**
     * 删除题目接口单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItem() throws Exception {
        long itemId = 1466387389099L;
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Assert.assertEquals("删除题目成功,正常测试",true, itemService.deleteItem(itemId));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals("删除题目失败,正常测试",false, itemService.deleteItem(itemId));
    }

    /**
     * 删除题目接口,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemThrowException() throws Exception {
        long itemId = 1466387389099L;
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals("删除题目失败,异常测试",false,itemService.deleteItem(itemId));
    }

    /**
     * 更新题目接口单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItem() throws Exception {
        Item item = new Item();
        item.setItemId(1466387389099L);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Assert.assertEquals("更新题目成功,正常测试",true, itemService.updateItem(item));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals("更新题目失败,正常测试",false, itemService.updateItem(item));
    }

    /**
     * 更新题目接口,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItemThrowException() throws Exception {
        Item item = new Item();
        item.setItemId(1466387389099L);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals("更新题目,异常测试",false,itemService.updateItem(item));
    }

    /**
     * 获取题目接口单元测试
     * @throws Exception
     */
    @Test
    public void testGetItem() throws Exception {
        long itemId = 1466387389099L;
        Item item = new Item();
        item.setItemId(itemId);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(itemList);
        Assert.assertEquals("获取题目,正常测试",item, itemService.getItem(itemId));
    }

    /**
     * 获取题目列表接口单元测试,含分页
     * @throws Exception
     */
    @Test
    public void testGetItemList() throws Exception {
        long itemBankId = 1001;
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setStartIndex(1);
        pagingEntity.setPageSize(10);
        pagingEntity.setSortColumn("ITEM_ID");
        pagingEntity.setSortDir("DESC");
        pagingEntity.setSearchValue("java");

        long itemId = 1466387389099L;
        Item item = new Item();
        item.setItemId(itemId);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(itemList);
        Assert.assertEquals("获取题目列表,正常测试", itemList, itemService.getItemList(itemBankId,pagingEntity));
    }

    /**
     * 获取题目列表接口单元测试,不含分页
     * @throws Exception
     */
    @Test
    public void testGetItemList1() throws Exception {
        long itemId = 1466387389099L;
        String searchName = "C#";
        Item item = new Item();
        item.setItemId(itemId);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(itemList);
        Assert.assertEquals("获取题目列表,正常测试", itemList, itemService.getItemList(itemId,searchName));
    }

    /**
     * 添加题目选项单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemOptions() throws Exception {
        ItemOption option = new ItemOption();
        option.setOptionId(1001L);
        ItemOption[] options = {option};
        int[] insertCounts = {1};
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(), (BatchPreparedStatementSetter)Mockito.anyObject())).thenReturn(insertCounts);
        Assert.assertEquals("添加成功,正常测试", true, itemService.addItemOptions(options));
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(), (BatchPreparedStatementSetter)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals("添加失败,正常测试", false, itemService.addItemOptions(options));
    }

    /**
     * 添加题目选项,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemOptionsThrowException() throws Exception {
        ItemOption option = new ItemOption();
        option.setOptionId(1001L);
        ItemOption[] options = {option};
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).batchUpdate(Mockito.anyString(), (BatchPreparedStatementSetter)Mockito.anyObject());
        Assert.assertEquals("添加题目选项,异常测试", false, itemService.addItemOptions(options));
    }

    /**
     * 获取题目选项单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemOptions() throws Exception {
        long itemId = 1466387389099L;
        ItemOption option = new ItemOption();
        option.setOptionId(1001L);
        List<ItemOption> options = new ArrayList<ItemOption>();
        options.add(option);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper) Mockito.anyObject())).thenReturn(options);
        Assert.assertEquals("获取题目选项,正常测试", options, itemService.getItemOptions(itemId));
    }

    /**
     * 删除题目选项单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemOption() throws Exception {
        long itemId = 1466387389099L;
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Assert.assertEquals("删除成功,正常测试", true, itemService.deleteItemOption(itemId));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals("删除失败,正常测试", false, itemService.deleteItemOption(itemId));
    }

    /**
     * 删除题目选项,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemOptionThrowException() throws Exception {
        long itemId = 1466387389099L;
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals("删除选项,异常测试", false, itemService.deleteItemOption(itemId));
    }

    /**
     * 获取题目总数单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemCount() throws Exception {
        long itemBankId = 1001;
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSearchValue("java");
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(1);
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        String searchSql = "";
        if(StringUtils.isNotBlank(searchValue)){
            searchSql = " AND ITEM_TITLE LIKE '%"+searchValue+"%'";
        }
        String sql = "SELECT COUNT(1) FROM tb_item WHERE ITEMBANK_ID="+itemBankId+" AND DELETE_FLAG=0 "+searchSql;
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.queryForList(sql, Integer.class)).thenReturn(counts);
        Assert.assertEquals("获取题目数为1,正常测试", 1, itemService.getItemCount(itemBankId,pagingEntity));
        PowerMockito.when(myJdbcTemplateMock.queryForList(sql, Integer.class)).thenReturn(null);
        Assert.assertEquals("获取题目数为0,正常测试", 0, itemService.getItemCount(itemBankId,pagingEntity));
    }

    /**
     * 根据题库ID，获取题目总数单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemCount1() throws Exception {
        long itemBankId = 1001;
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(1);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        String sql = "SELECT COUNT(1) FROM tb_item WHERE ITEMBANK_ID="+itemBankId+" AND DELETE_FLAG=0";
        PowerMockito.when(myJdbcTemplateMock.queryForList(sql, Integer.class)).thenReturn(counts);
        Assert.assertEquals("获取题目数为1,正常测试", 1, itemService.getItemCount(itemBankId));
        PowerMockito.when(myJdbcTemplateMock.queryForList(sql, Integer.class)).thenReturn(null);
        Assert.assertEquals("获取题目数为0,正常测试", 0, itemService.getItemCount(itemBankId));
    }

    /**
     * 根据题目ID列表删除题目，单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemByItemIDList() throws Exception {
        Assert.assertEquals(false, itemService.deleteItemByItemIDList(null));
        List<Long> itemIDList = new ArrayList<Long>();
        itemIDList.add(1L);
        itemIDList.add(2L);
        int[] updateCounts = {1,2};
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(),(BatchPreparedStatementSetter)Mockito.anyObject())).thenReturn(updateCounts);
        Assert.assertEquals("删除成功,正常测试", true, itemService.deleteItemByItemIDList(itemIDList));
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(),(BatchPreparedStatementSetter)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals("删除失败,异常测试", false, itemService.deleteItemByItemIDList(itemIDList));
    }

    /**
     * 根据题目ID列表删除题目，抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemByItemIDListThrowException() throws Exception {
        List<Long> itemIDList = new ArrayList<Long>();
        itemIDList.add(1L);
        itemIDList.add(2L);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).batchUpdate(Mockito.anyString(), (BatchPreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals("删除失败,异常测试", false, itemService.deleteItemByItemIDList(itemIDList));
    }

    /**
     * 根据试卷的部分Id获取题目,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemByPartId() throws Exception {
        long partId = 1L;
        Item item = new Item();
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(itemList);
        Assert.assertEquals("获取题目,正常测试", itemList, itemService.getItemByPartId(partId));
    }

    /**
     * 根据题目类型和题库ID获取题目,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemByItemTypeAndItemBank() throws Exception {
        Long itemBankId = new Long(1L);
        Integer itemBankType = new Integer(1);
        Item item = new Item();
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject())).thenReturn(itemList);
        Assert.assertEquals("获取题目,正常测试", itemList, itemService.getItemByItemTypeAndItemBank(itemBankId,itemBankType));
    }

    /**
     * 根据试卷的部分Id获取题目,单元测试,带分页
     * @throws Exception
     */
    @Test
    public void testGetItemByItemTypeAndItemBank1() throws Exception {
        Long itemBankId = new Long(1L);
        Integer itemBankType = new Integer(1);
        Integer quantity = new Integer(1);
        Item item = new Item();
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject())).thenReturn(itemList);
        Assert.assertEquals("获取题目,正常测试", itemList, itemService.getItemByItemTypeAndItemBank(itemBankId,itemBankType,quantity));
    }

    /**
     * 根据题目类型和题库ID获取题目数量,单元测试
     * @throws Exception
     */
    @Test
    public void testCountItemByItemTypeAndItemBank() throws Exception {
        Long itemBankId = new Long(1L);
        Integer itemBankType = new Integer(1);
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        String sql = "SELECT COUNT(*) FROM tb_item WHERE ITEMBANK_ID = "+itemBankId+" AND ITEM_TYPE = "+itemBankType+" AND DELETE_FLAG=0";
        PowerMockito.when(myJdbcTemplateMock.queryForObject(sql, Integer.class)).thenReturn(1);
        Assert.assertEquals("获取题目数,正常测试", new Integer(1), itemService.countItemByItemTypeAndItemBank(itemBankId, itemBankType));
    }

    /**
     * 根据试卷的部分ID和成绩表ID,获取答题结果,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemByPartId1() throws Exception {
        long partId = 1L;
        long achievementId = 2L;
        List<ResultInfo> resultInfoList = new ArrayList<ResultInfo>();
        itemService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.queryForObject(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(resultInfoList);
        Assert.assertEquals("获取结果,正常测试", resultInfoList, itemService.getItemByPartId(partId, achievementId));
    }
}