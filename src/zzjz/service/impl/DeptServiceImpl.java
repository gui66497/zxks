package zzjz.service.impl;

import com.mysql.jdbc.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzjz.bean.Dept;
import zzjz.service.DeptService;
import zzjz.util.MyJdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author caixiaolong
 * @version 2016/6/6 10:57
 * @ClassName: DeptServiceImpl
 * @Description: 部门操作service接口实现类
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private MyJdbcTemplate myJdbcTemplate;

    /**
     * 添加部门.
     *
     * @param dept 部门实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean addDept(Dept dept) throws DataAccessException {

        final Dept deptFin = dept;
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                String sqlStr = "INSERT INTO tb_dept( DEPT_ID,DEPT_NAME,PARENT_ID,"
                        +
                        "COMPANY_ID,CREATOR,CREATE_TIME,UPDATE_TIME,"
                        +
                        "DELETE_FLAG) VALUES(?,?,?,?,?,NOW(),NOW(),0)";
                PreparedStatement ps =
                        con.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);
                int i = 0;
                ps.setLong(++i, deptFin.getDeptId());
                ps.setString(++i, deptFin.getDeptName());
                ps.setLong(++i, deptFin.getParentId());
                ps.setLong(++i, deptFin.getCompanyId());
                ps.setLong(++i, deptFin.getCreator());

                return ps;
            }
        };
        try {
            int affectCount = myJdbcTemplate.update(psc);
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
     * 根据部门ID删除部门.
     *
     * @param deptId 部门Id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteDeptById(long deptId) throws DataAccessException {
        String sql = "UPDATE tb_dept  SET DELETE_FLAG = 1 WHERE DEPT_ID = "
                + deptId;

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
     * 编辑部门信息.
     *
     * @param dept 部门实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateDept(Dept dept) throws DataAccessException {
        String sql = "UPDATE tb_dept  SET DEPT_NAME = '"
                + dept.getDeptName()
                + "',"
                +
                " PARENT_ID = " + dept.getParentId()
                +
                ", COMPANY_ID = " + dept.getCompanyId()
                +
                ", UPDATE_TIME = NOW()  WHERE DEPT_ID = "
                + dept.getDeptId();

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
     * 根据部门名称获取部门信息.
     *
     * @param dept 部门实体
     * @return Dept
     * @throws DataAccessException 异常
     */
    public Dept getDeptByDeptName(Dept dept) throws DataAccessException {
        String sql = "select DEPT_ID,DEPT_NAME,PARENT_ID,COMPANY_ID,"
                +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_dept "
                +
                "where DELETE_FLAG = 0 AND DEPT_NAME = '"
                + dept.getDeptName()
                + "' AND"
                +
                " COMPANY_ID ='"
                + dept.getCompanyId()
                + "'";

        try {
            List<Dept> lstDept = myJdbcTemplate.query(sql, new DeptMapper());
            if (lstDept != null && lstDept.size() > 0) {
                return lstDept.get(0);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 根据部门ID获取部门信息.
     *
     * @param deptId 部门Id
     * @return Dept
     * @throws DataAccessException 异常
     */
    public Dept getDeptByDeptId(long deptId) throws DataAccessException {
        String sql = "select DEPT_NAME,DEPT_ID,PARENT_ID,COMPANY_ID,CREATOR,"
                +
                "UPDATE_TIME,CREATE_TIME,DELETE_FLAG from tb_dept "
                +
                "where DELETE_FLAG = 0 AND DEPT_ID = "
                + deptId;

        try {
            List<Dept> lstDept = myJdbcTemplate.query(sql, new DeptMapper());
            if (lstDept != null && lstDept.size() > 0) {
                return lstDept.get(0);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取部门信息列表.
     *
     * @return List
     * @throws DataAccessException 异常
     */
    public List<Dept> getDeptList() throws DataAccessException {
        String sql = "select DEPT_ID,DEPT_NAME,PARENT_ID,COMPANY_ID,CREATOR,"
                +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_dept "
                +
                "where DELETE_FLAG = 0 ";

        try {
            return myJdbcTemplate.query(sql, new DeptMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据单位ID获取部门信息列表.
     *
     * @param companyId 单位Id
     * @return List
     * @throws DataAccessException 异常
     */
    public List<Dept> getDeptListByCompanyId(long companyId) throws DataAccessException {
        String sql = "select DEPT_ID,DEPT_NAME,PARENT_ID,COMPANY_ID,CREATOR,"
                +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_dept "
                +
                "where DELETE_FLAG = 0 AND COMPANY_ID = "
                + companyId;

        try {
            return myJdbcTemplate.query(sql, new DeptMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据部门ID获取用户数.
     *
     * @param deptId 部门Id
     * @return int
     * @throws DataAccessException 异常
     */
    public int getUserCountByDeptId(long deptId) throws DataAccessException {
        String sql = "select count(1) from tb_user where DELETE_FLAG=0 AND DEPT=" + deptId;
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
     * 根据部门信息Mapper类
     *
     */
    private static final class DeptMapper implements RowMapper {
        public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            Dept dept = new Dept();
            dept.setDeptId(rs.getLong("DEPT_ID"));
            dept.setDeptName(rs.getString("DEPT_NAME"));
            dept.setParentId(rs.getLong("PARENT_ID"));
            dept.setCompanyId(rs.getLong("COMPANY_ID"));
            dept.setCreator(rs.getLong("CREATOR"));
            dept.setCreateTime(rs.getString("CREATE_TIME"));
            dept.setUpdateTime(rs.getString("UPDATE_TIME"));
            dept.setDeleteFlag(0);
            return dept;
        }
    }
}
