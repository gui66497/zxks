package zzjz.rest;

import com.google.common.collect.Lists;
import zzjz.bean.*;
import zzjz.service.TestPaperCategoryService;
import zzjz.service.TestPaperService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName: TestPaperCategoryRestTest
 * @Description: 考试分类管理rest测试类
 * @Author 房桂堂
 * @Date: 2016/7/14 10:07
 */
public class TestPaperCategoryRestTest {

    private TestPaperCategoryRest testPaperCategoryRest;
    private TestPaperService testPaperService;
    private TestPaperCategoryService testPaperCategoryService;
    private HttpServletRequest servletRequest;

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
        testPaperCategoryRest = new TestPaperCategoryRest();                            //初始化TestPaperCategoryRest类
        testPaperService = PowerMockito.mock(TestPaperService.class);                   //mock TestPaperService
        testPaperCategoryService = PowerMockito.mock(TestPaperCategoryService.class);   //mock TestPaperCategoryService
        servletRequest = PowerMockito.mock(HttpServletRequest.class);                   //mock HttpServletRequest
        headerParams = PowerMockito.mock(MultivaluedMap.class);                         //mock headerParams类
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");                //mock headerParams.getFirst("userId")
        testPaperCategoryRest.testPaperService = testPaperService;                      //mock的testPaperService对象赋值给 TestPaperCategoryRest
        testPaperCategoryRest.testPaperCategoryService = testPaperCategoryService;      //mock的testPaperCategoryService对象赋值给 TestPaperCategoryRest
    }

    @Test
    public void testAddCategory() throws Exception {
        TestPaperCategoryRequest request = new TestPaperCategoryRequest();
        Assert.assertEquals("考试类别为空！", ResultCode.BAD_REQUEST, testPaperCategoryRest.addCategory(request, headers).getResultCode());

        TestPaperCategory testPaperCategory = new TestPaperCategory();
        request.setTestPaperCategory(testPaperCategory);
        PowerMockito.when(testPaperCategoryService.getTestPaperCategory((TestPaperCategory)Mockito.anyObject())).thenReturn(testPaperCategory);
        Assert.assertEquals("考试类别已存在！", ResultCode.ERROR, testPaperCategoryRest.addCategory(request, headers).getResultCode());

        //mock分类信息不存在
        TestPaperCategory testPaperCategory1 = new TestPaperCategory();
        testPaperCategory1.setParentId(11L);
        TestPaperCategoryRequest request1 = new TestPaperCategoryRequest();
        request.setTestPaperCategory(testPaperCategory1);
        PowerMockito.when(testPaperCategoryService.getTestPaperCategory((TestPaperCategory)Mockito.anyObject())).thenReturn(null);
        TestPaperCategory fatherTestPaperCategory = new TestPaperCategory();
        fatherTestPaperCategory.setParentId(11L);
        PowerMockito.when(testPaperCategoryService.getFatherTestPaperCategory(Mockito.anyLong())).thenReturn(fatherTestPaperCategory);
        Assert.assertEquals("考试类别只支持最多二级分类！", ResultCode.ERROR, testPaperCategoryRest.addCategory(request, headers).getResultCode());

        request.setTestPaperCategory(testPaperCategory);
        PowerMockito.when(testPaperCategoryService.addTestPaperCategory((TestPaperCategory) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("考试类别添加成功！", ResultCode.SUCCESS, testPaperCategoryRest.addCategory(request, headers).getResultCode());
        PowerMockito.when(testPaperCategoryService.addTestPaperCategory((TestPaperCategory)Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("考试类别添加失败！", ResultCode.ERROR, testPaperCategoryRest.addCategory(request, headers).getResultCode());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        long categoryId = 0;
        BaseRequest request = new BaseRequest();
        request.setId(categoryId);
        Assert.assertEquals("考试类别Id为空！", ResultCode.BAD_REQUEST, testPaperCategoryRest.deleteCategory(request, headers).getResultCode());

        //mock分类信息已存在
        request.setId(11L);
        List<TestPaperCategory> testPaperCategoryList = Lists.newArrayList();
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        testPaperCategory.setId(1L);
        testPaperCategoryList.add(testPaperCategory);
        PowerMockito.when(testPaperCategoryService.getChildTestPaperCategoryById(Mockito.anyLong())).thenReturn(testPaperCategoryList);
        Assert.assertEquals("该考试类别存在子类别！", ResultCode.ERROR, testPaperCategoryRest.deleteCategory(request, headers).getResultCode());

        List<TestPaperCategory> testPaperCategoryList1 = Lists.newArrayList();
        List<TestPaper> testPaperList = Lists.newArrayList();
        //mock子类别为空
        PowerMockito.when(testPaperCategoryService.getChildTestPaperCategoryById(Mockito.anyLong())).thenReturn(testPaperCategoryList1);
        PowerMockito.when(testPaperService.getTestPaperByCategoryId(Mockito.anyLong())).thenReturn(testPaperList);
        PowerMockito.when(testPaperCategoryService.deleteTestPaperCategory(Mockito.anyLong())).thenReturn(true);
        Assert.assertEquals("考试类别删除成功！", ResultCode.SUCCESS, testPaperCategoryRest.deleteCategory(request, headers).getResultCode());

        PowerMockito.when(testPaperCategoryService.deleteTestPaperCategory(Mockito.anyLong())).thenReturn(false);
        Assert.assertEquals("考试类别删除失败！", ResultCode.ERROR, testPaperCategoryRest.deleteCategory(request, headers).getResultCode());

        //mock试卷信息存在
        TestPaper testPaper = new TestPaper();
        testPaper.setId(1L);
        testPaperList.add(testPaper);
        PowerMockito.when(testPaperService.getTestPaperByCategoryId(Mockito.anyLong())).thenReturn(testPaperList);
        Assert.assertEquals("该考试类别下存在试卷！", ResultCode.ERROR, testPaperCategoryRest.deleteCategory(request, headers).getResultCode());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        TestPaperCategoryRequest request = new TestPaperCategoryRequest();
        //触发非空验证
        Assert.assertEquals("考试类别为空！", ResultCode.BAD_REQUEST, testPaperCategoryRest.updateCategory(request, headers).getResultCode());

        TestPaperCategory testPaperCategory = new TestPaperCategory();
        request.setTestPaperCategory(testPaperCategory);
        PowerMockito.when(testPaperCategoryService.getTestPaperCategory((TestPaperCategory)Mockito.anyObject())).thenReturn(null);

        PowerMockito.when(testPaperCategoryService.updateTestPaperCategory((TestPaperCategory) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("考试类别更新成功！", ResultCode.SUCCESS, testPaperCategoryRest.updateCategory(request, headers).getResultCode());

        PowerMockito.when(testPaperCategoryService.updateTestPaperCategory((TestPaperCategory) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("考试类别更新失败！", ResultCode.ERROR, testPaperCategoryRest.updateCategory(request, headers).getResultCode());

        //触发已存在验证
        PowerMockito.when(testPaperCategoryService.getTestPaperCategory((TestPaperCategory)Mockito.anyObject())).thenReturn(testPaperCategory);
        Assert.assertEquals("考试类别已存在!", ResultCode.ERROR, testPaperCategoryRest.updateCategory(request, headers).getResultCode());

    }

    @Test
    public void testGetCategoryList() throws Exception {
        PowerMockito.when(testPaperCategoryService.getTestPaperCategoryList()).thenReturn(null);
        Assert.assertEquals("试类别信息获取成功!", ResultCode.ERROR, testPaperCategoryRest.getCategoryList(headers).getResultCode());

        //mock空数据
        List<TestPaperCategory> testPaperCategoryList = Lists.newArrayList();
        TestPaperCategory testPaperCategory = new TestPaperCategory();
        testPaperCategory.setId(2L);
        testPaperCategoryList.add(testPaperCategory);
        PowerMockito.when(testPaperCategoryService.getTestPaperCategoryList()).thenReturn(testPaperCategoryList);
        Assert.assertEquals("考试类别信息为空!", ResultCode.SUCCESS, testPaperCategoryRest.getCategoryList(headers).getResultCode());
    }
}