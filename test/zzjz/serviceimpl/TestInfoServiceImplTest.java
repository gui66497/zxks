package zzjz.serviceimpl;

import zzjz.bean.Item;
import zzjz.bean.PagingEntity;
import zzjz.bean.TestInfo;
import zzjz.bean.User;
import zzjz.service.impl.TestInfoServiceImpl;
import zzjz.util.MyJdbcTemplate;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @ClassName: TestInfoServiceImplTest
 * @Description: 考试信息service单元测试
 * @Author zhuxiaoxiao
 * @Date: 2016/7/13 9:08
 */
public class TestInfoServiceImplTest {
    private TestInfoServiceImpl testInfoService;
    private MyJdbcTemplate myJdbcTemplate;
    private MyJdbcTemplate myJdbcTemplateMock;
    @Before
    public void setUp() throws Exception {
        testInfoService = new TestInfoServiceImpl();
        MockitoAnnotations.initMocks(this);
        //PowerMockito 模拟类
        myJdbcTemplate = PowerMockito.spy(new MyJdbcTemplate());
        //PowerMockito mock类
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);
    }
    //获取考试信息单元测试
    @Test
    public void testGetTestInfo() throws Exception {
        String userId = "1465201138486";
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setStartIndex(1);
        pagingEntity.setPageSize(10);
        pagingEntity.setSortColumn("TESTPAPER_NAME");
        pagingEntity.setSortDir("DESC");
        pagingEntity.setSearchValue("java");
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfo);
        Assert.assertEquals(testInfo, testInfoService.getTestInfo(userId,pagingEntity));
        Assert.assertEquals(testInfo, testInfoService.getTestInfo("",pagingEntity));

    }
    //添加考试信息单元测试
    @Test
    public void testAddTestInfo() throws Exception {
        TestInfo testInfo = new TestInfo();
        long testInfoId = 1466587270311L;
        testInfo.setId(testInfoId);
        testInfoService.jdbcTemplate = myJdbcTemplate;
        //模拟muJdbcTemplate execute方法
        PowerMockito.doNothing().when(myJdbcTemplate,"execute", Mockito.anyString());
        //断言
        Assert.assertEquals(true,testInfoService.addTestInfo(testInfo));
    }

    @Test
    public void testAddItemThrowException() throws Exception {
        TestInfo testInfo = new TestInfo();
        long testInfoId = 1466587270311L;
        testInfo.setId(testInfoId);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).execute(Mockito.anyString());
        Assert.assertEquals(false,testInfoService.addTestInfo(testInfo));
    }
    //更新考试信息单元测试
    @Test
    public void testUpdateTestInfo() throws Exception {
        TestInfo testInfo = new TestInfo();
        long testInfoId = 1466587270311L;
        testInfo.setId(testInfoId);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Assert.assertEquals(true, testInfoService.updateTestInfo(testInfo));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals(false, testInfoService.updateTestInfo(testInfo));
    }
    @Test
    public void testUpdateTestInfoThrowException() throws Exception {
        TestInfo testInfo = new TestInfo();
        long testInfoId = 1466587270311L;
        testInfo.setId(testInfoId);
        testInfoService.jdbcTemplate =myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals(false,testInfoService.updateTestInfo(testInfo));
    }
 /*   //删除考试信息并且关联用户单测试
    @Test
    public void testDeleteTestInfoAndUserConnection() throws Exception {
        TestInfo testInfo = new TestInfo();
        long testInfoId = 1466587270311L;
        testInfo.setId(testInfoId);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Assert.assertEquals(true, testInfoService.deleteTestInfoAndUserConnection(testInfo));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals(false, testInfoService.deleteTestInfoAndUserConnection(testInfo));
    }
    @Test
    public void testDeleteTestInfoAndUserConnectionThrowException() throws Exception {
        TestInfo testInfo = new TestInfo();
        long testInfoId = 1466587270311L;
        testInfo.setId(testInfoId);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals(false,testInfoService.deleteTestInfoAndUserConnection(testInfo));
    }*/

    //添加考试信息并且关联用户
    @Test
    public void testAddTestInfoAndUserConnection() throws Exception {
        TestInfo testInfo =new TestInfo();
        List<Long> user = new ArrayList<Long>();
        user.add(110L);
        testInfo.setId(110L);
        testInfo.setCreator(110L);
         testInfo.setUserIdList(user);
        int[] result = new int[5];
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString())).thenReturn(result);
        Assert.assertEquals(true, testInfoService.addTestInfoAndUserConnection(testInfo));
    }
    @Test
    public void testAddTestInfoAndUserConnectionThrowException() throws Exception {
        TestInfo testInfo =new TestInfo();
        List<Long> user = new ArrayList<Long>();
        user.add(110L);
        testInfo.setId(110L);
        testInfo.setCreator(110L);
        testInfo.setUserIdList(user);
        int[] result = new int[5];
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).batchUpdate(Mockito.anyString());
        Assert.assertEquals(false, testInfoService.addTestInfoAndUserConnection(testInfo));
    }


    //获取成绩信息单元测试
    @Test
    public void testGetPointInfo() throws Exception {
        String userId = "1465201138486";
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setStartIndex(1);
        pagingEntity.setPageSize(10);
        pagingEntity.setSortColumn("TESTPAPER_NAME");
        pagingEntity.setSortDir("DESC");
        pagingEntity.setSearchValue("java");
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfo);
        Assert.assertEquals(testInfo, testInfoService.getPointInfo(userId, pagingEntity));
        Assert.assertEquals(testInfo, testInfoService.getPointInfo("", pagingEntity));

    }
    //获取考试信息条数单元测试
    @Test
    public void testGetTestInfoCount() throws Exception {
        String userId = "1465201138486";
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSearchValue("java");
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(new Integer(1));
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(), (Class<Integer>)Matchers.anyVararg())).thenReturn(counts);
        Assert.assertEquals(1, testInfoService.getTestInfoCount(userId, pagingEntity));
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(), (Class<Integer>)Matchers.anyVararg())).thenReturn(null);
        Assert.assertEquals(0, testInfoService.getTestInfoCount(userId, pagingEntity));
        Assert.assertEquals(1, testInfoService.getTestInfoCount("", pagingEntity));
    }
    //获取成绩信息单元测试
    @Test
    public void testGetPointInfoCount() throws Exception {
        String userId = "1465201138486";
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSearchValue("java");
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(new Integer(1));
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(),(Class<Integer>)Matchers.anyVararg())).thenReturn(counts);
        Assert.assertEquals(1, testInfoService.getPointInfoCount(userId, pagingEntity));
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(),(Class<Integer>)Matchers.anyVararg())).thenReturn(null);
        Assert.assertEquals(0, testInfoService.getPointInfoCount(userId, pagingEntity));
        Assert.assertEquals(1, testInfoService.getPointInfoCount("", pagingEntity));
    }
    //获取已过滤的考试信息单元测试
    @Test
    public void testGetFilteredTestInfo() throws Exception {
        String categoryName ="java题库";
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setStartIndex(1);
        pagingEntity.setPageSize(10);
        pagingEntity.setSortColumn("TESTPAPER_NAME");
        pagingEntity.setSortDir("DESC");
        pagingEntity.setSearchValue("java");
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        TestInfo testInfo1 = new TestInfo();
        testInfo1.setTestCategory(categoryName);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfo);
        Assert.assertEquals(testInfo, testInfoService.getFilteredTestInfo(testInfo1, pagingEntity));
    }
    @Test
    public void testGetFilteredTestInfoThrowException() throws Exception {
        String categoryName ="java题库";
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setStartIndex(1);
        pagingEntity.setPageSize(10);
        pagingEntity.setSortColumn("TESTPAPER_NAME");
        pagingEntity.setSortDir("DESC");
        pagingEntity.setSearchValue("java");
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        TestInfo testInfo1 = new TestInfo();
        testInfo1.setTestCategory(categoryName);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).query(Mockito.anyString(), (RowMapper) Mockito.anyObject());
        Assert.assertEquals(null,testInfoService.getFilteredTestInfo(testInfo1, pagingEntity));
    }
    //根据id获取考试信息单元测试
   @Test
    public void testGetTestInfoById() throws Exception {
        long testInfoId=1466587270311L;
        List<TestInfo> testInfoList = new ArrayList<TestInfo>();
        TestInfo testInfo = new TestInfo();
        testInfo.setId(1L);
        testInfoList.add(testInfo);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfoList);

        //PowerMockito.when(testInfoService.getUserListByTestId(Mockito.anyLong())).thenReturn(new ArrayList<User>());
        long id = 1466587270311L;
        //testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Assert.assertEquals(testInfoList,testInfoService.getTestInfoById(id));
       PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(null);
       Assert.assertEquals(null,testInfoService.getTestInfoById(id));
   }


    @Test
    public void testGetTestInfoByIdThrowException() throws Exception {
        long testInfoId=1466587270311L;
        List<TestInfo> testInfoList = new ArrayList<TestInfo>();
        TestInfo testInfo = new TestInfo();
        testInfo.setId(1L);
        testInfoList.add(testInfo);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfoList);
        long id = 1466587270311L;
        Assert.assertEquals(testInfoList,testInfoService.getTestInfoById(id));
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).query(Mockito.anyString(), (RowMapper) Mockito.anyObject());
        Assert.assertEquals(null,testInfoService.getTestInfoById(id));
    }
    //根据id删除考试信息单元测试
    @Test
    public void testDeleteTestInfoById() throws Exception {
        long id = 110L;
        int object = 1;
        String str ="11111";
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(2);
        Assert.assertEquals(true,testInfoService.deleteTestInfoById(id));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals(false,testInfoService.deleteTestInfoById(id));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals(false,testInfoService.deleteTestInfoById(id));
    }
    @Test
    public void testDeleteTestInfoByIdThrowException() throws Exception {
        long id = 110L;
        int object = 1;
        String str ="11111";
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(1);
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals(false,testInfoService.deleteTestInfoById(id));
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals(false,testInfoService.deleteTestInfoById(id));
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString());
        Assert.assertEquals(false,testInfoService.deleteTestInfoById(id));
    }
    //根据试卷id和时间获取考试信息单元测试
    @Test
    public void testGetTestInfoByTestIdAndTime() throws Exception {
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        TestInfo testInfo1 = new TestInfo();
        testInfo.add(testInfo1);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfo);
        Assert.assertEquals(testInfo.get(0),testInfoService.getTestInfoByTestIdAndTime(testInfo1));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals(null,testInfoService.getTestInfoByTestIdAndTime(testInfo1));
    }
    @Test
    public void testGetTestInfoByTestIdAndTimeThrowException() throws Exception {
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        TestInfo testInfo1 = new TestInfo();
        testInfo.add(testInfo1);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).query(Mockito.anyString(), (RowMapper) Mockito.anyObject());
        Assert.assertEquals(null,testInfoService.getTestInfoByTestIdAndTime(testInfo1));
    }
    //过去已过滤的考试信息条数单元测试
    @Test
    public void testGetFilteredTestInfoCount() throws Exception {
        TestInfo testInfo = new TestInfo();
        String categoryName ="java题库";
        testInfo.setTestCategory(categoryName);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSearchValue("java");
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(1);
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(),(Class<Integer>)Matchers.anyVararg())).thenReturn(counts);
        Assert.assertEquals(1, testInfoService.getFilteredTestInfoCount(testInfo, pagingEntity));
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(),(Class<Integer>)Matchers.anyVararg())).thenReturn(null);
        Assert.assertEquals(0, testInfoService.getFilteredTestInfoCount(testInfo, pagingEntity));
    }
    @Test
    public void testGetFilteredTestInfoCountThrowException() throws Exception {
        TestInfo testInfo = new TestInfo();
        String categoryName ="java题库";
        testInfo.setTestCategory(categoryName);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSearchValue("java");
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(1);
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).queryForList(Mockito.anyString(), (Class<Integer>) Matchers.anyVararg());
        Assert.assertEquals(0, testInfoService.getFilteredTestInfoCount(testInfo, pagingEntity));

    }
/*    @Test
    public void testAddUsersAndTestConnection() throws Exception {
        List<Long> userList = new ArrayList<Long>();
        userList.add(100L);
        long creator =10023L;
        String testId="89";
    }*/
    //根据试卷id获取用户列表
    @Test
    public void testGetUserListByTestId() throws Exception {
        long id = 1466587270311L;
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfo);
        Assert.assertEquals(testInfo, testInfoService.getUserListByTestId(id));

    }

    @Test
    public void testGetUserListByTestIdThrowException() throws Exception {
        long id = 1466587270311L;
        List<TestInfo> testInfo = new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).query(Mockito.anyString(), (RowMapper) Mockito.anyObject());
        Assert.assertEquals(null, testInfoService.getUserListByTestId(id));

    }
    //获取用户成绩单元测试
    @Test
    public void testUserAchievement() throws Exception {
        String testInfoId ="1466587270311";
        String userId = "118";
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(1);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(), (Class<Integer>) Matchers.anyVararg())).thenReturn(counts);
        Assert.assertEquals(true, testInfoService.userAchievement(testInfoId, userId));
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(), (Class<Integer>) Matchers.anyVararg())).thenReturn(null);
        Assert.assertEquals(false, testInfoService.userAchievement(testInfoId, userId));

    }
    @Test
    public void testConfirmActionById() throws Exception {
       long testInfoId =1466587270311L;
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(1);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(), (Class<Integer>) Matchers.anyVararg())).thenReturn(counts);
        Assert.assertEquals(true, testInfoService.confirmActionById(testInfoId));
        PowerMockito.when(myJdbcTemplateMock.queryForList(Mockito.anyString(), (Class<Integer>) Matchers.anyVararg())).thenReturn(null);
        Assert.assertEquals(false, testInfoService.confirmActionById(testInfoId));

    }
    @Test
    public void testConfirmActionByIdThrowException() throws Exception {
        long testInfoId =1466587270311L;
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(1);
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).queryForList(Mockito.anyString(), (Class<Integer>) Matchers.anyVararg());
        Assert.assertEquals(false, testInfoService.confirmActionById(testInfoId));


    }
    //根据试卷id获取已有的考试信息
    @Test
    public void testGetTestedInfoByPaperId() throws Exception {
        long testPaperId = 110L;
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper) Mockito.anyObject())).thenReturn(testInfoList);
        Assert.assertEquals(testInfoList, testInfoService.getTestedInfoByPaperId(testPaperId));

    }
    @Test
       public void testGetTestedInfoByPaperIdThrowException() throws Exception {
        long testPaperId = 110L;
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).query(Mockito.anyString(), (RowMapper) Mockito.anyObject());
        Assert.assertEquals(null, testInfoService.getTestedInfoByPaperId(testPaperId));
    }
    //获取参考人员信息单元测试
    @Test
    public void testGetTestpeopleNames() throws Exception {
        String testInfoId ="1466587270311";
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(testInfoList);
        Assert.assertEquals(testInfoList, testInfoService.getTestpeopleNames(testInfoId));
    }
    @Test
    public void testGetTestpeopleNamesThrowException() throws Exception {
        String testInfoId ="1466587270311";
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoService.jdbcTemplate = myJdbcTemplateMock;
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).query(Mockito.anyString(), (RowMapper) Mockito.anyObject());
        Assert.assertEquals(null, testInfoService.getTestpeopleNames(testInfoId));
    }
}