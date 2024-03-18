package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.ItemBank;
import zzjz.bean.ItemBankCategory;
import zzjz.service.ItemBankService;
import zzjz.util.MyJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/5/30 14:09
 * @ClassName: ItemBankCategoryServiceImpl
 * @Description: 题库操作service接口实现类
 */
@Service
public class ItemBankServiceImpl implements ItemBankService {

    /**
     * 注入MyJdbcTemplate.
     */
    @Autowired
    public MyJdbcTemplate myJdbcTemplate;

    private static String str = "',";

    /**
     * 添加题库分类.
     *
     * @param itemBankCategory 题库类型实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addItemBankCategory(ItemBankCategory itemBankCategory) throws DataAccessException {

        String sql = "INSERT INTO tb_itembankcategory(CATEGORY_ID,CATEGORY_NAME," +
                "PARENTID,CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                "VALUES ('" + itemBankCategory.getCategoryId() + str + "'" +
                itemBankCategory.getCategoryName() + str + itemBankCategory.getParentId()
                + "," + itemBankCategory.getCreator() + ",NOW(),NOW(),0)";
        try {
            myJdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除题库分类.
     *
     * @param categoryId 题库类型Id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteItemBankCategory(long categoryId) throws DataAccessException {
        String sql = "UPDATE tb_itembankcategory SET DELETE_FLAG=1" +
                " WHERE CATEGORY_ID =" + categoryId;
        try {
            myJdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 编辑题库分类.
     *
     * @param itemBankCategory 题库类型实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateItemBankCategory(ItemBankCategory itemBankCategory) throws DataAccessException {
        String sql = "update tb_itembankcategory set" +
                " CATEGORY_NAME='" + itemBankCategory.getCategoryName()
                + "',UPDATE_TIME=NOW() WHERE CATEGORY_ID =" + itemBankCategory.getCategoryId();
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
     * 获取题库分类列表.
     *
     * @return List
     */
    public List<ItemBankCategory> getItemBankCategoryList() {
        String sql = "SELECT count(t2.ITEMBANK_NAME) AS COUNTS,t1.CATEGORY_ID,t1.CATEGORY_NAME,t1.PARENTID,t1.CREATOR," +
                "t1.CREATE_TIME,t1.UPDATE_TIME,t1.DELETE_FLAG FROM tb_itembankcategory t1 LEFT JOIN tb_itembank t2 " +
                "ON t1.CATEGORY_ID = t2.CATEGORY_ID AND t2.DELETE_FLAG =0 WHERE t1.DELETE_FLAG=0 GROUP BY t1.CATEGORY_ID";
        return myJdbcTemplate.query(sql, new ItemBankCategoryMapper());
    }

    /**
     * 获取题库分类.
     *
     * @param itemBankCategory 题库类型实体类
     * @return ItemBankCategory
     */
    public ItemBankCategory getItemBankCategory(ItemBankCategory itemBankCategory) {
        String sql = "SELECT CATEGORY_ID,CATEGORY_NAME,PARENTID,CREATOR," +
                "CREATE_TIME,UPDATE_TIME,DELETE_FLAG FROM tb_itembankcategory " +
                "WHERE DELETE_FLAG=0 AND" +
                " PARENTID=" + itemBankCategory.getParentId() + "" +
                " AND CATEGORY_NAME='" + itemBankCategory.getCategoryName() + "'";
        List<ItemBankCategory> itemBankCategoryList =
                myJdbcTemplate.query(sql, new ItemBankCategoryMapper());
        //判断查询结果是否为空
        if (itemBankCategoryList != null && itemBankCategoryList.size() > 0) {
            return itemBankCategoryList.get(0);
        }
        return null;
    }

    /**
     * 根据Id获取题库分类.
     *
     * @param categoryId 分类Id
     * @return ItemBankCategory
     */
    public ItemBankCategory getItemBankCategoryById(long categoryId) {
        String sql = "SELECT CATEGORY_ID,CATEGORY_NAME,PARENTID," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG FROM tb_itembankcategory " +
                "WHERE DELETE_FLAG=0 AND PARENTID=" + categoryId;
        List<ItemBankCategory> itemBankCategoryList =
                myJdbcTemplate.query(sql, new ItemBankCategoryMapper());
        //判断查询结果是否为空
        if (itemBankCategoryList != null && itemBankCategoryList.size() > 0) {
            return itemBankCategoryList.get(0);
        }
        return null;
    }

    /**
     * 添加题库.
     *
     * @param itemBank 题库实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean addItemBank(ItemBank itemBank) throws DataAccessException {

        String sql = "INSERT INTO tb_itembank(ITEMBANK_ID,ITEMBANK_NAME," +
                "ITEMBANK_DESCRIPTION,CATEGORY_ID,CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG) " +
                "VALUES (" + itemBank.getItemBankId() + ",'"
                + itemBank.getItemBankName() + "','" + itemBank.getItemBankDescription() + str +
                itemBank.getCategoryId() + "," + itemBank.getCreator() + ",NOW(),NOW(),0)";
        try {
            myJdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除题库.
     *
     * @param itemBankId 题库信息Id
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean deleteItemBank(long itemBankId) throws DataAccessException {
        String sql = "UPDATE tb_itembank SET DELETE_FLAG=1 WHERE ITEMBANK_ID =" + itemBankId;
        try {
            myJdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 编辑题库.
     *
     * @param itemBank 题库实体类
     * @return boolean
     * @throws DataAccessException 异常
     */
    public boolean updateItemBank(ItemBank itemBank) throws DataAccessException {
        String sql = "UPDATE tb_itembank SET ITEMBANK_NAME='"
                + itemBank.getItemBankName() + "',ITEMBANK_DESCRIPTION='" +
                itemBank.getItemBankDescription() + str +
                "CATEGORY_ID=" + itemBank.getCategoryId() + ",UPDATE_TIME=NOW() " +
                "WHERE ITEMBANK_ID =" + itemBank.getItemBankId();
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
     * 获取题库列表.
     *
     * @param categoryId 分类Id
     * @return List
     */
    public List<ItemBank> getItemBankList(long categoryId) {
        String sql = "SELECT t1.ITEMBANK_ID,t1.ITEMBANK_NAME,t1.ITEMBANK_DESCRIPTION," +
                "t1.CREATOR,t1.CREATE_TIME,t1.UPDATE_TIME,t1.DELETE_FLAG," +
                " COUNT(t2.ITEM_TITLE) AS COUNTS " +
                " FROM tb_itembank t1 LEFT JOIN tb_item t2 ON t1.ITEMBANK_ID = t2.ITEMBANK_ID" +
                " AND t2.DELETE_FLAG=0 WHERE  t1.DELETE_FLAG=0  ";
        if (categoryId > 0) {
            sql += " and t1.CATEGORY_ID=" + categoryId + "";
        }
        sql += " GROUP BY t1.ITEMBANK_ID ORDER BY t1.UPDATE_TIME DESC";
        return myJdbcTemplate.query(sql, new ItemBankMapper());
    }

    /**
     * 获取题库信息.
     *
     * @param itemBank 题库类型实体类
     * @return ItemBank
     */
    public ItemBank getItemBank(ItemBank itemBank) {
        String sql = "SELECT ITEMBANK_ID,ITEMBANK_NAME,ITEMBANK_DESCRIPTION," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG " +
                "FROM tb_itembank WHERE CATEGORY_ID=" + itemBank.getCategoryId() + "" +
                " AND ITEMBANK_NAME ='" + itemBank.getItemBankName() + "'" +
                " AND ITEMBANK_ID !=" + itemBank.getItemBankId() +
                " AND DELETE_FLAG=0 ORDER BY ID DESC";
        List<ItemBank> itemBankList = myJdbcTemplate.query(sql, new ItemBankMapper());
        //判断查询结果是否为空
        if (itemBankList != null && itemBankList.size() > 0) {
            return itemBankList.get(0);
        }
        return null;
    }

    /**
     * 获取题库信息列表.
     *
     * @param categoryId 分类Id
     * @param searchName 查询条件
     * @return List
     */
    public List<ItemBank> getItemBankList(long categoryId, String searchName) {
        String sql = "SELECT t1.ITEMBANK_ID,t1.ITEMBANK_NAME,t1.ITEMBANK_DESCRIPTION," +
                "t1.CREATOR,t1.CREATE_TIME,t1.UPDATE_TIME,t1.DELETE_FLAG," +
                "COUNT(t2.ITEM_TITLE)  AS COUNTS " +
                "FROM tb_itembank t1 LEFT JOIN tb_item t2 ON" +
                " t1.ITEMBANK_ID = t2.ITEMBANK_ID AND t2.DELETE_FLAG=0" +
                " WHERE t1.ITEMBANK_NAME LIKE '%" + searchName + "%' AND" +
                " t1.DELETE_FLAG=0";

        if (categoryId > 0) {
            sql += " and t1.CATEGORY_ID=" + categoryId + "";
        }
        sql += " GROUP BY t1.ITEMBANK_ID ORDER BY t1.UPDATE_TIME DESC ";

        return myJdbcTemplate.query(sql, new ItemBankMapper());
    }

    /**
     * 根据ID获取题库信息.
     *
     * @param itemBankId 题库id
     * @return ItemBank
     */
    public ItemBank getItemBankById(long itemBankId) {
        String sql = "SELECT ITEMBANK_ID,ITEMBANK_NAME,ITEMBANK_DESCRIPTION," +
                "CREATOR,CREATE_TIME,UPDATE_TIME,DELETE_FLAG " +
                "FROM tb_itembank WHERE ITEMBANK_ID = ? AND DELETE_FLAG=0 ORDER BY ID DESC";
        List<ItemBank> itemBankList =
                myJdbcTemplate.query(sql, new ItemBankMapper(), itemBankId);
        //判断查询结果是否为空
        if (itemBankList != null && itemBankList.size() > 0) {
            return itemBankList.get(0);
        }
        return null;
    }


    @SuppressWarnings("rawtypes")
    private static final class ItemBankCategoryMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ItemBankCategory itemBankCategory = new ItemBankCategory();
            itemBankCategory.setCategoryId(rs.getLong("CATEGORY_ID"));
            itemBankCategory.setCategoryName(rs.getString("CATEGORY_NAME"));
            itemBankCategory.setParentId(rs.getLong("PARENTID"));
            itemBankCategory.setCreator(rs.getLong("CREATOR"));
            itemBankCategory.setCreateTime(rs.getDate("CREATE_TIME"));
            itemBankCategory.setUpdateTime(rs.getDate("UPDATE_TIME"));
            itemBankCategory.setDeleteFlag(rs.getInt("DELETE_FLAG"));
            if (isExistColumn(rs, "COUNTS")) {
                itemBankCategory.setCounts(rs.getInt("COUNTS"));
            }
            return itemBankCategory;
        }
    }

    @SuppressWarnings("rawtypes")
    private static final class ItemBankMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ItemBank itemBank = new ItemBank();
            itemBank.setItemBankId(rs.getLong("ITEMBANK_ID"));
            itemBank.setItemBankName(rs.getString("ITEMBANK_NAME"));
            itemBank.setItemBankDescription(rs.getString("ITEMBANK_DESCRIPTION"));
            itemBank.setCreator(rs.getLong("CREATOR"));
            itemBank.setCreateTime(rs.getDate("CREATE_TIME"));
            itemBank.setUpdateTime(sdf.format(rs.getTimestamp("UPDATE_TIME")));
            itemBank.setDeleteFlag(rs.getInt("DELETE_FLAG"));
            if (isExistColumn(rs, "COUNTS")) {
                itemBank.setItemCount(rs.getInt("COUNTS"));
            }
            return itemBank;
        }
    }

    //判断该列是否存在
    static boolean isExistColumn(ResultSet rs, String columnName) {
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
