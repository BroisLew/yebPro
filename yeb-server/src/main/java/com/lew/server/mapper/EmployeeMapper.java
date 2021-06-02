package com.lew.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lew.server.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    IPage<Employee> getEmployeeInfoByPage(Page<Employee> page,
                                          @Param("employee") Employee employee,
                                          @Param("dateScope") LocalDate[] dateScope);

    List<Employee> getEmployee(Integer id);
}
