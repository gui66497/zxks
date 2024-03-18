package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @ClassName: ItemRequest
 * @Description: 题目信息实体类request
 * @author guzhenggen
 * @version 2016/6/3 10:44
 */
@XmlRootElement
public class ItemRequest extends BaseRequest {

    //题目ID列表
    private List<Long> itemIDList;

    //题目实体类
    private Item item;


    /**
     * 获取题目ID列表.
     *
     * @return List
     */
    public List<Long> getItemIDList() {
        return this.itemIDList;
    }

    /**
     * 设置题目ID列表.
     *
     * @param itemIDList 题目ID列表
     */
    public void setItemIDList(List<Long> itemIDList) {
        this.itemIDList = itemIDList;
    }

    /**
     * 获取题目实体类.
     *
     * @return Item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * 设置题目实体类.
     *
     * @param item 题目实体类
     */
    public void setItem(Item item) {
        this.item = item;
    }
}
