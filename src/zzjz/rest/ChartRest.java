package zzjz.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ChartCountEntity;
import zzjz.bean.ChartRequest;
import zzjz.bean.Company;
import zzjz.bean.Dept;
import zzjz.bean.ResultCode;
import zzjz.service.ChartsService;
import zzjz.service.CompanyService;
import zzjz.service.DeptService;
import zzjz.util.StringUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengyunfeng
 * @version 2016/06/20
 * @ClassName: ChartRest
 * @Description: 统计分析用Rest实体类
 * @Date: 2016/6/20 15:21
 */
@Component
@Path("/charts")
public class ChartRest {
    private static Logger logger = Logger.getLogger(ChartRest.class);

    /**
     * 注入chartsService.
     */
    @Autowired
    public ChartsService chartsService;

    /**
     * 注入companyService.
     */
    @Autowired
    public CompanyService companyService;

    /**
     * 注入deptService.
     */
    @Autowired
    public DeptService deptService;

    /**
     * 获取统计信息.
     *
     * @param chartRequest 图标返回对象
     * @param headers      headers对象
     * @return 统计信息
     */
    @POST
    @Path("/getChart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> getChartInfo(ChartRequest chartRequest,
                                             @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        String companyId = chartRequest.getCompanyId();
        List<String> returnList = new ArrayList<String>();
        //以单位为统计x轴
        if (companyId == null || "".equals(companyId)) {
            //取得所有单位信息
            List<Company> companyList = companyService.getCompanyList();
            for (int i = 0; i < companyList.size(); i++) {
                System.out.print(companyList.get(i).getCompanyId());
                //根据单位取得该单位下的所有用户
                List<String> userIdList =
                        chartsService.getUserInfo(String.valueOf(companyList.get(i).getCompanyId()));
                //根据单位ID取得该单位下面的所有部门
                List<Dept>  listOfDept = new ArrayList<Dept>();
                listOfDept =  deptService.getDeptListByCompanyId(companyList.get(i).getCompanyId());
                //判断该单位下是否有部门存在
                if (listOfDept.size() > 0) {
                    List<String> listOfDeptId = new ArrayList<String>();
                    for (int index = 0; index < listOfDept.size(); index++) {
                        listOfDeptId.add(String.valueOf(listOfDept.get(index).getCompanyId()));
                    }
                    String deptIdStr = StringUtil.listConvertToString(listOfDeptId, ",");
                    //根据单位取得该单位下面的所有人
                    List<String> userIdListOfDept =
                            chartsService.getUserListByDeptIds(deptIdStr);
                    //把单位下各个部门的人和单位下的人汇总
                    userIdList.addAll(userIdListOfDept);
                }
                List<ChartCountEntity> chartList =
                        chartsService.returnChartInfo(chartRequest.getPaperId(), userIdList);
                //拼成特定的格式：单位/部门名称+平均分+参考人数
                String chartStr = "";
                String str = "▓";
                if (chartList.size() > 0) {
                    String personCount = chartList.get(0).getPersonCount();
                    String avgMarkCount = chartList.get(0).getAvgCount();
                    chartStr = companyList.get(i).getCompanyName()
                            + str + personCount + str + avgMarkCount;
                } else {
                    chartStr = companyList.get(i).getCompanyName() + str + "0" + str + "0";
                }
                returnList.add(chartStr);
            }
        } else {
            //以单位内的部门为统计x轴
            //根据单位取得该单位下的部门
            List<Dept> deptList = deptService.getDeptListByCompanyId(Long.parseLong(companyId));
            //单位下部门为空的情况
            if (deptList != null && deptList.size() > 0) {
                for (int i = 0; i < deptList.size(); i++) {
                    Long deptIdStr = deptList.get(i).getDeptId();
                    List<String> userIdList =
                            chartsService.getUserListByDeptId(deptIdStr);
                    List<ChartCountEntity> chartList =
                            chartsService.returnChartInfo(chartRequest.getPaperId(), userIdList);
                    //拼成特定的格式：单位/部门名称+平均分+参考人数
                    String chartStr = "";
                    String str = "▓";
                    if (chartList.size() > 0) {
                        String personCount = chartList.get(0).getPersonCount();
                        String avgMarkCount = chartList.get(0).getAvgCount();
                        chartStr = deptList.get(i).getDeptName()
                                + str + personCount + str + avgMarkCount;
                    } else {
                        chartStr = deptList.get(i).getDeptName() + "▓0▓0";
                    }
                    returnList.add(chartStr);
                }
            } else {
                String chartStrOfNull = "<--该单位下不存在部门-->" + "▓0▓0";
                returnList.add(chartStrOfNull);
            }
            //根据单位取得每个部门下的所有人员
        }
        if (returnList.size() != 0) {
            logger.debug("统计数据不为空");
            response.setMessage("统计数据获取成功！");
            response.setData(returnList);
            response.setResultCode(ResultCode.SUCCESS);
        }
        return response;
    }

    private void logDebugAndSetMessage(String strMessage, BaseResponse response) {
        logger.debug(strMessage);
        response.setMessage(strMessage);
    }
}
