package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guitang.fang
 * @version 2016/6/14 10:13
 * @ClassName: ExtractEntity
 * @Description: 传递抽题参数实体
 */
@XmlRootElement
public class ExtractEntity {

    //题库ID
    private Long itemBankId;

    //题目类型
    private Integer itemType;

    //数量
    private Integer quantity;


    /**
     * 获取题库ID.
     *
     * @return Long
     */
    public Long getItemBankId() {
        return this.itemBankId;
    }

    /**
     * 设置题库ID.
     *
     * @param itemBankId 题库ID
     */
    public void setItemBankId(Long itemBankId) {
        this.itemBankId = itemBankId;
    }

    /**
     * 获取题目类型.
     *
     * @return Integer
     */
    public Integer getItemType() {
        return this.itemType;
    }

    /**
     * 设置题目类型.
     *
     * @param itemType 题目类型
     */
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取数量.
     *
     * @return Integer
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * 设置数量.
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
