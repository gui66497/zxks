package zzjz.service;
import zzjz.bean.TestPaperPart;

import java.util.List;

/**
 * @ClassName: TestPaperPartService
 * @Description: 试卷每部分的操作service接口定义
 * @author guzhenggen
 * @version 2016/6/14 20:13
 */
public interface TestPaperPartService {

    /**
     * 根据试卷Id获取试卷每部分的信息.
     * @param testPaperId 试卷id
     * @return 每部分信息列表
     */
    public List<TestPaperPart> getPartInfoListByTestPaperId(long testPaperId);

    /**
     * 根据考试信息ID获取试卷每部分的信息.
     * @param testInfoId 考试信息ID
     * @return 每部分信息列表
     */
    public List<TestPaperPart> getPartInfoListByTestInfoId(long testInfoId);

    /**
     * 根据成绩单信息ID获取试卷每部分的信息.
     * @param achieveMentId 成绩单信息ID
     * @return 每部分信息列表
     */
    public List<TestPaperPart> getPartInfoListByAchieveMentId(long achieveMentId);

}
