package zzjz.serviceimpl;

import zzjz.bean.ItemBank;
import zzjz.bean.ItemBankCategory;
import zzjz.service.impl.ItemBankServiceImpl;
import zzjz.util.MyJdbcTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ItemBankServiceImplTest
 * @Description: 题库管理单元测试类
 * @Author guzhenggen
 * @Date: 2016/7/13 8:55
 */
public class ItemBankServiceImplTest {
    private ItemBankServiceImpl itemBankService;
    private MyJdbcTemplate myJdbcTemplate;
    private MyJdbcTemplate myJdbcTemplateMock;
    @Before
    public void setUp() throws Exception {
        itemBankService = new ItemBankServiceImpl();                    //初始化itemBankService类
        myJdbcTemplate = PowerMockito.spy(new MyJdbcTemplate());        //mock myJdbcTemplate类,用于mock无返回值的方法
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);   //mock myJdbcTemplate类,用于mock有返回值的方法
    }

    /**
     * 添加题库分类接口单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemBankCategory() throws Exception {
        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplate;
        //模拟muJdbcTemplate execute方法
        PowerMockito.doNothing().when(myJdbcTemplate,"execute", Mockito.anyString());
        //断言
        Assert.assertEquals("添加成功,正常测试", true, itemBankService.addItemBankCategory(category));
    }

    /**
     * 添加题库分类接口，抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemBankCategoryThrowException() throws Exception {
        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).execute(Mockito.anyString());
        Assert.assertEquals("添加失败,异常测试", false,itemBankService.addItemBankCategory(category));
    }

    /**
     * 删除题库分类接口，单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemBankCategory() throws Exception {
        long categoryId = 1L;
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplate;
        //模拟muJdbcTemplate execute方法
        PowerMockito.doNothing().when(myJdbcTemplate,"execute", Mockito.anyString());
        //断言
        Assert.assertEquals("删除成功,正常测试", true, itemBankService.deleteItemBankCategory(categoryId));
    }

    /**
     * 删除题库分类接口，抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemBankCategoryThrowException() throws Exception {
        long categoryId = 1L;
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).execute(Mockito.anyString());
        Assert.assertEquals("删除失败,异常测试", false,itemBankService.deleteItemBankCategory(categoryId));
    }

    /**
     * 更新题库分类接口，单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItemBankCategory() throws Exception {
        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Assert.assertEquals("更新成功,正常测试", true, itemBankService.updateItemBankCategory(category));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals("更新失败,异常测试", false, itemBankService.updateItemBankCategory(category));
    }

    /**
     * 更新题库分类接口，抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItemBankCategoryThrowException() throws Exception {
        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals("更新失败,异常测试", false,itemBankService.updateItemBankCategory(category));
    }

    /**
     * 获取题库分类列表接口,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBankCategoryList() throws Exception {
        List<ItemBankCategory> categoryList = new ArrayList<ItemBankCategory>();
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(categoryList);
        Assert.assertEquals("获取题库分类,正常测试", categoryList, itemBankService.getItemBankCategoryList());
    }

    /**
     * 获取题库分类信息接口,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBankCategory() throws Exception {
        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(1L);
        List<ItemBankCategory> categoryList = new ArrayList<ItemBankCategory>();
        categoryList.add(category);
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(categoryList);
        Assert.assertEquals("获取分类信息不为空,正常测试", category, itemBankService.getItemBankCategory(category));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals("获取分类信息为空,异常测试", null, itemBankService.getItemBankCategory(category));
    }

    /**
     * 根据分类ID获取题库分类信息接口,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBankCategoryById() throws Exception {
        long categoryId = 1L;
        ItemBankCategory category = new ItemBankCategory();
        category.setCategoryId(categoryId);
        List<ItemBankCategory> categoryList = new ArrayList<ItemBankCategory>();
        categoryList.add(category);
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(categoryList);
        Assert.assertEquals("获取分类信息不为空,正常测试", category, itemBankService.getItemBankCategoryById(categoryId));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals("获取分类信息为空,异常测试", null, itemBankService.getItemBankCategoryById(categoryId));
    }

    /**
     * 添加题库信息,单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemBank() throws Exception {
        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplate;
        //模拟muJdbcTemplate execute方法
        PowerMockito.doNothing().when(myJdbcTemplate,"execute", Mockito.anyString());
        //断言
        Assert.assertEquals("添加成功,正常测试", true, itemBankService.addItemBank(itemBank));
    }

    /**
     * 添加题库信息,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testAddItemBankThrowException() throws Exception {
        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).execute(Mockito.anyString());
        Assert.assertEquals("添加失败,异常测试", false,itemBankService.addItemBank(itemBank));
    }

    /**
     * 删除题库信息,单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemBank() throws Exception {
        long itemBankId = 1L;
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplate;
        //模拟muJdbcTemplate execute方法
        PowerMockito.doNothing().when(myJdbcTemplate,"execute", Mockito.anyString());
        //断言
        Assert.assertEquals("添加成功,正常测试", true, itemBankService.deleteItemBank(itemBankId));
    }

    /**
     * 删除题库信息,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testDeleteItemBankThrowException() throws Exception {
        long itemBankId = 1L;
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).execute(Mockito.anyString());
        Assert.assertEquals("添加失败,异常测试", false,itemBankService.deleteItemBank(itemBankId));
    }

    /**
     * 更新题库信息,单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItemBank() throws Exception {
        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Assert.assertEquals("更新成功,正常测试", true, itemBankService.updateItemBank(itemBank));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals("更新失败,异常测试", false, itemBankService.updateItemBank(itemBank));
    }

    /**
     * 更新题库信息,抛出异常单元测试
     * @throws Exception
     */
    @Test
    public void testUpdateItemBankThrowException() throws Exception {
        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        //myJdbcTemplate赋给itemBankService类
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals("更新失败,异常测试", false,itemBankService.updateItemBank(itemBank));
    }

    /**
     * 获取题库信息列表,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBankList() throws Exception {
        long categoryId = 1L;
        List<ItemBank> itemBankList = new ArrayList<ItemBank>();
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(itemBankList);
        Assert.assertEquals("获取题库信息,正常测试", itemBankList, itemBankService.getItemBankList(categoryId));
    }

    /**
     * 获取题库信息,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBank() throws Exception {
        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(1L);
        List<ItemBank> itemBankList = new ArrayList<ItemBank>();
        itemBankList.add(itemBank);
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(itemBankList);
        Assert.assertEquals("获取题库信息不为空,正常测试", itemBank, itemBankService.getItemBank(itemBank));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals("获取题库信息为空,异常测试", null, itemBankService.getItemBank(itemBank));
    }

    /**
     * 更具分类ID,检索条件,获取题库信息列表,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBankList1() throws Exception {
        long categoryId = 1L;
        String searchName = "java";
        List<ItemBank> itemBankList = new ArrayList<ItemBank>();
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(itemBankList);
        Assert.assertEquals("获取题库信息,正常测试", itemBankList, itemBankService.getItemBankList(categoryId, searchName));
    }

    /**
     * 根据题库ID,获取题库信息,单元测试
     * @throws Exception
     */
    @Test
    public void testGetItemBankById() throws Exception {
        long itemBankId = 1L;
        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankId(itemBankId);
        List<ItemBank> itemBankList = new ArrayList<ItemBank>();
        itemBankList.add(itemBank);
        itemBankService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject(), Mockito.anyLong())).thenReturn(itemBankList);
        Assert.assertEquals("获取题库信息不为空,正常测试", itemBank, itemBankService.getItemBankById(itemBankId));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject(), Mockito.anyLong())).thenReturn(null);
        Assert.assertEquals("获取题库信息为空,异常测试", null, itemBankService.getItemBankById(itemBankId));
    }

}