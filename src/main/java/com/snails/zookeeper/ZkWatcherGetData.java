package com.snails.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkWatcherGetData {

    String Ip = "127.0.0.1:2181";
    ZooKeeper zooKeeper = null;

    @Before
    public void before() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        zooKeeper = new ZooKeeper(Ip, 60000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
                try {
                    //解决一次性的问题，就是每次节点的修改，不用重启客户端。
                    zooKeeper.exists("/woniu1",this);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("path= " + event.getPath());
                System.out.println("path= " + event.getType());
            }
        });
        countDownLatch.await();
    }

    @After
    public void after() throws InterruptedException {
        zooKeeper.close();
    }


    @Test
    public void watchderFunction01() throws KeeperException, InterruptedException {
        zooKeeper.getData("/woniu1", true, null);
        Thread.sleep(500000);
        System.out.println("end-----");

    }

    @Test
    public void watchderFunction02() throws KeeperException, InterruptedException {
        zooKeeper.getData("/woniu1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("自定义watcher");
                System.out.println("path= " + event.getPath());
                System.out.println("path= " + event.getType());
            }
        }, null);

        Thread.sleep(500000);
        System.out.println("end-----");

    }
}
