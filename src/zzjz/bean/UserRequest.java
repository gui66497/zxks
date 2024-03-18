package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 郑雲峰
 * @ClassName: UserRequest
 * @Description:
 * @version 2016年5月30日 下午1:32:29
 */
@XmlRootElement
public class UserRequest extends BaseRequest {

    //用户实体
    private User user;

    //用户ID列表
    private List<Long> userIdList;

    /**
     * 获取用户实体.
     *
     * @return User
     */
    public User getUser() {
        return this.user;
    }

    /**
     * 设置用户实体.
     *
     * @param user 用户实体
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 获取用户ID列表.
     *
     * @return List
     */
    public List<Long> getUserIdList() {
        return this.userIdList;
    }

    /**
     * 设置用户ID列表.
     *
     * @param userIdList 用户ID列表
     */
    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }
}
