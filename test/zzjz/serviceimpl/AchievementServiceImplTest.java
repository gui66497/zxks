package zzjz.serviceimpl;

import com.google.common.collect.Lists;
import zzjz.bean.Achievement;
import zzjz.bean.ResultInfo;
import zzjz.service.impl.AchievementServiceImpl;
import zzjz.util.MyJdbcTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

/**
 * @ClassName: AchievementServiceImplTest
 * @Description: 成绩操作service接口实现测试类
 * @Author 房桂堂
 * @Date: 2016/7/14 16:40
 */
public class AchievementServiceImplTest {
    private AchievementServiceImpl achievementService;
    private MyJdbcTemplate myJdbcTemplate;
    private MyJdbcTemplate myJdbcTemplateMock;

    @Before
    public void setUp() throws Exception {
        achievementService = new AchievementServiceImpl();              //初始化AchievementServiceImpl类
        myJdbcTemplate = PowerMockito.spy(new MyJdbcTemplate());        //mock myJdbcTemplate类,用于mock无返回值的方法
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);   //mock myJdbcTemplate类,用于mock有返回值的方法
    }

    /**
     * 据考试id获取所有成绩
     */
    @Test
    public void testGetAchievementListByTestInfoId() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        long testInfoId = 1L;
        List<Achievement> achievements = Lists.newArrayList();
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(), (RowMapper)Mockito.anyObject(), Mockito.anyLong())).thenReturn(achievements);
        Assert.assertEquals("获取成功", achievements, achievementService.getAchievementListByTestInfoId(testInfoId));
    }

    /**
     * 根据考试信息id和用户id获取成绩信息
     */
    @Test
    public void testGetAchievement() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        Achievement achievement = new Achievement();
        achievement.setTestInfoId(-1);
        Assert.assertEquals("参数错误", null, achievementService.getAchievement(achievement));

        achievement.setTestInfoId(1);
        achievement.setUserId(1);
        List<Achievement> achievementList = Lists.newArrayList();
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper)Mockito.anyObject(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(achievementList);
        Assert.assertEquals("获取失败", null, achievementService.getAchievement(achievement));

        Achievement achievement1 = new Achievement();
        achievementList.add(achievement1);
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),
                (RowMapper)Mockito.anyObject(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(achievementList);
        Assert.assertEquals("获取成功", achievement1, achievementService.getAchievement(achievement));

    }

    /**
     * 添加成绩信息
     */
    @Test
    public void testAddAchievement() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        Achievement achievement = new Achievement();
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject())).thenReturn(1);
        Assert.assertEquals("添加成功", true, achievementService.addAchievement(achievement));
    }

    /**
     * 添加成绩信息 异常
     */
    @Test
    public void testAddAchievementThrowException() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        Achievement achievement = new Achievement();
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals("抛出异常", false, achievementService.addAchievement(achievement));
    }

    /**
     *  删除成绩信息
     */
    @Test
    public void testDeleteAchievementById() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplate;
        PowerMockito.doNothing().when(myJdbcTemplate, "execute", Mockito.anyString());
        Assert.assertEquals("删除成功", true, achievementService.deleteAchievementById(1));
    }

    /**
     *  删除成绩信息 异常
     */
    @Test
    public void testDeleteAchievementByIdThrowException() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplate;
        Mockito.doThrow(Exception.class).when(myJdbcTemplate).execute(Mockito.anyString());
        Assert.assertEquals("抛出异常", false, achievementService.deleteAchievementById(1));
    }

    /**
     * 修改成绩信息
     */
    @Test
    public void testUpdateAchievement() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        Achievement achievement = new Achievement();
        PowerMockito.when(myJdbcTemplateMock.update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject())).thenReturn(1);
        Assert.assertEquals("修改成功", true, achievementService.updateAchievement(achievement));
    }

    /**
     * 修改成绩信息 异常
     */
    @Test
    public void testUpdateAchievementThrowException() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        Achievement achievement = new Achievement();
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).update(Mockito.anyString(),
                (PreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals("抛出异常", false, achievementService.updateAchievement(achievement));
    }

    /**
     * 批量添加回答结果信息
     */
    @Test
    public void testAddResults() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        ResultInfo[] results = null;
        long userId = 1L;
        Assert.assertEquals("参数错误", true, achievementService.addResults(results, userId));
        results = new ResultInfo[1];
        results[0] = new ResultInfo();
        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(),
                (BatchPreparedStatementSetter) Mockito.anyObject())).thenReturn(new int[]{1, 1});
        Assert.assertEquals("修改成功", true, achievementService.addResults(results, userId));

        PowerMockito.when(myJdbcTemplateMock.batchUpdate(Mockito.anyString(),
                (BatchPreparedStatementSetter) Mockito.anyObject())).thenReturn(new int[]{});
        Assert.assertEquals("修改失败", false, achievementService.addResults(results, userId));
    }

    /**
     * 批量添加回答结果信息 异常
     */
    @Test
    public void testAddResultsThrowException() throws Exception {
        achievementService.myJdbcTemplate = myJdbcTemplateMock;
        ResultInfo[] results = null;
        long userId = 1L;
        Assert.assertEquals("参数错误", true, achievementService.addResults(results, userId));
        results = new ResultInfo[1];
        results[0] = new ResultInfo();
        Mockito.doThrow(Exception.class).when(myJdbcTemplateMock).batchUpdate(Mockito.anyString(),
                (BatchPreparedStatementSetter) Mockito.anyObject());
        Assert.assertEquals("抛出异常", false, achievementService.addResults(results, userId));
    }
}