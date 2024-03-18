package zzjz.rest;

import zzjz.bean.*;
import zzjz.service.ChartsService;
import zzjz.service.CompanyService;
import zzjz.service.DeptService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by wanghao on 2016/7/14.
 */
public class ChartRestTest {
    @Autowired
    private ChartRest chartRest;
    @Autowired
    private ChartsService chartsService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private DeptService deptService;
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

        chartRest = new ChartRest();
        //MockitoAnnotations.initMocks(this);
        //PowerMockito mock类
        chartsService = PowerMockito.mock(ChartsService.class);
        companyService = PowerMockito.mock(CompanyService.class);
        headerParams = PowerMockito.mock(MultivaluedMap.class);
        deptService = PowerMockito.mock(DeptService.class);

        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        chartRest.chartsService=chartsService;
        chartRest.companyService=companyService;
        chartRest.deptService=deptService;
    }


    @Test
    public void testGetChartInfo1() throws Exception {
        ChartRequest chartRequest = new ChartRequest();
        List<Company> companyList = new ArrayList<Company>();
        Company company = new Company();
        company.setCompanyId(1L);
        companyList.add(company);
        Mockito.when( companyService.getCompanyList()).thenReturn(companyList);
        List<String> userIdList = new ArrayList<String>();
        userIdList.add("1");
        Mockito.when( chartsService.getUserInfo(Mockito.anyString())).thenReturn(userIdList);
        List<ChartCountEntity> chartList = new ArrayList<ChartCountEntity>();
        ChartCountEntity chartCountEntity = new ChartCountEntity();
        chartCountEntity.setAvgCount("1");
        chartList.add(chartCountEntity);
        chartRequest.setPaperId("1");
        Mockito.when(chartsService.returnChartInfo(chartRequest.getPaperId(),userIdList)).thenReturn(chartList);
        Assert.assertEquals("测试char数据", ResultCode.SUCCESS, chartRest.getChartInfo(chartRequest, headers).getResultCode());

        Mockito.when(chartsService.returnChartInfo(chartRequest.getPaperId(),userIdList)).thenReturn(new ArrayList<ChartCountEntity>());
        Assert.assertEquals("测试char数据", ResultCode.SUCCESS, chartRest.getChartInfo(chartRequest, headers).getResultCode());
    }

    @Test
    public void testGetChartInfo2() throws Exception {
        ChartRequest chartRequest = new ChartRequest();
        chartRequest.setCompanyId("1");

        List<Dept> deptList =  new ArrayList<Dept>();
        Dept dept = new Dept();
        dept.setDeptId(1L);
        deptList.add(dept);

        Mockito.when( deptService.getDeptListByCompanyId(Mockito.anyLong())).thenReturn(deptList);
        List<String> userIdList = new ArrayList<String>();
        userIdList.add("1");
        Mockito.when( chartsService.getUserInfo(Mockito.anyString())).thenReturn(userIdList);
        List<ChartCountEntity> chartList = new ArrayList<ChartCountEntity>();
        ChartCountEntity chartCountEntity = new ChartCountEntity();
        chartCountEntity.setAvgCount("1");
        chartList.add(chartCountEntity);
        chartRequest.setPaperId("1");
        Mockito.when(chartsService.returnChartInfo(Mockito.anyString(),Mockito.anyList())).thenReturn(chartList);
        Assert.assertEquals("测试char数据", ResultCode.SUCCESS, chartRest.getChartInfo(chartRequest, headers).getResultCode());

        Mockito.when(chartsService.returnChartInfo(Mockito.anyString(),Mockito.anyList())).thenReturn(new ArrayList<ChartCountEntity>());
        Assert.assertEquals("测试char数据", ResultCode.SUCCESS, chartRest.getChartInfo(chartRequest, headers).getResultCode());
    }
}