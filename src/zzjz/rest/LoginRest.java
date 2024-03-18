package zzjz.rest;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.User;
import zzjz.bean.UserRequest;
import zzjz.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaoxiao
 * @version 2016/6/2 14:39
 * @ClassName: LoginRest
 * @Description: 登录操作Rest接口类
 */
@Component
@Path("/userLogin")
public class LoginRest {
    private static Logger logger = Logger.getLogger(UserRest.class);

    /**
     * 注入UserService.
     */
    @Autowired
    public UserService userService;

    @Context
    ServletContext context;

    @Context
    HttpServletRequest servletRequest;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * 用户登录.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return response
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> userLogin(UserRequest request,
                                        @Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        User user = request.getUser();
        User loginUser = userService.getUserByUserName(user.getUserName());
        List<User> userList = new ArrayList<User>();
        userList.add(loginUser);
        if (loginUser != null && loginUser.getPassWord().equals(user.getPassWord())) {
            String userName = loginUser.getUserName();
            String uid = String.valueOf(loginUser.getUserId());
            servletRequest.getSession().setAttribute("userName", userName);
            servletRequest.getSession().setAttribute("userId", uid);
            servletRequest.getSession().setAttribute("roleId", loginUser.getRoleId());
            logger.debug("用户登录成功！");
            response.setMessage("用户登录成功！");
            response.setData(userList);
            response.setResultCode(ResultCode.SUCCESS);
        } else if (loginUser == null) {
            logger.debug("用户名不存在！");
            response.setMessage("1");
            response.setResultCode(ResultCode.ERROR);
        } else if (!user.getPassWord().equals(loginUser.getPassWord())) {
            logger.debug("密码不正确！");
            response.setMessage("2");
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 获取用户信息.
     *
     * @param headers headers对象
     * @return 用户对象
     */
    @POST
    @Path("/getUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> getUserInfo(@Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        long uid = Long.parseLong((String) servletRequest.getSession().getAttribute("userId"));
        User userInfo = userService.getUserByUserId(uid);
        List<User> userList = new ArrayList<User>();
        userList.add(userInfo);
        if (userInfo != null) {
            logger.debug("用户信息获取成功！");
            response.setMessage("用户信息获取成功！");
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
     * 根据用户名获取用户.
     *
     * @param request 用户实体返回
     * @param headers headers对象
     * @return 用户对象
     */
    @POST
    @Path("/getUserByUserName")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<User> getUserByUserName(UserRequest request,
                                                @Context HttpHeaders headers) {
        BaseResponse<User> response = new BaseResponse<User>();
        String userName = request.getUser().getUserName();
        User userInfo = userService.getUserByUserName(userName);
        List<User> userList = new ArrayList<User>();
        userList.add(userInfo);
        if (userInfo != null) {
            logger.debug("用户已存在！");
            response.setMessage("用户已存在！");
            response.setData(userList);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 初始化word分词资源（ajax异步调用）
     * @param headers headers
     * @return
     */
    @POST
    @Path("/initWord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Word> initWord(@Context HttpHeaders headers) {
        BaseResponse<Word> response = new BaseResponse<Word>();
        logger.info("开始初始化word分词资源");
        List<Word> words = WordSegmenter.seg("今天天气不错");
        logger.info("初始化word分词资源结束!");
        System.out.println(words);
        response.setData(words);
        response.setMessage("初始化分词资源成功！");
        return response;
    }

    /**
     * 初始化word分词资源（spring装载结束后）
     */
    //@PostConstruct
    private void initWord() {
        logger.info("开始初始化word分词资源");
        List<Word> words = WordSegmenter.seg("天气不错，出门钓鱼");
        logger.info("初始化word分词资源结束!");
        System.out.println(words);
    }

}
