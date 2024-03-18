package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guzhenggen
 * @version 2016/6/15 13:35
 * @ClassName: AchievementRequest
 * @Description: 成绩实体类request
 */
@XmlRootElement
public class AchievementRequest extends BaseRequest {

    //成绩实体类
    private Achievement achievement;


    /**
     * 获取成绩实体类.
     *
     * @return Achievement
     */
    public Achievement getAchievement() {
        return this.achievement;
    }

    /**
     * 设置成绩实体类.
     *
     * @param achievement 成绩实体类
     */
    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }
}
