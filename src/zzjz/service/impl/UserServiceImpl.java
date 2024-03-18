package zzjz.service.impl;

import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzjz.bean.PagingEntity;
import zzjz.bean.User;
import zzjz.service.UserService;
import zzjz.util.MyJdbcTemplate;
import zzjz.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author 梅宏振
 * @version 2016年5月24日 上午8:32:29
 * @ClassName: UserService
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MyJdbcTemplate myJdbcTemplate;

    private static final String STR1 = "REALNAME";

    private static final String STR2 = "PASSWORD";

    private static final String STR3 = "BIRTH";

    private static final String STR4 = "IDCARDNO";

    private static final String STR5 = "POSITION";

    private static final String STR6 = "EDUCATION";

    private static final String STR7 = "GRADUATE_SCHOOL";

    private static final String STR8 = "GRADUATE_DATE";

    private static final String STR9 = "MAJOR";

    private static final String STRMESS1 = "EMAIL";

    private static final String STRMESS2 = "TEL";

    private static final String STRMESS3 = "";

    private static final String STRMESS5 = " ";

    private static final String STRMESS4 = ",";

    private static final String STRMESS6 = "RESUME_URL";

    private static final String STRMESS7 = "',";

    private static final String STRMESS8 = "'";

    private static final String STRMESS9 = " ) ";

    /**
     * 添加用户.
     *
     * @param user 用户实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean addUser(User user) throws DataAccessException {
        try {
            final User userFin = user;
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    String sqlStr = "INSERT INTO tb_user( USERID,USERNAME,PASSWORD,REALNAME," +
                            "GENDER,COMPANY,DEPT,IDCARDNO,POSITION,BIRTH,EDUCATION,GRADUATE_SCHOOL" +
                            ",GRADUATE_DATE,MAJOR,EMAIL,TEL,RESUME_URL,ROLE_ID,AUDIT_STATUS,CREATE_TIME," +
                            "UPDATE_TIME,DELETE_FLAG) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),0)";
                    PreparedStatement ps = con.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
                    ps = setValues(ps, userFin);
                    return ps;
                }
            };
            int affectCount = myJdbcTemplate.update(psc);
            return affectCount > 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param ps ps
     * @param userFin user实体
     * @return ps
     * @throws SQLException SQLException
     */
    public PreparedStatement setValues(PreparedStatement ps, User userFin) throws SQLException {
        int i = 0;
        ps.setLong(++i, userFin.getUserId());
        ps.setString(++i, userFin.getUserName());
        ps.setString(++i, userFin.getPassWord());
        ps.setString(++i, userFin.getRealName());
        ps.setInt(++i, userFin.getGender());
        ps.setLong(++i, userFin.getCompany());
        ps.setLong(++i, userFin.getDept());
        ps.setString(++i, userFin.getIdCardNo());
        ps.setString(++i, userFin.getPosition());
        ps.setString(++i, userFin.getBirth());
        ps.setString(++i, userFin.getEducation());
        ps.setString(++i, userFin.getGraduateSchool());
        ps.setString(++i, userFin.getGraduateDate());
        ps.setString(++i, userFin.getMajor());
        ps.setString(++i, userFin.getEmail());
        ps.setString(++i, userFin.getTel());
        ps.setString(++i, userFin.getResumeUrl());
        ps.setLong(++i, userFin.getRoleId());
        ps.setInt(++i, userFin.getAuditStatus());
        return ps;
    }

    /**
     * 根据用户ID删除用户.
     *
     * @param userId 用户Id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteUserById(long userId) throws DataAccessException {
        String sql = "UPDATE tb_user  SET DELETE_FLAG = 1 WHERE USERID = " + userId;
        try {
            int affectCount = myJdbcTemplate.update(sql);
            if (affectCount > 0) {
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
     * 根据用户ID列表批量删除用户.
     *
     * @param userIdList 用户Id列表
     * @return int[]
     * @throws DataAccessException 异常
     */
    public int[] deleteUserByUserIdList(final List<Long> userIdList) throws DataAccessException {
        if (!(userIdList != null && userIdList.size() > 0)) {
            return null;
        }

        String sql = "UPDATE tb_user  SET DELETE_FLAG = 1 WHERE USERID = ? ";
        try {
            int[] updateCounts =
                    myJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, userIdList.get(i));
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

    /**
     * 编辑用户.
     *
     * @param user 用户实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateUser(User user) throws DataAccessException {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("UPDATE tb_user SET USERNAME = '"
                + user.getUserName() + "', UPDATE_TIME = NOW() , ");
        strBuilder = initStringBuilder(strBuilder,
                STR1, user.getRealName(), true);
        strBuilder = initStringBuilder(strBuilder,
                STR2, user.getPassWord(), true);
        strBuilder = initStringBuilder(strBuilder,
                "GENDER", String.valueOf(user.getGender()), false);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STR4, user.getIdCardNo(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STR5, user.getPosition(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STR3, user.getBirth(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STR6, user.getEducation(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STR7, user.getGraduateSchool(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STR8, user.getGraduateDate(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STR9, user.getMajor(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STRMESS1, user.getEmail(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STRMESS2, user.getTel(), true);
        strBuilder = initStringBuilderContainsNull(strBuilder,
                STRMESS6, user.getResumeUrl(), true);
        strBuilder.append(" COMPANY = '" + user.getCompany() + STRMESS7);
        strBuilder.append(" DEPT = '" + user.getDept() + STRMESS7);
        strBuilder.append(" ROLE_ID = " + user.getRoleId());
        strBuilder.append(" WHERE USERID = " + user.getUserId());
        try {
            int affectCount = myJdbcTemplate.update(strBuilder.toString());
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
     * 根据用户名称获取用户信息.
     *
     * @param userName 用户姓名
     * @return User
     */
    public User getUserByUserName(String userName) {
        if (StringUtils.isNullOrEmpty(userName)) {
            return null;
        }

        return getUserByFilterSql("USERNAME = BINARY'" + userName + STRMESS8);
    }

    /**
     * 根据用户Id虎丘用户信息.
     *
     * @param userId 用户Id
     * @return User
     */
    public User getUserByUserId(long userId) {
        return getUserByFilterSql("USERID = " + userId);
    }

    /**
     * 很据用户身份证号获取用户信息.
     *
     * @param idCardNo 用户身份证号
     * @return User
     */
    public User getUserByUserIdCardNo(String idCardNo) {
        if (StringUtils.isNullOrEmpty(idCardNo)) {
            return null;
        }

        return getUserByFilterSql("IDCARDNO = '" + idCardNo + STRMESS8);
    }

    /**
     * 根据用户邮箱获取用户信息.
     *
     * @param email 用户邮箱
     * @return User
     */
    public User getUserByUserIdEmail(String email) {
        if (StringUtils.isNullOrEmpty(email)) {
            return null;
        }

        return getUserByFilterSql("EMAIL = '" + email + STRMESS8);
    }

    /**
     * 获取用户信息列表.
     *
     * @return List
     * @throws DataAccessException 异常
     */
    public List<User> getUserList() throws DataAccessException {
        String sql = "select USERID,USERNAME,PASSWORD,REALNAME,GENDER,COMPANY," +
                "DEPT,IDCARDNO,POSITION,BIRTH,EDUCATION,GRADUATE_SCHOOL" +
                ",GRADUATE_DATE,MAJOR,EMAIL,TEL,RESUME_URL,ROLE_ID," +
                "AUDIT_STATUS,CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_user " +
                " where DELETE_FLAG=0 AND ROLE_ID=0";
        try {
            return myJdbcTemplate.query(sql, new UserMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户模糊查询.
     *
     * @param userName 用户模糊查询的用户姓名
     * @return List
     * @throws DataAccessException 异常
     */
    public List<User> getUserListSearchByName(String userName) throws DataAccessException {
        String sql = "select USERID,USERNAME,PASSWORD,REALNAME,GENDER,COMPANY,DEPT," +
                "IDCARDNO,POSITION,BIRTH,EDUCATION,GRADUATE_SCHOOL" +
                ",GRADUATE_DATE,MAJOR,EMAIL,TEL,RESUME_URL," +
                "ROLE_ID,AUDIT_STATUS," +
                "CREATE_TIME,UPDATE_TIME," +
                "DELETE_FLAG from tb_user " +
                "where DELETE_FLAG=0 AND ROLE_ID=0";
        if (!StringUtils.isNullOrEmpty(userName)) {
            sql += " AND " +
                    "( USERNAME LIKE '%" + userName + "%' or" +
                    " REALNAME LIKE '%" + userName + "%')";
        }

        try {
            return myJdbcTemplate.query(sql, new UserMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户模糊查询.
     *
     * @param user         模糊查询的用户
     * @param pagingEntity 分页组件
     * @return List
     * @throws DataAccessException 异常
     */
    public List<User> getUserListSearchByName(User user, PagingEntity pagingEntity) throws DataAccessException {
        int startIndex = pagingEntity.getStartIndex();      //开始位置
        int pageSize = pagingEntity.getPageSize();          //每页大小
        String sortColumn = pagingEntity.getSortColumn();   //排序字段
        String sortDir = pagingEntity.getSortDir();         //排序方式
        //String searchValue = pagingEntity.getSearchValue(); //检索内容
        String sortSql = STRMESS3;                                //排序sql片段
        //String searchSql = STRMESS3;                              //检索sql片段

        //判空
        if (org.apache.commons.lang.StringUtils.isNotBlank(sortColumn) &&
                org.apache.commons.lang.StringUtils.isNotBlank(sortDir)) {

            if (STR1.equals(sortColumn)) {
                sortSql = "ORDER BY convert(" + sortColumn + " USING GBK) " + sortDir;
            } else {
                sortSql = "ORDER BY " + sortColumn + STRMESS5 + sortDir;
            }
        }
        String sql = "select USERID,USERNAME,PASSWORD,REALNAME,GENDER,COMPANY," +
                "DEPT,IDCARDNO,POSITION,BIRTH,EDUCATION,GRADUATE_SCHOOL" +
                ",GRADUATE_DATE,MAJOR,EMAIL,TEL,RESUME_URL,ROLE_ID,AUDIT_STATUS," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_user " +
                "where DELETE_FLAG=0 AND ROLE_ID=0 ";
        if (!StringUtils.isNullOrEmpty(user.getUserName())) {
            sql += " AND ( USERNAME LIKE '%" + StringUtil.zhuanyi(user.getUserName()) +
                    "%' or REALNAME LIKE '%" + StringUtil.zhuanyi(user.getUserName()) + "%') ";
        }

        if (0 != user.getCompany()) {
            sql += " AND ( COMPANY = " + user.getCompany() + STRMESS9;
        }

        if (0 != user.getDept()) {
            sql += " AND ( DEPT = " + user.getDept() + STRMESS9;
        }

        sql += sortSql + " LIMIT " + startIndex + STRMESS4 + pageSize;
        try {
            UserMapper userMapper = new UserMapper();
            userMapper.setReturnPasswordFlag(false);
            return myJdbcTemplate.query(sql, userMapper);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户模糊查询总数.
     *
     * @param user         模糊查询的用户
     * @param pagingEntity 分页组件
     * @return int
     * @throws DataAccessException 异常
     */
    public int getUserListSearchByNameCount(User user, PagingEntity pagingEntity) throws DataAccessException {
        String sql = "select COUNT(1) from tb_user where DELETE_FLAG=0 AND ROLE_ID=0 ";
        if (!StringUtils.isNullOrEmpty(user.getUserName())) {
            sql += " AND ( USERNAME LIKE '%" + StringUtil.zhuanyi(user.getUserName()) +
                    "%' or REALNAME LIKE '%" + StringUtil.zhuanyi(user.getUserName()) + "%') ";
        }

        if (0 != user.getCompany()) {
            sql += " AND ( COMPANY = " + user.getCompany() + STRMESS9;
        }

        if (0 != user.getDept()) {
            sql += " AND ( DEPT = " + user.getDept() + STRMESS9;
        }

        try {
            List<Integer> counts = myJdbcTemplate.queryForList(sql, Integer.class);
            if (counts != null) {
                return counts.get(0);
            }
            return 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 确认删除用户.
     *
     * @param userIdList 用户Id 列表
     * @return List
     * @throws DataAccessException 异常
     */
    public List<User> confirmToDeleteUsers(List<Long> userIdList) throws DataAccessException {
        if (userIdList == null || userIdList.size() == 0) {
            return null;
        }

        StringBuilder sbIdList = new StringBuilder();
        for (Long id : userIdList) {
            sbIdList.append(id + STRMESS3);
            sbIdList.append(STRMESS4);
        }
        String strIdList = sbIdList.toString();
        strIdList = strIdList.substring(0, strIdList.length() - 1);
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct USERID,USERNAME,PASSWORD,REALNAME,GENDER,");
        sql.append("COMPANY,DEPT,IDCARDNO,POSITION,BIRTH,EDUCATION,GRADUATE_SCHOOL,");
        sql.append("GRADUATE_DATE,MAJOR,EMAIL,TEL,RESUME_URL,ROLE_ID,");
        sql.append("AUDIT_STATUS,tuser.CREATE_TIME,tuser.UPDATE_TIME,tuser.DELETE_FLAG ");
        sql.append(" FROM tb_testinfotouser tp ");
        sql.append(" INNER JOIN tb_user tuser ON tuser.USERID = tp.USER_ID ");
        sql.append("  WHERE tp.DELETE_FLAG = 0  ");
        sql.append("  AND USERID in (" + strIdList + ") ");
        try {
            UserMapper userMapper = new UserMapper();
            userMapper.setReturnPasswordFlag(true);
            return myJdbcTemplate.query(sql.toString(), userMapper);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private StringBuilder initStringBuilder(StringBuilder strBuilder,
                                            String columnName, String strValue, boolean useSingleQuoteFlag)
            throws DataAccessException {
        if (!StringUtils.isNullOrEmpty(strValue) &&
                !StringUtils.isNullOrEmpty(columnName)) {
            if (useSingleQuoteFlag) {
                strBuilder.append(STRMESS5 + columnName + " = '" + strValue + STRMESS7);
            } else {
                strBuilder.append(STRMESS5 + columnName + " = " + strValue + STRMESS4);
            }
        }
        return strBuilder;
    }

    private StringBuilder initStringBuilderContainsNull(StringBuilder strBuilder,
                                                        String columnName, String strValue, boolean useSingleQuoteFlag) {
        if (!StringUtils.isNullOrEmpty(columnName)) {
            if (StringUtils.isNullOrEmpty(strValue)) {
                strBuilder.append(STRMESS5 + columnName + " = null,");
            } else {
                if (useSingleQuoteFlag) {
                    strBuilder.append(STRMESS5 + columnName + " = '" + strValue + STRMESS7);
                } else {
                    strBuilder.append(STRMESS5 + columnName + " = " + strValue + STRMESS4);
                }
            }
        }
        return strBuilder;
    }

    private User getUserByFilterSql(String strFilterSql) {
        String sql = "select USERID,USERNAME,PASSWORD,REALNAME,GENDER,COMPANY,DEPT," +
                "IDCARDNO,POSITION,BIRTH,EDUCATION,GRADUATE_SCHOOL" +
                ",GRADUATE_DATE,MAJOR,EMAIL,TEL,RESUME_URL,ROLE_ID,AUDIT_STATUS," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_user " +
                "where DELETE_FLAG=0 AND " + strFilterSql;
        try {
            UserMapper userMapper = new UserMapper();
            userMapper.setReturnPasswordFlag(true);
            List<User> lstUser = myJdbcTemplate.query(sql, userMapper);
            if (lstUser != null && lstUser.size() > 0) {
                return lstUser.get(0);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    @SuppressWarnings("rawtypes")
    private static final class UserMapper implements RowMapper {
        private boolean returnPasswordFlag; //是否直接返回密码 true：返回，false: 不返回

        public boolean isReturnPasswordFlag() {
            return returnPasswordFlag;
        }

        public void setReturnPasswordFlag(boolean returnPasswordFlag) {
            this.returnPasswordFlag = returnPasswordFlag;
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            User user = new User();
            user.setUserId(rs.getLong("USERID"));
            user.setUserName(rs.getString("USERNAME"));
            user.setRealName(rs.getString(STR1));
            if (this.isReturnPasswordFlag()) {
                user.setPassWord(rs.getString(STR2));
            } else {
                user.setPassWord(STR2);
            }
            user.setGender(rs.getInt("GENDER"));
            user.setCompany(rs.getLong("COMPANY"));
            user.setDept(rs.getLong("DEPT"));
            user.setIdCardNo(StringUtils.isNullOrEmpty(
                    rs.getString(STR4)) ? STRMESS3 : rs.getString(STR4));
            user.setPosition(StringUtils.isNullOrEmpty(
                    rs.getString(STR5)) ? STRMESS3 : rs.getString(STR5));
            user.setBirth(StringUtils.isNullOrEmpty(
                   rs.getString(STR3) ) ? STRMESS3 : sdf.format(rs.getTimestamp(STR3)));
            user.setEducation(StringUtils.isNullOrEmpty(
                    rs.getString(STR6)) ? STRMESS3 : rs.getString(STR6));
            user.setGraduateSchool(StringUtils.isNullOrEmpty(
                    rs.getString(STR7)) ? STRMESS3 : rs.getString(STR7));
            user.setGraduateDate(StringUtils.isNullOrEmpty(
                    rs.getString(STR8)) ? STRMESS3 : sdf.format(rs.getTimestamp(STR8)));
            user.setMajor(StringUtils.isNullOrEmpty(
                    rs.getString(STR9)) ? STRMESS3 : rs.getString(STR9));
            user.setEmail(StringUtils.isNullOrEmpty(
                    rs.getString(STRMESS1)) ? STRMESS3 : rs.getString(STRMESS1));
            user.setTel(StringUtils.isNullOrEmpty(
                    rs.getString(STRMESS2)) ? STRMESS3 : rs.getString(STRMESS2));
            user.setResumeUrl(StringUtils.isNullOrEmpty(
                    rs.getString(STRMESS6)) ? STRMESS3 : rs.getString(STRMESS6));
            user.setRoleId(rs.getLong("ROLE_ID"));
            user.setAuditStatus(rs.getInt("AUDIT_STATUS"));
            user.setCreateTime(rs.getString("CREATE_TIME"));
            user.setUpdateTime(rs.getString("UPDATE_TIME"));
            user.setDeleteFlag(0);
            return user;
        }
    }
}
