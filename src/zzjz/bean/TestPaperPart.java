package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/6/14 20:01
 * @ClassName: TestPaperPart
 * @Description: 试卷部分的信息
 */
@XmlRootElement
public class TestPaperPart {

    //试卷每部分的Id
    private long partId;

    //试卷每部分的名称
    private String partName;

    //试卷每部分的解释
    private String partExplain;

    //试卷每部分题目数
    private int partItemCounts;

    //试卷每部分的分数总和
    private int partMark;

    //模块序列 默认0 1 2
    private int partOrder;

    //试卷ID
    private long testPaperId;

    //试卷每部分的题目信息
    private List<Item> item;

    //试卷基本信息
    private TestPaper testPaper;

    //考试信息ID
    private long testInfoID;

    //创建者(ID)
    private long creator;

    //用户回答的信息
    private List<ResultInfo> resultInfoList;

    //题目id列表
    private Long[] itemIds;

    /**
     * 获取试卷每部分的Id.
     *
     * @return long
     */
    public long getPartId() {
        return this.partId;
    }

    /**
     * 设置试卷每部分的Id.
     *
     * @param partId 试卷每部分的Id
     */
    public void setPartId(long partId) {
        this.partId = partId;
    }

    /**
     * 获取试卷每部分的名称.
     *
     * @return String
     */
    public String getPartName() {
        return this.partName;
    }

    /**
     * 设置试卷每部分的名称.
     *
     * @param partName 试卷每部分的名称
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * 获取试卷每部分的解释.
     *
     * @return String
     */
    public String getPartExplain() {
        return this.partExplain;
    }

    /**
     * 设置试卷每部分的解释.
     *
     * @param partExplain 试卷每部分的解释
     */
    public void setPartExplain(String partExplain) {
        this.partExplain = partExplain;
    }

    /**
     * 获取试卷每部分题目数.
     *
     * @return int
     */
    public int getPartItemCounts() {
        return this.partItemCounts;
    }

    /**
     * 设置试卷每部分题目数.
     *
     * @param partItemCounts 试卷每部分题目数
     */
    public void setPartItemCounts(int partItemCounts) {
        this.partItemCounts = partItemCounts;
    }

    /**
     * 获取试卷每部分的分数总和.
     *
     * @return int
     */
    public int getPartMark() {
        return this.partMark;
    }

    /**
     * 设置试卷每部分的分数总和.
     *
     * @param partMark 试卷每部分的分数总和
     */
    public void setPartMark(int partMark) {
        this.partMark = partMark;
    }

    /**
     * 获取试卷ID.
     *
     * @return long
     */
    public long getTestPaperId() {
        return this.testPaperId;
    }

    /**
     * 设置试卷ID.
     *
     * @param testPaperId 试卷ID
     */
    public void setTestPaperId(long testPaperId) {
        this.testPaperId = testPaperId;
    }

    /**
     * 获取试卷每部分的题目信息.
     *
     * @return List
     */
    public List<Item> getItem() {
        return this.item;
    }

    /**
     * 设置试卷每部分的题目信息.
     *
     * @param item 试卷每部分的题目信息
     */
    public void setItem(List<Item> item) {
        this.item = item;
    }

    /**
     * 获取试卷基本信息.
     *
     * @return TestPaper
     */
    public TestPaper getTestPaper() {
        return this.testPaper;
    }

    /**
     * 设置试卷基本信息.
     *
     * @param testPaper 试卷基本信息
     */
    public void setTestPaper(TestPaper testPaper) {
        this.testPaper = testPaper;
    }

    /**
     * 获取考试信息ID.
     *
     * @return long
     */
    public long getTestInfoID() {
        return this.testInfoID;
    }

    /**
     * 设置考试信息ID.
     *
     * @param testInfoID 考试信息ID
     */
    public void setTestInfoID(long testInfoID) {
        this.testInfoID = testInfoID;
    }

    /**
     * 获取创建者(ID).
     *
     * @return long
     */
    public long getCreator() {
        return this.creator;
    }

    /**
     * 设置创建者(ID).
     *
     * @param creator 创建者(ID)
     */
    public void setCreator(long creator) {
        this.creator = creator;
    }

    /**
     * 获取用户回答的信息.
     *
     * @return List
     */
    public List<ResultInfo> getResultInfoList() {
        return this.resultInfoList;
    }

    /**
     * 设置用户回答的信息.
     *
     * @param resultInfoList 用户回答的信息
     */
    public void setResultInfoList(List<ResultInfo> resultInfoList) {
        this.resultInfoList = resultInfoList;
    }

    /**
     *
     * @return partOrder
     */
    public int getPartOrder() {
        return partOrder;
    }

    /**
     *
     * @param partOrder partOrder
     */
    public void setPartOrder(int partOrder) {
        this.partOrder = partOrder;
    }

    /**
     * 获取题目id列表.
     *
     * @return Long[]
     */
    public Long[] getItemIds() {
        if (itemIds == null) {
            return null;
        } else {
            return (Long[]) itemIds.clone();
        }
    }

    /**
     * 设置题目id列表.
     *
     * @param itemIds 题目id列表
     */
    public void setItemIds(Long[] itemIds) {
        if (itemIds == null) {
            this.itemIds = null;
        } else {
            this.itemIds = (Long[]) itemIds.clone();
        }
    }


}
