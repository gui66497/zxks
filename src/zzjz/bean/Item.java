package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: Item
 * @Descriptiona: 题目信息实体类
 * @author guzhenggen
 * @version 2016/6/3 10:34
 */
@XmlRootElement
public class Item {

    //序号
    private int no;

    //题目ID
    private long itemId;

    //题干
    private String itemTitle;

    //题目代码
    private String itemCode;

    //题目答案,选项ID
    private String answer;

    //题目答案解析
    private String answerResolution;

    //题目类型,1:单项选择,2:多项选择,3：判断
    private int itemType;

    //题库ID
    private long itemBankId;

    //题库中文名
    private String itemBankName;

    //题库IDS
    private String itemBankIds;

    //创建者(ID)
    private long creator;

    //创建时间
    private Date createTime;

    //更新时间
    private String updateTime;

    //删除标识,0:未删除,1:已删除
    private int deleteFlag;

    //选项List
    private List<ItemOption> optionList;

    //选项数组
    private ItemOption[] options;

    //题型
    private String itemTypeStr;

    public Item() {
    }

    /**
     *
     * @param no1 序号
     * @param itemTitle1 题目标题
     * @param itemTypeStr1 题目类型
     * @param updateTime1 更新时间
     */
    public Item(int no1, String itemTitle1, String itemTypeStr1, String updateTime1) {
        this.no = no1;
        this.itemTitle = itemTitle1;
        this.itemTypeStr = itemTypeStr1;
        this.updateTime = updateTime1;
    }


    /**
     * 获取序号.
     *
     * @return int
     */
    public int getNo() {
        return this.no;
    }

    /**
     * 设置序号.
     *
     * @param no 序号
     */
    public void setNo(int no) {
        this.no = no;
    }

    /**
     * 获取题目ID.
     *
     * @return long
     */
    public long getItemId() {
        return this.itemId;
    }

    /**
     * 设置题目ID.
     *
     * @param itemId 题目ID
     */
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    /**
     * 获取题干.
     *
     * @return String
     */
    public String getItemTitle() {
        return this.itemTitle;
    }

    /**
     * 设置题干.
     *
     * @param itemTitle 题干
     */
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    /**
     * 获取题目代码.
     *
     * @return String
     */
    public String getItemCode() {
        return this.itemCode;
    }

    /**
     * 设置题目代码.
     *
     * @param itemCode 题目代码
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * 获取题目答案,选项ID.
     *
     * @return String
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * 设置题目答案,选项ID.
     *
     * @param answer 题目答案,选项ID
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * 获取题目答案解析.
     *
     * @return String
     */
    public String getAnswerResolution() {
        return this.answerResolution;
    }

    /**
     * 设置题目答案解析.
     *
     * @param answerResolution 题目答案解析
     */
    public void setAnswerResolution(String answerResolution) {
        this.answerResolution = answerResolution;
    }

    /**
     * 获取题目类型,1:单项选择,2:多项选择,3：判断.
     *
     * @return int
     */
    public int getItemType() {
        return this.itemType;
    }

    /**
     * 设置题目类型,1:单项选择,2:多项选择,3：判断.
     *
     * @param itemType 题目类型,1:单项选择,2:多项选择,3：判断
     */
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取题库ID.
     *
     * @return long
     */
    public long getItemBankId() {
        return this.itemBankId;
    }

    /**
     * 设置题库ID.
     *
     * @param itemBankId 题库ID
     */
    public void setItemBankId(long itemBankId) {
        this.itemBankId = itemBankId;
    }

    /**
     * 获取题库中文名.
     *
     * @return String
     */
    public String getItemBankName() {
        return this.itemBankName;
    }

    /**
     * 设置题库中文名.
     *
     * @param itemBankName 题库中文名
     */
    public void setItemBankName(String itemBankName) {
        this.itemBankName = itemBankName;
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
     * @return String
     */
    public String getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置更新时间.
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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
     * 获取选项List.
     *
     * @return List
     */
    public List<ItemOption> getOptionList() {
        return this.optionList;
    }

    /**
     * 设置选项List.
     *
     * @param optionList 选项List
     */
    public void setOptionList(List<ItemOption> optionList) {
        this.optionList = optionList;
    }

    /**
     * 获取选项数组.
     *
     * @return ItemOption[]
     */
    public ItemOption[] getOptions() {
        if (options == null) {
            return null;
        } else {
            return (ItemOption[]) options.clone();
        }
    }

    /**
     * 设置选项数组.
     *
     * @param options 选项数组
     */
    public void setOptions(ItemOption[] options) {
        if (options == null) {
            this.options = null;
        } else {
            this.options = (ItemOption[]) options.clone();
        }
    }

    /**
     * 获取题型.
     *
     * @return String
     */
    public String getItemTypeStr() {
        return this.itemTypeStr;
    }

    /**
     * 设置题型.
     *
     * @param itemTypeStr 题型
     */
    public void setItemTypeStr(String itemTypeStr) {
        this.itemTypeStr = itemTypeStr;
    }


    /**
     * 获取题库IDS.
     * @return String
     */
    public String getItemBankIds() {
        return this.itemBankIds;
    }

    /**
     * 设置题库IDS.
     * @param itemBankIds 题库IDS
     */
    public void setItemBankIds(String itemBankIds) {
        this.itemBankIds = itemBankIds;
    }
}
