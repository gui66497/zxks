package zzjz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.TestInfo;
import zzjz.bean.User;
import zzjz.service.TestInfoService;
import zzjz.util.MyJdbcTemplate;
import zzjz.util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaoxiao
 * @version 2016/5/30 15:18
 * @ClassName: TestInfoServiceImpl
 * @Description: 考试信息Service实现类
 */
@Service
public class TestInfoServiceImpl implements TestInfoService {

    /**
     * 注入MyJdbcTemplate.
     */
    @Autowired
    public MyJdbcTemplate jdbcTemplate;

    private static String strMess = " INNER JOIN tb_testpaper tp ON ti.TEST_ID = tp.ID ";

    private static String strMess1 = ",";

    private static String strMess2 = "%'";

    private static String strMess3 = " ORDER BY ";

    private static String strMess4 = " ";

    private static String strSql2 = "TOTAL_USER_NUM";

    private static String strMess6 = "TEST_USER_NUM";

    private static String strMess7 = "TEST_ID";

    private static String strMess8 = "TEST_START_TIME";

    private static String strMess9 = "TEST_END_TIME";

    private static String strSql = "'";

    private static String strMess5 = " LIMIT ";

    private static String strSql3 = "yyyy-MM-dd HH:mm";

    private static String strSql4 = "TEST_STATUS";

    private static String strSql5 = "CREATOR";

    private static String strSql6 = "CREATE_TIME";

    private static String strSql7 = "UPDATE_TIME";

    private static String strSql8 = "DELETE_FLAG";

    private static String strArr1 = "TID";

    private static String strArr2 = "TPNAME";

    private static String strArr3 = "TESTHOUR";

    private static String strArr4 = "TOTALMARK";

    private static final int NUM_THREE = 3;

    private static final int NUM_THOUSAND = 100;


    /**
     * 获取考试信息.
     *
     * @param userId       用户ID
     * @param pagingEntity 分页排序
     * @return List
     */
    public List<TestInfo> getTestInfo(String userId, PagingEntity pagingEntity) {
        int startIndex = pagingEntity.getStartIndex();      //开始位置
        int pageSize = pagingEntity.getPageSize();          //每页大小
        String sortColumn = pagingEntity.getSortColumn();   //排序字段
        String sortDir = pagingEntity.getSortDir();         //排序方式
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        String sortSql = "";                                //排序sql片段
        String searchSql = "";                              //检索sql片段
        if (StringUtils.isBlank(userId)) {
            return new ArrayList<TestInfo>();
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select ti.ID AS TID ,ti.TEST_ID,titu.USER_ID AS UID," +
                "tp.MARK_TOTAL AS TOTALMARK," +
                "tp.ID AS PID,tp.TESTPAPER_NAME AS TPNAME,tp.TEST_TIME AS TESTHOUR," +
                "tp.TESTPAPER_MODE AS PAPERMODE, ti.TOTAL_USER_NUM,ti.TEST_USER_NUM," +
                "ti.TEST_STATUS,")
                .append(" ti.TEST_START_TIME,ti.TEST_END_TIME,ti.CREATOR,ti.CREATE_TIME," +
                        "ti.UPDATE_TIME,ti.DELETE_FLAG from tb_testinfo ti ")
                .append(strMess)
                .append(" INNER JOIN tb_testinfotouser titu ON ti.ID = titu.TESTINFO_ID ")
                .append(" WHERE titu.USER_ID='" + userId + "' and" +
                        " ti.DELETE_FLAG=0 " +
                        "and titu.DELETE_FLAG = 0 and tp.DELETE_FLAG = 0 ");


        //试卷名称检索
        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND " +
                    "tp.TESTPAPER_NAME like'%" + searchValue + strMess2);
        }
        if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
            sql.append(strMess3 + sortColumn + strMess4 + sortDir);
        }
        sql.append(strMess5 + startIndex + strMess1 + pageSize);
        List<TestInfo> list = this.jdbcTemplate.query(sql.toString(), new TestInfoMapper());
        return list;
    }

    /**
     * 添加考试信息.
     *
     * @param testInfo 试卷实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addTestInfo(TestInfo testInfo) throws DataAccessException {
        String sql = "insert into tb_testinfo (ID,TEST_ID,TOTAL_USER_NUM,TEST_USER_NUM," +
                "TEST_STATUS,TEST_START_TIME,TEST_END_TIME,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG)" +
                "values(" + testInfo.getId() + strMess1 + testInfo.getTestId() + strMess1 +
                testInfo.getTotalUserNo() + strMess1
                + testInfo.getTestUserNo() + strMess1 + testInfo.getTestStatus() + ",'" +
                testInfo.getStartTime() + "','"
                + testInfo.getEndTime() + "',"
                + testInfo.getCreator() + ",NOW(),NOW(),'0')";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 编辑考试信息.
     *
     * @param testInfo 试卷实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateTestInfo(TestInfo testInfo) throws DataAccessException {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("UPDATE tb_testinfo SET UPDATE_TIME = NOW() , ");
        strBuilder = initStringBuilder(strBuilder, strSql2,
                String.valueOf(testInfo.getTotalUserNo()), true);
        strBuilder = initStringBuilder(strBuilder, strMess6,
                String.valueOf(testInfo.getTestUserNo()), true);
        strBuilder = initStringBuilder(strBuilder, strMess7, testInfo.getTestId(), false);
        strBuilder = initStringBuilder(strBuilder,
                strMess8, testInfo.getStartTime(), true);
        strBuilder = initStringBuilder(strBuilder, strMess9, testInfo.getEndTime(), true);
        strBuilder.append(" TEST_END_TIME = '" + testInfo.getEndTime() + strSql);
        strBuilder.append(" WHERE ID = " + testInfo.getId());
        try {
            int affectCount = jdbcTemplate.update(strBuilder.toString());
            if (affectCount == 1) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除考试以及考生关联信息.
     *
     * @param testInfo 试卷实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteTestInfoAndUserConnection(TestInfo testInfo) throws DataAccessException {

        String sql = "UPDATE tb_testinfotouser  SET DELETE_FLAG = 1" +
                " WHERE TESTINFO_ID =" + testInfo.getId();
        try {
            int count = jdbcTemplate.update(sql);
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加考试以及考生关联信息.
     *
     * @param testInfo 试卷实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addTestInfoAndUserConnection(TestInfo testInfo) throws DataAccessException {
        try {
            //添加数据至考试用户关联表
            int[] insertResult = addUsersAndTestConnection(testInfo.getUserIdList(),
                    testInfo.getCreator(), String.valueOf(testInfo.getId()));
            if (insertResult.length > 0) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取成绩信息.
     *
     * @param userId       用户ID
     * @param pagingEntity 分页排序
     * @return List
     */
    public List<TestInfo> getPointInfo(String userId, PagingEntity pagingEntity) {
        int startIndex = pagingEntity.getStartIndex();      //开始位置
        int pageSize = pagingEntity.getPageSize();          //每页大小
        String sortColumn = pagingEntity.getSortColumn();   //排序字段
        String sortDir = pagingEntity.getSortDir();         //排序方式
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        String sortSql = "";                                //排序sql片段
        String searchSql = "";                              //检索sql片段

        if (StringUtils.isBlank(userId)) {
            return new ArrayList<TestInfo>();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select ti.ID AS TID ,ti.TEST_ID,am.USER_ID AS UID,am.USER_MARK AS USERMARK," +
                "am.ACHIEVEMENT_ID,tp.MARK_TOTAL AS TOTALMARK,tp.ID AS PID," +
                "tp.TESTPAPER_NAME AS TPNAME,tp.TEST_TIME AS TESTHOUR," +
                "tp.TESTPAPER_MODE AS PAPERMODE, ti.TOTAL_USER_NUM,ti.TEST_USER_NUM," +
                "ti.TEST_STATUS,")
                .append(" ti.TEST_START_TIME,ti.TEST_END_TIME,ti.CREATOR,ti.CREATE_TIME," +
                        "ti.UPDATE_TIME,ti.DELETE_FLAG from tb_achievement am ")
                .append(" INNER JOIN tb_testinfo ti ON am.TESTINFO_ID  = ti.ID ")
                .append(strMess)
                .append(" WHERE am.USER_ID='" + userId + "' and" +
                        " ti.DELETE_FLAG=0 " +
                        "and tp.DELETE_FLAG =0 and am.DELETE_FLAG=0");

        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND" +
                    " tp.TESTPAPER_NAME like'%" + searchValue + strMess2);
        }
        if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
            sql.append(strMess3 + sortColumn + strMess4 + sortDir);
        }
        sql.append(strMess5 + startIndex + strMess1 + pageSize);
        List<TestInfo> list = this.jdbcTemplate.query(sql.toString(), new PointInfoMapper());
        return list;

    }

    /**
     * 获取考试信息条数.
     *
     * @param userId       用户ID
     * @param pagingEntity 分页排序
     * @return int
     */
    public int getTestInfoCount(String userId, PagingEntity pagingEntity) {
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        if (StringUtils.isBlank(userId)) {
            return 1;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from tb_testinfo ti ")
                .append(strMess)
                .append(" INNER JOIN tb_testinfotouser titu ON ti.ID = titu.TESTINFO_ID ")
                .append(" WHERE titu.USER_ID='" + userId + "' and ti.DELETE_FLAG=0 and titu.DELETE_FLAG=0");

        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND tp.TESTPAPER_NAME like'%" + searchValue + strMess2);
        }
        List<Integer> counts = jdbcTemplate.queryForList(sql.toString(), Integer.class);
        if (counts != null) {
            return counts.get(0);
        }
        return 0;
    }

    /**
     * 获取成绩信息总数.
     *
     * @param userId       用户ID
     * @param pagingEntity 分页排序
     * @return int
     */
    public int getPointInfoCount(String userId, PagingEntity pagingEntity) {
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        if (StringUtils.isBlank(userId)) {
            return 1;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from tb_achievement am ")
                .append(" INNER JOIN tb_testinfo ti ON am.TESTINFO_ID  = ti.ID ")
                .append(strMess)
                .append(" WHERE am.USER_ID='" + userId + "' and ti.DELETE_FLAG=0 ");

        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND tp.TESTPAPER_NAME like'%" + searchValue + strMess2);
        }
        List<Integer> counts = jdbcTemplate.queryForList(sql.toString(), Integer.class);
        if (counts != null) {
            return counts.get(0);
        }
        return 0;
    }

    /**
     * 获取已过滤的考试信息列表.
     *
     * @param testInfo     试卷实体
     * @param pagingEntity 分页排序
     * @return List
     * @throws DataAccessException 异常
     */
    public List<TestInfo> getFilteredTestInfo(TestInfo testInfo, PagingEntity pagingEntity) throws DataAccessException {
        int startIndex = pagingEntity.getStartIndex();      //开始位置
        int pageSize = pagingEntity.getPageSize();          //每页大小
        String sortColumn = pagingEntity.getSortColumn();   //排序字段
        String sortDir = pagingEntity.getSortDir();         //排序方式
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        searchValue = StringUtil.zhuanyi(searchValue);
        //String sortSql = "";                                //排序sql片段
        //String searchSql = "";                              //检索sql片段

        StringBuffer sql = new StringBuffer();
        sql.append("select ti.ID AS TID ,ti.TEST_ID,tp.MARK_TOTAL AS TOTALMARK,")
                .append(" tp.ID AS PID,tp.TESTPAPER_NAME AS TPNAME,tp.TEST_TIME AS TESTHOUR,")
                .append(" ti.TOTAL_USER_NUM,")
                .append(" ti.TEST_USER_NUM,ti.TEST_STATUS,")
                .append(" ti.TEST_START_TIME,ti.TEST_END_TIME,ti.CREATOR," +
                        "ti.CREATE_TIME,ti.UPDATE_TIME,")
                .append(" ti.DELETE_FLAG,tiCategory.CATEGORY_NAME as CATEGORY_NAME, ")
                .append(" tiCategory.ID as CATEGORY_ID  from tb_testinfo ti ")
                .append(strMess +
                        " and tp.DELETE_FLAG = 0 ");
        //试卷名称检索
        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND tp.TESTPAPER_NAME like '%" + searchValue + strMess2);
        }
        sql.append(" INNER JOIN tb_testpapercategory tiCategory ON " +
                "tp.CATEGORY_ID = tiCategory.ID AND tiCategory.DELETE_FLAG = 0 ");
        //试卷类别检索
        if (StringUtils.isNotBlank(testInfo.getTestCategory())) {
            sql.append(" AND" +
                    " tiCategory.CATEGORY_NAME ='" + testInfo.getTestCategory() + strSql);
        }
        sql.append(" WHERE ti.DELETE_FLAG=0 ");
        if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
            sql.append(strMess3 + sortColumn + strMess4 + sortDir);

        }
        sql.append(strMess5 + startIndex + strMess1 + pageSize);
        try {
            return this.jdbcTemplate.query(sql.toString(), new TestInfoMapperMultiple());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据考试信息ID获取考试信息列表.
     *
     * @param id 考试ID
     * @return List
     * @throws DataAccessException 异常
     */
    public List<TestInfo> getTestInfoById(long id) throws DataAccessException {
        String sql = "select ID ,TEST_ID,TEST_USER_NUM,TOTAL_USER_NUM,"
                + "TEST_STATUS,TEST_START_TIME,TEST_END_TIME,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG from tb_testinfo " +
                "where DELETE_FLAG=0 AND ID =" + id;
        try {
            List<TestInfo> lstTestInfo = jdbcTemplate.query(sql, new TestInfoMapperSingle());
            if (lstTestInfo != null && lstTestInfo.size() > 0) {
                lstTestInfo.get(0).setUserList(getUserListByTestId(lstTestInfo.get(0).getId()));
                return lstTestInfo;
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据考试信息ID删除考试信息.
     *
     * @param id 考试ID
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteTestInfoById(long id) throws DataAccessException {
        String sql = "UPDATE tb_testinfo  SET DELETE_FLAG = 1 WHERE ID =" + id;
        try {
            int affectCount = jdbcTemplate.update(sql);
            if (affectCount > 0) {
                //删除考试以及考生关联信息
                String sql1 = "UPDATE tb_testinfotouser " +
                        " SET DELETE_FLAG = 1 WHERE TESTINFO_ID =" + id;
                try {
                    int count = jdbcTemplate.update(sql1);
                    if (count > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据试卷ID获取考试信息和时间.
     *
     * @param testInfo 考试实体
     * @return TestInfo
     * @throws DataAccessException 异常
     */
    public TestInfo getTestInfoByTestIdAndTime(TestInfo testInfo) throws DataAccessException {
        String sql = "select ID ,TEST_ID,TOTAL_USER_NUM,TEST_USER_NUM,"
                + "TEST_STATUS,TEST_START_TIME,TEST_END_TIME,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG" +
                " from tb_testinfo " +
                "where DELETE_FLAG=0 AND TEST_ID =" + testInfo.getTestId()
                + " AND TEST_START_TIME ='" + testInfo.getStartTime() + strSql
                + " AND TEST_END_TIME ='" + testInfo.getEndTime() + strSql;
        try {
            List<TestInfo> lstTestInfo = jdbcTemplate.query(sql, new TestInfoMapperSingle());
            if (lstTestInfo != null && lstTestInfo.size() > 0) {
                return lstTestInfo.get(0);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取筛选过的考试信息总数.
     *
     * @param testInfo     试卷实体
     * @param pagingEntity 分页排序
     * @return int
     * @throws DataAccessException 异常
     */
    public int getFilteredTestInfoCount(TestInfo testInfo, PagingEntity pagingEntity) throws DataAccessException {
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        searchValue = StringUtil.zhuanyi(searchValue);
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from tb_testinfo ti ")
                .append(" INNER JOIN tb_testpaper tp ON" +
                        " ti.TEST_ID = tp.ID and tp.DELETE_FLAG = 0 ");
        //试卷名称检索
        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND tp.TESTPAPER_NAME like '%" + searchValue + strMess2);
        }
        sql.append(" INNER JOIN tb_testpapercategory tiCategory ON " +
                "tp.CATEGORY_ID = tiCategory.ID AND tiCategory.DELETE_FLAG = 0 ");
        //试卷类别检索
        if (StringUtils.isNotBlank(testInfo.getTestCategory())) {
            sql.append(" AND " +
                    "tiCategory.CATEGORY_NAME ='" + testInfo.getTestCategory() + "' ");
        }
        sql.append(" WHERE ti.DELETE_FLAG=0 ");
        try {
            List<Integer> counts = jdbcTemplate.queryForList(sql.toString(), Integer.class);
            if (counts != null) {
                return counts.get(0);
            }
            return 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int[] addUsersAndTestConnection(final List<Long> userIdList,
                                            final long creator, final String testId) throws DataAccessException {
        if (!(userIdList != null && userIdList.size() > 0)) {
            return null;
        }

        if (com.mysql.jdbc.StringUtils.isNullOrEmpty(testId)) {
            return null;
        }
        String sql = "insert into tb_testinfotouser (TESTINFO_ID,USER_ID,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG)" +
                "values(?,?,?,NOW(),NOW(),0)";
        try {
            int[] updateCounts = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, Long.parseLong(testId));
                    ps.setLong(2, userIdList.get(i));
                    ps.setLong(NUM_THREE, creator);
                }

                public int getBatchSize() {
                    return userIdList.size();
                }
            });
            return updateCounts;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private StringBuilder initStringBuilder(StringBuilder strBuilder,
                                            String columnName, String strValue, boolean useSingleQuoteFlag) {
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(strValue) &&
                !com.mysql.jdbc.StringUtils.isNullOrEmpty(columnName)) {
            if (useSingleQuoteFlag) {
                strBuilder.append(strMess4 + columnName + " = '" + strValue + "',");
            } else {
                strBuilder.append(strMess4 + columnName + " = " + strValue + strMess1);
            }
        }
        return strBuilder;
    }

    /**
     * 根据试卷ID获取用户列表.
     *
     * @param id 是按ID
     * @return List
     * @throws DataAccessException 异常
     */
    public List<User> getUserListByTestId(long id) throws DataAccessException {
        String sql = "select tuser.USERID,tuser.USERNAME,tuser.REALNAME,tuser.GENDER," +
                "tuser.COMPANY,tuser.DEPT,tuser.IDCARDNO FROM tb_testinfotouser tp"
                + " INNER JOIN tb_user tuser ON tuser.USERID = tp.USER_ID AND tuser.DELETE_FLAG=0 "
                + "where tp.DELETE_FLAG=0 AND tp.TESTINFO_ID =" + id;
        try {
            return jdbcTemplate.query(sql, new UserIdMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final class TestInfoMapperSingle implements RowMapper {
        SimpleDateFormat sdf = new SimpleDateFormat(strSql3);

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestInfo testInfo = new TestInfo();
            testInfo.setId(rs.getLong("ID"));
            testInfo.setTotalUserNo(rs.getInt(strSql2));
            testInfo.setTestUserNo(rs.getInt(strMess6));
            testInfo.setTestStatus(rs.getInt(strSql4));
            testInfo.setCreator(rs.getLong(strSql5));
            testInfo.setCreateTime(rs.getDate(strSql6));
            testInfo.setEndTime(sdf.format(rs.getTimestamp(strMess9)));
            testInfo.setTestId(rs.getString(strMess7));
            testInfo.setStartTime(sdf.format(rs.getTimestamp(strMess8)));
            testInfo.setUpdateTime(rs.getDate(strSql7));
            testInfo.setDeleteFlag(rs.getInt(strSql8));
            return testInfo;
        }
    }

    private static final class TestInfoMapperMultiple implements RowMapper {
        SimpleDateFormat sdf = new SimpleDateFormat(strSql3);

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestInfo testInfo = new TestInfo();
            testInfo.setId(rs.getLong(strArr1));
            testInfo.setTotalUserNo(rs.getInt(strSql2));
            testInfo.setTestpaperName(rs.getString(strArr2));
            testInfo.setTestCategory(rs.getString("CATEGORY_NAME"));
            testInfo.setTestCategoryId(rs.getLong("CATEGORY_ID"));
            testInfo.setTestHour(rs.getInt(strArr3));
            testInfo.setTotalMark(rs.getInt(strArr4));
            testInfo.setTestUserNo(rs.getInt(strMess6));
            testInfo.setTestStatus(rs.getInt(strSql4));
            testInfo.setCreator(rs.getLong(strSql5));
            testInfo.setCreateTime(rs.getDate(strSql6));
            testInfo.setEndTime(sdf.format(rs.getTimestamp(strMess9)));
            testInfo.setTestId(rs.getString(strMess7));
            testInfo.setStartTime(sdf.format(rs.getTimestamp(strMess8)));
            testInfo.setUpdateTime(rs.getDate(strSql7));
            testInfo.setDeleteFlag(rs.getInt(strSql8));
            return testInfo;
        }
    }

    @SuppressWarnings("rawtypes")
    private static final class TestInfoMapper implements RowMapper {
        SimpleDateFormat sdf = new SimpleDateFormat(strSql3);

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestInfo testInfo = new TestInfo();
            testInfo.setId(rs.getLong(strArr1));
            testInfo.setTotalUserNo(rs.getInt(strSql2));
            testInfo.setTestpaperName(rs.getString(strArr2));
            testInfo.setTestHour(rs.getInt(strArr3));
            testInfo.setTestPaperMode(rs.getInt("PAPERMODE"));
            testInfo.setTotalMark(rs.getInt(strArr4));
            testInfo.setTestUserNo(rs.getInt(strMess6));
            testInfo.setTestStatus(rs.getInt(strSql4));
            testInfo.setCreator(rs.getLong(strSql5));
            testInfo.setCreateTime(rs.getDate(strSql6));
            testInfo.setEndTime(sdf.format(rs.getTimestamp(strMess9)));
            testInfo.setTestId(rs.getString(strMess7));
            testInfo.setStartTime(sdf.format(rs.getTimestamp(strMess8)));
            testInfo.setUpdateTime(rs.getDate(strSql7));
            testInfo.setDeleteFlag(rs.getInt(strSql8));
            return testInfo;
        }
    }


    private static final class UserIdMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("USERID"));
            user.setUserName(rs.getString("USERNAME"));
            user.setRealName(rs.getString("REALNAME"));
            user.setGender(rs.getInt("GENDER"));
            user.setCompany(rs.getLong("COMPANY"));
            user.setDept(rs.getLong("DEPT"));
            user.setIdCardNo(rs.getString("IDCARDNO"));
            return user;
        }
    }

    private static final class PointInfoMapper implements RowMapper {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestInfo testInfo = new TestInfo();
            float peMark = (NUM_THOUSAND * (float) rs.getInt("USERMARK") / (float) rs.getInt(strArr4));
            int percentMark = Math.round(peMark);
            testInfo.setId(rs.getLong(strArr1));
            testInfo.setTotalUserNo(rs.getInt(strSql2));
            testInfo.setTestpaperName(rs.getString(strArr2));
            testInfo.setTestHour(rs.getInt(strArr3));
            testInfo.setUserMark(rs.getInt("USERMARK"));
            testInfo.setPercentMark(percentMark);
            testInfo.setAchievementId(rs.getString("am.ACHIEVEMENT_ID"));
            testInfo.setTestPaperMode(rs.getInt("PAPERMODE"));
            testInfo.setTotalMark(rs.getInt(strArr4));
            testInfo.setTestUserNo(rs.getInt(strMess6));
            testInfo.setTestStatus(rs.getInt(strSql4));
            testInfo.setCreator(rs.getLong(strSql5));
            testInfo.setCreateTime(rs.getDate(strSql6));
            testInfo.setEndTime(sdf.format(rs.getTimestamp(strMess9)));
            testInfo.setTestId(rs.getString(strMess7));
            testInfo.setStartTime(sdf.format(rs.getTimestamp(strMess8)));
            testInfo.setUpdateTime(rs.getDate(strSql7));
            testInfo.setDeleteFlag(rs.getInt(strSql8));
            return testInfo;
        }
    }

    /**
     * 用户成绩.
     *
     * @param testInfoId 考试信息Id
     * @param userId     用户Id
     * @return boolean
     */
    public boolean userAchievement(String testInfoId, String userId) {
        StringBuffer sql = new StringBuffer()
                .append("select count(1) from tb_achievement")
                .append(" where testInfo_id='" + testInfoId + strSql)
                .append(" and user_id='" + userId + strSql)
                .append(" and delete_flag=0");

        List<Integer> counts = jdbcTemplate.queryForList(sql.toString(), Integer.class);
        if (counts != null && counts.size() > 0 && counts.get(0) > 0) {
            return true;
        }
        return false;

    }

    /**
     * 根据考试信息ID确认用户行为.
     *
     * @param id 考试ID
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean confirmActionById(long id) throws DataAccessException {
        StringBuffer sql = new StringBuffer()
                .append(" SELECT count(1) FROM tb_testinfo")
                .append(" WHERE  NOW() < TEST_START_TIME ")
                .append(" AND DELETE_FLAG = 0 AND ID =" + id);


        try {
            List<Integer> counts = jdbcTemplate.queryForList(sql.toString(), Integer.class);
            if (counts != null && counts.size() > 0 && counts.get(0) > 0) {
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据试卷ID获取已考试信息.
     *
     * @param testPaperId 试卷id
     * @return List
     * @throws DataAccessException 异常
     */
    public List<TestInfo> getTestedInfoByPaperId(long testPaperId) throws DataAccessException {
        String sql = "select ID ,TEST_ID,TOTAL_USER_NUM,TEST_USER_NUM,"
                + "TEST_STATUS,TEST_START_TIME,TEST_END_TIME,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG" +
                " from tb_testinfo " +
                "where DELETE_FLAG=0 AND TEST_ID =" + testPaperId;
        try {
            List<TestInfo> listTestInfo = jdbcTemplate.query(sql, new TestInfoMapperSingle());
            return listTestInfo;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取考试人员列表.
     *
     * @param testInfoId 考试信息ID
     * @return List
     * @throws DataAccessException 异常
     */
    public List<TestInfo> getTestpeopleNames(String testInfoId) throws DataAccessException {
        String sql = "select t2.username AS peopleName,t2.realName AS realName,t1.user_id as userId," +
                "t1.testinfo_id as testInfoId from tb_testinfotouser t1" +
                " LEFT JOIN tb_user t2 ON  t1.user_id = t2.USERID" +
                " where t1.DELETE_FLAG = 0 and t1.testinfo_id =" + testInfoId;
        try {
            List<TestInfo> peopleNameList = jdbcTemplate.query(sql, new PeopleNameMapper());
            return peopleNameList;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static final class PeopleNameMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestInfo testInfo = new TestInfo();
            testInfo.setTestpeopleName(rs.getString("realName"));
            testInfo.setTestpeopleId(rs.getString("userId"));
            testInfo.setId(rs.getLong("testInfoId"));
            return testInfo;
        }
    }
}
