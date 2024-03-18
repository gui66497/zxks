package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guzhenggen
 * @version 2016/6/1 15:16
 * @ClassName: ItemBankRequest
 * @Description: 题库信息实体类request
 */
@XmlRootElement
public class ItemBankRequest extends BaseRequest {

    //题库信息
    private ItemBank itemBank;


    /**
     * 获取题库信息.
     *
     * @return ItemBank
     */
    public ItemBank getItemBank() {
        return this.itemBank;
    }

    /**
     * 设置题库信息.
     *
     * @param itemBank 题库信息
     */
    public void setItemBank(ItemBank itemBank) {
        this.itemBank = itemBank;
    }
}
