package zzjz.serviceimpl;

import zzjz.bean.ChartCountEntity;
import zzjz.bean.ResultCode;
import zzjz.service.impl.ChartServiceImpl;
import zzjz.util.MyJdbcTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wanghao on 2016/7/14.
 */
public class ChartServiceImplTest {
    private ChartServiceImpl chartService;
    private MyJdbcTemplate myJdbcTemplate;
    private MyJdbcTemplate myJdbcTemplateMock;
    @Before
    public void setUp() throws Exception {
        chartService = new ChartServiceImpl();
        MockitoAnnotations.initMocks(this);
        //PowerMockito 模拟类
        myJdbcTemplate = PowerMockito.spy(new MyJdbcTemplate());
        //PowerMockito mock类
        myJdbcTemplateMock = PowerMockito.mock(MyJdbcTemplate.class);
    }

    @Test
    public void testReturnChartInfo() throws Exception {
        String paperId="1111";
        ChartCountEntity chartCountEntity=new ChartCountEntity();
        List<String> userIDList=new ArrayList<String>();
        userIDList.add("2222");

        List<ChartCountEntity> returnList = new ArrayList<ChartCountEntity>();
        chartService.myJdbcTemplate= myJdbcTemplateMock;

        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(returnList);
        Assert.assertEquals(returnList, chartService.returnChartInfo(paperId,userIDList));

       PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(null);
        Assert.assertEquals(null, chartService.returnChartInfo(null,userIDList));

//        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(null);
//        Assert.assertEquals(null, chartService.returnChartInfo(paperId,null));

    }



    @Test
    public void testGetUserInfo() throws Exception {
        String companyId="123211";
        String companyId1=null;
        List<String> returnList = new ArrayList<String>();
        chartService.myJdbcTemplate= myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(returnList);
        Assert.assertEquals(returnList, chartService.getUserInfo(companyId));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(returnList);
        Assert.assertEquals(returnList, chartService.getUserInfo(companyId1));

    }

    @Test
    public void testGetUserListByDeptId() throws Exception {
        long deptId=11111L;
        long deptId1=0L;
        List<String> returnList = new ArrayList<String>();
        chartService.myJdbcTemplate= myJdbcTemplateMock;
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(returnList);
        Assert.assertEquals(returnList, chartService.getUserListByDeptId(deptId));
        PowerMockito.when(myJdbcTemplateMock.query(Mockito.anyString(),(RowMapper)Mockito.anyObject())).thenReturn(returnList);
        Assert.assertEquals(returnList, chartService.getUserListByDeptId(deptId1));


    }
}