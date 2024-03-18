package zzjz.rest;

import zzjz.bean.ResultCode;
import zzjz.bean.User;
import zzjz.bean.UserRequest;
import zzjz.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @ClassName: LoginRestTest
 * @Description: 登录rest单元测试
 * @Author zhuxiaoxiao
 * @Date: 2016/7/14 16:31
 */
public class LoginRestTest {
    private HttpServletRequest servletRequest;
    private UserService userService;
    private HttpServletResponse servletResponse;
    private LoginRest loginRest;
    private HttpSession session;

    @Mock
    private MultivaluedMap<String, String> headerParams;
    HttpHeaders headers = new HttpHeaders() {
        public MultivaluedMap<String, String> getRequestHeaders() {
            return headerParams;
        }

        public List<String> getRequestHeader(String arg0) {
            return null;
        }

        public MediaType getMediaType() {
            return null;
        }

        public Locale getLanguage() {
            return null;
        }

        public Map<String, Cookie> getCookies() {
            return null;
        }

        public List<MediaType> getAcceptableMediaTypes() {
            return null;
        }

        public List<Locale> getAcceptableLanguages() {
            return null;
        }
    };
    @Before
    public void setUp() throws Exception {
        loginRest = new LoginRest();
        userService = PowerMockito.mock(UserService.class);
        servletRequest = PowerMockito.mock(HttpServletRequest.class);
        headerParams = PowerMockito.mock(MultivaluedMap.class);
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        loginRest.servletRequest = servletRequest;
        session =Mockito.mock(HttpSession.class);
        loginRest.userService = userService;
    }
    //用户登录单元测试
    @Test
    public void testUserLogin() throws Exception {
        UserRequest request = new UserRequest();
        Mockito.when(headerParams.getFirst("userId")).thenReturn("118");
        User user = new User();
        String userName = "admin";
        String userId = "118";
        String password = "123456";
        user.setUserName(userName);
        user.setPassWord(password);
        request.setUser(user);
        User user1 = new User();
        String password1 = "1234567";
        String userName1 = "admin";
        user1.setPassWord(password1);
        user1.setUserName(userName1);
        Mockito.when(userService.getUserByUserName(Mockito.anyString())).thenReturn(user1);
        List<User> userList = new ArrayList<User>();
        userList.add(user1);
        user1.setPassWord("123456");
        user.setPassWord("123456");
        Mockito.when(session.getAttribute("userName")).thenReturn(userName);
        Mockito.when(session.getAttribute("userId")).thenReturn(110L);
        Mockito.when(session.getAttribute("roleId")).thenReturn(1);
        Mockito.when(servletRequest.getSession()).thenReturn(session);
        Assert.assertEquals("用户登录成功", ResultCode.SUCCESS, loginRest.userLogin(request, headers).getResultCode());



        Mockito.when(userService.getUserByUserName(Mockito.anyString())).thenReturn(null);
        Assert.assertEquals("用户名不存在", ResultCode.ERROR, loginRest.userLogin(request, headers).getResultCode());
        Mockito.when(userService.getUserByUserName(Mockito.anyString())).thenReturn(user1);
        user1.setPassWord("123456");
        user.setPassWord("1234");
        Assert.assertEquals("密码不正确", ResultCode.ERROR, loginRest.userLogin(request, headers).getResultCode());

    }
    //获取用户信息单元测试
    @Test
    public void testGetUserInfo() throws Exception {
        UserRequest request = new UserRequest();
        User user = new User();
        String userName = "admin";
        long userId = 118L;
        user.setUserName(userName);
        request.setUser(user);
        Mockito.when(session.getAttribute("userId")).thenReturn("118");
        Mockito.when(servletRequest.getSession()).thenReturn(session);
        Mockito.when(userService.getUserByUserId(Mockito.anyLong())).thenReturn(new User());
        List<User> userList = new ArrayList<User>();
        userList.add(new User());
        Assert.assertEquals("用户信息获取成功", ResultCode.SUCCESS, loginRest.getUserInfo(headers).getResultCode());
        Mockito.when(userService.getUserByUserId(Mockito.anyLong())).thenReturn(null);
        Assert.assertEquals("用户不存在", ResultCode.ERROR, loginRest.getUserInfo(headers).getResultCode());

    }
    //根据用户名获取用户信息
    @Test
    public void testGetUserByUserName() throws Exception {
        UserRequest request = new UserRequest();
        User user = new User();
        String userName = "admin";
        long userId = 118L;
        int roleId = 1;
        user.setUserName(userName);
        user.setRoleId(roleId);
        request.setUser(user);
        Mockito.when(userService.getUserByUserName(Mockito.anyString())).thenReturn(new User());
        List<User> userList = new ArrayList<User>();
        userList.add(new User());
        Assert.assertEquals("用户已存在", ResultCode.ERROR, loginRest.getUserByUserName(request, headers).getResultCode());
    }
}