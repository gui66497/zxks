package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2016/6/14 21:15
 * @ClassName: TestpaperAddRequest
 * @Description:
 */
@XmlRootElement
public class TestpaperAddRequest extends BaseRequest {

    //试卷基本信息
    private TestPaper testPaper;

    //试卷部分的list
    List<String> partList = new ArrayList<String>();


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
     * 获取试卷部分的list.
     *
     * @return List
     */
    public List<String> getPartList() {
        return this.partList;
    }

    /**
     * 设置试卷部分的list.
     *
     * @param partList 试卷部分的list
     */
    public void setPartList(List<String> partList) {
        this.partList = partList;
    }
}
