package zzjz.service;

import zzjz.bean.ItemBank;
import zzjz.bean.ItemBankCategory;
import java.util.List;

/**
 * @ClassName: ItemBankService
 * @Description: 题库操作service接口定义类
 * @author guzhenggen
 * @version 2016/5/30 13:58
 */
public interface ItemBankService {

    /**
     * 添加题库类型信息.
     * @param itemBankCategory 题库类型实体类
     * @return 添加结果
     */
    public boolean addItemBankCategory(ItemBankCategory itemBankCategory);

    /**
     * 根据categoryId删除题库类型信息.
     * @param categoryId 题库类型Id
     * @return 删除结果
     */
    public boolean deleteItemBankCategory(long categoryId);

    /**
     * 修改题库类型信息.
     * @param itemBankCategory 题库类型实体类
     * @return 更新结果
     */
    public boolean updateItemBankCategory(ItemBankCategory itemBankCategory);

    /**
     * 获取题库类型信息List.
     * @return 题库类型信息List
     */
    public List<ItemBankCategory> getItemBankCategoryList();

    /**
     * 根据题库类型实体类的parentId,categoryName获取题库分类信息.
     * @param itemBankCategory 题库类型实体类
     * @return 题库分类信息
     */
    public ItemBankCategory getItemBankCategory(ItemBankCategory itemBankCategory);

    /**
     * 根据categoryId查询该分类下是否有子分类.
     * @param categoryId 分类Id
     * @return 题库分类信息
     */
    public ItemBankCategory getItemBankCategoryById(long categoryId);

    /**
     * 添加题库信息.
     * @param itemBank 题库实体类
     * @return 添加结果
     */
    public boolean addItemBank(ItemBank itemBank);

    /**
     * 删除题库信息.
     * @param itemBankId 题库信息Id
     * @return 删除结果
     */
    public boolean deleteItemBank(long itemBankId);

    /**
     * 更新题库信息.
     * @param itemBank 题库实体类
     * @return 更新结果
     */
    public boolean updateItemBank(ItemBank itemBank);

    /**
     * 根据categoryId查询该分类下的题库信息列表.
     * @param categoryId 分类Id
     * @return 题库信息
     */
    public List<ItemBank> getItemBankList(long categoryId);

    /**
     * 根据题库实体类的categoryId,itemBankName获取题库信息.
     * @param itemBank 题库类型实体类
     * @return 题库信息
     */
    public ItemBank getItemBank(ItemBank itemBank);

    /**
     * 根据categoryId和查询条件,模糊查询该分类下的题库信息列表.
     * @param categoryId 分类Id
     * @param searchName 查询条件
     * @return 题库信息
     */
    public List<ItemBank> getItemBankList(long categoryId, String searchName);

    /**
     * 根据题库id获取题库信息.
     * @param itemBankId 题库id
     * @return 题库信息
     */
    public ItemBank getItemBankById(long itemBankId);

}
