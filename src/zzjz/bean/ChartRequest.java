package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: ChartRequest
 * @Description: 报表闯入数据实体类
 * @author zhengyunfeng
 * @version 2016/6/20 18:35
 */
@XmlRootElement
public class ChartRequest extends BaseRequest {

    //单位ID
    private String companyId;

    //试卷ID
    private String paperId;


    /**
     * 获取单位ID.
     *
     * @return String
     */
    public String getCompanyId() {
        return this.companyId;
    }

    /**
     * 设置单位ID.
     *
     * @param companyId 单位ID
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取试卷ID.
     *
     * @return String
     */
    public String getPaperId() {
        return this.paperId;
    }

    /**
     * 设置试卷ID.
     *
     * @param paperId 试卷ID
     */
    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }
}
