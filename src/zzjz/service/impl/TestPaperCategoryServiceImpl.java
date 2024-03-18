package zzjz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.TestPaperCategory;
import zzjz.service.TestPaperCategoryService;
import zzjz.util.MyJdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author guitang.fang
 * @version 2016/5/31 13:53
 * @ClassName: TestPaperCategoryService
 * @Description: 考试类型操作service接口实现类
 */
@Service
public class TestPaperCategoryServiceImpl implements TestPaperCategoryService {

    /**
     * 注入MyJdbcTemplate.
     */
    @Autowired
    public MyJdbcTemplate myJdbcTemplate;

    private static final int NUM_THREE = 3;

    private static Logger logger = Logger.getLogger(TestPaperCategoryServiceImpl.class);

    /**
     * 获取试卷分类.
     *
     * @param testPaperCategory 考试类别实体类
     * @return TestPaperCategory
     */
    public TestPaperCategory getTestPaperCategory(TestPaperCategory testPaperCategory) {
        String sql = "select ID,PARENT_ID,CATEGORY_NAME,CREATOR,CREATE_TIME," +
                "DELETE_FLAG,UPDATE_TIME from tb_testpapercategory " +
                " where DELETE_FLAG=0 " +
                " and CATEGORY_NAME='" + testPaperCategory.getCategoryName() + "'" +
                " and PARENT_ID='" + testPaperCategory.getParentId() + "'";
        List<TestPaperCategory> testPaperCategoryList =
                myJdbcTemplate.query(sql, new TestPaperCategoryMapper());
        TestPaperCategory testPaperCategoryReturn = null;
        //判断查询结果是否为空
        if (testPaperCategoryList != null && testPaperCategoryList.size() > 0) {
            testPaperCategoryReturn = testPaperCategoryList.get(0);
        }
        return testPaperCategoryReturn;
    }

    /**
     * 添加试卷分类.
     *
     * @param testPaperCategory 考试类别实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addTestPaperCategory(final TestPaperCategory testPaperCategory) throws DataAccessException {
        if (!StringUtils.isNotBlank(testPaperCategory.getCategoryName()) ||
                testPaperCategory.getParentId() < 0) {
            logger.debug("添加失败：参数不正确！");
            return false;
        }
        String sql = "insert into tb_testpapercategory(CATEGORY_NAME,PARENT_ID," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                "values(?,?,?,NOW(),NOW(),0)";
        try {
            myJdbcTemplate.update(sql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, testPaperCategory.getCategoryName());
                    ps.setLong(2, testPaperCategory.getParentId());
                    ps.setLong(NUM_THREE, testPaperCategory.getCreator());
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.debug("添加失败：" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取试卷分类列表.
     *
     * @return List
     */
    public List<TestPaperCategory> getTestPaperCategoryList() {
        String sql = "select ID,CATEGORY_NAME,CREATOR,PARENT_ID,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG from tb_testpapercategory where DELETE_FLAG=0";
        return myJdbcTemplate.query(sql, new TestPaperCategoryMapper());
    }

    /**
     * 根据ID获取试卷子分类信息.
     *
     * @param categoryId 考试类别id
     * @return List
     */
    public List<TestPaperCategory> getChildTestPaperCategoryById(long categoryId) {
        if (categoryId < 0) {
            return null;
        }
        String sql = "select ID,CATEGORY_NAME,PARENT_ID,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG from tb_testpapercategory " +
                "where DELETE_FLAG = 0 and PARENT_ID = ?";
        return myJdbcTemplate.query(sql, new TestPaperCategoryMapper(), categoryId);
    }

    /**
     * 删除试卷分类.
     *
     * @param categoryId 考试类别id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteTestPaperCategory(long categoryId) throws DataAccessException {
        if (categoryId < 0) {
            return false;
        }
        String sql = "update tb_testpapercategory set DELETE_FLAG=1 where ID =?";
        try {
            int res = myJdbcTemplate.update(sql, categoryId);
            return res == 1;
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.debug("删除失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 更新试卷分类.
     *
     * @param testPaperCategory 考试类别实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateTestPaperCategory(TestPaperCategory testPaperCategory) throws DataAccessException {
        if (!StringUtils.isNotBlank(testPaperCategory.getCategoryName()) ||
                testPaperCategory.getId() < 0) {
            logger.debug("修改失败：参数不正确!");
            return false;
        }
        String sql = "update tb_testpapercategory " +
                "set CATEGORY_NAME=?,UPDATE_TIME=NOW() where ID =?";
        try {
            int row = myJdbcTemplate.update(sql, testPaperCategory.getCategoryName(),
                    testPaperCategory.getId());
            return row > 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.debug("修改失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取父类试卷分类.
     *
     * @param parentId 考试类别parentId
     * @return TestPaperCategory
     */
    public TestPaperCategory getFatherTestPaperCategory(long parentId) {
        String sql = "select ID,CATEGORY_NAME,PARENT_ID,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG from tb_testpapercategory " +
                "where DELETE_FLAG=0 and ID=?";
        List<TestPaperCategory> testPaperCategoryList =
                myJdbcTemplate.query(sql, new TestPaperCategoryMapper(), parentId);
        return (testPaperCategoryList != null &&
                testPaperCategoryList.size() > 0) ? testPaperCategoryList.get(0) : null;
    }

    //ResultSet转实体类
    @SuppressWarnings("rawtypes")
    private static final class TestPaperCategoryMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestPaperCategory testPaperCategory = new TestPaperCategory();
            testPaperCategory.setCategoryName(rs.getString("CATEGORY_NAME"));
            testPaperCategory.setCreator(rs.getLong("CREATOR"));
            testPaperCategory.setCreateTime(rs.getDate("CREATE_TIME"));
            testPaperCategory.setUpdateTime(rs.getDate("UPDATE_TIME"));
            testPaperCategory.setParentId(rs.getLong("PARENT_ID"));
            testPaperCategory.setId(rs.getLong("ID"));
            testPaperCategory.setDeleteFlag(0);
            return testPaperCategory;
        }
    }
}