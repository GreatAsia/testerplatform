package com.okay.testcenter;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.controller.tool.ToolController;
import com.okay.testcenter.tools.BaseZookeeper;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 2019/9/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GetZkList {


    private static Logger logger = LoggerFactory.getLogger(ToolController.class);
    private String findText = "mysql-dev.wf";
    private List<String> listServer = new ArrayList<>();
    List<String> outServices = new ArrayList<>();
    List<String> listPath = new ArrayList<>();


    @Test
    public void TestZKAll() throws Exception {
//        ls("/xdfapp");
        BaseZookeeper zk = BaseZookeeper.getInstance();
        zk.connectZookeeper("10.10.0.195:2181");
        ls(zk,"/xdfapp");
        System.out.println("服务列表==" + JSONObject.toJSONString(listServer));
        zk.closeConnection();

    }


    @Test
    public void testCreateNode() {
        BaseZookeeper zk = BaseZookeeper.getInstance();
        List<String> list = new ArrayList<>();
        try {
            zk.connectZookeeper("10.10.1.7");
            zk.createNode("/xdfapp/test", "v1.0");

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
    }


    /**
     * 列出指定path下所有孩子
     */
    public void ls(String path) throws Exception {
        System.out.println(path);
       ZooKeeper zk = new ZooKeeper("10.10.0.195:2181", 10000, null);
        List<String> list = zk.getChildren(path, null);
        //判断是否有子节点
        if (list.isEmpty() || list == null) {
            return;
        }

        System.out.println("[list]==" + list);

    }



    /**
     * 列出指定path下所有子节点
     */
    public void ls(BaseZookeeper zk, String path) throws Exception {


        List<String> list = zk.getChildren(path);
        //判断是否有子节点
        if (list.isEmpty() || list == null) {

            //获取path目录节点的配置数据
            String value = new String(zk.getData(path));
            logger.info("value==" + value);
            if (value.contains(findText)) {
                String serverName = path.split("/")[2];
                if (!listServer.contains(serverName) && !outServices.contains(serverName)) {
                    listServer.add(serverName);
                    listPath.add(path);
                }
            }
            return;
        }
        for (String s : list) {
            //判断是否为v1.0$这样的无效目录
            if (s.contains("$")) {
                continue;
            }
            //判断是否为根目录
            if (("/").equals(path)) {
                ls(zk, path + s);

            } else {

                ls(zk, path + "/" + s);
            }
        }

    }


    @Test
    public void testMap() {
        Map map = new HashMap();
        map.put("test", 1);
        System.out.println("value = " + map.get(1));
    }

}
