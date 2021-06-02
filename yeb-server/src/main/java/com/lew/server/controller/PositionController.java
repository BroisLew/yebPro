package com.lew.server.controller;


import com.lew.server.pojo.Position;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.service.IPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
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
@RequestMapping("/system/basic/position")
@Api(tags = "职位管理")
public class PositionController {
    @Autowired
    private IPositionService positionService;
    @GetMapping("/getPosition")
    @ApiOperation(value = "获所有取职位信息")
    public RespBean getPosition(){
        List<Position> positions = positionService.list();
        return RespBean.success("查询成功",positions);
    }

    @PostMapping("/addPosition")
    @ApiOperation(value = "添加职位")
    public RespBean addPosition(@RequestBody Position position){
        if(positionService.save(position)){
            return RespBean.success("保存成功");
        }
        return RespBean.error("保存失败");
    }

    @PutMapping("/updatePosition")
    @ApiOperation(value = "修改职位")
    public RespBean updatePosition(@RequestBody Position position){
        if(positionService.updateById(position)){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除职位")
    public RespBean deletePosition(@PathVariable Integer id){
        if(positionService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @DeleteMapping("/")
    @ApiOperation(value = "批量删除职位")
    public RespBean deletePositions(Integer[] ids){
        if (positionService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
