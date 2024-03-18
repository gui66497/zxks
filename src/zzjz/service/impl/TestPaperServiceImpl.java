package zzjz.service.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzjz.bean.PagingEntity;
import zzjz.bean.TestPaper;
import zzjz.bean.TestPaperPart;
import zzjz.service.TestPaperService;
import zzjz.util.Constants;
import zzjz.util.MyJdbcTemplate;
import zzjz.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author guitang.fang
 * @version 2016/6/01 13:53
 * @ClassName: TestPaperServiceImpl
 * @Description: 试卷操作service接口实现类
 */
@Service
public class TestPaperServiceImpl implements TestPaperService {

    /**
     * 注入MyJdbcTemplate.
     */
    @Autowired
    public MyJdbcTemplate myJdbcTemplate;

    private static Logger logger = Logger.getLogger(TestPaperServiceImpl.class);

    private static String strMess = "批量删除出错";

    private static final int NUM_THREE = 3;

    private static final int NUM_FOUR = 4;

    private static final int NUM_FIVE = 5;

    private static final int NUM_SIX = 6;

    private static final int NUM_SEVEN = 7;

    private static final int NUM_EIGHT = 8;

    private static final int NUM_NINE = 9;

    private static final int NUM_TEN = 10;

    private static final int NUM_ELEVEN = 11;


    /**
     * 获取试卷.
     *
     * @param testPaper 试卷实体类
     * @return TestPaper
     */
    public TestPaper getTestPaper(TestPaper testPaper) {
        String sql = "select ID,TESTPAPER_NAME,TESTPAPER_DESCRIPTION,TESTPAPER_EXPLAIN," +
                "TESTPAPER_MODE,CATEGORY_ID,TESTPAPER_TYPE," +
                "MARK_TOTAL,TEST_TIME,ITEM_SEQUENCE,ANSWER_SEQUENCE,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_testpaper " +
                "where DELETE_FLAG=0 and TESTPAPER_NAME=? and CATEGORY_ID=?";
        List<TestPaper> testPaperList =
                myJdbcTemplate.query(sql, new TestPaperMapper(), testPaper.getTestpaperName(),
                        testPaper.getCategoryId());
        /*List<TestPaper> testPaperList = myJdbcTemplate.query(sql,new TestPaperMapper());*/
        //判断查询结果是否为空
        if (testPaperList != null && testPaperList.size() > 0) {
            return testPaperList.get(0);
        }
        return null;
    }

    /**
     * 添加试卷.
     *
     * @param testPaper 试卷实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addTestPaper(final TestPaper testPaper) throws DataAccessException {
        String sql = "insert into tb_testpaper(TESTPAPER_NAME,TESTPAPER_DESCRIPTION," +
                "TESTPAPER_EXPLAIN,TESTPAPER_MODE," +
                "CATEGORY_ID,TESTPAPER_TYPE,MARK_TOTAL,TEST_TIME,ITEM_SEQUENCE," +
                "ANSWER_SEQUENCE,CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),0)";
        try {
            myJdbcTemplate.update(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, testPaper.getTestpaperName());
                    ps.setString(2, testPaper.getTestpaperDescription());
                    ps.setString(NUM_THREE, testPaper.getTestpaperExplain());
                    ps.setInt(NUM_FOUR, testPaper.getTestpaperMode());
                    ps.setLong(NUM_FIVE, testPaper.getCategoryId());
                    ps.setInt(NUM_SIX, testPaper.getTestpaperType());
                    ps.setInt(NUM_SEVEN, testPaper.getMarkTotal());
                    ps.setInt(NUM_EIGHT, testPaper.getTestTime());
                    ps.setInt(NUM_NINE, testPaper.getItemSequence());
                    ps.setInt(NUM_TEN, testPaper.getAnswerSequence());
                    ps.setLong(NUM_ELEVEN, testPaper.getCreator());
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据分类ID获取试卷列表.
     *
     * @param categoryId   考试分类categoryId
     * @param pagingEntity 分页实体
     * @return List
     */
    public List<TestPaper> getTestPaperListByCategoryId(long categoryId,
                                                        PagingEntity pagingEntity) {
        int start = pagingEntity.getStartIndex(); //开始位置
        int pageSize = pagingEntity.getPageSize(); //每页条数
        String searchStr = pagingEntity.getSearchValue(); //搜索值
        searchStr = StringUtil.zhuanyi(searchStr);
        String sortDir = pagingEntity.getSortDir();
        String sortColumnIndex = pagingEntity.getSortColumn();
        String sortColumn = String.valueOf(Constants.TestpaperColumn.getName(sortColumnIndex));
        StringBuilder sql = new StringBuilder("select tp.ID,tp.TESTPAPER_NAME," +
                "tp.TESTPAPER_DESCRIPTION,tp.TESTPAPER_EXPLAIN," +
                "tp.TESTPAPER_MODE,tp.CATEGORY_ID,tp.TESTPAPER_TYPE,tp.MARK_TOTAL,tp.TEST_TIME," +
                "tp.ITEM_SEQUENCE," +
                "tp.ANSWER_SEQUENCE,tp.CREATOR,tp.CREATE_TIME,tp.UPDATE_TIME,tp.DELETE_FLAG," +
                "tpc.CATEGORY_NAME as CATEGORY_NAME from tb_testpaper tp " +
                "left join tb_testpapercategory tpc on tp.CATEGORY_ID = tpc.ID" +
                " where tp.DELETE_FLAG=0");
        if (categoryId > 0) {
            sql.append(" and tp.CATEGORY_ID=" + categoryId + " ");
        }
        if (StringUtils.isNotBlank(searchStr)) {
            sql.append(" and tp.TESTPAPER_NAME like '%" + searchStr + "%'");
        }
        if (StringUtils.isNotBlank(sortDir) && StringUtils.isNotBlank(sortColumn)) {
            if ("1".equals(sortColumnIndex)) { //按试卷名排序
                sql.append(" order by convert(tp." + sortColumn + " USING GBK) " + sortDir);
            } else {
                sql.append(" order by tp." + sortColumn + " " + sortDir);
            }
        } else {
            sql.append(" order by tp.ID DESC ");
        }
        sql.append(" limit " + start + ", " + pageSize + " ");
        return myJdbcTemplate.query(sql.toString(),
                new TestPaperMapper());
    }

    /**
     * 根据分类Id获取试卷总数.
     *
     * @param categoryId   分类ID
     * @param pagingEntity 分页实体
     * @return int
     */
    public int getTestpaperCountByCategory(long categoryId, PagingEntity pagingEntity) {
        String searchStr = pagingEntity.getSearchValue();
        searchStr = StringUtil.zhuanyi(searchStr);
        String sql = "select count(*) from tb_testpaper" +
                " where DELETE_FLAG=0  ";
        if (categoryId > 0) {
            sql += " and CATEGORY_ID = " + categoryId + "";
        }
        if (StringUtils.isNotBlank(searchStr)) {
            sql += " and TESTPAPER_NAME like '%" + searchStr + "%'";
        }
        return myJdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 根据试卷ID删除试卷.
     *
     * @param id 试卷Id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteTestPaperById(long id) throws DataAccessException {
        String sql = "update tb_testpaper set DELETE_FLAG =1 where ID=?";
        try {
            myJdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据名称和类别ID获取试卷.
     *
     * @param testPaperName 试卷名称
     * @param categoryId    类别ID
     * @return TestPaper
     */
    public TestPaper getTestPaperByNameAndId(String testPaperName, String categoryId) {
        String sql = "select ID,TESTPAPER_NAME,TESTPAPER_DESCRIPTION," +
                "TESTPAPER_EXPLAIN,TESTPAPER_MODE,CATEGORY_ID,TESTPAPER_TYPE," +
                "MARK_TOTAL,TEST_TIME,ITEM_SEQUENCE,ANSWER_SEQUENCE," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_testpaper " +
                "where DELETE_FLAG=0 and TESTPAPER_NAME=? and CATEGORY_ID=?";
        List<TestPaper> testPaperList =
                myJdbcTemplate.query(sql, new TestPaperMapper(), testPaperName, categoryId);
        //判断查询结果是否为空
        if (testPaperList != null && testPaperList.size() > 0) {
            return testPaperList.get(0);
        }
        return null;
    }

    /**
     * 根据名称获取试卷.
     *
     * @param testPaperName 试卷名称
     * @return TestPaper
     */
    public TestPaper getTestPaperByName(String testPaperName) {
        String sql = "select ID,TESTPAPER_NAME,TESTPAPER_DESCRIPTION," +
                "TESTPAPER_EXPLAIN,TESTPAPER_MODE,CATEGORY_ID,TESTPAPER_TYPE," +
                "MARK_TOTAL,TEST_TIME,ITEM_SEQUENCE,ANSWER_SEQUENCE," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_testpaper " +
                "where DELETE_FLAG=0 and TESTPAPER_NAME=?";
        List<TestPaper> testPaperList =
                myJdbcTemplate.query(sql, new TestPaperMapper(), testPaperName);
        //判断查询结果是否为空
        if (testPaperList != null && testPaperList.size() > 0) {
            return testPaperList.get(0);
        }
        return null;
    }

    /**
     * 编辑试卷.
     *
     * @param testPaper 试卷实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateTestPaper(final TestPaper testPaper) throws DataAccessException {
        String sql = "update tb_testpaper set TESTPAPER_NAME=?,TESTPAPER_DESCRIPTION=?," +
                "TESTPAPER_EXPLAIN=?,TESTPAPER_MODE=?,CATEGORY_ID=?,TESTPAPER_TYPE=?," +
                "MARK_TOTAL=?,TEST_TIME=?,ITEM_SEQUENCE=?,ANSWER_SEQUENCE=?," +
                "UPDATE_TIME=NOW() where ID =?";
        try {
            myJdbcTemplate.update(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, testPaper.getTestpaperName());
                    ps.setString(2, testPaper.getTestpaperDescription());
                    ps.setString(NUM_THREE, testPaper.getTestpaperExplain());
                    ps.setInt(NUM_FOUR, testPaper.getTestpaperMode());
                    ps.setLong(NUM_FIVE, testPaper.getCategoryId());
                    ps.setInt(NUM_SIX, testPaper.getTestpaperType());
                    ps.setInt(NUM_SEVEN, testPaper.getMarkTotal());
                    ps.setInt(NUM_EIGHT, testPaper.getTestTime());
                    ps.setInt(NUM_NINE, testPaper.getItemSequence());
                    ps.setInt(NUM_TEN, testPaper.getAnswerSequence());
                    ps.setLong(NUM_ELEVEN, testPaper.getId());
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据试卷ID获取试卷信息.
     *
     * @param id 试卷id
     * @return TestPaper
     */
    public TestPaper getTestPaperById(long id) {
        String sql = "select ID,TESTPAPER_NAME,TESTPAPER_DESCRIPTION,TESTPAPER_EXPLAIN," +
                "TESTPAPER_MODE,CATEGORY_ID,TESTPAPER_TYPE," +
                "MARK_TOTAL,TEST_TIME,ITEM_SEQUENCE,ANSWER_SEQUENCE,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG from tb_testpaper " +
                "where DELETE_FLAG=0 and ID=?";
        List<TestPaper> testPaperList =
                myJdbcTemplate.query(sql, new TestPaperMapper(), id);
        if (testPaperList != null && testPaperList.size() > 0) {
            return testPaperList.get(0);
        }
        return null;
    }

    /**
     * 根据试卷ID集合批量删除试卷.
     *
     * @param ids 试卷id集合
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean batchDeleteTestPaperById(final List<Long> ids) throws DataAccessException {
        String sql = "update tb_testpaper set DELETE_FLAG =1 where ID=?";
        try {
            myJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, ids.get(i));
                }

                public int getBatchSize() {
                    return ids.size();
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.debug(strMess + e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * 添加试卷.
     *
     * @param testPaper 试卷实体 包含模块实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean createTestpaper(final TestPaper testPaper) throws DataAccessException {
        //先新建试卷基本信息
        final String sql = "insert into tb_testpaper(TESTPAPER_NAME,TESTPAPER_DESCRIPTION," +
                "TESTPAPER_EXPLAIN," +
                "CATEGORY_ID,MARK_TOTAL,TEST_TIME,ITEM_SEQUENCE,ANSWER_SEQUENCE,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                "values(?,?,?,?,?,?,?,?,?,NOW(),NOW(),0)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        myJdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                try {
                    ps.setString(1, testPaper.getTestpaperName());
                    ps.setString(2, testPaper.getTestpaperDescription());
                    ps.setString(NUM_THREE, testPaper.getTestpaperExplain());
                    ps.setLong(NUM_FOUR, testPaper.getCategoryId());
                    ps.setInt(NUM_FIVE, testPaper.getMarkTotal());
                    ps.setInt(NUM_SIX, testPaper.getTestTime());
                    ps.setInt(NUM_SEVEN, testPaper.getItemSequence());
                    ps.setInt(NUM_EIGHT, testPaper.getAnswerSequence());
                    ps.setLong(NUM_NINE, testPaper.getCreator());
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            return false;
        }
        final Long generatedId = keyHolder.getKey().longValue();
        if (generatedId < 0) {
            return false;
        }

        //创建模块
        return createTestPaperParts(generatedId, testPaper);
    }


    /**
     * 创建模块.
     * @param generatedId 试卷ID
     * @param testPaper   试卷对象
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean createTestPaperParts(final Long generatedId, final TestPaper testPaper) throws DataAccessException {
        TestPaperPart[] testPaperParts = testPaper.getTestPaperParts();
        if (testPaperParts == null) {
            return false;
        }

        //为确保每次的时间戳id不一样，添加一位i
        int i = 1;
        for (final TestPaperPart part : testPaperParts) {
            String uniqueId = String.valueOf(new Date().getTime());
            part.setPartId(Long.parseLong(uniqueId + i));

            String sqlPart = "insert into tb_testpaperpart(TESTPAPERPART_ID,PART_NAME," +
                    "PART_EXPLAIN,TESTPAPER_ID," +
                    "PART_MARK,PART_ORDER,CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                    "values(?,?,?,?,?,?,?,NOW(),NOW(),0)";

            try {
                myJdbcTemplate.update(sqlPart, new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, part.getPartId());
                        ps.setString(2, part.getPartName());
                        ps.setString(NUM_THREE, part.getPartExplain());
                        ps.setLong(NUM_FOUR, generatedId);
                        ps.setInt(NUM_FIVE, part.getPartMark());
                        ps.setInt(NUM_SIX, part.getPartOrder());
                        ps.setLong(NUM_SEVEN, testPaper.getCreator());
                    }
                });
            } catch (DataAccessException e) {
                e.printStackTrace();
                return false;
            }

            //创建试题
            createItems(part, testPaper);
            i++;
        }
        return true;
    }


    /**
     * 创建试题.
     *
     * @param part      试卷模块对象
     * @param testPaper 试卷对象
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean createItems(final TestPaperPart part, final TestPaper testPaper) throws DataAccessException {
        final Long[] itemIds = part.getItemIds();
        String itemSql = "insert INTO tb_testpaperparttoitem(TESTPAPERPART_ID,ITEM_ID," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                "VALUES (?,?,?,now(),now(),0)";
        try {
            myJdbcTemplate.batchUpdate(itemSql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, part.getPartId());
                    ps.setLong(2, itemIds[i]);
                    ps.setLong(NUM_THREE, testPaper.getCreator());
                }

                public int getBatchSize() {
                    return itemIds.length;
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.debug(strMess + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 编辑试卷和模块.
     *
     * @param testPaper 试卷实体 包含part信息
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean updateTestpaperAndPart(final TestPaper testPaper) throws DataAccessException {
        String sql = "update tb_testpaper set TESTPAPER_NAME=?,TESTPAPER_DESCRIPTION=?," +
                "TESTPAPER_EXPLAIN=?,CATEGORY_ID=?," +
                "MARK_TOTAL=?,TEST_TIME=?,ITEM_SEQUENCE=?,ANSWER_SEQUENCE=?," +
                "UPDATE_TIME=NOW() where ID =?";
        try {
            myJdbcTemplate.update(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, testPaper.getTestpaperName());
                    ps.setString(2, testPaper.getTestpaperDescription());
                    ps.setString(NUM_THREE, testPaper.getTestpaperExplain());
                    ps.setLong(NUM_FOUR, testPaper.getCategoryId());
                    ps.setInt(NUM_FIVE, testPaper.getMarkTotal());
                    ps.setInt(NUM_SIX, testPaper.getTestTime());
                    ps.setInt(NUM_SEVEN, testPaper.getItemSequence());
                    ps.setInt(NUM_EIGHT, testPaper.getAnswerSequence());
                    ps.setLong(NUM_NINE, testPaper.getId());
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

        //先删除该试卷下的原有模块
        String delSql = "UPDATE tb_testpaperpart SET DELETE_FLAG = 1" +
                " WHERE TESTPAPER_ID = " + testPaper.getId();
        try {
            myJdbcTemplate.update(delSql);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

        TestPaperPart[] testPaperParts = testPaper.getTestPaperParts();
        int i = 1;
        for (final TestPaperPart part : testPaperParts) {

            if (part.getPartId() > 0) {
                //先删除改part下原来的item信息
                String delItemSql = "UPDATE tb_testpaperparttoitem SET DELETE_FLAG = 1" +
                        " WHERE TESTPAPERPART_ID = " + part.getPartId();
                try {
                    myJdbcTemplate.execute(delItemSql);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            if (part.getItemIds() != null) {
                //为确保每次的时间戳id不一样，添加一位i
                String uniqueId = String.valueOf(new Date().getTime());
                final long testPaperpartId = Long.parseLong(uniqueId + i);

                String sqlPart = "insert into tb_testpaperpart(TESTPAPERPART_ID,PART_NAME," +
                        "PART_EXPLAIN,TESTPAPER_ID," +
                        "PART_MARK,PART_ORDER,CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                        "values(?,?,?,?,?,?,?,NOW(),NOW(),0)";

                try {
                    myJdbcTemplate.update(sqlPart, new PreparedStatementSetter() {
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setLong(1, testPaperpartId);
                            ps.setString(2, part.getPartName());
                            ps.setString(NUM_THREE, part.getPartExplain());
                            ps.setLong(NUM_FOUR, testPaper.getId());
                            ps.setInt(NUM_FIVE, part.getPartMark());
                            ps.setInt(NUM_SIX, part.getPartOrder());
                            ps.setLong(NUM_SEVEN, testPaper.getCreator());
                        }
                    });
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    return false;
                }

                //如果该部分没有题目的话就不保存

                final Long[] itemIds = part.getItemIds();

                String itemSql = "insert INTO tb_testpaperparttoitem(TESTPAPERPART_ID,ITEM_ID," +
                        "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                        "VALUES (?,?,?,now(),now(),0)";
                try {
                    int[] updateCounts =
                            myJdbcTemplate.batchUpdate(itemSql, new BatchPreparedStatementSetter() {
                                public void setValues(PreparedStatement ps, int i) throws SQLException {
                                    ps.setLong(1, testPaperpartId);
                                    ps.setLong(2, itemIds[i]);
                                    ps.setLong(NUM_THREE, testPaper.getCreator());
                                }

                                public int getBatchSize() {
                                    return itemIds.length;
                                }
                            });
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    logger.debug(strMess + e.getMessage());
                    return false;
                }
            }

            i++;
        }
        return true;

    }

    /**
     * testpaper表的所有字段.
     */
    private static final String TESTPAPER_COLUMNS = " tp.ID,tp.TESTPAPER_NAME,tp.TESTPAPER_DESCRIPTION,tp.TESTPAPER_EXPLAIN," +
            "tp.TESTPAPER_MODE,tp.CATEGORY_ID,tp.TESTPAPER_TYPE,tp.MARK_TOTAL,tp.TEST_TIME,tp.ITEM_SEQUENCE," +
            "tp.ANSWER_SEQUENCE,tp.CREATOR,tp.CREATE_TIME,tp.UPDATE_TIME,tp.DELETE_FLAG ";

    /**
     * 根据试卷分类ID获取试卷信息.
     *
     * @param categoryId 试卷分类id
     * @return List
     */
    public List<TestPaper> getTestPaperByCategoryId(long categoryId) {
        StringBuilder sql = new StringBuilder("select tp.ID,tp.TESTPAPER_NAME," +
                "tp.TESTPAPER_DESCRIPTION,tp.TESTPAPER_EXPLAIN," +
                "tp.TESTPAPER_MODE,tp.CATEGORY_ID,tp.TESTPAPER_TYPE," +
                "tp.MARK_TOTAL,tp.TEST_TIME,tp.ITEM_SEQUENCE," +
                "tp.ANSWER_SEQUENCE,tp.CREATOR,tp.CREATE_TIME,tp.UPDATE_TIME," +
                "tp.DELETE_FLAG,tpc.CATEGORY_NAME as CATEGORY_NAME from tb_testpaper tp " +
                "left join tb_testpapercategory tpc on tp.CATEGORY_ID = tpc.ID" +
                " where tp.DELETE_FLAG=0 and tp.CATEGORY_ID=? ");
        return myJdbcTemplate.query(sql.toString(), new TestPaperMapper(), categoryId);
    }

    /**
     * 根据题目id获取关联的试.
     * @param itemId 题目id
     * @return 试卷集合
     */
    public List<TestPaper> gettestpaperByItemId(Long itemId) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT ").append(TESTPAPER_COLUMNS);
        sql.append(" FROM tb_testpaperparttoitem tppi " +
                "LEFT JOIN tb_testpaperpart tpp ON tppi.TESTPAPERPART_ID = tpp.TESTPAPERPART_ID " +
                "LEFT JOIN tb_testpaper tp ON tpp.TESTPAPER_ID = tp.ID " +
                "WHERE 1 = 1 AND tppi.ITEM_ID = ? AND tp.DELETE_FLAG = 0");
        return myJdbcTemplate.query(sql.toString(), new TestPaperMapper(), itemId);
    }

    @SuppressWarnings("rawtypes")
    private static final class TestPaperMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestPaper testPaper = new TestPaper();
            testPaper.setTestpaperName(rs.getString("TESTPAPER_NAME"));
            testPaper.setTestpaperDescription(rs.getString("TESTPAPER_DESCRIPTION"));
            testPaper.setTestpaperExplain(rs.getString("TESTPAPER_EXPLAIN"));
            testPaper.setTestpaperMode(rs.getInt("TESTPAPER_MODE"));
            testPaper.setCategoryId(rs.getLong("CATEGORY_ID"));
            if (isExistColumn(rs, "CATEGORY_NAME")) {
                testPaper.setCategoryName(rs.getString("CATEGORY_NAME"));
            }
            testPaper.setTestpaperType(rs.getInt("TESTPAPER_TYPE"));
            testPaper.setMarkTotal(rs.getInt("MARK_TOTAL"));
            testPaper.setTestTime(rs.getInt("TEST_TIME"));
            testPaper.setItemSequence(rs.getInt("ITEM_SEQUENCE"));
            testPaper.setAnswerSequence(rs.getInt("ANSWER_SEQUENCE"));
            testPaper.setCreator(rs.getLong("CREATOR"));
            testPaper.setCreateTime(rs.getTimestamp("CREATE_TIME"));
            testPaper.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
            testPaper.setId(rs.getLong("ID"));
            testPaper.setDeleteFlag(0);
            return testPaper;
        }

        boolean isExistColumn(ResultSet rs, String columnName) {
            try {
                if (rs.findColumn(columnName) > 0) {
                    return true;
                }
            } catch (SQLException e) {
                logger.info("通过异常判断结果集中是否存在这个属性：" + e.getMessage());
                return false;
            }
            return false;
        }
    }


    /**
     * 获取试卷考试详细信息.
     *
     * @param userId 用户ID
     * @return List
     */
    @Transactional
    public List<TestPaper> getTestDetail(String userId) {
        String sql = "SELECT ta.USER_MARK, ta.TESTINFO_ID, tt.TEST_ID, tr.ITEM_ID," +
                " tr.RESULT, tm.ANSWER FROM tb_result tr" +
                " LEFT JOIN tb_achievement ta ON ta.ACHIEVEMENT_ID = tr.ACHIEVEMENT_ID" +
                " LEFT JOIN tb_testinfo tt ON ta.TESTINFO_ID = tt.ID" +
                " LEFT JOIN tb_testpaper tp ON tt.TEST_ID = tp.ID" +
                " LEFT JOIN tb_testinfotouser ti ON tt.ID = ti.TESTINFO_ID" +
                " LEFT JOIN tb_item tm ON tm.ITEM_ID = tr.ITEM_ID" +
                " WHERE ta.USER_ID = '" + userId + "' AND tr.DELETE_FLAG = 0 ";

        return myJdbcTemplate.query(sql.toString(), new TestDetailMapper());
    }

    private static final class TestDetailMapper implements RowMapper {
        /*        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestPaper testPaper = new TestPaper();
            testPaper.setId(rs.getLong("tt.TEST_ID"));
            testPaper.setAnswer(rs.getString("tm.ANSWER"));
            testPaper.setItemId(rs.getString("tr.ITEM_ID"));
            testPaper.setUserMark(rs.getString("ta.USER_MARK"));
            testPaper.setUserResult(rs.getString("tr.RESULT"));
            return testPaper;
        }
    }

}

