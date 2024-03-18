package zzjz.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 梅宏振
 * @version 2016年5月23日 下午6:16:18
 * @ClassName: ResponseEntity
 * @Description: REST接口通用响应类
 * @param <T>
 */


@XmlRootElement
public class BaseResponse<T> {

    //结果状态码
    private ResultCode resultCode;

    //数据集
    private List<T> data;

    //其它数据
    private List<Object> otherData;

    //提示消息
    private String message = "";

    //实际的行数
    private int iTotalRecords;

    //过滤之后，实际的行数
    private int iTotalDisplayRecords;


    /**
     * 获取结果状态码.
     *
     * @return ResultCode
     */
    public ResultCode getResultCode() {
        return this.resultCode;
    }

    /**
     * 设置结果状态码.
     *
     * @param resultCode 结果状态码
     */
    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * 获取数据集.
     *
     * @return List
     */
    public List<T> getData() {
        return this.data;
    }

    /**
     * 设置数据集.
     *
     * @param data 数据集
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 获取其它数据.
     *
     * @return List
     */
    public List<Object> getOtherData() {
        return this.otherData;
    }

    /**
     * 设置其它数据.
     *
     * @param otherData 其它数据
     */
    public void setOtherData(List<Object> otherData) {
        this.otherData = otherData;
    }

    /**
     * 获取提示消息.
     *
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 设置提示消息.
     *
     * @param message 提示消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取实际的行数.
     *
     * @return int
     */
    public int getiTotalRecords() {
        return this.iTotalRecords;
    }

    /**
     * 设置实际的行数.
     *
     * @param iTotalRecords 实际的行数
     */
    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    /**
     * 获取过滤之后，实际的行数.
     *
     * @return int
     */
    public int getiTotalDisplayRecords() {
        return this.iTotalDisplayRecords;
    }

    /**
     * 设置过滤之后，实际的行数.
     *
     * @param iTotalDisplayRecords 过滤之后，实际的行数
     */
    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }
}
