package zzjz.serviceimpl;

import com.google.common.collect.Lists;
import zzjz.bean.PagingEntity;
import zzjz.bean.TestPaper;
import zzjz.bean.TestPaperCategory;
import zzjz.bean.TestPaperPart;
import zzjz.service.impl.TestPaperServiceImpl;
import zzjz.util.MyJdbcTemplate;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @ClassName: TestPaperServiceImplTest
 * @Description: 试卷管理Service单元测试
 * @Author 房桂堂
 * @Date: 2016/7/13 14:18
 */
@RunWith(PowerMockRunner.class)
public class TestPaperServiceImplTest {
    private TestPaperServiceImpl testPaperService;
    private MyJdbcTemplate myJdbcTemplate;
    private MyJdbcTemplate myJdbcTemplateMock;

    @Before
    public void setUp() throws Exception {
        testPaperService = new TestPaperServiceImpl();
        MockitoAnnotations.initMocks(this);
        //PowerMockito 模拟类
        myJdbcTemplate = PowerMockito.spy(new MyJdbcTemplate());
        //PowerMockito mock类
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);
    }

    @Test
    public void testGetTestPaper() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setTestpaperName("java");
        List<TestPaper> testPaperList = Lists.newArrayList();
        testPaperList.add(testPaper);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper) Mockito.anyObject(), Mockito.anyString(), Mockito.anyLong())).thenReturn(testPaperList);
        Assert.assertEquals(testPaper, testPaperService.getTestPaper(testPaper));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper) Mockito.anyObject(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Assert.assertEquals(null, testPaperService.getTestPaper(testPaper));
    }

    /**
     * 添加试卷测试
     * @throws Exception
     */
    @Test
    public void testAddTestPaper() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject())).thenReturn(1);
        Assert.assertEquals(true, testPaperService.addTestPaper(testPaper));
    }

    /**
     * 添加试卷测试  异常
     * @throws Exception
     */
    @Test
    public void testAddTestPaperThrowException() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals(false, testPaperService.addTestPaper(testPaper));
    }

    /**
     * 获取试卷列表测试 包含分页
     * @throws Exception
     */
    @Test
    public void testGetTestPaperListByCategoryId() throws Exception {
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setStartIndex(1);
        pagingEntity.setPageSize(10);
        pagingEntity.setSortColumn("ITEM_ID");
        pagingEntity.setSortDir("DESC");
        pagingEntity.setSearchValue("java");

        long categoryId = 111L;
        TestPaper testPaper = new TestPaper();
        List<TestPaper> testPaperList = Lists.newArrayList();
        testPaperList.add(testPaper);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper) Mockito.anyObject(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(testPaperList);
        Assert.assertEquals(testPaperList, testPaperService.getTestPaperListByCategoryId(categoryId, pagingEntity));
    }

    /**
     * 获取试卷数量通过类型
     * @throws Exception
     */
    @Test
    public void testGetTestpaperCountByCategory() throws Exception {
        String searchStr = "java";
        long categoryId = 111L;
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSearchValue("java");

        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        String sql = "select count(*) from tb_testpaper where DELETE_FLAG=0 and CATEGORY_ID = " + categoryId;
        if (StringUtils.isNotBlank(searchStr)) {
            sql += " and TESTPAPER_NAME like '%"+searchStr+"%'";
        }
        PowerMockito.when(myJdbcTemplateMock.queryForObject(sql, Integer.class)).thenReturn(1);
        Assert.assertEquals(1, testPaperService.getTestpaperCountByCategory(categoryId, pagingEntity));
    }

    /**
     * 通过id删除试卷测试
     * @throws Exception
     */
    @Test
    public void testDeleteTestPaperById() throws Exception {
        long id = 11L;
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
        Assert.assertEquals(true, testPaperService.deleteTestPaperById(id));
    }

    /**
     * 通过id删除试卷测试 异常
     * @throws Exception
     */
    @Test
    public void testDeleteTestPaperByIdThrowException() throws Exception {
        long id = 11L;
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(), Mockito.anyLong());
        Assert.assertEquals(false, testPaperService.deleteTestPaperById(id));
    }

    /**
     * 根据试卷name获取试卷 测试
     * @throws Exception
     */
    @Test
    public void testGetTestPaperByName() throws Exception {
        String testpaperName = "java考试";
        TestPaper testPaper = new TestPaper();
        testPaper.setTestpaperName(testpaperName);
        List<TestPaper> testPaperList = Lists.newArrayList();
        testPaperList.add(testPaper);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper) Mockito.anyObject(), Mockito.anyString())).thenReturn(testPaperList);
        Assert.assertEquals(testPaper, testPaperService.getTestPaperByName(testpaperName));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper) Mockito.anyObject(), Mockito.anyString())).thenReturn(null);
        Assert.assertEquals(null, testPaperService.getTestPaperByName(testpaperName));
    }

    /**
     * 修改试卷信息
     * @throws Exception
     */
    @Test
    public void testUpdateTestPaper() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject())).thenReturn(1);
        Assert.assertEquals(true, testPaperService.updateTestPaper(testPaper));
    }

    /**
     * 修改试卷信息 异常
     * @throws Exception
     */
    @Test
    public void testUpdateTestPaperThrowException() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setTestpaperName("java");
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(), (PreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals(false, testPaperService.updateTestPaper(testPaper));
    }

    /**
     * 根据id获取试卷信息
     * @throws Exception
     */
    @Test
    public void testGetTestPaperById() throws Exception {
        long id = 11L;
        TestPaper testPaper = new TestPaper();
        testPaper.setId(id);
        List<TestPaper> testPaperList = Lists.newArrayList();
        testPaperList.add(testPaper);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper) Mockito.anyObject(), Mockito.anyLong())).thenReturn(testPaperList);
        Assert.assertEquals(testPaper, testPaperService.getTestPaperById(id));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper) Mockito.anyObject(), Mockito.anyLong())).thenReturn(null);
        Assert.assertEquals(null, testPaperService.getTestPaperById(id));
    }

    /**
     * 根据id集合删除试卷信息
     * @throws Exception
     */
    @Test
    public void testBatchDeleteTestPaperById() throws Exception {
        List<Long> ids = Lists.newArrayList(1L,2L,3L);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(),
                (BatchPreparedStatementSetter) Mockito.anyObject())).thenReturn(new int[]{1, 1});
        Assert.assertEquals(true, testPaperService.batchDeleteTestPaperById(ids));
    }

    /**
     * 根据id集合删除试卷信息 异常
     * @throws Exception
     */
    @Test
    public void testBatchDeleteTestPaperByIdThrowException() throws Exception {
        List<Long> ids = Lists.newArrayList(1L,2L,3L);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).batchUpdate(Mockito.anyString(),
                (BatchPreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals(false, testPaperService.batchDeleteTestPaperById(ids));
    }

    /**
     * 创建试卷
     * @throws Exception
     */
    @Test
    public void testCreateTestpaper() throws Exception {
        TestPaper testPaper = new TestPaper();
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartName("多选题");
        testPaperParts[0] = testPaperPart;
        testPaper.setTestPaperParts(testPaperParts);

        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update((PreparedStatementCreator) Mockito.anyObject(),
                (GeneratedKeyHolder) Mockito.anyObject() )).thenReturn(1);

        Assert.assertEquals(false, testPaperService.createTestpaper(testPaper));

        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(), (BatchPreparedStatementSetter) Mockito.anyObject())).thenReturn(new int[]{1, 1});

    }

    /**
     * 创建模块
     * @throws Exception
     */
    @Test
    public void testCreateTestPaperParts() throws Exception {
        long generatedId = 111L;
        TestPaper testPaper = new TestPaper();
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartName("多选题");
        testPaperParts[0] = testPaperPart;
        Assert.assertEquals(false, testPaperService.createTestPaperParts(generatedId, testPaper));
        testPaper.setTestPaperParts(testPaperParts);

        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), (PreparedStatementSetter) Mockito.anyObject())).thenReturn(1);
        Assert.assertEquals(true, testPaperService.createTestPaperParts(generatedId, testPaper));
    }

    /**
     * 抛出异常
     * @throws Exception
     */
    @Test
    public void testCreateTestPaperPartsThrowException() throws Exception {
        long generatedId = 111L;
        TestPaper testPaper = new TestPaper();
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartName("多选题");
        testPaperParts[0] = testPaperPart;
        Assert.assertEquals(false, testPaperService.createTestPaperParts(generatedId, testPaper));
        testPaper.setTestPaperParts(testPaperParts);

        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals(false, testPaperService.createTestPaperParts(generatedId, testPaper));
    }

    /**
     * 创建题目
     * @throws Exception
     */
    @Test
    public void testCreateItems() throws Exception {
        long generatedId = 111L;
        TestPaper testPaper = new TestPaper();
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartName("多选题");
        testPaperParts[0] = testPaperPart;
        Assert.assertEquals(false, testPaperService.createTestPaperParts(generatedId, testPaper));
        testPaper.setTestPaperParts(testPaperParts);

        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(),
                (BatchPreparedStatementSetter) Mockito.anyObject())).thenReturn(new int[]{1, 1});

        Assert.assertEquals(true, testPaperService.createItems(testPaperParts[0], testPaper));
    }

    /**
     * 抛出异常
     * @throws Exception
     */
    @Test
    public void testCreateItemsThrowException() throws Exception {
        long generatedId = 111L;
        TestPaper testPaper = new TestPaper();
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartName("多选题");
        testPaperParts[0] = testPaperPart;
        Assert.assertEquals(false, testPaperService.createTestPaperParts(generatedId, testPaper));
        testPaper.setTestPaperParts(testPaperParts);

        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).batchUpdate(Mockito.anyString(),
                (BatchPreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals(false, testPaperService.createItems(testPaperParts[0], testPaper));
    }

    /**
     * 修改试卷
     * @throws Exception
     */
    @Test
    public void testUpdateTestpaperAndPart() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setId(118L);
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(11L);
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaperParts[0] = testPaperPart;
        testPaper.setTestPaperParts(testPaperParts);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject())).thenReturn(1);
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);

        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(), (PreparedStatementSetter) Mockito.anyObject())).thenReturn(1);
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(), (BatchPreparedStatementSetter) Mockito.anyObject())).thenReturn(new int[]{1, 1});

        Assert.assertEquals("修改成功", true, testPaperService.updateTestpaperAndPart(testPaper));
    }

    /**
     * 修改试卷 异常1
     * @throws Exception
     */
    @Test
    public void testUpdateTestpaperAndPartThrowException1() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setId(118L);
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(11L);
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaperParts[0] = testPaperPart;
        testPaper.setTestPaperParts(testPaperParts);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject());


        Assert.assertEquals("修改失败", false, testPaperService.updateTestpaperAndPart(testPaper));
    }

    /**
     * 修改试卷 异常2
     * @throws Exception
     */
    @Test
    public void testUpdateTestpaperAndPartThrowException2() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setId(118L);
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(11L);
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaperParts[0] = testPaperPart;
        testPaper.setTestPaperParts(testPaperParts);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals("修改失败", false, testPaperService.updateTestpaperAndPart(testPaper));
    }

    /**
     * 修改试卷 异常3
     * @throws Exception
     */
    @Test
    public void testUpdateTestpaperAndPartThrowException3() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setId(118L);
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(11L);
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaperParts[0] = testPaperPart;
        testPaper.setTestPaperParts(testPaperParts);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).execute(Mockito.anyString());
        Assert.assertEquals("修改失败", false, testPaperService.updateTestpaperAndPart(testPaper));
    }

    /**
     * 修改试卷 异常4
     * @throws Exception
     */
    @Test
    public void testUpdateTestpaperAndPartThrowException4() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setId(118L);
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(11L);
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaperParts[0] = testPaperPart;
        testPaper.setTestPaperParts(testPaperParts);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(), (PreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals("修改失败", false, testPaperService.updateTestpaperAndPart(testPaper));
    }

    /**
     * 修改试卷 异常5
     * @throws Exception
     */
    @Test
    public void testUpdateTestpaperAndPartThrowException5() throws Exception {
        TestPaper testPaper = new TestPaper();
        testPaper.setId(118L);
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(11L);
        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaperParts[0] = testPaperPart;
        testPaper.setTestPaperParts(testPaperParts);
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).batchUpdate(Mockito.anyString(), (BatchPreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals("修改失败", false, testPaperService.updateTestpaperAndPart(testPaper));
    }

    /**
     * 根据考试类别查询所有试卷
     * @throws Exception
     */
    @Test
    public void testGetTestPaperByCategoryId() throws Exception {
        long categoryId = 11L;
        List<TestPaper> testPaperList = Lists.newArrayList();
        testPaperService.myJdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper) Mockito.anyObject(), Mockito.anyLong())).thenReturn(testPaperList);
        Assert.assertEquals(testPaperList, testPaperService.getTestPaperByCategoryId(categoryId));
    }

}