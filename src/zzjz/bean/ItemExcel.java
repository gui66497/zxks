package zzjz.bean;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author pengpeng
 * @version 2017/10/12 13:47
 * @ClassName: ItemExcel
 * @Description: 试卷导入
 */
@ExcelTarget("ItemExcel")
@XmlRootElement
public class ItemExcel {

    @Excel(name = "类型",replace = {"单选_1", "多选_2", "判断_3"})
    private String type;

    @Excel(name = "题干")
    private String title;

    @Excel(name = "代码")
    private String code;

    @Excel(name = "选项A")
    private String optionA;

    @Excel(name = "选项B")
    private String optionB;

    @Excel(name = "选项C")
    private String optionC;

    @Excel(name = "选项D")
    private String optionD;

    @Excel(name = "选项E")
    private String optionE;

    @Excel(name = "选项F")
    private String optionF;

    @Excel(name = "选项G")
    private String optionG;

    @Excel(name = "选项H")
    private String optionH;

    @Excel(name = "答案")
    private String answer;

    @Excel(name = "解析")
    private String analysis;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public String getOptionF() {
        return optionF;
    }

    public void setOptionF(String optionF) {
        this.optionF = optionF;
    }

    public String getOptionG() {
        return optionG;
    }

    public void setOptionG(String optionG) {
        this.optionG = optionG;
    }

    public String getOptionH() {
        return optionH;
    }

    public void setOptionH(String optionH) {
        this.optionH = optionH;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
