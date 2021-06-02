package com.lew.server.controller;


import com.lew.server.pojo.Joblevel;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.service.IJoblevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@RestController
@RequestMapping("/system/basic/jobLevel")
@Api(tags = "职位等级")
public class JoblevelController {
    @Autowired
    private IJoblevelService joblevelService;
    @ApiOperation(value = "获取所有职位等级")
    @GetMapping("/all")
    public RespBean getAllJobLevel(){
        List<Joblevel> joblevels = joblevelService.list();
        return RespBean.success("获取成功",joblevels);
    }

    @ApiOperation(value = "添加职位等级")
    @PostMapping("/add")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if(joblevelService.save(joblevel)){
            return RespBean.success("保存成功");
        }
        return RespBean.error("保存失败");
    }

    @ApiOperation(value = "修改职位等级")
    @PutMapping("/update")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel){
        if(joblevelService.updateById(joblevel)){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    @ApiOperation(value = "删除职位等级")
    @DeleteMapping("/delete/{id}")
    public RespBean deleteJobLevel(@PathVariable Integer id){
        if(joblevelService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除职位")
    @DeleteMapping("/delete")
    public RespBean deleteJobLevels(Integer[] ids){
        if(joblevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }
}
