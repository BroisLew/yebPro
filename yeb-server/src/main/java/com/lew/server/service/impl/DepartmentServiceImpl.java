package com.lew.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.server.mapper.DepartmentMapper;
import com.lew.server.pojo.Department;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getDepartments() {
        return departmentMapper.getDepartments(-1);
    }

    @Override
    public RespBean addDepartment(Department department) {
        department.setEnabled(true);
        departmentMapper.addDepartment(department);
        if(department.getResult()==1)
            return RespBean.success("添加成功");
        return RespBean.error("添加失败");
    }

    @Override
    public RespBean deleteDepartment(int id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDepartment(department);
        int result = department.getResult();
        if(result==-2)
            return RespBean.error("删除失败，该部门下还有子部门");
        if(result==-1)
            return RespBean.error("删除失败，该部门下还有员工");
        if(result==1)
            return RespBean.success("删除成功");
        return RespBean.error("删除失败");
    }
}
