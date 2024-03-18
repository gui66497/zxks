package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guitang.fang
 * @ClassName: Answer
 * @Description: 用户回答实体
 * @version  2016/9/28 17:04
 */
@XmlRootElement
public class Answer {

    /**
     * 题目id
     */
    private long itemId;

    /**
     * 用户回答.
     */
    private String result;

    /**
     * 正确答案.
     */
    private String answer;

    /**
     * 题目类型 1单选 2多选 3判断 4简答
     */
    private int type;

    /**
     * 分值.
     */
    private int value;

    /**
     * 本题得分
     */
    private double score;

    /**
     *
     * @return itemId
     */
    public long getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId itemId
     */
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    /**
     *
     * @return score
     */
    public double getScore() {
        return score;
    }

    /**
     *
     * @param score score
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     *
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     *
     * @param result result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     * @return answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     *
     * @param answer answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     *
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @param value value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     *
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @param type type
     */
    public void setType(int type) {
        this.type = type;
    }
}
