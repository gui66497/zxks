package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 梅宏振
 * @version 2015年3月4日 下午1:32:29
 * @ClassName: BaseRequest
 * @Description: BaseRequest
 */
@XmlRootElement
public class BaseRequest {

    //ID
    private Long id;

    //分页实体
    private PagingEntity paging;


    /**
     * 获取ID.
     *
     * @return Long
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 设置ID.
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取分页实体.
     *
     * @return PagingEntity
     */
    public PagingEntity getPaging() {
        return this.paging;
    }

    /**
     * 设置分页实体.
     *
     * @param paging 分页实体
     */
    public void setPaging(PagingEntity paging) {
        this.paging = paging;
    }
}
 