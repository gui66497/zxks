package zzjz.serviceimpl;

import com.google.common.collect.Lists;
import zzjz.bean.TestPaperCategory;
import zzjz.service.impl.TestPaperCategoryServiceImpl;
import zzjz.util.MyJdbcTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

/**
 * @ClassName: TestPaperCategoryServiceImplTest
 * @Description: 试卷类型管理Service单元测试
 * @Author 房桂堂
 * @Date: 2016/7/13 9:17
 */
@RunWith(PowerMockRunner.class)
public class TestPaperCategoryServiceImplTest {
    private TestPaperCategoryServiceImpl testPaperCategoryService;
    private MyJdbcTemplate myJdbcTemplate;
    private MyJdbcTemplate myJdbcTemplateMock;

    @Before
    public void setUp() throws Exception {
        testPaperCategoryService = new TestPaperCategoryServiceImpl();
        MockitoAnnotations.initMocks(this);
        //PowerMockito 模拟类
        myJdbcTemplate = PowerMockito.spy(new MyJdbcTemplate());
        //PowerMockito mock类
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);
    }

    /**
     * 根据考试类别实体类的parentId,categoryName获取考试类别信息
     * @throws Exception
     */
    @Test
    public void testGetTestPaperCategory() throws Exception {
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        List<TestPaperCategory> testPaperCategoryList = Lists.newArrayList();
        testPaperCategoryList.add(testPaperCategory);
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper) Mockito.anyObject())).thenReturn(testPaperCategoryList);
        Assert.assertEquals("获取成功", testPaperCategory, testPaperCategoryService.getTestPaperCategory(testPaperCategory));
    }

    /**
     * 添加考试类别信息
     * @throws Exception
     */
    @Test
    public void testAddTestPaperCategory() throws Exception {
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        Assert.assertEquals(false, testPaperCategoryService.addTestPaperCategory(testPaperCategory));
        testPaperCategory.setCategoryName("语文");
        testPaperCategory.setParentId(11L);
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        //mock service处理结果为1
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), Mockito.anyObject())).thenReturn(1);
        Assert.assertEquals("添加成功", true, testPaperCategoryService.addTestPaperCategory(testPaperCategory));
    }

    /**
     * 添加考试类别信息 异常抛出
     * @throws Exception
     */
    @Test
    public void testAddTestPaperCategoryThrowException() throws Exception {
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        testPaperCategory.setCategoryName("语文");
        testPaperCategory.setParentId(11L);
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        //mock异常
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(), (PreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals("添加失败", false, testPaperCategoryService.addTestPaperCategory(testPaperCategory));
    }

    /**
     * 获取所有考试类别信息
     * @throws Exception
     */
    @Test
    public void testGetTestPaperCategoryList() throws Exception {
        List<TestPaperCategory> testPaperCategoryList = Lists.newArrayList();
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(testPaperCategoryList);
        Assert.assertEquals("获取成功", testPaperCategoryList, testPaperCategoryService.getTestPaperCategoryList());

    }

    /**
     * 根据类别id获取此类别的所有子类别
     * @throws Exception
     */
    @Test
    public void testGetChildTestPaperCategoryById() throws Exception {
        long categoryId = 111;
        long categoryId2 = -1;
        List<TestPaperCategory> testPaperCategoryList = Lists.newArrayList();
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject())).thenReturn(testPaperCategoryList);
        Assert.assertEquals("获取成功", testPaperCategoryList, testPaperCategoryService.getChildTestPaperCategoryById(categoryId));
        Assert.assertEquals("获取失败", null, testPaperCategoryService.getChildTestPaperCategoryById(categoryId2));
    }

    /**
     * 根据类别id删除该考试类别
     * @throws Exception
     */
    @Test
    public void testDeleteTestPaperCategory() throws Exception {
        long categoryId = 11;
        long categoryId2 = -1;
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        //true
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
        Assert.assertEquals("删除成功", true, testPaperCategoryService.deleteTestPaperCategory(categoryId));
        //false
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), Mockito.anyLong())).thenReturn(0);
        Assert.assertEquals("删除失败", false, testPaperCategoryService.deleteTestPaperCategory(categoryId));
        //前置验证
        Assert.assertEquals("参数错误", false, testPaperCategoryService.deleteTestPaperCategory(categoryId2));
    }

    /**
     * 根据类别id删除该考试类别 抛出异常
     * @throws Exception
     */
    @Test
    public void testDeleteTestPaperCategoryThrowException() throws Exception {
        long categoryId = 11L;
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(), Mockito.anyLong());
        Assert.assertEquals("删除失败", false,testPaperCategoryService.deleteTestPaperCategory(categoryId));
    }

    /**
     * 修改考试类别信息
     * @throws Exception
     */
    @Test
    public void testUpdateTestPaperCategory() throws Exception {
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(0);
        Assert.assertEquals(false, testPaperCategoryService.updateTestPaperCategory(testPaperCategory));
        testPaperCategory.setCategoryName("语文");
        testPaperCategory.setId(111L);
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).thenReturn(0);
        Assert.assertEquals("修改失败", false, testPaperCategoryService.updateTestPaperCategory(testPaperCategory));
    }

    /**
     * 修改考试类别信息  抛出异常
     * @throws Exception
     */
    @Test
    public void testUpdateTestPaperCategoryThrowException() throws Exception {
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        testPaperCategory.setCategoryName("语文");
        testPaperCategory.setId(111L);
        myJdbcTemplate = PowerMockito.mock(MyJdbcTemplate.class);
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplate;
        Mockito.doThrow(Exception.class).when(myJdbcTemplate).update(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        Assert.assertEquals("修改失败", false,testPaperCategoryService.updateTestPaperCategory(testPaperCategory));
    }

    /**
     * 根据parentId获取父考试类别
     * @throws Exception
     */
    @Test
    public void testGetFatherTestPaperCategory() throws Exception {
        long parentId = 11l;
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        testPaperCategory.setParentId(parentId);
        List<TestPaperCategory> testPaperCategoryList = Lists.newArrayList();
        testPaperCategoryList.add(testPaperCategory);
        testPaperCategoryService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper) Mockito.anyObject())).thenReturn(testPaperCategoryList);
        Assert.assertEquals("获取失败", null, testPaperCategoryService.getFatherTestPaperCategory(parentId));
    }
}