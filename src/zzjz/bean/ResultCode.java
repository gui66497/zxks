package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 梅宏振
 * @version 2016年5月23日-上午9:54:10
 * @ClassName: ResultCode
 * @Description: REST结果实体类
 */
@XmlRootElement
public enum ResultCode {

    /**
     * 操作成功.
     */
    SUCCESS(1000),

    /**
     * 错误的请求.
     */
    BAD_REQUEST(1001),

    /**
     * 已存在.
     */
    RECORD_EXIST(1002),

    /**
     * 操作失败.
     */
    ERROR(1003),

    /**
     * 过期.
     */
    ROUTOFDATE(1004),

    /**
     * 不存在.
     */
    RECORD_NOT_EXIST(1005),

    /**
     * 存在关联.
     */
    RELATED(1006),

    /**
     * 未授权.
     */
    NOT_AUTHORIZED(401);

    //提示码
    private final int code;

    /**
     * 获取提示码.
     *
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * sdfsd
     *
     * @param code1
     */
    ResultCode(int code1) {
        this.code = code1;
    }

}
