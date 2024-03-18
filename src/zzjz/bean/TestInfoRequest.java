package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author zhuxiaoxiao
 * @version 2016/5/30 16:29
 * @ClassName: TestInfoRequest
 * @Description: 考试信息实体类request
 */
@XmlRootElement
public class TestInfoRequest extends BaseRequest {

    //考试信息实体
    private TestInfo testInfo;


    /**
     * 获取考试信息实体.
     *
     * @return TestInfo
     */
    public TestInfo getTestInfo() {
        return this.testInfo;
    }

    /**
     * 设置考试信息实体.
     *
     * @param testInfo 考试信息实体
     */
    public void setTestInfo(TestInfo testInfo) {
        this.testInfo = testInfo;
    }
}
