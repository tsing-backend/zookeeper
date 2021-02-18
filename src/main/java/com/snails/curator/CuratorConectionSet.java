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

public class CuratorConectionSet {

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
                                .namespace("set") //
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
    public void set01() throws Exception {
        curatorFramework
                .setData()
                .forPath("/nodo", "set-node-update".getBytes());
        System.out.println("end");
    }
}
