package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * @author guitang.fang
 * @ClassName: TestPaper
 * @Description: 试卷实体类
 * @version 2016/6/1 8:59
 */
@XmlRootElement
public class TestPaper {


    //考生得分
    private String userMark;

    //考生选项
    private String userResult;

    //试卷标准答案
    private String answer;

    //题目ID
    private String itemId;

    //id
    private long id;

    //试卷名称
    private String testpaperName;

    //试卷描述
    private String testpaperDescription;

    //试卷说明
    private String testpaperExplain;

    //试卷出题方式,1:手工组卷,2:智能组卷
    private int testpaperMode;

    //考试分类ID(关联试卷类型表)
    private long categoryId;

    //考试分类中文名
    private String categoryName;

    //试卷类型,0:普通考试,1:模拟考试,2:个人练习
    private int testpaperType;

    //试卷总分值
    private int markTotal;

    //考试时间(单位分钟)
    private int testTime;

    //题目顺序,1:乱序,2:顺序
    private int itemSequence;

    //选项顺序,1:乱序,2:顺序
    private int answerSequence;

    //创建者即出卷人（ID）
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    //存放模块
    private TestPaperPart[] testPaperParts;

    //存放试卷模块
    private List<TestPaperPart> testPaperPartList;

    /**
     * 获取考生得分.
     *
     * @return String
     */
    public String getUserMark() {
        return this.userMark;
    }

    /**
     * 设置考生得分.
     *
     * @param userMark 考生得分
     */
    public void setUserMark(String userMark) {
        this.userMark = userMark;
    }

    /**
     * 获取考生选项.
     *
     * @return String
     */
    public String getUserResult() {
        return this.userResult;
    }

    /**
     * 设置考生选项.
     *
     * @param userResult 考生选项
     */
    public void setUserResult(String userResult) {
        this.userResult = userResult;
    }

    /**
     * 获取试卷标准答案.
     *
     * @return String
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * 设置试卷标准答案.
     *
     * @param answer 试卷标准答案
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * 获取题目ID.
     *
     * @return String
     */
    public String getItemId() {
        return this.itemId;
    }

    /**
     * 设置题目ID.
     *
     * @param itemId 题目ID
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * 获取id.
     *
     * @return long
     */
    public long getId() {
        return this.id;
    }

    /**
     * 设置id.
     *
     * @param id id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取试卷名称.
     *
     * @return String
     */
    public String getTestpaperName() {
        return this.testpaperName;
    }

    /**
     * 设置试卷名称.
     *
     * @param testpaperName 试卷名称
     */
    public void setTestpaperName(String testpaperName) {
        this.testpaperName = testpaperName;
    }

    /**
     * 获取试卷描述.
     *
     * @return String
     */
    public String getTestpaperDescription() {
        return this.testpaperDescription;
    }

    /**
     * 设置试卷描述.
     *
     * @param testpaperDescription 试卷描述
     */
    public void setTestpaperDescription(String testpaperDescription) {
        this.testpaperDescription = testpaperDescription;
    }

    /**
     * 获取试卷说明.
     *
     * @return String
     */
    public String getTestpaperExplain() {
        return this.testpaperExplain;
    }

    /**
     * 设置试卷说明.
     *
     * @param testpaperExplain 试卷说明
     */
    public void setTestpaperExplain(String testpaperExplain) {
        this.testpaperExplain = testpaperExplain;
    }

    /**
     * 获取试卷出题方式,1:手工组卷,2:智能组卷.
     *
     * @return int
     */
    public int getTestpaperMode() {
        return this.testpaperMode;
    }

    /**
     * 设置试卷出题方式,1:手工组卷,2:智能组卷.
     *
     * @param testpaperMode 试卷出题方式,1:手工组卷,2:智能组卷
     */
    public void setTestpaperMode(int testpaperMode) {
        this.testpaperMode = testpaperMode;
    }

    /**
     * 获取考试分类ID(关联试卷类型表).
     *
     * @return long
     */
    public long getCategoryId() {
        return this.categoryId;
    }

    /**
     * 设置考试分类ID(关联试卷类型表).
     *
     * @param categoryId 考试分类ID(关联试卷类型表)
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取考试分类中文名.
     *
     * @return String
     */
    public String getCategoryName() {
        return this.categoryName;
    }

    /**
     * 设置考试分类中文名.
     *
     * @param categoryName 考试分类中文名
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取试卷类型,0:普通考试,1:模拟考试,2:个人练习.
     *
     * @return int
     */
    public int getTestpaperType() {
        return this.testpaperType;
    }

    /**
     * 设置试卷类型,0:普通考试,1:模拟考试,2:个人练习.
     *
     * @param testpaperType 试卷类型,0:普通考试,1:模拟考试,2:个人练习
     */
    public void setTestpaperType(int testpaperType) {
        this.testpaperType = testpaperType;
    }

    /**
     * 获取试卷总分值.
     *
     * @return int
     */
    public int getMarkTotal() {
        return this.markTotal;
    }

    /**
     * 设置试卷总分值.
     *
     * @param markTotal 试卷总分值
     */
    public void setMarkTotal(int markTotal) {
        this.markTotal = markTotal;
    }

    /**
     * 获取考试时间(单位分钟).
     *
     * @return int
     */
    public int getTestTime() {
        return this.testTime;
    }

    /**
     * 设置考试时间(单位分钟).
     *
     * @param testTime 考试时间(单位分钟)
     */
    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }

    /**
     * 获取题目顺序,1:乱序,2:顺序.
     *
     * @return int
     */
    public int getItemSequence() {
        return this.itemSequence;
    }

    /**
     * 设置题目顺序,1:乱序,2:顺序.
     *
     * @param itemSequence 题目顺序,1:乱序,2:顺序
     */
    public void setItemSequence(int itemSequence) {
        this.itemSequence = itemSequence;
    }

    /**
     * 获取选项顺序,1:乱序,2:顺序.
     *
     * @return int
     */
    public int getAnswerSequence() {
        return this.answerSequence;
    }

    /**
     * 设置选项顺序,1:乱序,2:顺序.
     *
     * @param answerSequence 选项顺序,1:乱序,2:顺序
     */
    public void setAnswerSequence(int answerSequence) {
        this.answerSequence = answerSequence;
    }

    /**
     * 获取创建者即出卷人（ID）.
     *
     * @return long
     */
    public long getCreator() {
        return this.creator;
    }

    /**
     * 设置创建者即出卷人（ID）.
     *
     * @param creator 创建者即出卷人（ID）
     */
    public void setCreator(long creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间.
     *
     * @return Date
     */
    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        } else {
            return (Date) createTime.clone();
        }
    }

    /**
     * 设置创建时间.
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }

    /**
     * 获取更新时间.
     *
     * @return Date
     */
    public Date getUpdateTime() {
        if (this.updateTime == null) {
            return null;
        } else {
            return (Date) updateTime.clone();
        }
    }

    /**
     * 设置更新时间.
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        if (updateTime == null) {
            this.updateTime = null;
        } else {
            this.updateTime = (Date) updateTime.clone();
        }
    }

    /**
     * 获取删除标识,0:未删除,1:已删除.
     *
     * @return int
     */
    public int getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * 设置删除标识,0:未删除,1:已删除.
     *
     * @param deleteFlag 删除标识,0:未删除,1:已删除
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 获取存放模块.
     *
     * @return TestPaperPart[]
     */
    public TestPaperPart[] getTestPaperParts() {
        if (testPaperParts == null) {
            return null;
        } else {
            return (TestPaperPart[]) testPaperParts.clone();
        }
    }

    /**
     * 设置存放模块.
     *
     * @param testPaperParts 存放模块
     */
    public void setTestPaperParts(TestPaperPart[] testPaperParts) {
        if (testPaperParts == null) {
            this.testPaperParts = null;
        } else {
            this.testPaperParts = (TestPaperPart[]) testPaperParts.clone();
        }
    }

    /**
     * 获取存放试卷模块.
     *
     * @return List
     */
    public List<TestPaperPart> getTestPaperPartList() {
        return this.testPaperPartList;
    }

    /**
     * 设置存放试卷模块.
     *
     * @param testPaperPartList 存放试卷模块
     */
    public void setTestPaperPartList(List<TestPaperPart> testPaperPartList) {
        this.testPaperPartList = testPaperPartList;
    }
}
