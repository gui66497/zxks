package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author guitang.fang
 * @ClassName:
 * @Description:
 * @version 2016/6/1 9:30
 */
@XmlRootElement
public class TestPaperRequest extends BaseRequest {

    //试卷基本信息
    private TestPaper testPaper;

    //试卷ID列表
    private List<Long> testPaperIdList;


    /**
     * 获取试卷基本信息.
     *
     * @return TestPaper
     */
    public TestPaper getTestPaper() {
        return this.testPaper;
    }

    /**
     * 设置试卷基本信息.
     *
     * @param testPaper 试卷基本信息
     */
    public void setTestPaper(TestPaper testPaper) {
        this.testPaper = testPaper;
    }

    /**
     * 获取试卷ID列表.
     *
     * @return List
     */
    public List<Long> getTestPaperIdList() {
        return this.testPaperIdList;
    }

    /**
     * 设置试卷ID列表.
     *
     * @param testPaperIdList 试卷ID列表
     */
    public void setTestPaperIdList(List<Long> testPaperIdList) {
        this.testPaperIdList = testPaperIdList;
    }
}
