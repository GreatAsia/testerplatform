package com.okay.testcenter.controller.dubbo;


import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.service.dubbo.DubboCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * asia
 */

@Api(description = "Dubbo运行接口")
@RequestMapping(value = "/dubbo")
@Controller
public class DubboRunController {



    @Resource
    DubboCaseService dubboCaseService;





    @ApiOperation(value = "运行Dubbo接口", notes = "通过ID运行Dubbo接口")
    @PostMapping(value = "/runById")
    @ResponseBody
    @Transactional()
    public Object runDubboById(@Validated @RequestBody JSONObject request) {
        return dubboCaseService.runDubboById(request);

    }

    @ApiOperation(value = "通过模块运行Dubbo接口", notes = "通过模块运行Dubbo接口")
    @PostMapping(value = "/runByModel")
    @ResponseBody
    public RetResult<Object> dubboRunWithModel(@Validated @RequestBody JSONObject request) {

        dubboCaseService.runDubboByModule(request);
            return RetResponse.makeOKRsp();
    }





}
