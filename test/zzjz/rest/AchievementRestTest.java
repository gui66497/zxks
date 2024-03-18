package zzjz.rest;

import com.google.common.collect.Lists;
import zzjz.bean.*;
import zzjz.service.AchievementService;
import zzjz.service.TestPaperPartService;
import zzjz.service.TestResultService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @ClassName: AchievementRestTest
 * @Description: 成绩管理rest 测试类
 * @Author 房桂堂
 * @Date: 2016/7/14 15:59
 */
public class AchievementRestTest {

    private AchievementRest achievementRest;            //成绩管理rest
    private AchievementService achievementService;      //成绩管理service
    private TestResultService testResultService;        //成绩详情service
    private TestPaperPartService testPaperPartService;  //模块详情管理service
    @Mock
    MultivaluedMap<String, String> headerParams;
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
        achievementRest = new AchievementRest();                                //初始化成绩管理rest
        achievementService = PowerMockito.mock(AchievementService.class);       //mock achievementService
        testResultService = PowerMockito.mock(TestResultService.class);         //mock testResultService
        testPaperPartService = PowerMockito.mock(TestPaperPartService.class);   //mock testPaperPartService
        achievementRest.achievementService = achievementService;                //mock的achievementService对象赋值给achievementRest
        headerParams = PowerMockito.mock(MultivaluedMap.class);                 //mock headerParams类
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");        //mock headerParams.getFirst("userId")
        achievementRest.testResultService = testResultService;                  //mock的testResultService对象赋值给achievementRest
        achievementRest.testPaperPartService = testPaperPartService;            //mock的testPaperPartService 对象赋值给achievementRest
    }

    /**
     * 添加成绩信息
     */
    @Test
    public void testAddAchievement() throws Exception {
        AchievementRequest request = new AchievementRequest();
        Assert.assertEquals("成绩信息为空！", ResultCode.BAD_REQUEST, achievementRest.addAchievement(request, headers).getResultCode());

        Achievement achievement = new Achievement();
        achievement.setTestInfoId(1L);
        request.setAchievement(achievement);
        //mock获取结果为null
        PowerMockito.when(achievementService.getAchievement((Achievement) Mockito.anyObject())).thenReturn(null);
        //mock添加结果为true
        PowerMockito.when(achievementService.addAchievement((Achievement) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("添加成功！", ResultCode.SUCCESS, achievementRest.addAchievement(request, headers).getResultCode());

        //mock添加结果为false
        PowerMockito.when(achievementService.addAchievement((Achievement) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("添加失败！", ResultCode.ERROR, achievementRest.addAchievement(request, headers).getResultCode());

        //mock查询结果为true
        PowerMockito.when(achievementService.getAchievement((Achievement) Mockito.anyObject())).thenReturn(new Achievement());
        Assert.assertEquals("成绩信息已存在！", ResultCode.RECORD_EXIST, achievementRest.addAchievement(request, headers).getResultCode());
    }

    /**
     * 删除成绩信息
     */
    @Test
    public void testDeleteAchievement() throws Exception {
        AchievementRequest request = new AchievementRequest();
        request.setId(0L);
        Assert.assertEquals("成绩信息Id不正确！", ResultCode.BAD_REQUEST, achievementRest.deleteAchievement(request, headers).getResultCode());

        request.setId(2L);
        PowerMockito.when(achievementService.deleteAchievementById(Mockito.anyLong())).thenReturn(true);
        Assert.assertEquals("成绩信息删除成功！", ResultCode.SUCCESS, achievementRest.deleteAchievement(request, headers).getResultCode());

        PowerMockito.when(achievementService.deleteAchievementById(Mockito.anyLong())).thenReturn(false);
        Assert.assertEquals("成绩信息删除失败！", ResultCode.ERROR, achievementRest.deleteAchievement(request, headers).getResultCode());
    }

    /**
     * 修改成绩信息
     */
    @Test
    public void testUpdateAchievement() throws Exception {
        //触发非空验证
        Assert.assertEquals("成绩信息为空！", ResultCode.BAD_REQUEST, achievementRest.updateAchievement(null, headers).getResultCode());

        Achievement request = new Achievement();
        PowerMockito.when(achievementService.updateAchievement((Achievement)Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("成绩信息更新成功！", ResultCode.SUCCESS, achievementRest.updateAchievement(request, headers).getResultCode());

        //mock service处理返回false
        PowerMockito.when(achievementService.updateAchievement((Achievement)Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("成绩信息更新失败！", ResultCode.ERROR, achievementRest.updateAchievement(request, headers).getResultCode());
    }

    /**
     * 获取指定考试的所有成绩信息
     */
    @Test
    public void testGetAchievement() throws Exception {
        //触发参数验证
        Assert.assertEquals("参数不正确！", ResultCode.BAD_REQUEST, achievementRest.getAchievement(0, headers).getResultCode());

        //mock空的返回
        List<Achievement> achievementList = Lists.newArrayList();
        PowerMockito.when(achievementService.getAchievementListByTestInfoId(Mockito.anyLong())).thenReturn(achievementList);
        Assert.assertEquals("成绩信息获取失败！", ResultCode.ERROR, achievementRest.getAchievement(2L, headers).getResultCode());

        Achievement achievement = new Achievement();
        achievementList.add(achievement);
        PowerMockito.when(achievementService.getAchievementListByTestInfoId(Mockito.anyLong())).thenReturn(achievementList);
        Assert.assertEquals("成绩信息获取成功！", ResultCode.SUCCESS, achievementRest.getAchievement(2L, headers).getResultCode());
    }

    /**
     * 获取试卷每部分的详细信息，包括试卷信息和题目信息
     */
    @Test
    public void testGetAchievementDetail() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        request.setId(0L);
        //触发参数验证
        Assert.assertEquals("参数错误！", ResultCode.BAD_REQUEST, achievementRest.getAchievementDetail(request, headers).getResultCode());

        //mock空的返回
        request.setId(2L);
        List<TestPaperPart> testPaperPartList = Lists.newArrayList();
        PowerMockito.when(testPaperPartService.getPartInfoListByAchieveMentId(Mockito.anyLong())).thenReturn(testPaperPartList);
        Assert.assertEquals("试卷信息获取失败！", ResultCode.ERROR, achievementRest.getAchievementDetail(request, headers).getResultCode());

        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPart.setPartId(1L);
        testPaperPartList.add(testPaperPart);
        PowerMockito.when(testPaperPartService.getPartInfoListByAchieveMentId(Mockito.anyLong())).thenReturn(testPaperPartList);
        Assert.assertEquals("试卷信息获取成功！", ResultCode.SUCCESS, achievementRest.getAchievementDetail(request, headers).getResultCode());
    }
}