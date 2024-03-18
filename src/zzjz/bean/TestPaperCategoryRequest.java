package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guitang.fang
 * @ClassName: TestPaperCategoryRequest
 * @Description: 考试类型实体类request
 * @version 2016/5/31 14:15
 */
@XmlRootElement
public class TestPaperCategoryRequest extends BaseRequest {

    //试卷分类
    private TestPaperCategory testPaperCategory;


    /**
     * 获取试卷分类.
     *
     * @return TestPaperCategory
     */
    public TestPaperCategory getTestPaperCategory() {
        return this.testPaperCategory;
    }

    /**
     * 设置试卷分类.
     *
     * @param testPaperCategory 试卷分类
     */
    public void setTestPaperCategory(TestPaperCategory testPaperCategory) {
        this.testPaperCategory = testPaperCategory;
    }
}
