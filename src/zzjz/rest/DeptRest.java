package zzjz.rest;

import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Company;
import zzjz.bean.Dept;
import zzjz.bean.DeptRequest;
import zzjz.bean.ResultCode;
import zzjz.service.CompanyService;
import zzjz.service.DeptService;

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
 * @author caixiaolong
 * @version 2016/6/6 11:28
 * @ClassName: DeptRest
 * @Description: 部门相关操作Rest接口类
 */
@Component
@Path("/dept")
public class DeptRest {
    private static Logger logger = Logger.getLogger(DeptRest.class);

    @Autowired
    private DeptService deptService;

    @Autowired
    private CompanyService companyService;

    @Context
    ServletContext context;

    /**
     * 获取部门信息列表.
     *
     * @param headers headers对象
     * @return 部门信息列表
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Dept> getDeptList(@Context HttpHeaders headers) {
        BaseResponse<Dept> response = new BaseResponse<Dept>();
        List<Dept> deptList = deptService.getDeptList(); //获取部门列表
        String str = "部门信息为空!";
        if (deptList != null) {
            logger.debug("部门信息获取成功！");
            response.setMessage("部门信息获取成功！");
            response.setData(deptList);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logger.debug(str);
            response.setMessage(str);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 添加部门信息.
     *
     * @param request 部门实体返回
     * @param headers headers对象
     * @return true or false
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addDept(DeptRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        Dept dept = request.getDept();
        if (dept == null) {
            logDebugAndSetMessage("参数为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }

        //验证单位Id
        long companyId = dept.getCompanyId();
        if (0 == companyId) {
            logDebugAndSetMessage("单位信息不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            //判断该单位是否存在
            Company searchResult = companyService.getCompanyByCompanyId(companyId);
            if (searchResult == null) {
                logDebugAndSetMessage("单位不存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }
        //验证parentId
        if (0 != dept.getParentId()) {
            //判断该部门是否存在
            Dept searchResult = deptService.getDeptByDeptId(dept.getParentId());
            if (searchResult == null) {
                logDebugAndSetMessage("部门不存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }

        if (StringUtils.isNullOrEmpty(dept.getDeptName())) {
            logDebugAndSetMessage("部门名不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            //检验部门名重复
            Dept searchResult = deptService.getDeptByDeptName(dept);
            if (searchResult != null) {
                logDebugAndSetMessage("部门名已存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }


        long deptId = System.currentTimeMillis(); //时间戳，即为部门Id
        dept.setDeptId(deptId); //设置部门Id
        // 获取restful的headers信息
        MultivaluedMap<String, String> headerParams = headers.getRequestHeaders();
        String userId = headerParams.getFirst("userId");
        dept.setCreator(Long.parseLong(userId)); //设置创建者
        boolean success = deptService.addDept(dept); //添加部门信息
        if (success) {
            logDebugAndSetMessage("部门信息添加成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("部门信息添加失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 更新部门信息.
     *
     * @param request 部门实体返回
     * @param headers headers对象
     * @return true or false
     */
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateDept(DeptRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        Dept dept = request.getDept();
        if (dept == null || dept.getDeptId() == 0) {
            logDebugAndSetMessage("参数为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        //验证单位Id
        long companyId = dept.getCompanyId();
        if (0 == companyId) {
            logDebugAndSetMessage("单位信息不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            //判断该单位是否存在
            Company searchResult = companyService.getCompanyByCompanyId(companyId);
            if (searchResult == null) {
                logDebugAndSetMessage("单位不存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }
        if (StringUtils.isNullOrEmpty(dept.getDeptName())) {
            logDebugAndSetMessage("部门名不能为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        } else {
            //检验部门名重复
            Dept searchResult = deptService.getDeptByDeptName(dept);
            if (searchResult != null && searchResult.getDeptId() != dept.getDeptId()) {
                logDebugAndSetMessage("部门名已存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }


        //验证parentId
        long parentId = dept.getParentId();
        if (0 != parentId) {
            //判断该部门是否存在
            Dept searchResult = deptService.getDeptByDeptId(parentId);
            if (searchResult == null) {
                logDebugAndSetMessage("部门不存在！", response);
                response.setResultCode(ResultCode.BAD_REQUEST);
                return response;
            }
        }

        boolean success = deptService.updateDept(dept); //更新部门信息
        if (success) {
            logDebugAndSetMessage("部门信息修改成功！", response);
            response.setResultCode(ResultCode.SUCCESS);
        } else {
            logDebugAndSetMessage("部门信息修改失败！", response);
            response.setResultCode(ResultCode.ERROR);
        }
        return response;
    }

    /**
     * 删除部门.
     *
     * @param request 部门实体返回
     * @param headers headers对象
     * @return true or false
     */
    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteDept(DeptRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<String>();
        Dept dept = request.getDept(); //获取部门信息
        if (dept == null || dept.getDeptId() == 0) {
            logDebugAndSetMessage("部门信息为空！", response);
            response.setResultCode(ResultCode.BAD_REQUEST);
            return response;
        }
        logger.debug("开始验证部门下是否存在用户！");
        if (deptService.getUserCountByDeptId(dept.getDeptId()) > 0) {
            logDebugAndSetMessage("该部门下存在用户！", response);
            response.setResultCode(ResultCode.ERROR);
        } else {
            boolean success = deptService.deleteDeptById(dept.getDeptId()); //删除部门信息
            if (success) {
                logDebugAndSetMessage("部门信息删除成功！", response);
                response.setResultCode(ResultCode.SUCCESS);
            } else {
                logDebugAndSetMessage("部门信息删除失败！", response);
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
