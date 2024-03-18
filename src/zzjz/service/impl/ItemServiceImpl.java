package zzjz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.Item;
import zzjz.bean.ItemOption;
import zzjz.bean.PagingEntity;
import zzjz.bean.ResultInfo;
import zzjz.service.ItemService;
import zzjz.util.MyJdbcTemplate;
import zzjz.util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/6/3 16:03
 * @ClassName: ItemServiceImpl
 * @DESCription: 题目操作service接口实现类
 */
@Service
public class ItemServiceImpl implements ItemService {

    private static Logger logger = Logger.getLogger(ItemServiceImpl.class);

    /**
     * 注入MyJdbcTemplate.
     */
    @Autowired
    public MyJdbcTemplate myJdbcTemplate;

    private static String strMess1 = "','";

    private static String strMess2 = ",";

    private static String strMess3 = "'";

    private static String strMess4 = "ITEM_TYPE";

    private static String strMess5 = "ITEM_ID";

    private static String strMess6 = "ITEM_TITLE";

    private static String strMess7 = "UPDATE_TIME";

    private static final int NUM_THREE = 3;

    private static final int NUM_FOUR = 4;




    /**
     * 添加题目.
     *
     * @param item 题目信息
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addItem(Item item) throws DataAccessException {
        String sql = "INSERT INTO tb_item(ITEM_ID,ITEM_TITLE,ITEM_CODE," +
                "ANSWER,ANSWER_RESOLUTION,ITEM_TYPE,ITEMBANK_ID," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                " values(" + item.getItemId() + ",'" + item.getItemTitle() + strMess1 +
                item.getItemCode() + strMess1 + item.getAnswer() + strMess1
                + item.getAnswerResolution() + "'," + item.getItemType() + strMess2 +
                item.getItemBankId() + strMess2 + item.getCreator() + ",NOW(),NOW(),0)";
        try {
            myJdbcTemplate.execute(sql);
            ItemOption[] options = item.getOptions();
            return addItemOptions(options); //添加选项
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除题目.
     *
     * @param itemId 题目ID
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteItem(long itemId) throws DataAccessException {
        String sql = "DELETE FROM tb_item WHERE ITEM_ID=" + itemId;
        try {
            int row = myJdbcTemplate.update(sql);
            if (row == 1) {
                deleteItemOption(itemId);
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 编辑题目.
     *
     * @param item 题目信息
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateItem(Item item) throws DataAccessException {
        String sql = "UPDATE tb_item SET ITEM_TITLE='" + item.getItemTitle() +
                "',ITEM_CODE='" + item.getItemCode() + "',ANSWER='" +
                item.getAnswer() + "',ANSWER_RESOLUTION=" +
                strMess3 + item.getAnswerResolution() + "',ITEM_TYPE=" +
                "" + item.getItemType() + strMess2 +
                "ITEMBANK_ID=" + item.getItemBankId() + ",CREATOR=" + item.getCreator() + strMess2 +
                "UPDATE_TIME=NOW() WHERE ITEM_ID =" + item.getItemId();
        try {
            int row = myJdbcTemplate.update(sql);
            if (row == 1) {
                ItemOption[] options = item.getOptions();
                deleteItemOption(item.getItemId());
                addItemOptions(options); //添加选项
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取题目信息.
     *
     * @param itemId 题目信息ID
     * @return Item
     */
    public Item getItem(Long itemId) {
        String sql = "SELECT ITEM_TITLE,ITEM_ID,ANSWER,ITEM_CODE,ANSWER_RESOLUTION," +
                "ITEMBANK_ID,ITEM_TYPE,CREATOR," +
                "UPDATE_TIME,CREATE_TIME,DELETE_FLAG FROM tb_item" +
                " WHERE ITEM_ID=" + itemId + " AND" +
                " DELETE_FLAG=0";
        List<Item> itemList = myJdbcTemplate.query(sql, new ItemMapper());
        List<ItemOption> optionList = getItemOptions(itemId);
        Item item = null;
        if (itemList != null && itemList.size() > 0) {
            item = itemList.get(0);
            if (item != null) {
                item.setOptionList(optionList);
            }
        }
        return item;
    }

    /**
     * 获取题目信息列表.
     *
     * @param itemBankId   题库ID
     * @param pagingEntity 分页组件
     * @return List
     */
    public List<Item> getItemList(long itemBankId, PagingEntity pagingEntity) {
        int startIndex = pagingEntity.getStartIndex();      //开始位置
        int pageSize = pagingEntity.getPageSize();          //每页大小
        String sortColumn = pagingEntity.getSortColumn();   //排序字段
        String sortDir = pagingEntity.getSortDir();         //排序方式
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        searchValue = StringUtil.zhuanyi(searchValue);
        String sortSql = "";                                //排序sql片段
        String searchSql = "";                              //检索sql片段
        if (StringUtils.isNotBlank(searchValue)) {
            searchSql = " AND ITEM_TITLE" +
                    " LIKE '%" + searchValue + "%'";
        }
        //判空
        if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
            sortSql = " ORDER BY " + sortColumn + " " + sortDir;
        }
        String sql = "SELECT ITEM_TITLE,ITEM_ID,ITEM_CODE,ANSWER,ANSWER_RESOLUTION," +
                "ITEMBANK_ID,ITEM_TYPE,CREATOR," +
                "UPDATE_TIME,CREATE_TIME,DELETE_FLAG FROM tb_item  WHERE " +
                "ITEMBANK_ID=" + itemBankId + " AND DELETE_FLAG=0 " +
                searchSql + sortSql + " LIMIT " + startIndex + strMess2 + pageSize;
        logger.debug("getItemList的sql语句：" + sql);
        return myJdbcTemplate.query(sql, new ItemMapper());
    }

    /**
     * 获取题目列表.
     *
     * @param itemBankId 题库ID
     * @param searchName 查询条件
     * @return List
     */
    public List<Item> getItemList(long itemBankId, String searchName) {
        String sql = "SELECT ITEM_TITLE,ITEM_ID,ITEM_CODE,ANSWER,ANSWER_RESOLUTION," +
                "ITEM_TYPE,CREATOR,ITEMBANK_ID," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG FROM tb_item" +
                " WHERE ITEMBANK_ID=" + itemBankId + " AND ITEM_TITLE LIKE '%" +
                searchName + "%' AND DELETE_FLAG=0 ORDER BY ID DESC";
        return myJdbcTemplate.query(sql, new ItemMapper());
    }

    /**
     * 添加题目选项.
     *
     * @param options 题目选项
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addItemOptions(final ItemOption[] options) throws DataAccessException {

        if (!(options != null && options.length > 0)) {
            return false;
        }

        String sql = "INSERT INTO tb_itemoption(OPTION_ID,CONTENT,ITEM_ID," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) VALUES (?,?,?,?,NOW(),NOW(),0)";
        try {
            int[] insertCounts =
                    myJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, options[i].getOptionId());
                            ps.setString(2, options[i].getContent());
                            ps.setLong(NUM_THREE, options[i].getItemId());
                            ps.setLong(NUM_FOUR, options[i].getCreator());
                        }

                        public int getBatchSize() {
                            return options.length;
                        }

                    });
            if (insertCounts != null && insertCounts.length > 0) {
                return true;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获取题目选项列表.
     *
     * @param itemId 题目ID
     * @return List
     */
    public List<ItemOption> getItemOptions(long itemId) {
        String sql = "SELECT OPTION_ID,CONTENT,ITEM_ID,CREATOR,CREATE_TIME," +
                "UPDATE_TIME,DELETE_FLAG FROM tb_itemoption WHERE" +
                " ITEM_ID=" + itemId + " AND DELETE_FLAG=0 ORDER BY ID ASC";
        return myJdbcTemplate.query(sql, new ItemOptionMapper());
    }

    /**
     * 删除题目选项.
     *
     * @param itemId 题目ID
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteItemOption(long itemId) throws DataAccessException {
        String sql = "DELETE FROM tb_itemoption WHERE ITEM_ID=" + itemId;
        try {
            int row = myJdbcTemplate.update(sql);
            if (row == 1) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取题目数.
     *
     * @param itemBankId   题库ID
     * @param pagingEntity 分页组件
     * @return int
     */
    public int getItemCount(long itemBankId, PagingEntity pagingEntity) {
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        searchValue = StringUtil.zhuanyi(searchValue);
        String searchSql = "";                              //检索sql片段
        if (StringUtils.isNotBlank(searchValue)) {
            searchSql = " AND ITEM_TITLE LIKE '%" + searchValue + "%'";
        }
        String sql = "SELECT COUNT(1) FROM tb_item WHERE" +
                " ITEMBANK_ID=" + itemBankId + " AND DELETE_FLAG=0 " + searchSql;
        List<Integer> counts = myJdbcTemplate.queryForList(sql, Integer.class);
        if (counts != null && counts.size() > 0) {
            return counts.get(0);
        }
        return 0;
    }

    /**
     * 获取题目总数.
     *
     * @param itemBankId 题库ID
     * @return int
     */
    public int getItemCount(long itemBankId) {
        String sql = "SELECT COUNT(1) FROM tb_item WHERE" +
                " ITEMBANK_ID=" + itemBankId + " AND DELETE_FLAG=0";
        List<Integer> counts = myJdbcTemplate.queryForList(sql, Integer.class);
        if (counts != null && counts.size() > 0) {
            return counts.get(0);
        }
        return 0;
    }

    /**
     * 根据题目ID列表删除题目.
     *
     * @param itemIDList 题目ID列表
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteItemByItemIDList(final List<Long> itemIDList) throws DataAccessException {
        if (!(itemIDList != null && itemIDList.size() > 0)) {
            return false;
        }

        String sql = "UPDATE tb_item SET DELETE_FLAG = 1 WHERE ITEM_ID = ? ";
        try {
            int[] updateCounts =
                    myJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, itemIDList.get(i));
                        }

                        public int getBatchSize() {
                            return itemIDList.size();
                        }
                    });
            if (updateCounts != null && updateCounts.length == itemIDList.size()) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据模块ID获取题目.
     *
     * @param partId 试卷部分Id
     * @return List
     */
    public List<Item> getItemByPartId(long partId) {
        String sql = "SELECT t1.ITEM_ID,t1.ITEM_CODE,t1.ITEM_TITLE," +
                "t1.ANSWER," +
                "t1.ANSWER_RESOLUTION,t1.ITEM_TYPE," +
                "t1.ITEMBANK_ID,t1.CREATOR," +
                "t1.CREATE_TIME,t1.UPDATE_TIME,t1.DELETE_FLAG,ib.ITEMBANK_NAME" +
                "  FROM tb_item t1  LEFT JOIN tb_testpaperparttoitem t2 " +
                "ON t1.ITEM_ID = t2.ITEM_ID JOIN tb_itembank ib on" +
                " t1.ITEMBANK_ID = ib.ITEMBANK_ID  WHERE t2.TESTPAPERPART_ID = " + partId;
        List<Item> list = myJdbcTemplate.query(sql, new ItemMapper());

        return list;
    }

    /**
     * 根据题目类型获取题目和题库信息.
     *
     * @param itemBankId   题库ID
     * @param itemBankType 题目类型
     * @return List
     */
    public List<Item> getItemByItemTypeAndItemBank(Long itemBankId, Integer itemBankType) {
        String sql = "SELECT t1.ITEM_ID,t1.ITEM_TITLE," +
                "t1.ITEM_CODE,t1.ANSWER," +
                "t1.ANSWER_RESOLUTION,t1.ITEM_TYPE,t1.ITEMBANK_ID,t1.CREATOR," +
                " t1.CREATE_TIME,t1.UPDATE_TIME,t1.DELETE_FLAG,t2.ITEMBANK_NAME FROM tb_item t1 " +
                " LEFT JOIN tb_itembank t2 on t2.ITEMBANK_ID = t1.ITEMBANK_ID" +
                " WHERE t1.ITEMBANK_ID=? AND t1.ITEM_TYPE = ? AND t1.DELETE_FLAG=0";

        return myJdbcTemplate.query(sql, new ItemMapper(), itemBankId, itemBankType);
    }


    /**
     * 根据题目类型获取题目和题库信息.
     *
     * @param itemBankIds  题库IDs
     * @param itemBankType 题目类型
     * @param pagingEntity 分页组件
     * @return List
     */
    public List<Item> getItemByItemTypeAndItemBankIds(String itemBankIds, Integer itemBankType, PagingEntity pagingEntity) {
        int startIndex = pagingEntity.getStartIndex();      //开始位置
        int pageSize = pagingEntity.getPageSize();          //每页大小
        String sortColumn = pagingEntity.getSortColumn();   //排序字段
        String sortDir = pagingEntity.getSortDir();         //排序方式
        String searchValue = pagingEntity.getSearchValue(); //检索内容
        searchValue = StringUtil.zhuanyi(searchValue);

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT t1.ITEM_ID,t1.ITEM_TITLE,t1.ITEM_CODE,t1.ANSWER," +
                "t1.ANSWER_RESOLUTION,t1.ITEM_TYPE,t1.ITEMBANK_ID,t1.CREATOR," +
                " t1.CREATE_TIME,t1.UPDATE_TIME,t1.DELETE_FLAG,t2.ITEMBANK_NAME FROM tb_item t1 " +
                " LEFT JOIN tb_itembank t2 on t2.ITEMBANK_ID = t1.ITEMBANK_ID" +
                " WHERE t1.ITEMBANK_ID in ( " + itemBankIds + " ) AND" +
                " t1.ITEM_TYPE = " + itemBankType + " AND t1.DELETE_FLAG=0");
        //试卷名称检索
        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND " +
                    "t1.ITEM_TITLE like'%" + searchValue + "%'");
        }
        if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
            sql.append(" ORDER BY " + sortColumn + " " + sortDir);
        }
        sql.append(" LIMIT " + startIndex + "," + pageSize);
        return myJdbcTemplate.query(sql.toString(), new ItemMapper());
    }

    /**
     * 根据题目类型获取题目和题库信息.
     *
     * @param itemBankIds  题库IDs
     * @param itemBankType 题目类型
     * @param pagingEntity 分页组件
     * @return int
     */
    public int countByItemTypeAndItemBankIds(String itemBankIds, Integer itemBankType, PagingEntity pagingEntity) {

        String searchValue = pagingEntity.getSearchValue(); //检索内容
        searchValue = StringUtil.zhuanyi(searchValue);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM tb_item t1 " +
                " LEFT JOIN tb_itembank t2 on t2.ITEMBANK_ID = t1.ITEMBANK_ID" +
                " WHERE t1.ITEMBANK_ID in ( " + itemBankIds + " ) AND" +
                " t1.ITEM_TYPE = " + itemBankType + " AND t1.DELETE_FLAG=0");
        //试卷名称检索
        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" AND " +
                    "t1.ITEM_TITLE like'%" + searchValue + "%'");
        }

        List<Integer> counts = myJdbcTemplate.queryForList(sql.toString(), Integer.class);
        if (counts != null) {
            return counts.get(0);
        }
        return 0;
    }


    /**
     * 根据题目类型获取题目和题库信息.
     *
     * @param itemBankId   题库ID
     * @param itemBankType 题目类型
     * @param quantity     所需数量
     * @return List
     */
    public List<Item> getItemByItemTypeAndItemBank(Long itemBankId,
                                                   Integer itemBankType, Integer quantity) {
        String sql = "SELECT ITEM_ID,ITEM_TITLE,ITEM_CODE,ANSWER,ANSWER_RESOLUTION," +
                "ITEM_TYPE,ITEMBANK_ID,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG FROM tb_item WHERE" +
                " ITEMBANK_ID=? AND ITEM_TYPE = ? AND DELETE_FLAG=0 ORDER BY rand() limit ?";
        return myJdbcTemplate.query(sql, new ItemMapper(), itemBankId, itemBankType, quantity);
    }

    /**
     * 根据题目类型获取题目总数和题库信息.
     *
     * @param itemBankId   题库ID
     * @param itemBankType 题目类型
     * @return Integer
     */
    public Integer countItemByItemTypeAndItemBank(Long itemBankId, Integer itemBankType) {
        String sql = "SELECT COUNT(*) FROM tb_item WHERE" +
                " ITEMBANK_ID = " + itemBankId + " AND" +
                " ITEM_TYPE = " + itemBankType + " AND DELETE_FLAG=0";
        return myJdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 根据模块ID获取题目信息.
     *
     * @param partId        试卷部分Id
     * @param achievementId 成绩Id
     * @return List
     */
    public List<ResultInfo> getItemByPartId(long partId, long achievementId) {
        String sql = "SELECT t1.ITEM_ID,t1.ITEM_TITLE,t1.ITEM_CODE,t1.ANSWER," +
                "t1.ANSWER_RESOLUTION,t1.ITEM_TYPE,t1.ITEMBANK_ID," +
                "t3.RESULT,t3.SCORE FROM tb_item t1 LEFT JOIN tb_testpaperparttoitem t2 ON" +
                " t1.ITEM_ID = t2.ITEM_ID LEFT JOIN tb_result t3 " +
                "ON t1.ITEM_ID = t3.ITEM_ID WHERE t2.TESTPAPERPART_ID = " + partId + "" +
                " AND t3.ACHIEVEMENT_ID = " + achievementId +
                " GROUP BY t1.ITEM_ID";
        return myJdbcTemplate.query(sql, new ResultInfoMapper());
    }

    /**
     * 根据题库ID获取题目列表.
     *
     * @param itemBankId 题库Id
     * @return List
     */
    public List<Item> getItemListByItemBankId(long itemBankId) {
        String sql = "SELECT ITEM_ID,ITEM_TITLE,ITEM_CODE,ANSWER,ANSWER_RESOLUTION," +
                "ITEM_TYPE,ITEMBANK_ID,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG FROM tb_item" +
                " WHERE ITEMBANK_ID=" + itemBankId + " " +
                "AND DELETE_FLAG=0 ORDER BY ID DESC";
        return myJdbcTemplate.query(sql, new ItemMapper());
    }

    @SuppressWarnings("rawtypes")
    private static final class ItemMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            int itemType = rs.getInt(strMess4);
            String itemTypeStr = "";
            if (itemType == 1) {
                itemTypeStr = "单选题";
            } else if (itemType == 2) {
                itemTypeStr = "多选题";
            } else if (itemType == 3) {
                itemTypeStr = "判断题";
            } else if (itemType == 4) {
                itemTypeStr = "简答题";
            } else if (itemType == 5) {
                itemTypeStr = "填空题";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Item item = new Item();
            item.setItemId(rs.getLong(strMess5));
            item.setItemTitle(rs.getString(strMess6));
            item.setItemCode(rs.getString("ITEM_CODE"));
            item.setAnswer(rs.getString("ANSWER"));
            item.setAnswerResolution(rs.getString("ANSWER_RESOLUTION"));
            item.setItemType(itemType);
            item.setItemBankId(rs.getLong("ITEMBANK_ID"));
            item.setCreator(rs.getLong("CREATOR"));
            item.setCreateTime(rs.getDate("CREATE_TIME"));
            if (isExistColumn(rs, "ITEMBANK_NAME")) {
                item.setItemBankName(rs.getString("ITEMBANK_NAME"));
            }
            item.setUpdateTime(sdf.format(rs.getTimestamp(strMess7)));
            item.setDeleteFlag(rs.getInt("DELETE_FLAG"));
            item.setItemTypeStr(itemTypeStr);
            /*item.setOption(optionList);*/

            return item;
        }

        boolean isExistColumn(ResultSet rs, String columnName) {
            try {
                if (rs.findColumn(columnName) > 0) {
                    return true;
                }
            } catch (SQLException e) {
                return false;
            }
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
    private static final class ItemDetailMapper implements RowMapper {
        private int i; //序号

        public ItemDetailMapper(int j) {
            this.i = j;
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int itemType = rs.getInt(strMess4);
            String itemTypeStr = "";
            //1:单项选择,2:多项选择,3：判断
            if (itemType == 1) {
                itemTypeStr = "单项选择题";
            } else if (itemType == 2) {
                itemTypeStr = "多项选择题";
            } else {
                itemTypeStr = "判断题";
            }
            Item item = new Item(i, rs.getString(strMess6),
                    itemTypeStr, sdf.format(rs.getTimestamp(strMess7)));
            return item;
        }
    }

    @SuppressWarnings("rawtypes")
    private static final class ItemOptionMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ItemOption itemOption = new ItemOption();
            itemOption.setOptionId(rs.getLong("OPTION_ID"));
            itemOption.setContent(rs.getString("CONTENT"));
            itemOption.setItemId(rs.getLong(strMess5));
            itemOption.setCreator(rs.getLong("CREATOR"));
            itemOption.setCreateTime(rs.getDate("CREATE_TIME"));
            itemOption.setUpdateTime(rs.getDate(strMess7));
            itemOption.setDeleteFlag(rs.getInt("DELETE_FLAG"));
            return itemOption;
        }
    }

    @SuppressWarnings("rawtypes")
    private static final class ResultInfoMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ResultInfo resultInfo = new ResultInfo();
            Item item = new Item();
            item.setItemId(rs.getLong(strMess5));
            item.setItemTitle(rs.getString(strMess6));
            item.setItemCode(rs.getString("ITEM_CODE"));
            item.setAnswer(rs.getString("ANSWER"));
            item.setAnswerResolution(rs.getString("ANSWER_RESOLUTION"));
            item.setItemType(rs.getInt(strMess4));
            item.setItemBankId(rs.getLong("ITEMBANK_ID"));
            resultInfo.setItem(item);
            resultInfo.setResult(rs.getString("RESULT"));
            resultInfo.setScore(rs.getDouble("SCORE"));
            return resultInfo;
        }
    }
}
