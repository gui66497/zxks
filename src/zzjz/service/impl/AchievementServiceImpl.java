package zzjz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apdplat.word.analysis.SimpleTextSimilarity;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.Achievement;
import zzjz.bean.Answer;
import zzjz.bean.ResultInfo;
import zzjz.service.AchievementService;
import zzjz.util.MyJdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2016/6/03 17:26
 * @ClassName: AchievementServiceImpl
 * @Description: 成绩操作service接口实现类
 */
@Service
public class AchievementServiceImpl implements AchievementService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AchievementServiceImpl.class);

    /**
     * 注入MyJdbcTemplate.
     */
    @Autowired
    public MyJdbcTemplate myJdbcTemplate;

    private static final int NUM_THREE = 3;

    private static final int NUM_FOUR = 4;

    private static final int NUM_FIVE = 5;

    /**
     * 根据考试信息Id获取成绩列表.
     *
     * @param testInfoId 考试信息Id
     * @return List
     */
    public List<Achievement> getAchievementListByTestInfoId(long testInfoId) {
        String sql = "select ACHIEVEMENT_ID,TESTINFO_ID,USER_ID,USER_MARK,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_achievement where TESTINFO_ID=?";
        return myJdbcTemplate.query(sql, new AchievementMapper(), testInfoId);
    }

    /**
     * 获取成绩.
     *
     * @param achievement 成绩实体类
     * @return Achievement
     */
    public Achievement getAchievement(Achievement achievement) {
        if (achievement.getTestInfoId() < 0 || achievement.getUserId() < 0) {
            return null;
        }
        String sql = "select ACHIEVEMENT_ID,TESTINFO_ID,USER_ID,USER_MARK,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG from tb_achievement " +
                "where DELETE_FLAG=0 and TESTINFO_ID=? and USER_ID=?";
        List<Achievement> achievementList =
                myJdbcTemplate.query(sql, new AchievementMapper(), achievement.getTestInfoId(),
                        achievement.getUserId());
        //判断查询结果是否为空
        if (achievementList != null && achievementList.size() > 0) {
            return achievementList.get(0);
        }
        return null;
    }

    /**
     * 添加成绩.
     *
     * @param achievement 成绩实体类
     * @return boolean
     */
    public boolean addAchievement(final Achievement achievement) {
        String sql = "insert into tb_achievement(ACHIEVEMENT_ID,TESTINFO_ID,USER_ID," +
                "USER_MARK,CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                "values(?,?,?,?,?,NOW(),NOW(),0)";
        myJdbcTemplate.update(sql, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, achievement.getAchievementId());
                ps.setLong(2, achievement.getTestInfoId());
                ps.setLong(NUM_THREE, achievement.getUserId());
                ps.setDouble(NUM_FOUR, achievement.getUserMark());
                ps.setLong(NUM_FIVE, achievement.getCreator());
            }
        });
        ResultInfo[] results = achievement.getResults();
        long userId = achievement.getUserId();
        return addResults(results, userId); //添加回答结果

    }

    /**
     * 根据成绩Id删除成绩.
     *
     * @param id 成绩信息id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteAchievementById(long id) throws DataAccessException {
        String sql = "update tb_achievement set DELETE_FLAG=1 where ACHIEVEMENT_ID =" + id;
        try {
            myJdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        return true;
    }

    /**
     * 编辑成绩信息.
     *
     * @param achievement 成绩实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateAchievement(final Achievement achievement) throws DataAccessException {
        String sql = "update tb_achievement set USER_MARK=?,UPDATE_TIME=NOW()," +
                "UPDATE_TIME=NOW() where ACHIEVEMENT_ID =?";

        myJdbcTemplate.update(sql, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setDouble(1, achievement.getUserMark());
                ps.setLong(2, achievement.getAchievementId());
            }
        });
        return true;
    }

    /**
     * 添加成绩结果.
     *
     * @param results 结果信息
     * @param userId  用户id,当前考试人员
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addResults(final ResultInfo[] results, final long userId) throws DataAccessException {
        if (!(results != null && results.length > 0)) {
            return true;
        }

        String sql = "INSERT INTO tb_result(ACHIEVEMENT_ID,ITEM_ID,RESULT,SCORE," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) VALUES (?,?,?,?,?,NOW(),NOW(),0)";

        int[] insertCounts = new int[0];
        try {
            insertCounts = myJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, results[i].getAchievementId());
                    ps.setLong(2, results[i].getItemId());
                    ps.setString(NUM_THREE, results[i].getResult());
                    ps.setDouble(NUM_FOUR, results[i].getScore());
                    ps.setLong(NUM_FIVE, userId);
                }

                public int getBatchSize() {
                    return results.length;
                }
            });
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if (insertCounts != null && insertCounts.length > 0) {
            return true;
        }

        return false;
    }

    /**
     * 计算分值.
     * @param answerList 回答列表
     * @return 分值
     */
    public List<Answer> calMark(List<Answer> answerList) {

        for (int i = 0; i < answerList.size(); i++) {
            String answer = answerList.get(i).getAnswer();
            String result = answerList.get(i).getResult();
            int value = answerList.get(i).getValue();

            if (answerList.get(i).getType() == 5) {
                String answerStr = answerList.get(i).getAnswer();
                String resultStr = answerList.get(i).getResult();
                if (StringUtils.isNotBlank(answerStr) && StringUtils.isNotBlank(resultStr)) {
                    String[] answerArr = answerStr.split("░");
                    String[] resultArr = resultStr.split("░");
                    int correctNum = 0;
                    if (answerArr.length == resultArr.length) {
                        for (int k = 0; k < answerArr.length; k++) {
                            if (answerArr[k].toLowerCase().equals(resultArr[k].toLowerCase())) {
                                correctNum += 1;
                            }
                        }
                    }
                    answerList.get(i).setScore(value * (correctNum / (double) answerArr.length));
                }

            } else if (answerList.get(i).getType() == 4) {
                //简答题进行相似度判断
                long t1 = System.currentTimeMillis();
                LOGGER.debug("开始计算 " + result + "和 " + answer + "的相似度");
                //CosineTextSimilarity similarity = new CosineTextSimilarity();
                SimpleTextSimilarity similarity = new SimpleTextSimilarity();
                double similarResult = similarity.similarScore(result, answer);
                long t2 = System.currentTimeMillis();
                LOGGER.debug("计算结束 " + "相似度为 " + similarResult + " ，耗时：" + (t2 - t1) + "ms");
                answerList.get(i).setScore(value * similarResult);
            } else {
                if (answer.equals(result)) {
                    answerList.get(i).setScore(value);
                } else {
                    answerList.get(i).setScore(0L);
                }
            }
        }
        return answerList;
    }

    @SuppressWarnings("rawtypes")
    private static final class AchievementMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Achievement achievement = new Achievement();
            achievement.setAchievementId(rs.getLong("ACHIEVEMENT_ID"));
            achievement.setTestInfoId(rs.getLong("TESTINFO_ID"));
            achievement.setUserId(rs.getLong("USER_ID"));
            achievement.setUserMark(rs.getInt("USER_MARK"));
            achievement.setCreator(rs.getLong("CREATOR"));
            achievement.setCreateTime(rs.getDate("CREATE_TIME"));
            achievement.setUpdateTime(rs.getDate("UPDATE_TIME"));
            achievement.setDeleteFlag(rs.getInt("DELETE_FLAG"));
            return achievement;
        }
    }

}
