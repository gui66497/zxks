package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author caixiaolong
 * @version 2016/6/6 9:02
 * @ClassName: CompanyRequest
 * @Description: 单位实体类request
 */
@XmlRootElement
public class CompanyRequest extends BaseRequest {

    //单位实体
    private Company company;


    /**
     * 获取单位实体.
     *
     * @return Company
     */
    public Company getCompany() {
        return this.company;
    }

    /**
     * 设置单位实体.
     *
     * @param company 单位实体
     */
    public void setCompany(Company company) {
        this.company = company;
    }
}
