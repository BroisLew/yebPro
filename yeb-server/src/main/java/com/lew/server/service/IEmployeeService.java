package com.lew.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.server.pojo.Employee;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.pojo.common.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface IEmployeeService extends IService<Employee> {
    RespPageBean getEmployeeInfoByPage(Integer currentPage, Integer pageSize, Employee employee, LocalDate[] dateScope);

    RespBean getMaxWorkId();

    RespBean addEmployee(Employee employee);

    List<Employee> getEmployee(Integer id);
}
