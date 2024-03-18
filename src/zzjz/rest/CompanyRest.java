package zzjz.rest;

import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Company;
import zzjz.bean.CompanyRequest;
import zzjz.bean.ResultCode;
import zzjz.service.CompanyService;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 * @author caixiaoloang
 * @version 2016/6/6 10:07
 * @ClassName: CompanyRest
 * @Description: 单位相关操作Rest接口类
 */
@Component
@Path("/company")
public class CompanyRest {
    private static Logger logger = Logger.getLogger(CompanyRest.class);

    @Autowired
    private CompanyService companyService;

    @Context
    ServletContext context;

    /**
     * 获取单位信息列表.
     *
     * @param headers headers对象
     * @return 单位信息列表
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Company> getCompanyList(@Context HttpHeaders headers) {
        BaseResponse<Company> response = new BaseResponse<Company>();
        List<Company> companyList = companyService.getCompanyList(); //获取单位列表
        String str = "单位信息为空";
        if (companyList != null) {
            logger.debug("单位信息获取成功！");
            response.setMessage("单位信息获取成功！");
            response.setData(companyList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(str);
            response.setMessage(str);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 增加单位信息.
     *
     * @param request 单位实体返回
     * @param headers headers对象
     * @return true or false
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addCompany(CompanyRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        Company company = request.getCompany();
        //将空格替换为nbsp
        if (request.getCompany() == null) {
            logDebugAndSetMessage("参数为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isNullOrEmpty(request.getCompany().getCompanyName())) {
            logDebugAndSetMessage("单位名不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            //检验单位名重复
            Company searchResult = companyService.getCompanyByCompanyName(request.getCompany().getCompanyName());
            if (searchResult != null) {
                logDebugAndSetMessage("单位名已存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }


        long companyId = System.currentTimeMillis(); //时间戳，即为单位Id
        request.getCompany().setCompanyId(companyId); //设置单位Id
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        request.getCompany().setCreator(Long.parseLong(userId)); //设置创建者

        boolean success = companyService.addCompany(company); //添加单位信息
        if (success) {
            logDebugAndSetMessage("单位信息添加成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("单位信息添加失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 更新单位信息.
     *
     * @param request 单位实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateCompany(CompanyRequest request,
                                              @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        Company company = request.getCompany();
        if (company == null || company.getCompanyId() == 0) {
            logDebugAndSetMessage("用户信息为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        if (StringUtils.isNullOrEmpty(company.getCompanyName())) {
            logDebugAndSetMessage("单位名不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            //检验单位名重复
            Company searchResult = companyService.getCompanyByCompanyName(company.getCompanyName());
            if (searchResult != null && searchResult.getCompanyId() != company.getCompanyId()) {
                logDebugAndSetMessage("单位名已存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }

        boolean success = companyService.updateCompany(company); //更新单位信息
        if (success) {
            logDebugAndSetMessage("单位信息修改成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("单位信息修改失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 删除单位.
     *
     * @param request 单位实体返回
     * @param headers headers对象
     * @return true or false
     */
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteCompany(CompanyRequest request,
                                              @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        Company company = request.getCompany(); //获取单位信息
        if (company == null || company.getCompanyId() == 0) {
            logDebugAndSetMessage("单位信息为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        logger.debug("开始验证单位下是否存在用户！");
        if (companyService.getUserCountByCompanyId(company.getCompanyId()) > 0) {
            logDebugAndSetMessage("该单位下存在用户！", response);
            response.setResultCode(ResultCode.ERROR);
        } else {
            boolean success = companyService.deleteCompanyById(company.getCompanyId()); //删除单位信息
            if (success) {
                logDebugAndSetMessage("单位信息删除成功！", response);
                response.setResultCode(ResultCode.SUCCESS);
            } else {
                logDebugAndSetMessage("单位信息删除失败！", response);
                response.setResultCode(ResultCode.ERROR);
            }
        }

        return response;
    }

    private void logDebugAndSetMessage(String strMessage, BaseResponse response) {
        logger.debug(strMessage);
        response.setMessage(strMessage);
    }
}
