package com.lew.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface DepartmentMapper extends BaseMapper<Department> {
    List<Department> getDepartments(int parentId);

    void addDepartment(Department department);

    void deleteDepartment(Department department);
}
