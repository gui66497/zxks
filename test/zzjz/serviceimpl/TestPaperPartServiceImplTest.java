package zzjz.serviceimpl;

import zzjz.bean.Item;
import zzjz.bean.ItemOption;
import zzjz.bean.ResultInfo;
import zzjz.bean.TestPaperPart;
import zzjz.service.ItemService;
import zzjz.service.impl.TestPaperPartServiceImpl;
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
 * @ClassName: TestPaperPartServiceImplTest
 * @Description: 试卷每部分的信息单元测试类
 * @Author guzhenggen
 * @Date: 2016/7/13 14:29
 */
public class TestPaperPartServiceImplTest {
    private TestPaperPartServiceImpl testPaperPartService;
    private ItemService itemService;
    private MyJdbcTemplate myJdbcTemplateMock;

    @Before
    public void setUp() throws Exception {
        testPaperPartService = new TestPaperPartServiceImpl();              //mock testPaperPartService类
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);       //mock myJdbcTemplate类,用于mock有返回值的方法
        itemService = PowerMockito.mock(ItemService.class);                 //mock itemService类
    }

    /**
     * 根据试卷ID获取每部分信息,单元测试
     * @throws Exception
     */
    @Test
    public void testGetPartInfoListByTestPaperId() throws Exception {
        long testPaperId = 1L;
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(1L);
        List<TestPaperPart> testPaperPartList = new ArrayList<TestPaperPart>();
        testPaperPartList.add(testPaperPart);
        //myJdbcTemplate赋给testPaperPartService类
        testPaperPartService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(testPaperPartList);
        testPaperPartService.itemService = itemService;
        Item item = new Item();
        Long itemId = 2001L;
        item.setItemId(itemId);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        PowerMockito.when(itemService.getItemByPartId(Mockito.anyLong())).thenReturn(itemList);
        List<ItemOption> optionList = new ArrayList<ItemOption>();
        ItemOption option = new ItemOption();
        option.setOptionId(3001L);
        PowerMockito.when(itemService.getItemOptions(Mockito.anyLong())).thenReturn(optionList);
        Assert.assertEquals("获取每部分信息,正常测试", testPaperPartList, testPaperPartService.getPartInfoListByTestPaperId(testPaperId));
    }

    /**
     * 根据考试信息ID获取每部分信息,单元测试
     * @throws Exception
     */
    @Test
    public void testGetPartInfoListByTestInfoId() throws Exception {
        long testInfoId = 1L;
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(1L);
        List<TestPaperPart> testPaperPartList = new ArrayList<TestPaperPart>();
        testPaperPartList.add(testPaperPart);
        //myJdbcTemplate赋给testPaperPartService类
        testPaperPartService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(testPaperPartList);
        testPaperPartService.itemService = itemService;
        Item item = new Item();
        Long itemId = 2001L;
        item.setItemId(itemId);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(item);
        PowerMockito.when(itemService.getItemByPartId(Mockito.anyLong())).thenReturn(itemList);
        List<ItemOption> optionList = new ArrayList<ItemOption>();
        ItemOption option = new ItemOption();
        option.setOptionId(3001L);
        PowerMockito.when(itemService.getItemOptions(Mockito.anyLong())).thenReturn(optionList);
        Assert.assertEquals("获取每部分信息,正常测试", testPaperPartList, testPaperPartService.getPartInfoListByTestInfoId(testInfoId));
    }

    /**
     * 根据成绩信息ID获取每部分信息,单元测试
     * @throws Exception
     */
    @Test
    public void testGetPartInfoListByAchieveMentId() throws Exception {
        long achieveMentId = 1L;
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(1L);
        List<TestPaperPart> testPaperPartList = new ArrayList<TestPaperPart>();
        testPaperPartList.add(testPaperPart);
        //myJdbcTemplate赋给testPaperPartService类
        testPaperPartService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(testPaperPartList);
        testPaperPartService.itemService = itemService;
        ResultInfo resultInfo = new ResultInfo();
        Long itemId = 2001L;
        resultInfo.setItemId(itemId);
        Item item = new Item();
        item.setItemId(itemId);
        resultInfo.setItem(item);
        List<ResultInfo> resultInfoList = new ArrayList<ResultInfo>();
        resultInfoList.add(resultInfo);
        PowerMockito.when(itemService.getItemByPartId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(resultInfoList);
        List<ItemOption> optionList = new ArrayList<ItemOption>();
        ItemOption option = new ItemOption();
        option.setOptionId(3001L);
        PowerMockito.when(itemService.getItemOptions(Mockito.anyLong())).thenReturn(optionList);
        Assert.assertEquals("获取每部分信息,正常测试", testPaperPartList, testPaperPartService.getPartInfoListByAchieveMentId(achieveMentId));
    }
}