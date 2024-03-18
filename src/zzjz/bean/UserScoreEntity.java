package zzjz.bean;

/**
 * @author 梅宏振
 * @version 2015年6月16日 下午9:32:29
 * @ClassName: TestResultEntity
 * @Description: 考生考试成绩实体类
 */
public class UserScoreEntity {

    //用户ID
    private long userId;

    //试卷名称
    private String testPaperName;

    //用户名
    private String userName;

    //真实姓名
    private String realName;

    //性别
    private int gender;

    //得分
    private int score;

    //总分
    private int totalScore;

    //成绩ID
    private String achievementId;


    /**
     * 获取用户ID.
     *
     * @return long
     */
    public long getUserId() {
        return this.userId;
    }

    /**
     * 设置用户ID.
     *
     * @param userId 用户ID
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 获取试卷名称.
     *
     * @return String
     */
    public String getTestPaperName() {
        return this.testPaperName;
    }

    /**
     * 设置试卷名称.
     *
     * @param testPaperName 试卷名称
     */
    public void setTestPaperName(String testPaperName) {
        this.testPaperName = testPaperName;
    }

    /**
     * 获取用户名.
     *
     * @return String
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置用户名.
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取真实姓名.
     *
     * @return String
     */
    public String getRealName() {
        return this.realName;
    }

    /**
     * 设置真实姓名.
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取性别.
     *
     * @return int
     */
    public int getGender() {
        return this.gender;
    }

    /**
     * 设置性别.
     *
     * @param gender 性别
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * 获取得分.
     *
     * @return int
     */
    public int getScore() {
        return this.score;
    }

    /**
     * 设置得分.
     *
     * @param score 得分
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 获取总分.
     *
     * @return int
     */
    public int getTotalScore() {
        return this.totalScore;
    }

    /**
     * 设置总分.
     *
     * @param totalScore 总分
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 获取成绩ID.
     *
     * @return String
     */
    public String getAchievementId() {
        return this.achievementId;
    }

    /**
     * 设置成绩ID.
     *
     * @param achievementId 成绩ID
     */
    public void setAchievementId(String achievementId) {
        this.achievementId = achievementId;
    }
}
