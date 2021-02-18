package com.snails.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CuratorConectionExists {

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
                                .namespace("get") //
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
    public void exists01() throws Exception {
        Stat stat = curatorFramework
                .checkExists()
                .forPath("/node");

        System.out.println(stat.getVersion());
    }

}
