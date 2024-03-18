package zzjz.rest;

import zzjz.bean.PagingEntity;
import zzjz.bean.ResultCode;
import zzjz.bean.TestInfo;
import zzjz.bean.TestInfoRequest;
import zzjz.service.TestInfoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @ClassName: TestInfoRestTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author zhuxiaoxiao
 * @Date: 2016/7/14 9:36
 */
public class TestInfoRestTest {
    private TestInfoRest testInfoRest;
    private TestInfoService testInfoService;
    private HttpServletRequest servletRequest;
    private HttpSession session;
    @Mock
    private MultivaluedMap<String, String> headerParams;

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
        testInfoRest = new TestInfoRest();
        //MockitoAnnotations.initMocks(this);
        //PowerMockito mock类
        testInfoService = PowerMockito.mock(TestInfoService.class);
        servletRequest = PowerMockito.mock(HttpServletRequest.class);
        headerParams = PowerMockito.mock(MultivaluedMap.class);
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        testInfoRest.testInfoService = testInfoService;
        testInfoRest.servletRequest = servletRequest;
        session = PowerMockito.mock(HttpSession.class);
    }

    @Test
    public void testAddTestInfo() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        List<Long> userList = new ArrayList<Long>();
        userList.add(110L);
        Mockito.when(headerParams.getFirst("userId")).thenReturn("");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());

        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());

        testInfo.setTestId("0");
        request.setTestInfo(testInfo);
        Assert.assertEquals("试卷不能为空", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());

        testInfo.setTestId("1");
        testInfo.setStartTime("");
        request.setTestInfo(testInfo);
        Assert.assertEquals("开始时间不能为空", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());

        testInfo.setStartTime("2016-01-01");
        testInfo.setEndTime("");
        request.setTestInfo(testInfo);
        Assert.assertEquals("结束时间不能为空", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());
        testInfo.setEndTime("2016-02-02");
        testInfo.setUserIdList(null);
        request.setTestInfo(testInfo);
        Assert.assertEquals("考生列表不能为空", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());
        testInfo.setUserIdList(userList);
        testInfo.setTotalUserNo(0);
        request.setTestInfo(testInfo);
        Assert.assertEquals("参考总人数不能为空", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());
        testInfo.setTotalUserNo(2);
        Mockito.when(testInfoService.getTestInfoByTestIdAndTime((TestInfo)Mockito.anyObject())).thenReturn(testInfo);
        Assert.assertEquals("同考试时段已存在该考试", ResultCode.BAD_REQUEST, testInfoRest.addTestInfo(request, headers).getResultCode());

        testInfo.setCreator(118L);
        testInfo.setTestStatus(1);
        testInfo.setId(110L);
        boolean success = true;
        Mockito.when(testInfoService.getTestInfoByTestIdAndTime((TestInfo)Mockito.anyObject())).thenReturn(null);
        Mockito.when(testInfoService.addTestInfo((TestInfo) Mockito.anyObject())).thenReturn(success);
        Mockito.when(testInfoService.addTestInfoAndUserConnection((TestInfo) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("考试信息添加成功", ResultCode.SUCCESS, testInfoRest.addTestInfo(request, headers).getResultCode());
        Mockito.when(testInfoService.addTestInfoAndUserConnection((TestInfo) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("考试信息添加成功,考生添加失败", ResultCode.ERROR, testInfoRest.addTestInfo(request, headers).getResultCode());
        Mockito.when(testInfoService.addTestInfo((TestInfo) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("考试信息添加失败", ResultCode.ERROR, testInfoRest.addTestInfo(request, headers).getResultCode());

    }

    @Test
    public void testUpdateTestInfo() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        List<Long> userList = new ArrayList<Long>();
        userList.add(110L);
        Mockito.when(headerParams.getFirst("userId")).thenReturn("");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        testInfo.setId(0);
        request.setTestInfo(testInfo);
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        int id= 110;
        testInfo.setId(1);
        Mockito.when(testInfoService.confirmActionById(testInfo.getId())).thenReturn(false);
        Assert.assertEquals("该操作仅支持在考试开始前执行", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        testInfo.setTestId("0");
        request.setTestInfo(testInfo);
        Mockito.when(testInfoService.confirmActionById(testInfo.getId())).thenReturn(true);
        Assert.assertEquals("试卷不能为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        testInfo.setTestId("1");
        testInfo.setStartTime("");
        request.setTestInfo(testInfo);
        Assert.assertEquals("开始时间不能为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        testInfo.setStartTime("2016-01-01");
        testInfo.setEndTime("");
        request.setTestInfo(testInfo);
        Assert.assertEquals("结束时间不能为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        testInfo.setEndTime("2016-02-02");
        testInfo.setUserIdList(null);
        request.setTestInfo(testInfo);
        Assert.assertEquals("考生列表不能为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        testInfo.setUserIdList(userList);
        testInfo.setTotalUserNo(0);
        request.setTestInfo(testInfo);
        Assert.assertEquals("参考总人数不能为空", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        testInfo.setTotalUserNo(1);
        TestInfo searchList = new TestInfo();
        searchList.setId(222L);
        Mockito.when(testInfoService.getTestInfoByTestIdAndTime((TestInfo)Mockito.anyObject())).thenReturn(searchList);
        Assert.assertEquals("同考试时段已存在该考试", ResultCode.BAD_REQUEST, testInfoRest.updateTestInfo(request, headers).getResultCode());
        boolean success = true;
        Mockito.when(testInfoService.getTestInfoByTestIdAndTime((TestInfo)Mockito.anyObject())).thenReturn(null);
        Mockito.when(testInfoService.updateTestInfo((TestInfo) Mockito.anyObject())).thenReturn(success);
        Mockito.when(testInfoService.deleteTestInfoAndUserConnection((TestInfo) Mockito.anyObject())).thenReturn(true);
        Mockito.when(testInfoService.addTestInfoAndUserConnection((TestInfo) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("考试信息更新成功", ResultCode.SUCCESS, testInfoRest.updateTestInfo(request, headers).getResultCode());
        Mockito.when(testInfoService.addTestInfoAndUserConnection((TestInfo) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("考试信息更新成功,考生添加失败", ResultCode.ERROR, testInfoRest.updateTestInfo(request, headers).getResultCode());
        Mockito.when(testInfoService.deleteTestInfoAndUserConnection((TestInfo) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("考试信息修改成功,清除之前考生失败", ResultCode.ERROR,testInfoRest.updateTestInfo(request, headers).getResultCode());
        Mockito.when(testInfoService.updateTestInfo((TestInfo) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("考试信息修改失败",ResultCode.ERROR, testInfoRest.updateTestInfo(request, headers).getResultCode());

    }

    @Test
    public void testDeleteTestInfo() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.deleteTestInfo(request, headers).getResultCode());
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        testInfo.setId(118L);
        request.setTestInfo(testInfo);
        Mockito.when(testInfoService.confirmActionById(testInfo.getId())).thenReturn(false);
        Assert.assertEquals("该操作仅支持在考试开始前执行",ResultCode.BAD_REQUEST, testInfoRest.deleteTestInfo(request, headers).getResultCode());
        Mockito.when(testInfoService.confirmActionById(testInfo.getId())).thenReturn(true);
        Mockito.when(testInfoService.deleteTestInfoById(testInfo.getId())).thenReturn(true);
        Assert.assertEquals("考试信息删除成功",ResultCode.SUCCESS, testInfoRest.deleteTestInfo(request, headers).getResultCode());
        Mockito.when(testInfoService.deleteTestInfoById(testInfo.getId())).thenReturn(false);
        Assert.assertEquals("考试信息删除失败",ResultCode.ERROR, testInfoRest.deleteTestInfo(request, headers).getResultCode());
    }

    @Test
    public void testGetFilterTestInfo() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.getFilterTestInfo(request, headers).getResultCode());
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        testInfo.setId(118L);
        request.setTestInfo(testInfo);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSortColumn("1");
        request.setPaging(pagingEntity);
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoList.add(testInfo);
        Mockito.when(testInfoService.getFilteredTestInfo(testInfo, pagingEntity)).thenReturn(testInfoList);
        Mockito.when(testInfoService.getFilteredTestInfoCount(testInfo, pagingEntity)).thenReturn(3);
        Assert.assertEquals("考试信息获取成功",ResultCode.SUCCESS, testInfoRest.getFilterTestInfo(request, headers).getResultCode());
        pagingEntity.setSortColumn("1");
        request.setPaging(pagingEntity);
        Mockito.when(testInfoService.getFilteredTestInfo(testInfo, pagingEntity)).thenReturn(null);
        Assert.assertEquals("考试信息为空",ResultCode.ERROR, testInfoRest.getFilterTestInfo(request, headers).getResultCode());

    }

    @Test
    public void testGetTestInfoData() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.getTestInfoData(request, headers).getResultCode());
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        Assert.assertEquals("参数为空", ResultCode.BAD_REQUEST, testInfoRest.getTestInfoData(request, headers).getResultCode());

        testInfo.setId(118L);
        request.setTestInfo(testInfo);
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoList.add(testInfo);
        Mockito.when(testInfoService.getTestInfoById(testInfo.getId())).thenReturn(testInfoList);
        Assert.assertEquals("考试信息获取成功",ResultCode.SUCCESS, testInfoRest.getTestInfoData(request, headers).getResultCode());
        Mockito.when(testInfoService.getTestInfoById(testInfo.getId())).thenReturn(null);
        Assert.assertEquals("考试信息为空",ResultCode.ERROR, testInfoRest.getTestInfoData(request, headers).getResultCode());

    }

    @Test
    public void testGetTestInfo() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");

        Mockito.when(session.getAttribute("userId")).thenReturn("118");
        Mockito.when(servletRequest.getSession()).thenReturn(session);
        testInfo.setId(118L);
        request.setTestInfo(testInfo);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSortColumn("1");
        request.setPaging(pagingEntity);
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoList.add(testInfo);
        Mockito.when(testInfoService.getTestInfo(Mockito.anyString(),(PagingEntity)Mockito.anyObject())).thenReturn(testInfoList);
        Mockito.when(testInfoService.getTestInfoCount(Mockito.anyString(), (PagingEntity)Mockito.anyObject())).thenReturn(3);
        Assert.assertEquals("查询成功",ResultCode.SUCCESS, testInfoRest.getTestInfo(request, headers).getResultCode());
    }

    @Test
    public void testGetPointInfo() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        Mockito.when(session.getAttribute("userId")).thenReturn("118");
        Mockito.when(servletRequest.getSession()).thenReturn(session);
        testInfo.setId(118L);
        request.setTestInfo(testInfo);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setSortColumn("1");
        request.setPaging(pagingEntity);
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoList.add(testInfo);
        Mockito.when(testInfoService.getPointInfo(Mockito.anyString(), (PagingEntity)Mockito.anyObject())).thenReturn(testInfoList);
        Mockito.when(testInfoService.getPointInfoCount(Mockito.anyString(), (PagingEntity)Mockito.anyObject())).thenReturn(3);
        Assert.assertEquals("考试信息查询成功",ResultCode.SUCCESS, testInfoRest.getPointInfo(request, headers).getResultCode());
        pagingEntity.setSortColumn("1");
        request.setPaging(pagingEntity);
        Mockito.when(testInfoService.getPointInfo(Mockito.anyString(), (PagingEntity)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals("考试信息查询失败",ResultCode.ERROR, testInfoRest.getPointInfo(request, headers).getResultCode());

    }

    @Test
    public void testUserAchievement() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        Mockito.when(session.getAttribute("userId")).thenReturn("118");
        Mockito.when(servletRequest.getSession()).thenReturn(session);
        testInfo.setId(118L);
        request.setTestInfo(testInfo);
        Mockito.when(testInfoService.userAchievement(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        Assert.assertEquals("您已参加过该场考试",ResultCode.BAD_REQUEST, testInfoRest.userAchievement(request, headers).getResultCode());
        Mockito.when(testInfoService.userAchievement(Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Assert.assertEquals("进入考试",ResultCode.SUCCESS, testInfoRest.userAchievement(request, headers).getResultCode());

    }

    @Test
    public void testGetTestpeopleNames() throws Exception {
        TestInfoRequest request = new TestInfoRequest();
        TestInfo testInfo = new TestInfo();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        testInfo.setId(118L);
        request.setTestInfo(testInfo);
        List<TestInfo> testInfoList =new ArrayList<TestInfo>();
        testInfoList.add(testInfo);
        Mockito.when(testInfoService.getTestpeopleNames(Mockito.anyString())).thenReturn(testInfoList);
        Assert.assertEquals("您已参加过该场考试",ResultCode.SUCCESS, testInfoRest.getTestpeopleNames(request, headers).getResultCode());
        Mockito.when(testInfoService.getTestpeopleNames(Mockito.anyString())).thenReturn(null);
        Assert.assertEquals("无人参加该场考试",ResultCode.BAD_REQUEST, testInfoRest.getTestpeopleNames(request, headers).getResultCode());

    }
}