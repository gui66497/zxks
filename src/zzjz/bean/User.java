package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


/**
 * @author 郑雲峰
 * @ClassName: User
 * @Description: 用户实体类
 * @version 2016年5月30日 下午1:32:29
 */
@XmlRootElement
public class User implements Serializable {

    //ID
    private long id;

    //用户ID
    private long userId;

    //用户名
    private String userName;

    //用户密码
    private String passWord;

    //真实姓名
    private String realName;

    //性别 1男 2女
    private int gender;

    //单位
    private long company;

    //部门
    private long dept;

    //身份证号
    private String idCardNo;

    //应聘岗位
    private String position;

    //出生日期
    private String birth;

    //学历
    private String education;

    //毕业院校
    private String graduateSchool;

    //毕业时间
    private String graduateDate;

    //专业
    private String major;

    //邮箱
    private String email;

    //联系电话
    private String tel;

    //简历地址
    private String resumeUrl;

    //角色ID 0.普通用户 1.管理员
    private long roleId;

    //用户审核状态
    private int auditStatus;

    //创建时间
    private String createTime;

    //更新时间
    private String updateTime;

    //删除标识
    private int deleteFlag;


    /**
     * 获取ID.
     *
     * @return long
     */
    public long getId() {
        return this.id;
    }

    /**
     * 设置ID.
     *
     * @param id ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取用户ID.
     *
     * @return long
     */
    public long getUserId() {
        return this.userId;
    }

    /**
     * 设置用户ID.
     *
     * @param userId 用户ID
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 获取用户名.
     *
     * @return String
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置用户名.
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取用户密码.
     *
     * @return String
     */
    public String getPassWord() {
        return this.passWord;
    }

    /**
     * 设置用户密码.
     *
     * @param passWord 用户密码
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * 获取真实姓名.
     *
     * @return String
     */
    public String getRealName() {
        return this.realName;
    }

    /**
     * 设置真实姓名.
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取性别1男2女.
     *
     * @return int
     */
    public int getGender() {
        return this.gender;
    }

    /**
     * 设置性别1男2女.
     *
     * @param gender 性别1男2女
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * 获取单位.
     *
     * @return long
     */
    public long getCompany() {
        return this.company;
    }

    /**
     * 设置单位.
     *
     * @param company 单位
     */
    public void setCompany(long company) {
        this.company = company;
    }

    /**
     * 获取部门.
     *
     * @return long
     */
    public long getDept() {
        return this.dept;
    }

    /**
     * 设置部门.
     *
     * @param dept 部门
     */
    public void setDept(long dept) {
        this.dept = dept;
    }

    /**
     * 获取身份证号.
     *
     * @return String
     */
    public String getIdCardNo() {
        return this.idCardNo;
    }

    /**
     * 设置身份证号.
     *
     * @param idCardNo 身份证号
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    /**
     * 获取应聘岗位.
     *
     * @return String
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * 设置应聘岗位.
     *
     * @param position 应聘岗位
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * 获取出生日期.
     *
     * @return String
     */
    public String getBirth() {
        return this.birth;
    }

    /**
     * 设置出生日期.
     *
     * @param birth 出生日期
     */
    public void setBirth(String birth) {
        this.birth = birth;
    }

    /**
     * 获取学历.
     *
     * @return String
     */
    public String getEducation() {
        return this.education;
    }

    /**
     * 设置学历.
     *
     * @param education 学历
     */
    public void setEducation(String education) {
        this.education = education;
    }

    /**
     * 获取毕业院校.
     *
     * @return String
     */
    public String getGraduateSchool() {
        return this.graduateSchool;
    }

    /**
     * 设置毕业院校.
     *
     * @param graduateSchool 毕业院校
     */
    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    /**
     * 获取毕业时间.
     *
     * @return String
     */
    public String getGraduateDate() {
        return this.graduateDate;
    }

    /**
     * 设置毕业时间.
     *
     * @param graduateDate 毕业时间
     */
    public void setGraduateDate(String graduateDate) {
        this.graduateDate = graduateDate;
    }

    /**
     * 获取专业.
     *
     * @return String
     */
    public String getMajor() {
        return this.major;
    }

    /**
     * 设置专业.
     *
     * @param major 专业
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * 获取邮箱.
     *
     * @return String
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * 设置邮箱.
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取联系电话.
     *
     * @return String
     */
    public String getTel() {
        return this.tel;
    }

    /**
     * 设置联系电话.
     *
     * @param tel 联系电话
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取简历地址.
     *
     * @return String
     */
    public String getResumeUrl() {
        return this.resumeUrl;
    }

    /**
     * 设置简历地址.
     *
     * @param resumeUrl 简历地址
     */
    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    /**
     * 获取角色ID0.普通用户1.管理员.
     *
     * @return long
     */
    public long getRoleId() {
        return this.roleId;
    }

    /**
     * 设置角色ID0.普通用户1.管理员.
     *
     * @param roleId 角色ID0.普通用户1.管理员
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }


    /**
     * 获取创建时间.
     *
     * @return String
     */
    public String getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置创建时间.
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间.
     *
     * @return String
     */
    public String getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置更新时间.
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取删除标识.
     *
     * @return int
     */
    public int getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * 设置删除标识.
     *
     * @param deleteFlag 删除标识
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 获取用户审核状态.
     *
     * @return int
     */
    public int getAuditStatus() {
        return this.auditStatus;
    }

    /**
     * 设置用户审核状态.
     *
     * @param auditStatus 用户审核状态
     */
    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }
}
