package com.lew.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.lew.server.pojo.*;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.pojo.common.RespPageBean;
import com.lew.server.service.*;
import com.sun.deploy.net.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@RestController
@RequestMapping("/employee")
@Api(tags = "员工操作")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IDepartmentService departmentService;
    @ApiOperation(value = "分页获取所有员工信息")
    @GetMapping("/list")
    public RespPageBean getEmployeeInfo(@RequestParam(defaultValue = "1")Integer currentPage,
                                        @RequestParam(defaultValue = "10")Integer pageSize,
                                        Employee employee,
                                        LocalDate[] dateScope){
        return employeeService.getEmployeeInfoByPage(currentPage,pageSize,employee,dateScope);
    }

    @GetMapping("/joblevelList")
    @ApiOperation("获取所有职称")
    public List<Joblevel> getJobLevel(){
        return joblevelService.list();
    }

    @GetMapping("/nations")
    @ApiOperation("获取所有名族")
    public List<Nation> getNations(){
        return nationService.list();
    }

    @GetMapping("/politicsStatus")
    @ApiOperation("获取所有政治面貌")
    public List<PoliticsStatus> getPoliticsStatus(){
        return politicsStatusService.list();
    }

    @GetMapping("/positions")
    @ApiOperation("获取所有职位")
    public List<Position> getPosition(){
        return positionService.list();
    }

    @GetMapping("/departments")
    @ApiOperation("获取所有部门")
    public List<Department> getDepartment(){
        return departmentService.getDepartments();
    }

    @GetMapping("/maxWorkID")
    @ApiOperation("获取工号")
    public RespBean getMaxWorkId(){
        return employeeService.getMaxWorkId();
    }

    @ApiOperation("添加员工")
    @PostMapping("/addEmp")
    public RespBean addEmployee(@RequestBody Employee employee){
        return employeeService.addEmployee(employee);
    }

    @ApiOperation("删除员工")
    @DeleteMapping("/delete/{id}")
    public RespBean deleteEmployee(@PathVariable("id") Integer id){
        if(employeeService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation("更新员工信息")
    @PutMapping("/updateEmployee")
    public RespBean updateEmployee(@RequestBody Employee employee){
        if(employeeService.updateById(employee)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "导出数据")
    @GetMapping(value = "/exports",produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> employees = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("员工表","员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, employees);
        ServletOutputStream stream = null;
        try {
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("content-disposition","attachment;filename="+
                    URLEncoder.encode("员工表.xls","UTF-8"));
            stream = response.getOutputStream();
            workbook.write(stream);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        List<Nation> nations = nationService.list();
        List<Joblevel> joblevels = joblevelService.list();
        try {
            List<Employee> employees = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            employees.forEach(employee -> {
                employee.setNationId(nations.get(nations.indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setJobLevelId(joblevels.get(joblevels.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
