package com.lew.server.controller;


import com.lew.server.pojo.Department;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@RestController
@RequestMapping("/department")
@Api(tags = "部门操作")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "获取所有部门信息")
    @GetMapping("/get")
    public List<Department> getDepartments(){
        return departmentService.getDepartments();
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/add")
    public RespBean addDepartment(@RequestBody Department department){
        return departmentService.addDepartment(department);
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/delete/{id}")
    public RespBean deleteDepartment(@PathVariable("id") int id){
        return departmentService.deleteDepartment(id);
    }
}
