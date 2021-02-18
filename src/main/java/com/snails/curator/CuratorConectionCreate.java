package com.snails.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CuratorConectionCreate {

    private String IP = "127.0.0.1:2181";
    CuratorFramework curatorFramework = null;

    @Before
    public void brfore() {
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        curatorFramework = CuratorFrameworkFactory.builder()
                                //IP地址端口号
                                .connectString(IP)
                                //会话超时时间
                                .sessionTimeoutMs(5000)
                                //重连机制
                                .retryPolicy(exponentialBackoffRetry)
                                //命名空间
                                .namespace("create") //
                                //构建连接对象
                                .build();

        curatorFramework.start();

        System.out.println(curatorFramework.isStarted());
    }

    @After
    public void after() {
        curatorFramework.close();
    }

    @Test
    public void create01() throws Exception {
        curatorFramework.create()
                //节点类型
                .withMode(CreateMode.PERSISTENT)
                //节点权限列表
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                //会加上命名空间指定的名称
                .forPath("/tsing", "测试数据".getBytes());
        System.out.println("end");
    }

    //自定义权限列表
    @Test
    public void create02() throws Exception {
        ArrayList<ACL> list = new ArrayList<>();

        Id id = new Id("ip", "127.0.0.1");
        list.add(new ACL(ZooDefs.Perms.ALL, id));
        curatorFramework
                .create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(list)
                .forPath("/tsing1", "tsing1".getBytes());

    }

    //递归创建节点
    @Test
    public void create03() throws Exception {
        curatorFramework
                .create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/tsing2/child1", "tsing2".getBytes());
    }
}
