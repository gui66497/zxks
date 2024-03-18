package zzjz.service;

import zzjz.bean.PagingEntity;
import zzjz.bean.User;
import java.util.List;
/**
 * @ClassName: UserService
 * @Description: 用户操作service接口定义
 * @author 梅宏振
 * @version 2016年5月24日 上午8:32:29
 */
public interface UserService {

    /**
     * 创建新用户.
     * @param user 用户实体
     * @return true or false
     */
    public boolean addUser(User user);

    /**
     * 根据用户ID删除用户.
     * @param userId 用户Id
     * @return true or false
     */
    public boolean deleteUserById(long userId);

    /**
     * 根据用户列表删除用户.
     * @param userIdList 用户Id列表
     * @return int类型的数组
     */
    public int[] deleteUserByUserIdList(List<Long> userIdList);

    /**
     * 修改用户.
     * @param user 用户实体类
     * @return 修改结果
     */
    public boolean updateUser(User user);

    /**
     * 根据用户名获取用户信息.
     * @param userName 用户姓名
     * @return 查询结果
     */
    public User getUserByUserName(String userName);

    /**
     * 根据用户Id获取用户信息.
     * @param userId 用户Id
     * @return 查询结果
     */
    public User getUserByUserId(long userId);

    /**
     * 根据用户身份证号获取用户信息.
     * @param idCardNo 用户身份证号
     * @return 查询结果
     */
    public User getUserByUserIdCardNo(String idCardNo);

    /**
     * 根据用户邮箱获取用户信息.
     * @param email 用户邮箱
     * @return 查询结果
     */
    public User getUserByUserIdEmail(String email);

    /**
     * 获取用户信息List.
     * @return 查询结果
     */
    public List<User> getUserList();

    /**
     * 根据姓名检索并获取用户信息List.
     * @param userName 用户模糊查询的用户姓名
     * @return 查询结果
     */
    public List<User> getUserListSearchByName(String userName);

    /**
     * 根据姓名检索并获取用户信息List.
     * @param user 模糊查询的用户
     * @param pagingEntity 分页组件
     * @return 用户信息列表
     */
    public List<User> getUserListSearchByName(User user, PagingEntity pagingEntity);

    /**
     * 根据姓名检索并获取用户数量.
     * @param user 模糊查询的用户
     * @param pagingEntity 分页组件
     * @return 用户数
     */
    public int getUserListSearchByNameCount(User user, PagingEntity pagingEntity);

    /**
     * 获取不可删除的用户信息List.
     * @param userIdList 用户Id 列表
     * @return 查询结果
     */
    public List<User> confirmToDeleteUsers(List<Long> userIdList);
}
