package zzjz.rest;

import com.google.common.collect.Lists;
import zzjz.bean.*;
import zzjz.service.*;
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
 * @ClassName: TestPaperRestTest
 * @Description: 试卷相关操作Rest接口测试类
 * @author 房桂堂
 * @date 2016/7/14 11:11
 */
public class TestPaperRestTest {
    private TestPaperRest testPaperRest;
    private TestPaperService testPaperService;
    private TestInfoService testInfoService;
    private TestPaperPartService testPaperPartService;
    private ItemBankService itemBankService;
    private ItemService itemService;
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
        testPaperRest = new TestPaperRest();                                    //初始化TestPaperRest类
        testPaperService = PowerMockito.mock(TestPaperService.class);           //mock TestPaperService类
        testInfoService = PowerMockito.mock(TestInfoService.class);             //mock TestInfoService类
        testPaperPartService = PowerMockito.mock(TestPaperPartService.class);   //mock TestPaperPartService类
        itemBankService = PowerMockito.mock(ItemBankService.class);             //mock ItemBankService类
        itemService = PowerMockito.mock(ItemService.class);                     //mock ItemService类
        servletRequest = PowerMockito.mock(HttpServletRequest.class);           //HttpServletRequest类
        headerParams = PowerMockito.mock(MultivaluedMap.class);                 //mock headerParams类
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");        //mock headerParams.getFirst("userId")
        testPaperRest.testPaperService = testPaperService;                      //mock的testPaperService对象赋值给testPaperRest
        testPaperRest.testInfoService = testInfoService;                        //mock的testInfoService对象赋值给testPaperRest
        testPaperRest.testPaperPartService = testPaperPartService;              //mock的testPaperPartService对象赋值给testPaperRest
        testPaperRest.itemBankService = itemBankService;                        //mock的itemBankService对象赋值给testPaperRest
        testPaperRest.itemService = itemService;                                //mock的itemService对象赋值给testPaperRest
    }

    /**
     * 添加试卷信息
     */
    @Test
    public void testAddTestPaper() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        Assert.assertEquals("试卷信息为空！", ResultCode.BAD_REQUEST, testPaperRest.addTestPaper(request, headers).getResultCode());

        TestPaper testPaper = new TestPaper();
        request.setTestPaper(testPaper);
        PowerMockito.when(testPaperService.addTestPaper((TestPaper) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("试卷信息添加成功！", ResultCode.SUCCESS, testPaperRest.addTestPaper(request, headers).getResultCode());

        PowerMockito.when(testPaperService.addTestPaper((TestPaper) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("试卷信息添加失败！", ResultCode.ERROR, testPaperRest.addTestPaper(request, headers).getResultCode());

        TestPaper testPaperTemp = new TestPaper();
        testPaperTemp.setId(11L);
        testPaper.setTestpaperName("java");
        PowerMockito.when(testPaperService.getTestPaperByName(Mockito.anyString())).thenReturn(testPaperTemp);
        Assert.assertEquals("试卷信息已存在！", ResultCode.RECORD_EXIST, testPaperRest.addTestPaper(request, headers).getResultCode());

    }

    /**
     * 删除考试类别信息
     */
    @Test
    public void testDeleteCategory() throws Exception {

        BaseRequest request = new BaseRequest();
        request.setId(0L);
        Assert.assertEquals("试卷信息Id不正确！", ResultCode.BAD_REQUEST, testPaperRest.deleteCategory(request, headers).getResultCode());

        request.setId(119L);
        PowerMockito.when(testInfoService.getTestedInfoByPaperId(Mockito.anyLong())).thenReturn(null);

        PowerMockito.when(testPaperService.deleteTestPaperById(Mockito.anyLong())).thenReturn(true);
        Assert.assertEquals("试卷信息删除成功！", ResultCode.SUCCESS, testPaperRest.deleteCategory(request, headers).getResultCode());

        PowerMockito.when(testPaperService.deleteTestPaperById(Mockito.anyLong())).thenReturn(false);
        Assert.assertEquals("试卷信息删除成功！", ResultCode.ERROR, testPaperRest.deleteCategory(request, headers).getResultCode());

        List<TestInfo> testInfoList = Lists.newArrayList();
        PowerMockito.when(testInfoService.getTestedInfoByPaperId(Mockito.anyLong())).thenReturn(testInfoList);
        Assert.assertEquals("存在相应考试,无法删除！", ResultCode.ERROR, testPaperRest.deleteCategory(request, headers).getResultCode());
    }

    /**
     * 修改试卷信息
     */
    @Test
    public void testUpdateCategory() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        Assert.assertEquals("试卷信息不正确！", ResultCode.BAD_REQUEST, testPaperRest.updateCategory(request, headers).getResultCode());

        TestPaper testPaper = new TestPaper();
        testPaper.setTestpaperName("java");
        request.setTestPaper(testPaper);

        PowerMockito.when(testPaperService.getTestPaperById(Mockito.anyLong())).thenReturn(testPaper);
        PowerMockito.when(testPaperService.getTestPaperByName(Mockito.anyString())).thenReturn(null);

        PowerMockito.when(testPaperService.updateTestPaper((TestPaper) Mockito.anyObject())).thenReturn(true);
        Assert.assertEquals("试卷信息更新成功！", ResultCode.SUCCESS, testPaperRest.updateCategory(request, headers).getResultCode());

        PowerMockito.when(testPaperService.updateTestPaper((TestPaper) Mockito.anyObject())).thenReturn(false);
        Assert.assertEquals("试卷信息更新失败！", ResultCode.ERROR, testPaperRest.updateCategory(request, headers).getResultCode());

        //mock试卷信息已存在
        TestPaper testPaperTemp = new TestPaper();
        testPaperTemp.setId(11L);
        PowerMockito.when(testPaperService.getTestPaperById(Mockito.anyLong())).thenReturn(testPaperTemp);
        PowerMockito.when(testPaperService.getTestPaperByName(Mockito.anyString())).thenReturn(testPaperTemp);
        Assert.assertEquals("试卷信息已存在！", ResultCode.ERROR, testPaperRest.updateCategory(request, headers).getResultCode());
    }

    /**
     * 获取指定考试分类下试卷信息
     */
    @Test
    public void testGetTestPaperList() throws Exception {
        //触发参数错误
        TestPaperRequest request = new TestPaperRequest();
        request.setId(0L);
        Assert.assertEquals("参数错误！", ResultCode.BAD_REQUEST, testPaperRest.getTestPaperList(request, headers).getResultCode());

        request.setId(11L);
        List<TestPaper> testPaperList = Lists.newArrayList();
        PowerMockito.when(testPaperService.getTestPaperListByCategoryId(Mockito.anyLong(),
                (PagingEntity) Mockito.anyObject())).thenReturn(testPaperList);
        Assert.assertEquals("试卷信息获取失败！", ResultCode.ERROR, testPaperRest.getTestPaperList(request, headers).getResultCode());

        TestPaper testPaper = new TestPaper();
        testPaper.setId(1L);
        testPaperList.add(testPaper);
        PowerMockito.when(testPaperService.getTestPaperListByCategoryId(Mockito.anyLong(),
                (PagingEntity) Mockito.anyObject())).thenReturn(testPaperList);
        Assert.assertEquals("试卷信息获取成功！", ResultCode.SUCCESS, testPaperRest.getTestPaperList(request, headers).getResultCode());
    }

    /**
     * 批量删除试卷信息
     */
    @Test
    public void testBatchDeleteCategory() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        Assert.assertEquals("参数不正确！", ResultCode.BAD_REQUEST, testPaperRest.batchDeleteCategory(request, headers).getResultCode());

        List<Long> ids = Lists.newArrayList();
        ids.add(1L);
        request.setTestPaperIdList(ids);
        PowerMockito.when(testPaperService.batchDeleteTestPaperById(ids)).thenReturn(true);
        Assert.assertEquals("试卷信息删除成功！", ResultCode.SUCCESS, testPaperRest.batchDeleteCategory(request, headers).getResultCode());

        PowerMockito.when(testPaperService.batchDeleteTestPaperById(ids)).thenReturn(false);
        Assert.assertEquals("试卷信息删除失败！", ResultCode.ERROR, testPaperRest.batchDeleteCategory(request, headers).getResultCode());
    }

    /**
     * 获取试卷每部分的详细信息，包括试卷信息和题目信息
     */
    @Test
    public void testGetTestPaperAllInfo() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        request.setId(0L);
        Assert.assertEquals("参数错误！", ResultCode.BAD_REQUEST, testPaperRest.getTestPaperAllInfo(request, headers).getResultCode());

        request.setId(10L);
        PowerMockito.when(testPaperPartService.getPartInfoListByTestInfoId(Mockito.anyLong())).thenReturn(null);
        Assert.assertEquals("试卷信息获取失败！", ResultCode.ERROR, testPaperRest.getTestPaperAllInfo(request, headers).getResultCode());

        List<TestPaperPart> testPaperPartList = Lists.newArrayList();
        TestPaperPart testPaperPart = new TestPaperPart();
        testPaperPartList.add(testPaperPart);
        PowerMockito.when(testPaperPartService.getPartInfoListByTestInfoId(Mockito.anyLong())).thenReturn(testPaperPartList);
        Assert.assertEquals("试卷信息获取成功！", ResultCode.SUCCESS, testPaperRest.getTestPaperAllInfo(request, headers).getResultCode());
    }

    /**
     * 抽题
     */
    @Test
    public void testExtractItem() throws Exception {
        Assert.assertEquals("参数不正确！", ResultCode.BAD_REQUEST, testPaperRest.extractItem(null, headers).getResultCode());

        //构造所需参数
        BatchExtract batchEntity = new BatchExtract();
        List<ExtractEntity> objList = Lists.newArrayList();
        ExtractEntity extractEntity = new ExtractEntity();
        extractEntity.setQuantity(1);
        extractEntity.setItemType(1);
        extractEntity.setItemBankId(1L);
        objList.add(extractEntity);
        Long[] existItemIds = new Long[1];
        existItemIds[0] = 1L;
        batchEntity.setObjList(objList);
        batchEntity.setExistItemIds(existItemIds);

        ItemBank itemBank = new ItemBank();
        itemBank.setItemBankName("java");
        PowerMockito.when(itemBankService.getItemBankById(Mockito.anyLong())).thenReturn(itemBank);

        List<Item> itemList = Lists.newArrayList();//即itemCount
        Item item = new Item();
        item.setItemId(1);
        itemList.add(item);
        PowerMockito.when(itemService.getItemByItemTypeAndItemBank(Mockito.anyLong(), Mockito.anyInt())).thenReturn(itemList);
        Assert.assertEquals("题目数不足！", ResultCode.ERROR, testPaperRest.extractItem(batchEntity, headers).getResultCode());

        Item item1 = new Item();
        Item item2 = new Item();
        itemList.add(item1);
        itemList.add(item2);
        Assert.assertEquals("抽取题目成功！", ResultCode.SUCCESS, testPaperRest.extractItem(batchEntity, headers).getResultCode());
    }

    /**
     * 创建试卷
     */
    @Test
    public void testCreateTestpaper() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        Assert.assertEquals("试卷信息不正确！", ResultCode.BAD_REQUEST, testPaperRest.createTestpaper(request, headers).getResultCode());

        TestPaper testPaper = new TestPaper();
        testPaper.setTestpaperName("java");
        request.setTestPaper(testPaper);
        PowerMockito.when(testPaperService.getTestPaperByName(Mockito.anyString())).thenReturn(null);
        Assert.assertEquals("试卷没有模块信息！", ResultCode.ERROR, testPaperRest.createTestpaper(request, headers).getResultCode());

        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaper.setTestPaperParts(testPaperParts);
        Assert.assertEquals("试卷创建失败！", ResultCode.ERROR, testPaperRest.createTestpaper(request, headers).getResultCode());

        PowerMockito.when(testPaperService.createTestpaper(testPaper)).thenReturn(true);
        Assert.assertEquals("试卷创建成功！", ResultCode.SUCCESS, testPaperRest.createTestpaper(request, headers).getResultCode());

        PowerMockito.when(testPaperService.getTestPaperByName(Mockito.anyString())).thenReturn(new TestPaper());
        Assert.assertEquals("试卷名称已存在！", ResultCode.RECORD_EXIST, testPaperRest.createTestpaper(request, headers).getResultCode());
    }

    /**
     * 获取试卷信息和每部分的详细信息
     */
    @Test
    public void testGetTestPaperWithPart() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        request.setId(0L);
        Assert.assertEquals("参数错误！", ResultCode.BAD_REQUEST, testPaperRest.getTestPaperWithPart(request, headers).getResultCode());

        request.setId(1L);
        PowerMockito.when(testPaperService.getTestPaperById(Mockito.anyLong())).thenReturn(null);
        Assert.assertEquals("试卷信息获取失败！", ResultCode.ERROR, testPaperRest.getTestPaperWithPart(request, headers).getResultCode());

        PowerMockito.when(testPaperService.getTestPaperById(Mockito.anyLong())).thenReturn(new TestPaper());
        Assert.assertEquals("试卷信息获取成功！", ResultCode.SUCCESS, testPaperRest.getTestPaperWithPart(request, headers).getResultCode());
    }

    /**
     * 修改试卷
     */
    @Test
    public void testUpdateTestPaper() throws Exception {
        TestPaperRequest request = new TestPaperRequest();
        Assert.assertEquals("试卷信息不正确！", ResultCode.BAD_REQUEST, testPaperRest.updateTestPaper(request, headers).getResultCode());

        TestPaper testPaper = new TestPaper();
        testPaper.setTestpaperName("java");
        testPaper.setId(2L);
        request.setTestPaper(testPaper);
        TestPaper originTestPaper = new TestPaper();
        originTestPaper.setTestpaperName("c++");
        PowerMockito.when(testPaperService.getTestPaperById(Mockito.anyLong())).thenReturn(originTestPaper);
        PowerMockito.when(testPaperService.getTestPaperByName(Mockito.anyString())).thenReturn(null);
        Assert.assertEquals("试卷没有模块信息！", ResultCode.ERROR, testPaperRest.updateTestPaper(request, headers).getResultCode());

        TestPaperPart[] testPaperParts = new TestPaperPart[1];
        testPaper.setTestPaperParts(testPaperParts);
        Assert.assertEquals("试卷修改失败！", ResultCode.ERROR, testPaperRest.updateTestPaper(request, headers).getResultCode());

        PowerMockito.when(testPaperService.updateTestpaperAndPart(testPaper)).thenReturn(true);
        Assert.assertEquals("试卷修改成功！", ResultCode.SUCCESS, testPaperRest.updateTestPaper(request, headers).getResultCode());

        PowerMockito.when(testPaperService.getTestPaperByName(Mockito.anyString())).thenReturn(new TestPaper());
        Assert.assertEquals("试卷名称已存在！", ResultCode.RECORD_EXIST, testPaperRest.updateTestPaper(request, headers).getResultCode());
    }
}