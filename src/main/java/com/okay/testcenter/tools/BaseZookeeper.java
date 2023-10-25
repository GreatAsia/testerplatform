package com.okay.testcenter.tools;

import com.alibaba.fastjson.JSONObject;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhou
 * @date 2020/6/11
 */

public class BaseZookeeper implements Watcher {


    /**
     * 超时时间
     */
    private static final int SESSION_TIME_OUT = 10000;
    private static final Logger logger = LoggerFactory.getLogger(BaseZookeeper.class);
    private ZooKeeper zookeeper;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private List<String> listServer = new ArrayList<>();
    List<String> outServices = new ArrayList<>();
    List<String> listPath = new ArrayList<>();



    private BaseZookeeper(){}
    private static class ZookeeperFactory{
        public final static  BaseZookeeper instance = new BaseZookeeper();
    }
    public static BaseZookeeper getInstance(){

        return ZookeeperFactory.instance;
    }


//    public static void main(String[] args) {
//        try {
//            BaseZookeeper baseZookeeper =  BaseZookeeper.getInstance();
//            baseZookeeper.connectZookeeper("10.10.0.195:2181");
//            List<String> list = baseZookeeper.findContent("mysql-dev.wf","/xdfapp");
//            logger.info("服务列表==" + JSONObject.toJSONString(list));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    @Override
    public void process(WatchedEvent event) {

        if (event.getState() == KeeperState.SyncConnected) {
            logger.info("Watch received event");
            countDownLatch.countDown();
        }
    }

    /**
     * 连接zookeeper
     *
     * @param host
     * @throws Exception
     */
    public void connectZookeeper(String host) throws Exception {
        zookeeper = new ZooKeeper(host, SESSION_TIME_OUT, this);
        countDownLatch.await();
        logger.info("zookeeper connection success");
    }


    /**
     *     * 创建节点
     *     * @param path
     *     * @param data
     *     * @throws Exception
     *    
     */
    public String createNode(String path, String data) throws Exception {
        return this.zookeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     *     * 获取路径下所有子节点
     *     * @param path
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> children = zookeeper.getChildren(path, false);
        return children;
    }

    /**
     *     * 获取节点上面的数据
     *     * @param path  路径
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public String getData(String path) throws KeeperException, InterruptedException {
        byte[] data = zookeeper.getData(path, false, null);
        if (data == null) {
            return "";
        }
        return new String(data);
    }

    /**
     *     * 设置节点信息
     *     * @param path  路径
     *     * @param data  数据
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public Stat setData(String path, String data) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.setData(path, data.getBytes(), -1);
        return stat;
    }

    /**
     *     * 删除节点
     *     * @param path
     *     * @throws InterruptedException
     *     * @throws KeeperException
     *    
     */
    public void deleteNode(String path) throws InterruptedException, KeeperException {
        zookeeper.delete(path, -1);
    }

    /**
     *     * 获取创建时间
     *     * @param path
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public String getCTime(String path) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.exists(path, false);
        return String.valueOf(stat.getCtime());
    }

    /**
     *     * 获取某个路径下孩子的数量
     *     * @param path
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public Integer getChildrenNum(String path) throws KeeperException, InterruptedException {
        int childenNum = zookeeper.getChildren(path, false).size();
        return childenNum;
    }

    /**
     *     * 关闭连接
     *     * @throws InterruptedException
     *    
     */
    public void closeConnection() throws InterruptedException {
        if (zookeeper != null) {
            zookeeper.close();
        }
        logger.info("close zk connect");
    }

    /**
     * 查找内容
     * @param content
     * @param path
     * @throws Exception
     */
    public  List<String> findContent(String content, String path) throws Exception {
        listServer.clear();
        listPath.clear();

        ls(content,path);
        return listServer;
    }

    /**
     * 列出指定path下所有子节点
     */
    public void ls(String content, String path) throws Exception {
        setOutServices();
        List<String> list = getChildren(path);
        //判断是否有子节点
        if (list.isEmpty() || list == null) {

            //获取path目录节点的配置数据
            String value = getData(path);
            if (value.contains(content)) {
                String serverName = path.split("/")[2];
                if (listServer == null) {
                    listServer.add(serverName);
                } else {
                    if (!listServer.contains(serverName) && !outServices.contains(serverName)) {
                        listServer.add(serverName);
                        listPath.add(path);
                    }
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
                ls(content, path + s);

            } else {
                ls(content,path + "/" + s);
            }
        }

    }


    public void setOutServices(){
        //不用查找的服务
        outServices.add("family-svr");
        outServices.add("jmeter-web");
        outServices.add("recommend-web");
        outServices.add("gru-stat");
        outServices.add("euler-web");
        outServices.add("resources-task");
        outServices.add("studentpad-middle");
        outServices.add("exercise-task");
        outServices.add("okay-paycenter-admin");
        outServices.add("qa-webplatform");
        outServices.add("webplatform");
        outServices.add("lessontask-svr");
    }
}
