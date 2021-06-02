package com.lew.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.server.pojo.Department;
import com.lew.server.pojo.common.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getDepartments();

    RespBean addDepartment(Department department);

    RespBean deleteDepartment(int id);
}
