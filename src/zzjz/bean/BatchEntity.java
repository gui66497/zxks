package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2016年6月3日 上午9:16:18
 * @ClassName: BatchEntity
 * @Description: 存放批量操作相关参数
 */
@XmlRootElement
public class BatchEntity {

    //批量int的集合
    private List<Integer> ids;

    //批量String的集合
    private List<String> strList;


    /**
     * 获取批量int的集合.
     *
     * @return List
     */
    public List<Integer> getIds() {
        return this.ids;
    }

    /**
     * 设置批量int的集合.
     *
     * @param ids 批量int的集合
     */
    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    /**
     * 获取批量String的集合.
     *
     * @return List
     */
    public List<String> getStrList() {
        return this.strList;
    }

    /**
     * 设置批量String的集合.
     *
     * @param strList 批量String的集合
     */
    public void setStrList(List<String> strList) {
        this.strList = strList;
    }
}
