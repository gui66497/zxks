package zzjz.rest;

import com.mysql.jdbc.StringUtils;
import zzjz.bean.User;
import zzjz.bean.UserRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.PagingEntity;
import zzjz.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 梅宏振
 * @version 2015年3月4日 下午1:32:29
 * @ClassName: UserRest
 * @Description: 用户相关操作Rest接口类
 */

@Component
@Path("/user")
public class UserRest {

    private static Logger logger = Logger.getLogger(UserRest.class);

    private static String strUser = "用户信息获取成功！";

    private static String strUserFa = "用户信息为空！";

    private static String strNull = "该用户不存在！";

    @Autowired
    private UserService userService;

    @Context
    ServletContext context;

    @Context
    HttpServletRequest httpServletRequest;

    /**
     * 获取用户列表.
     *
     * @param headers headers对象
     * @return 用户列表
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> getUserList(@Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        List<User> userList = userService.getUserList(); //获取用户列表
        if (userList != null) {
            logger.debug(strUser);
            response.setMessage(strUser);
            response.setData(userList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(strUserFa);
            response.setMessage(strUserFa);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 根据用户名获取用户列表.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return 用户列表
     */
    @POST
    @Path("/searchByName")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> getSearchUserList(UserRequest request,
                                                @Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        User user = request.getUser(); //获取用户信息
        //定义列名
        String[] cols = {"", "USERNAME", "REALNAME", "GENDER", "", "", "", "", "", ""}; //字段名称，对应页面表格
        PagingEntity pagingEntity = request.getPaging();
        String sortIndex = pagingEntity.getSortColumn(); //排序字段名称对应的位置
        pagingEntity.setSortColumn(cols[Integer.parseInt(sortIndex)]); //修改为排序的字段名称
        List<User> userList =
                userService.getUserListSearchByName(user, pagingEntity); //根据用户名和真实姓名检索用户
        int count = 0;

        if (userList != null) {
            logger.debug(strUser);
            response.setMessage(strUser);
            count = userService.getUserListSearchByNameCount(user, pagingEntity); //获取总条数
            logger.debug("总条数:" + count);
            response.setData(userList);
            response.setiTotalRecords(count);
            response.setiTotalDisplayRecords(count);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug("用户信息获取失败！");
            response.setMessage("用户信息获取失败！");
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 删除用户前确认.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return 用户列表
     */
    @POST
    @Path("/confirmDelete")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> confirmUsersBeforeDel(UserRequest request,
                                                    @Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        List<Long> userIdList = request.getUserIdList(); //获取用户列表信息
        if (userIdList == null || userIdList.size() == 0) {
            logDebugAndSetMessage("用户列表信息为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        List<User> userList = userService.confirmToDeleteUsers(userIdList); //检测不可删除的用户
        if (userList != null) {
            logger.debug("获取不可删除的用户列表信息获取成功！");
            response.setMessage("获取不可删除的用户列表信息获取成功！");
            response.setData(userList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug("获取不可删除的用户列表信息失败！");
            response.setMessage("获取不可删除的用户列表信息失败！");
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 添加用户.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return true or false
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addUser(UserRequest request,
                                        @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        User user = request.getUser();
        response = checkUserInfo(user, response, false);
        if (ResultCode.BAD_REQUEST.equals(response.getResultCode())) {
            return response;
        }

        long userId = System.currentTimeMillis(); //时间戳，即为角色Id
        user.setUserId(userId); //设置角色Id


        boolean success = userService.addUser(user); //添加用户信息
        if (success) {
            logDebugAndSetMessage("用户信息添加成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("用户信息添加失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 删除用户.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return true or false
     */
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteUser(UserRequest request,
                                           @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        User user = request.getUser(); //获取用户信息
        if (user == null || user.getUserId() == 0) {
            logDebugAndSetMessage(strUserFa, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        User adminUser = userService.getUserByUserName("admin");
        if (adminUser != null && adminUser.getUserId() == user.getUserId()) {
            logDebugAndSetMessage("管理员账号不可删除！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        boolean success = userService.deleteUserById(user.getUserId()); //删除用户信息
        if (success) {
            logDebugAndSetMessage("用户信息删除成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("用户信息删除失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 根据用户列表删除用户信息.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return 删除结果
     */
    @DELETE
    @Path("/deleteUsers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteUserByList(UserRequest request,
                                                 @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        List<Long> userIdList = request.getUserIdList(); //获取用户列表信息
        if (userIdList == null || userIdList.size() == 0) {
            logDebugAndSetMessage("用户列表信息为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        int[] deleteResult = userService.deleteUserByUserIdList(userIdList); //根据用户列表删除用户信息
        if (deleteResult != null) {
            logDebugAndSetMessage("用户信息删除成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("用户信息删除失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 编辑用户.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateUser(UserRequest request,
                                           @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        User user = request.getUser();
        if (user == null || user.getUserId() == 0) {
            logDebugAndSetMessage(strUserFa, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        //获取用户密码
        User searchResult = userService.getUserByUserId(user.getUserId());
        if (searchResult == null) {
            logDebugAndSetMessage(strNull, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            user.setPassWord(searchResult.getPassWord());
        }

        response = checkUserInfo(user, response, true);
        if (ResultCode.BAD_REQUEST.equals(response.getResultCode())) {
            return response;
        }
        boolean success = userService.updateUser(user); //更新用户信息
        if (success) {
            logDebugAndSetMessage("用户信息修改成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("用户信息修改失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 更新密码.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/updatePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updatePassword(UserRequest request,
                                               @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        User user = request.getUser();
        if (user == null || user.getUserId() == 0) {
            logDebugAndSetMessage(strUserFa, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isNullOrEmpty(user.getPassWord())) {
            logDebugAndSetMessage("密码不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        User searchResult = userService.getUserByUserId(user.getUserId());
        if (searchResult != null) {
            searchResult.setPassWord(user.getPassWord());
        } else {
            logDebugAndSetMessage(strNull, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        response = checkUserInfo(searchResult, response, true);
        if (ResultCode.BAD_REQUEST.equals(response.getResultCode())) {
            return response;
        }
        boolean success = userService.updateUser(searchResult); //更新用户信息
        if (success) {
            logDebugAndSetMessage("密码修改成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("密码修改失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 重置密码.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/resetPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> resetPassword(UserRequest request,
                                              @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        User user = request.getUser();
        if (user == null || user.getUserId() == 0) {
            logDebugAndSetMessage(strUserFa, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        User searchResult = userService.getUserByUserId(user.getUserId());
        if (searchResult != null) {
            searchResult.setPassWord("123456");
        } else {
            logDebugAndSetMessage(strNull, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        response = checkUserInfo(searchResult, response, true);
        if (ResultCode.BAD_REQUEST.equals(response.getResultCode())) {
            return response;
        }
        boolean success = userService.updateUser(searchResult); //更新用户信息
        if (success) {
            logDebugAndSetMessage("密码重设成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("密码重设失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 根据用户ID获取用户信息.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return 用户信息
     */
    @POST
    @Path("/getUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> getUserInfo(UserRequest request, @Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        User user = request.getUser();
        User userInfo = userService.getUserByUserId(user.getUserId());
        List<User> userList = new ArrayList<User>();
        userList.add(userInfo);
        if (userInfo != null) {
            logger.debug(strUser);
            response.setMessage(strUser);
            response.setData(userList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug("用户不存在！");
            response.setMessage("用户不存在！");
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 确认密码.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return 确认信息
     */
    @POST
    @Path("/confirmPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> confirmPassword(UserRequest request,
                                                @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        User user = request.getUser();
        if (user == null) {
            logDebugAndSetMessage("参数为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        //检验用户是否存在以及密码是否匹配
        User searchResult = userService.getUserByUserName(user.getUserName());
        if (searchResult != null) {
            if (searchResult.getPassWord().equals(user.getPassWord())) {
                logDebugAndSetMessage("用户原密码匹配成功！", response);
                response.setResultCode(ResultCode.SUCCESS);
            } else {
                logDebugAndSetMessage("用户原密码错误！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
            }
        } else {
            logDebugAndSetMessage(strNull, response);
            response.setResultCode(ResultCode.BAD_REQUEST);
        }
        return response;
    }

    /**
     * 非空和重复验证.
     *
     * @param boolUpdate 是否为更新
     * @return true or false
     */
    private BaseResponse checkUserInfo(User user, BaseResponse response,
                                       boolean boolUpdate) {
        if (user == null) {
            logDebugAndSetMessage("参数为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isNullOrEmpty(user.getUserName())) {
            logDebugAndSetMessage("用户名不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            //检验用户名重复
            User searchResult = userService.getUserByUserName(user.getUserName());
            if (searchResult != null) {
                Boolean boolCheck =
                        boolUpdate ? searchResult.getUserId() != user.getUserId() : true;
                if (boolCheck) {
                    logDebugAndSetMessage("用户名已存在！", response);
                    response.setResultCode(ResultCode.BAD_REQUEST);
                    return response;
                }
            }
        }

        if (StringUtils.isNullOrEmpty(user.getPassWord())) {
            logDebugAndSetMessage("密码不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        if (StringUtils.isNullOrEmpty(user.getRealName())) {
            logDebugAndSetMessage("真实姓名不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        return response;
    }

    private void logDebugAndSetMessage(String strMessage, BaseResponse response) {
        logger.debug(strMessage);
        response.setMessage(strMessage);
    }

}
