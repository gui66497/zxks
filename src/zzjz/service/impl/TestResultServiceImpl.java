package zzjz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.TestInfo;
import zzjz.bean.TestResultEntity;
import zzjz.bean.UserScoreEntity;
import zzjz.service.TestResultService;
import zzjz.util.MyJdbcTemplate;
import zzjz.util.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 梅宏振
 * @version 2015年6月16日 下午7:32:29
 * @ClassName: TestResultServiceImpl
 * @Description: 考试成绩管理接口实现类
 */
@Service
public class TestResultServiceImpl implements TestResultService {

    private static Logger logger = Logger.getLogger(TestResultServiceImpl.class);

    private static String strMess = "考试成绩总数出现异常：";

    @Autowired
    private MyJdbcTemplate myJdbcTemplate;

    /**
     * 获取考试结果列表.
     *
     * @param testInfo     考试信息实体
     * @param pagingEntity 分页实体类
     * @return List
     * @throws Exception 异常
     */
    public List<TestResultEntity> getTestReusltList(TestInfo testInfo,
                                                    PagingEntity pagingEntity) {

        int startIndex = pagingEntity.getStartIndex();      //开始位置
        int pageSize = pagingEntity.getPageSize();          //每页大小
        String sortColumn = pagingEntity.getSortColumn();   //排序字段
        String sortDir = pagingEntity.getSortDir();         //排序方式
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        searchValue = StringUtil.zhuanyi(searchValue);
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TI.ID,TI.TEST_ID,TI.TOTAL_USER_NUM,TI.TEST_USER_NUM," +
                "TI.TEST_START_TIME,TI.TEST_END_TIME,TP.TESTPAPER_NAME," +
                "TP.TESTPAPER_MODE,TP.MARK_TOTAL,TP.TEST_TIME,TP.CATEGORY_ID," +
                "TP.CATEGORY_NAME FROM tb_testinfo TI ")
                .append("LEFT JOIN (SELECT TP1.ID,TP1.CATEGORY_ID,TP1.MARK_TOTAL," +
                        "TP1.TEST_TIME,TP2.CATEGORY_NAME,TP1.TESTPAPER_NAME," +
                        "TP1.TESTPAPER_MODE FROM tb_testpaper TP1 INNER JOIN" +
                        " tb_testpapercategory TP2 ON TP1.CATEGORY_ID=TP2.ID" +
                        " WHERE TP1.DELETE_FLAG=0 AND TP2.DELETE_FLAG=0) TP ON" +
                        " TI.TEST_ID=TP.ID ")
                .append("WHERE TI.DELETE_FLAG=0 ");
        if (StringUtils.isNotBlank(searchValue)) {
            sql.append("AND TP.TESTPAPER_NAME LIKE '%" + searchValue + "%' ");
        }

        if (StringUtils.isNotBlank(testInfo.getTestCategory())) {
            sql.append(" AND TP.CATEGORY_NAME like '%" + testInfo.getTestCategory() + "%'");
        }
        if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
            if ("1".equals(sortColumn)) {
                sql.append("ORDER BY TP.TESTPAPER_NAME " + sortDir);
            } else if ("6".equals(sortColumn)) {
                sql.append("ORDER BY TI.TEST_START_TIME " + sortDir);
            }
        }
        sql.append(" LIMIT " + startIndex + "," + pageSize);

        logger.debug("考试成绩查询执行SQL语句：" + sql.toString());

        return myJdbcTemplate.query(sql.toString(), new TestRestMapper());

    }

    /**
     * 获取考试结果总数.
     *
     * @param testInfo     考试信息实体
     * @param pagingEntity 分页实体类
     * @return int
     * @throws DataAccessException 异常
     */
    public int getTestReusltCount(TestInfo testInfo,
                                  PagingEntity pagingEntity) throws DataAccessException {
        try {
            String searchValue = pagingEntity.getSearchValue(); //检索内容
            searchValue = StringUtil.zhuanyi(searchValue);
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT COUNT(1) FROM tb_testinfo TI ")
                    .append("LEFT JOIN (SELECT TP1.ID,TP1.CATEGORY_ID,TP1.MARK_TOTAL," +
                            "TP1.TEST_TIME,TP2.CATEGORY_NAME,TP1.TESTPAPER_NAME," +
                            "TP1.TESTPAPER_MODE FROM tb_testpaper TP1 INNER JOIN" +
                            " tb_testpapercategory TP2 ON TP1.CATEGORY_ID=TP2.ID" +
                            " WHERE TP1.DELETE_FLAG=0 AND TP2.DELETE_FLAG=0) TP ON" +
                            " TI.TEST_ID=TP.ID ")
                    .append("WHERE TI.DELETE_FLAG=0 ");
            if (StringUtils.isNotBlank(searchValue)) {
                sql.append("AND TP.TESTPAPER_NAME LIKE '%" + searchValue + "%' ");
            }
            if (StringUtils.isNotBlank(testInfo.getTestCategory())) {
                sql.append(" AND TP.CATEGORY_NAME like '%" + testInfo.getTestCategory() + "%'");
            }
            logger.debug("考试成绩总数执行SQL语句：" + sql.toString());
            return myJdbcTemplate.queryForObject(sql.toString(), Integer.class);
        } catch (DataAccessException e) {
            logger.debug(strMess + e.getMessage());
            return 0;
        }
    }

    /**
     * 获取用户考试分数列表.
     *
     * @param id           Id
     * @param pagingEntity 分页实体类
     * @return List
     * @throws DataAccessException 异常
     */
    public List<UserScoreEntity> getTestUserScoreList(long id,
                                                      PagingEntity pagingEntity) throws DataAccessException {
        try {
            int startIndex = pagingEntity.getStartIndex();      //开始位置
            int pageSize = pagingEntity.getPageSize();          //每页大小
            String sortColumn = pagingEntity.getSortColumn();   //排序字段
            String sortDir = pagingEntity.getSortDir();         //排序方式
            String searchValue = pagingEntity.getSearchValue(); //检索内容
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT TA.ID,TP.TESTPAPER_NAME,TA.USER_ID,TU.USERNAME," +
                    "TU.REALNAME,TU.GENDER,TA.USER_MARK,TA.ACHIEVEMENT_ID AS ACHIEVEMENT_ID," +
                    "TP.MARK_TOTAL FROM tb_achievement TA ," +
                    "tb_testinfo TI,tb_user TU,tb_testpaper TP ")
                    .append("WHERE TA.DELETE_FLAG=0 AND TI.DELETE_FLAG=0 AND TU.DELETE_FLAG=0" +
                            " AND TP.DELETE_FLAG=0 AND TESTINFO_ID=" + id + " AND" +
                            " TA.TESTINFO_ID=TI.ID AND TA.USER_ID=TU.USERID AND TI.TEST_ID=TP.ID ");

            if (StringUtils.isNotBlank(searchValue)) {
                sql.append("AND (TU.USERNAME LIKE '%" + searchValue + "%' OR TU.REALNAME LIKE '%"
                        + searchValue + "%')");
            }
            if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
                if ("2".equals(sortColumn)) {
                    sql.append("ORDER BY TU.USERNAME " + sortDir);
                } else if ("3".equals(sortColumn)) {
                    sql.append("ORDER BY convert(TU.REALNAME USING GBK) " + sortDir);
                } else if ("4".equals(sortColumn)) {
                    sql.append("ORDER BY TU.GENDER " + sortDir);
                } else if ("6".equals(sortColumn)) {
                    sql.append("ORDER BY TA.USER_MARK " + sortDir);
                }
            }
            sql.append(" LIMIT " + startIndex + "," + (startIndex + pageSize));

            logger.debug("考生考试成绩列表查询执行SQL语句：" + sql.toString());

            return myJdbcTemplate.query(sql.toString(), new TestUserScoreMapper());
        } catch (DataAccessException e) {
            logger.debug(strMess + e.getMessage());
            return null;
        }

    }

    /**
     * 获取用户考试分数总数.
     *
     * @param id           Id
     * @param pagingEntity 分页实体类
     * @return int
     * @throws DataAccessException 异常
     */
    public int getTestUserScoreCount(long id, PagingEntity pagingEntity)
            throws DataAccessException {
        try {
            /*int startIndex = pagingEntity.getStartIndex();      //开始位置
            int pageSize = pagingEntity.getPageSize();          //每页大小
            String sortColumn = pagingEntity.getSortColumn();   //排序字段
            String sortDir = pagingEntity.getSortDir();         //排序方式*/
            String searchValue = pagingEntity.getSearchValue(); //检索内容
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT COUNT(1) FROM tb_achievement TA ,tb_testinfo TI," +
                    "TB_USER TU,tb_testpaper TP ")
                    .append("WHERE TA.DELETE_FLAG=0 AND TI.DELETE_FLAG=0 AND" +
                            " TU.DELETE_FLAG=0 AND TP.DELETE_FLAG=0 AND" +
                            " TESTINFO_ID=" + id + " AND TA.TESTINFO_ID=TI.ID AND" +
                            " TA.USER_ID=TU.USERID AND TI.TEST_ID=TP.ID ");
            if (StringUtils.isNotBlank(searchValue)) {
                sql.append("AND (TU.USERNAME LIKE '%" + searchValue + "%' OR" +
                        " TU.REALNAME LIKE '%" + searchValue + "%')");
            }
            logger.debug("考生考试成绩列表总数查询执行SQL语句：" + sql.toString());
            return myJdbcTemplate.queryForObject(sql.toString(), Integer.class);
        } catch (DataAccessException e) {
            logger.debug(strMess + e.getMessage());
            return 0;
        }
    }

    /**
     * 编辑用户考试人数.
     *
     * @param id Id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateTestUserNum(long id) throws DataAccessException {
        try {
            StringBuffer sql = new StringBuffer("UPDATE tb_testinfo" +
                    " SET TEST_USER_NUM=TEST_USER_NUM+1 WHERE ID=").append(id);
            int updateRecord = myJdbcTemplate.update(sql.toString());
            if (updateRecord > 0) {
                return true;
            }
        } catch (DataAccessException e) {
            logger.debug("更新指定考试的参考人数出现异常：" + e.getMessage());
            return false;
        }
        return false;
    }

    private static final class TestRestMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestResultEntity testResultEntity = new TestResultEntity();
            testResultEntity.setTestInfoId(rs.getLong("ID"));
            testResultEntity.setTestPaperId(rs.getInt("TEST_ID"));
            testResultEntity.setTestPaperName(rs.getString("TESTPAPER_NAME"));
            testResultEntity.setTestPaperModel(rs.getInt("TESTPAPER_MODE"));
            testResultEntity.setTestPaperCategoryId(rs.getInt("CATEGORY_ID"));
            testResultEntity.setTestPaperCategoryName(rs.getString("CATEGORY_NAME"));
            testResultEntity.setTotalScore(rs.getInt("MARK_TOTAL"));
            testResultEntity.setTotalTime(rs.getInt("TEST_TIME"));
            testResultEntity.setTotalUserNum(rs.getInt("TOTAL_USER_NUM"));
            testResultEntity.setTestUserNum(rs.getInt("TEST_USER_NUM"));
            testResultEntity.setStartTime(rs.getTimestamp("TEST_START_TIME"));
            testResultEntity.setEndTime(rs.getTimestamp("TEST_END_TIME"));
            return testResultEntity;
        }
    }

    private static final class TestUserScoreMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserScoreEntity userScoreEntity = new UserScoreEntity();
            userScoreEntity.setUserId(rs.getLong("USER_ID"));
            userScoreEntity.setUserName(rs.getString("USERNAME"));
            userScoreEntity.setRealName(rs.getString("REALNAME"));
            userScoreEntity.setGender(rs.getInt("GENDER"));
            userScoreEntity.setAchievementId(rs.getString("ACHIEVEMENT_ID"));
            userScoreEntity.setTestPaperName(rs.getString("TESTPAPER_NAME"));
            userScoreEntity.setTotalScore(rs.getInt("MARK_TOTAL"));
            userScoreEntity.setScore(rs.getInt("USER_MARK"));
            return userScoreEntity;
        }
    }
}
