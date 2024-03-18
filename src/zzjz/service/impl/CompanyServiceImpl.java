package zzjz.service.impl;

import com.mysql.jdbc.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzjz.bean.Company;
import zzjz.service.CompanyService;
import zzjz.util.MyJdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * @author caixiaolong
 * @version 2016/6/6 9:21
 * @ClassName: CompanyServiceImpl
 * @Description: 单位操作service接口实现类
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private MyJdbcTemplate myJdbcTemplate;

    /**
     * 添加单位.
     *
     * @param company 单位实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    @Transactional
    public boolean addCompany(Company company) throws DataAccessException {
        final Company companyFin = company;
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                String sqlStr = "INSERT INTO tb_company( COMPANY_ID,COMPANY_NAME," +
                        "CREATOR,CREATE_TIME,UPDATE_TIME," +
                        "DELETE_FLAG) VALUES(?,?,?,NOW(),NOW(),0)";
                PreparedStatement ps =
                        con.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

                int i = 0;
                ps.setLong(++i, companyFin.getCompanyId());
                ps.setString(++i, companyFin.getCompanyName());
                ps.setLong(++i, companyFin.getCreator());

                return ps;
            }
        };
        int affectCount = myJdbcTemplate.update(psc);
        if (affectCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据Id删除单位.
     *
     * @param companyId 单位Id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteCompanyById(long companyId) throws DataAccessException {
        String sql = "UPDATE tb_company  SET DELETE_FLAG = 1 WHERE COMPANY_ID = " + companyId;
        int affectCount = myJdbcTemplate.update(sql);
        if (affectCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 编辑单位信息.
     *
     * @param company 单位实体
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateCompany(Company company) throws DataAccessException {
        String sql = "UPDATE tb_company  SET COMPANY_NAME = '" + company.getCompanyName() + "'," +
                " UPDATE_TIME = NOW()  WHERE COMPANY_ID = " + company.getCompanyId();

        int affectCount = myJdbcTemplate.update(sql);
        if (affectCount > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 根据单位名称获取单位信息.
     *
     * @param companyName 单位名
     * @return Company
     * @throws DataAccessException 异常
     */
    public Company getCompanyByCompanyName(String companyName) throws DataAccessException {
        String sql = "select COMPANY_NAME,COMPANY_ID,CREATOR,CREATE_TIME," +
                "DELETE_FLAG,UPDATE_TIME from tb_company " +
                "where DELETE_FLAG = 0 AND COMPANY_NAME = '" + companyName + "'";

        List<Company> lstCompany = myJdbcTemplate.query(sql, new CompanyMapper());
        if (lstCompany != null && lstCompany.size() > 0) {
            return lstCompany.get(0);
        } else {
            return null;
        }

    }

    /**
     * 根据单位Id获取单位信息.
     *
     * @param companyId 单位Id
     * @return Company
     * @throws DataAccessException 异常
     */
    public Company getCompanyByCompanyId(long companyId) throws DataAccessException {
        String sql = "select COMPANY_ID,COMPANY_NAME,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG from tb_company " +
                "where DELETE_FLAG = 0 AND COMPANY_ID = " + companyId;

        List<Company> lstCompany = myJdbcTemplate.query(sql, new CompanyMapper());
        if (lstCompany != null && lstCompany.size() > 0) {
            return lstCompany.get(0);
        } else {
            return null;
        }

    }

    /**
     * 获取单位信息列表.
     *
     * @return List
     * @throws DataAccessException 异常
     */
    public List<Company> getCompanyList() throws DataAccessException {
        String sql = "select COMPANY_ID,COMPANY_NAME,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG from tb_company " +
                "where DELETE_FLAG = 0 ";
        try {
            return myJdbcTemplate.query(sql, new CompanyMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 根据单位ID获取用户数.
     *
     * @param companyId 单位Id
     * @return int
     * @throws DataAccessException 异常
     */
    public int getUserCountByCompanyId(long companyId) throws DataAccessException {
        String sql = "select count(1) from tb_user where DELETE_FLAG=0 AND COMPANY=" + companyId;

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

    @SuppressWarnings("rawtypes")
    private static final class CompanyMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company company = new Company();
            company.setCompanyId(rs.getLong("COMPANY_ID"));
            company.setCompanyName(rs.getString("COMPANY_NAME"));
            company.setCreator(rs.getLong("CREATOR"));
            company.setCreateTime(rs.getString("CREATE_TIME"));
            company.setUpdateTime(rs.getString("UPDATE_TIME"));
            company.setDeleteFlag(0);
            return company;
        }
    }
}
