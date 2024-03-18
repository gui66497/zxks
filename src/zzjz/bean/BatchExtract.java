package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author guitang.fang
 * @version 2016/6/14 10:13
 * @ClassName: BatchExtract
 * @Description: 批量传递抽题参数实体
 */
@XmlRootElement
public class BatchExtract {

    //批量抽取题目List对象
    private List<ExtractEntity> objList;

    //已存在的考题id
    private Long[] existItemIds;

    /**
     * 获取批量抽取题目List对象.
     *
     * @return List
     */
    public List<ExtractEntity> getObjList() {
        return this.objList;
    }

    /**
     * 设置批量抽取题目List对象.
     *
     * @param objList 批量抽取题目List对象
     */
    public void setObjList(List<ExtractEntity> objList) {
        this.objList = objList;
    }

    /**
     * 获取已存在的考题id.
     *
     * @return Long[]
     */
    public Long[] getExistItemIds() {
        if (existItemIds == null) {
            return null;
        } else {
            return (Long[]) existItemIds.clone();
        }
    }

    /**
     * 设置已存在的考题id.
     *
     * @param existItemIds 已存在的考题id
     */
    public void setExistItemIds(Long[] existItemIds) {
        if (existItemIds == null) {
            this.existItemIds = null;
        } else {
            this.existItemIds = (Long[]) existItemIds.clone();
        }
    }
}
