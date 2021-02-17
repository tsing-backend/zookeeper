package com.snails.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZkConnectionWatcher implements Watcher {

    //计数器对象
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    //链接对象
    static ZooKeeper zookeeper;

    @Override
    public void process(WatchedEvent event) {

        try {
            //事件类型
            if(event.getType() == Event.EventType.None) {
                if(event.getState() == Event.KeeperState.SyncConnected ) {
                    System.out.println("创建连接成功"); //当连接到zookeeper服务器时，会执行这一块的代码块。
                    countDownLatch.countDown();
                } else if (event.getState() == Event.KeeperState.Disconnected) {
                    System.out.println("断开连接");    //  当和zookeeper服务器断开连接时，会执行这一块代码。
                                                      // 当网络重新连接成功连接成功之后，并且在sessionTimeOut设置的范围之内，会自动重新连接到服务器上。
                } else if (event.getState() == Event.KeeperState.Expired) {
                    System.out.println("会话超时");  // 当网络时间超过sessionTimeOut设置的时长时，会执行这一块代码。
                    zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZkConnectionWatcher()); //网络超时一般设置重新连接
                } else if (event.getState() == Event.KeeperState.AuthFailed) {
                    System.out.println("认证失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZkConnectionWatcher());

            //通过授权模式获取节点数据
            zookeeper.addAuthInfo("digest", "woniu:123456".getBytes());
            byte[] data = zookeeper.getData("/woniu", false, null);
            System.out.println(new String(data));

            countDownLatch.await();

            System.out.println(zookeeper.getSessionId());

            Thread.sleep(50000);

            zookeeper.close();

            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
