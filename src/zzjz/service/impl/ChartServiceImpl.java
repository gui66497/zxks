package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.ChartCountEntity;
import zzjz.service.ChartsService;
import zzjz.util.MyJdbcTemplate;
import zzjz.util.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengyunfeng
 * @version 2016/6/21 9:20
 * @ClassName: ChartServiceImpl
 * @Description: 图表统计实现类
 */
@Service
public class ChartServiceImpl implements ChartsService {
    /**
     * 根据实体类的parentId,companyId获取需要统计的数据.
     *
     * @param paperId  试卷ID
     * @param companyId  单位Id
     * @return 统计数据
     */
    @Autowired
    public MyJdbcTemplate myJdbcTemplate;

    /**
     * 返回统计信息.
     *
     * @param paperId    试卷ID
     * @param userIDList 用户ID列表
     * @return List
     */
    public List<ChartCountEntity> returnChartInfo(String paperId, List<String> userIDList) {
        List<ChartCountEntity> returnList = new ArrayList<ChartCountEntity>();
        if (userIDList.isEmpty()) {
            return returnList;
        }
        //String userIdStr = StringUtil.listConvertToString(userIDList, ",");
        String sql = "SELECT  AVG(TA.USER_MARK) AS AVGCOUNT," +
                "COUNT(TA.USER_ID) AS COUNT FROM tb_achievement TA " +
                "LEFT JOIN tb_testinfo TT ON TA.TESTINFO_ID = TT.ID WHERE 1 = 1 AND ";
        if (paperId != null) {
            sql = sql + "TEST_ID = " + paperId + " AND TT.DELETE_FLAG = 0 AND" +
                    " TA.DELETE_FLAG = 0 AND  " + StringUtil.getInCondition("USER_ID", userIDList);
        } else {
            sql = sql + "  TT.DELETE_FLAG = 0 AND TA.DELETE_FLAG = 0" +
                    " AND  " + StringUtil.getInCondition("USER_ID", userIDList);
        }

        returnList = myJdbcTemplate.query(sql.toString(), new ChartInfoMapper());
        return returnList;
    }

    /**
     * 根据单位实体类的companyId取得该单位下每个部门的人员.
     *
     * @param companyId 单位Id
     * @return 统计数据
     */
    public List<String> getUserInfo(String companyId) {
        List<String> returnList = new ArrayList<String>();
        if (companyId == null) {
            return returnList;
        }
        String sql = "SELECT USERID from tb_user" +
                " where  DELETE_FLAG = 0 AND   COMPANY  = " + companyId;
        returnList = myJdbcTemplate.queryForList(sql.toString(), String.class);
        return returnList;
    }

    /**
     * 根据部门ID获取用户列表.
     *
     * @param deptId 部门Id
     * @return List
     */
    public List<String> getUserListByDeptId(long deptId) {
        List<String> returnList = new ArrayList<String>();
        if (deptId == 0L) {
            return returnList;
        }
        String sql = "SELECT USERID from tb_user where DELETE_FLAG = 0 AND  DEPT = " + deptId;
        returnList = myJdbcTemplate.query(sql.toString(), new ChartMapper());
        return returnList;
    }

    /**
     * 根据部门ids获取用户列表.
     * @param deptId 部门id
     * @return List
     */
    public List<String> getUserListByDeptIds(String deptId) {
        List<String> returnList = new ArrayList<String>();
        if (deptId == null) {
            return returnList;
        }
        String sql = "SELECT USERID from tb_user where DELETE_FLAG = 0 AND  DEPT in( " + deptId + ")";
        returnList = myJdbcTemplate.query(sql.toString(), new ChartMapper());
        return returnList;
    }

    private static final class ChartMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            String returnList = rs.getString("USERID");
            return returnList;
        }
    }

    private static final class ChartInfoMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChartCountEntity chartCountEntity = new ChartCountEntity();
            chartCountEntity.setAvgCount(
                    rs.getString("AVGCOUNT") == null ? "0" : rs.getString("AVGCOUNT"));
            chartCountEntity.setPersonCount(rs.getString("COUNT"));
            return chartCountEntity;
        }
    }
}
