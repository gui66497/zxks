package zzjz.service;

import zzjz.bean.Achievement;
import zzjz.bean.Answer;
import zzjz.bean.ResultInfo;

import java.util.List;

/**
 * @author guitang.fang
 * @ClassName:
 * @Description:
 * @version 2016/6/3 17:11
 */
public interface AchievementService {

    /**
     * 根据考试id获取所有成绩.
     * @param testInfoId 考试信息Id
     * @return 成绩实体集合
     */
    List<Achievement> getAchievementListByTestInfoId(long testInfoId);

    /**
     * 根据考试信息id和用户id获取成绩信息.
     * @param achievement 成绩实体类
     * @return 成绩实体类
     */
    Achievement getAchievement(Achievement achievement);

    /**
     * 添加成绩信息.
     * @param achievement 成绩实体类
     * @return 成绩实体类
     */
    boolean addAchievement(Achievement achievement);

    /**
     * 删除成绩信息.
     * @param id 成绩信息id
     * @return 删除结果
     */
    boolean deleteAchievementById(long id);

    /**
     * 修改成绩信息.
     * @param achievement 成绩实体类
     * @return 修改结果
     */
    boolean updateAchievement(Achievement achievement);

    /**
     * 批量添加回答结果信息.
     * @param results 结果信息
     * @param userId 用户id,当前考试人员
     * @return 操作结果
     */
    public boolean addResults(final ResultInfo[] results, final long userId);

    /**
     * 计算总分
     * @param answerList 回答列表
     * @return 总分
     */
    List<Answer> calMark(List<Answer> answerList);
}
