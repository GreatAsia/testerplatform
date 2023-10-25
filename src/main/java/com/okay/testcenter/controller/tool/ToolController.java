package com.okay.testcenter.controller.tool;


import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.tools.BaseZookeeper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 2019/9/18
 */
@Api(description = "工具接口")
@Controller
@RequestMapping(value = "/tool")
public class ToolController {

    private static Logger logger = LoggerFactory.getLogger(ToolController.class);



    @GetMapping(value = "/list")
    public String listTool() {

        return "tools/Server";
    }

    @ApiOperation(value = "获取ZK指定路径下的所有节点", notes = "获取ZK指定路径下的所有节点")
    @GetMapping(value = "/zk/list")
    @ResponseBody
    public RetResult zkListTool(String address, String path) {

        BaseZookeeper zk = BaseZookeeper.getInstance();
        List<String> list = new ArrayList<>();
        try {
            zk.connectZookeeper(address);
            list = zk.getChildren(path);

        } catch (Exception e) {
            logger.error("KeeperException==" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                zk.closeConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //判断是否有子节点
        if (list.isEmpty() || list == null) {
            return RetResponse.makeErrRsp("节点为空");
        }
        Map map = new HashMap(15);
        map.put("total",  list.size());
        map.put("list",list);
        return RetResponse.makeOKRsp(map);
    }

    @ApiOperation(value = "获取ZK指定内容的所有服务", notes = "获取ZK指定内容的所有服务")
    @GetMapping(value = "/zk/find/list")
    @ResponseBody
    public RetResult zkFindListTool(String address, String path, String findContent) throws Exception {
        BaseZookeeper zk = BaseZookeeper.getInstance();
        zk.connectZookeeper(address);
        List<String> listServer =  zk.findContent(findContent,path);
        Map map = new HashMap(2);
        map.put("total",  listServer.size());
        map.put("listServer",listServer);
        zk.closeConnection();
        return RetResponse.makeOKRsp(map);
    }



}
