package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author caixiaolong
 * @version 2016/6/6 10:45
 * @ClassName: DeptRequest
 * @Description: BaseRequest
 */
@XmlRootElement
public class DeptRequest extends BaseRequest {

    //部门实体
    private Dept dept;


    /**
     * 获取部门实体.
     *
     * @return Dept
     */
    public Dept getDept() {
        return this.dept;
    }

    /**
     * 设置部门实体.
     *
     * @param dept 部门实体
     */
    public void setDept(Dept dept) {
        this.dept = dept;
    }
}
