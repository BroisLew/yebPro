package com.lew.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.server.mapper.EmployeeMapper;
import com.lew.server.pojo.Employee;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.pojo.common.RespPageBean;
import com.lew.server.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public RespPageBean getEmployeeInfoByPage(Integer currentPage, Integer pageSize, Employee employee, LocalDate[] dateScope) {
        Page<Employee> page = new Page<>(currentPage,pageSize);
        IPage<Employee> employeeIPage = employeeMapper.getEmployeeInfoByPage(page,employee,dateScope);
        return new RespPageBean(employeeIPage.getTotal(),employeeIPage.getRecords());
    }

    @Override
    public RespBean getMaxWorkId() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        System.out.println(String.format("%08d",Integer.parseInt(maps.get(0).get("max(workID)").toString())+1));
        return RespBean.success(null,String.format("%08d",Integer.parseInt(maps.get(0).get("max(workID)").toString())+1));
    }

    @Override
    public RespBean addEmployee(Employee employee) {
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long until = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(until/365.00)));
        if(employeeMapper.insert(employee)==1){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }
}
