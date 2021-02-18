package com.snails.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorConectionWatcher {

    private String IP = "127.0.0.1:2181";
    CuratorFramework curatorFramework = null;

    @Before
    public void brfore() {
        RetryPolicy exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        curatorFramework = CuratorFrameworkFactory
                                .builder()
                                //IP地址端口号
                                .connectString(IP)
                                //会话超时时间
                                .sessionTimeoutMs(5000)
                                //重连机制
                                .retryPolicy(exponentialBackoffRetry)
                                //构建连接对象
                                .build();

        curatorFramework.start();
    }

    @After
    public void after() {
        curatorFramework.close();
    }

    //watcher
    //监视子节点的变化
}
