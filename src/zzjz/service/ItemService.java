package zzjz.service;

import zzjz.bean.Item;
import zzjz.bean.ItemOption;
import zzjz.bean.PagingEntity;
import zzjz.bean.ResultInfo;

import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/6/3 16:02
 * @ClassName: ItemService
 * @Description: 题目操作service接口定义类
 */
public interface ItemService {

    /**
     * 添加题目信息.
     *
     * @param item 题目信息
     * @return 添加结果
     */
    public boolean addItem(Item item);

    /**
     * 根据题目ID删除题目信息.
     *
     * @param itemId 题目ID
     * @return 删除结果
     */
    public boolean deleteItem(long itemId);

    /**
     * 更新题目信息.
     *
     * @param item 题目信息
     * @return 更新结果
     */
    public boolean updateItem(Item item);

    /**
     * 根据题目部分信息获取题目信息.
     *
     * @param itemId 题目信息ID
     * @return 题目信息
     */
    public Item getItem(Long itemId);

    /**
     * 根据题库ID查询题目信息列表.
     *
     * @param itemBankId   题库ID
     * @param pagingEntity 分页组件
     * @return 题目信息列表
     */
    public List<Item> getItemList(long itemBankId, PagingEntity pagingEntity);

    /**
     * 根据题库ID和查询条件查询题目信息列表.
     *
     * @param itemBankId 题库ID
     * @param searchName 查询条件
     * @return 题目信息列表
     */
    public List<Item> getItemList(long itemBankId, String searchName);

    /**
     * 批量添加题目选项.
     *
     * @param options 题目选项
     * @return 添加结果
     */
    public boolean addItemOptions(ItemOption[] options);

    /**
     * 根据题目ID获取题目选项列表.
     *
     * @param itemId 题目ID
     * @return 题目选项信息列表
     */
    public List<ItemOption> getItemOptions(long itemId);

    /**
     * 根据题目ID删除题目选项.
     *
     * @param itemId 题目ID
     * @return 删除结果
     */
    public boolean deleteItemOption(long itemId);

    /**
     * 根据题库ID获取题目数.
     *
     * @param itemBankId   题库ID
     * @param pagingEntity 分页组件
     * @return 题目数
     */
    public int getItemCount(long itemBankId, PagingEntity pagingEntity);

    /**
     * 根据题库ID获取题目数.
     *
     * @param itemBankId 题库ID
     * @return 题目数
     */
    public int getItemCount(long itemBankId);

    /**
     * 根据题目ID列表.
     *
     * @param itemIDList 题目ID列表
     * @return 操作结果
     */
    public boolean deleteItemByItemIDList(final List<Long> itemIDList);

    /**
     * 根据试卷部分Id查询题目信息.
     *
     * @param partId 试卷部分Id
     * @return 题目信息
     */
    public List<Item> getItemByPartId(long partId);

    /**
     * 根据题库ID和题目类型获取题目.
     *
     * @param itemBankId   题库ID
     * @param itemBankType 题目类型
     * @return 题目实体
     */
    List<Item> getItemByItemTypeAndItemBank(Long itemBankId, Integer itemBankType);

    /**
     * 根据题库ID和题目类型获取题目.
     *
     * @param itemBankId   题库ID
     * @param itemBankType 题目类型
     * @param quantity     所需数量
     * @return 题目实体
     */
    List<Item> getItemByItemTypeAndItemBank(Long itemBankId, Integer itemBankType, Integer quantity);

    /**
     * 根据题库ID和题目类型获取题目数目.
     *
     * @param itemBankId   题库ID
     * @param itemBankType 题目类型
     * @return 题目数目
     */
    Integer countItemByItemTypeAndItemBank(Long itemBankId, Integer itemBankType);

    /**
     * 根据试卷部分Id和成绩单Id查询题目信息，以及对应的答案信息.
     *
     * @param partId        试卷部分Id
     * @param achievementId 成绩Id
     * @return 题目信息
     */
    public List<ResultInfo> getItemByPartId(long partId, long achievementId);

    /**
     * 根据题库ID获取题目列表.
     *
     * @param itemBankId 题库Id
     * @return 题目列表
     */
    public List<Item> getItemListByItemBankId(long itemBankId);

    /**
     * 根据题库IDs和题目类型获取题目列表.
     *
     * @param itemBankIds  题库IDs
     * @param itemType     题目类型
     * @param pagingEntity 分页组件
     * @return List
     */
    public List<Item> getItemByItemTypeAndItemBankIds(String itemBankIds, Integer itemType, PagingEntity pagingEntity);

    /**
     * 根据题库IDs和题目类型获取题目列表.
     *
     * @param itemBankIds  题库IDs
     * @param itemType     题目类型
     * @param pagingEntity 分页组件
     * @return int
     */
    public int countByItemTypeAndItemBankIds(String itemBankIds, Integer itemType, PagingEntity pagingEntity);

}
