package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guzhenggen
 * @version 2016/5/30 13:48
 * @ClassName: ItemBankCategoryEntityRequest
 * @Description: 题库类型实体类request
 */
@XmlRootElement
public class ItemBankCategoryRequest extends BaseRequest {

    //题库类型实体
    private ItemBankCategory itemBankCategory;

    /**
     * 获取题库类型实体.
     *
     * @return ItemBankCategory
     */
    public ItemBankCategory getItemBankCategory() {
        return this.itemBankCategory;
    }

    /**
     * 设置题库类型实体.
     *
     * @param itemBankCategory 题库类型实体
     */
    public void setItemBankCategory(ItemBankCategory itemBankCategory) {
        this.itemBankCategory = itemBankCategory;
    }
}
