package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.TestPaperPart;
import zzjz.bean.Item;
import zzjz.bean.ItemOption;
import zzjz.bean.ResultInfo;
import zzjz.bean.TestPaper;
import zzjz.service.ItemService;
import zzjz.service.TestPaperPartService;
import zzjz.util.MyJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/6/14 20:14
 * @ClassName: TestPaperPartServiceImpl
 * @Description: 试卷每部分操作service接口实现类
 */
@Service
public class TestPaperPartServiceImpl implements TestPaperPartService {

    /**
     * 注入MyJdbcTemplate.
     */
    @Autowired
    public MyJdbcTemplate myJdbcTemplate;

    /**
     * 注入ItemService.
     */
    @Autowired
    public ItemService itemService;

    /**
     * 根据试卷ID获取模块信息列表.
     *
     * @param testPaperId 试卷id
     * @return List
     */
    public List<TestPaperPart> getPartInfoListByTestPaperId(long testPaperId) {
        String sql = "SELECT t1.TESTPAPERPART_ID,t1.PART_EXPLAIN,t1.PART_NAME," +
                "t1.TESTPAPER_ID,t1.PART_MARK,t1.PART_ORDER,t2.TESTPAPER_DESCRIPTION,t2.TESTPAPER_NAME," +
                "t2.TESTPAPER_EXPLAIN,t2.MARK_TOTAL,t2.TEST_TIME FROM tb_testpaperpart" +
                " t1 LEFT JOIN tb_testpaper t2 " +
                "ON t1.TESTPAPER_ID = t2.ID WHERE t1.DELETE_FLAG=0 AND" +
                " t1.TESTPAPER_ID=" + testPaperId;
        List<TestPaperPart> testPaperPartList =
                myJdbcTemplate.query(sql, new TestPaperPartMapper());
        for (TestPaperPart part : testPaperPartList) {
            long partId = part.getPartId();
            List<Item> itemList = itemService.getItemByPartId(partId);
            for (Item item : itemList) {
                long itemId = item.getItemId();
                List<ItemOption> optionList = itemService.getItemOptions(itemId);
                item.setOptionList(optionList); //添加选项列表
            }
            part.setItem(itemList); //添加信息列表
            part.setPartItemCounts(itemList.size()); //添加题目数
        }
        return testPaperPartList;
    }

    /**
     * 根据考试ID获取模块信息列表.
     *
     * @param testInfoId 考试信息ID
     * @return List
     */
    public List<TestPaperPart> getPartInfoListByTestInfoId(long testInfoId) {
        String sql = "SELECT t1.TESTPAPERPART_ID,t1.PART_NAME,t1.PART_EXPLAIN," +
                "t1.TESTPAPER_ID,t1.PART_MARK,t1.PART_ORDER,t2.TESTPAPER_NAME,t2.TESTPAPER_DESCRIPTION," +
                "t2.TESTPAPER_EXPLAIN,t2.MARK_TOTAL,t2.TEST_TIME FROM tb_testpaperpart t1" +
                " LEFT JOIN tb_testpaper t2 " +
                "ON t1.TESTPAPER_ID = t2.ID WHERE t1.DELETE_FLAG=0 AND" +
                " t1.TESTPAPER_ID=(SELECT TEST_ID FROM tb_testinfo WHERE ID = " +
                testInfoId + " AND DELETE_FLAG = 0 )";
        List<TestPaperPart> testPaperPartList =
                myJdbcTemplate.query(sql, new TestPaperPartMapper());
        for (TestPaperPart part : testPaperPartList) {
            long partId = part.getPartId();
            List<Item> itemList = itemService.getItemByPartId(partId);
            for (Item item : itemList) {
                long itemId = item.getItemId();
                List<ItemOption> optionList = itemService.getItemOptions(itemId);
                item.setOptionList(optionList); //添加选项列表
            }
            part.setItem(itemList); //添加信息列表
            part.setPartItemCounts(itemList.size()); //添加题目数
        }
        return testPaperPartList;
    }

    /**
     * 根据成绩ID获取模块信息列表.
     *
     * @param achieveMentId 成绩单信息ID
     * @return List
     */
    public List<TestPaperPart> getPartInfoListByAchieveMentId(long achieveMentId) {
        String sql = "SELECT t1.TESTPAPERPART_ID,t1.PART_NAME,t1.PART_EXPLAIN," +
                "t1.TESTPAPER_ID,t1.PART_MARK,t1.PART_ORDER,t2.TESTPAPER_NAME,t2.TESTPAPER_DESCRIPTION," +
                "t2.TESTPAPER_EXPLAIN,t2.MARK_TOTAL,t2.TEST_TIME FROM tb_testpaperpart t1" +
                " LEFT JOIN tb_testpaper t2 " +
                " ON t1.TESTPAPER_ID = t2.ID WHERE t1.DELETE_FLAG=0 AND" +
                " t1.TESTPAPER_ID=(SELECT TEST_ID FROM tb_testinfo " +
                " WHERE ID = (SELECT TESTINFO_ID FROM tb_achievement" +
                " WHERE ACHIEVEMENT_ID=" + achieveMentId + " AND DELETE_FLAG = 0)" +
                " AND DELETE_FLAG = 0 )";
        List<TestPaperPart> testPaperPartList =
                myJdbcTemplate.query(sql, new TestPaperPartMapper());
        for (TestPaperPart part : testPaperPartList) {
            long partId = part.getPartId();
            List<ResultInfo> resultInfoList =
                    itemService.getItemByPartId(partId, achieveMentId);
            for (ResultInfo resultInfo : resultInfoList) {
                Item item = resultInfo.getItem();
                long itemId = item.getItemId();
                List<ItemOption> optionList = itemService.getItemOptions(itemId);
                item.setOptionList(optionList); //添加选项列表
            }
            part.setResultInfoList(resultInfoList); //添加结果信息和题目信息的列表
            part.setPartItemCounts(resultInfoList.size()); //添加题目数
        }
        return testPaperPartList;
    }


    @SuppressWarnings("rawtypes")
    private static final class TestPaperPartMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestPaperPart part = new TestPaperPart();
            part.setPartId(rs.getLong("TESTPAPERPART_ID"));
            part.setPartName(rs.getString("PART_NAME"));
            part.setPartExplain(rs.getString("PART_EXPLAIN"));
            part.setTestPaperId(rs.getLong("TESTPAPER_ID"));
            part.setPartMark(rs.getInt("PART_MARK"));
            part.setPartOrder(rs.getInt("PART_ORDER"));
            //试卷信息
            TestPaper testPaper = new TestPaper();
            testPaper.setTestpaperName(rs.getString("TESTPAPER_NAME"));
            testPaper.setTestpaperDescription(rs.getString("TESTPAPER_DESCRIPTION"));
            testPaper.setTestpaperExplain(rs.getString("TESTPAPER_EXPLAIN"));
            testPaper.setMarkTotal(rs.getInt("MARK_TOTAL"));
            testPaper.setTestTime(rs.getInt("TEST_TIME"));
            part.setTestPaper(testPaper);
            return part;
        }
    }
}
