package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 梅宏振
 * @version 2016年5月23日 下午1:46:13
 * @ClassName: PagingEntity
 * @Description: 分页实体类
 */
@XmlRootElement
public class PagingEntity {

    private static final int NUM_TEN = 10;

    //每页记录条数(默认每页十条)
    private int pageSize = NUM_TEN;

    //页码(从1开始)(默认显示第一页数据)
    private int pageNo = 1;

    //记录起始索引
    private int startIndex;

    //查询条件
    private String searchValue = "";

    //排列字段
    private String sortColumn;

    //排列方式
    private String sortDir;

    //来自客户端的复制品,这个参数需要以后原封不动地返回给页面
    private String sEcho = "";


    /**
     * 获取每页记录条数(默认每页十条).
     *
     * @return int
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * 设置每页记录条数(默认每页十条).
     *
     * @param pageSize 每页记录条数(默认每页十条)
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取页码(从1开始)(默认显示第一页数据).
     *
     * @return int
     */
    public int getPageNo() {
        return this.pageNo;
    }

    /**
     * 设置页码(从1开始)(默认显示第一页数据).
     *
     * @param pageNo 页码(从1开始)(默认显示第一页数据)
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * 获取记录起始索引.
     *
     * @return int
     */
    public int getStartIndex() {
        return this.startIndex;
    }

    /**
     * 设置记录起始索引.
     *
     * @param startIndex 记录起始索引
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * 获取查询条件.
     *
     * @return String
     */
    public String getSearchValue() {
        return this.searchValue;
    }

    /**
     * 设置查询条件.
     *
     * @param searchValue 查询条件
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    /**
     * 获取排列字段.
     *
     * @return String
     */
    public String getSortColumn() {
        return this.sortColumn;
    }

    /**
     * 设置排列字段.
     *
     * @param sortColumn 排列字段
     */
    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    /**
     * 获取排列方式.
     *
     * @return String
     */
    public String getSortDir() {
        return this.sortDir;
    }

    /**
     * 设置排列方式.
     *
     * @param sortDir 排列方式
     */
    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    /**
     * 获取来自客户端的复制品,这个参数需要以后原封不动地返回给页面.
     *
     * @return String
     */
    public String getSEcho() {
        return this.sEcho;
    }

    /**
     * 设置来自客户端的复制品,这个参数需要以后原封不动地返回给页面.
     *
     * @param sEcho1 来自客户端的复制品,这个参数需要以后原封不动地返回给页面
     */
    public void setSEcho(String sEcho1) {
        this.sEcho = sEcho1;
    }
}
