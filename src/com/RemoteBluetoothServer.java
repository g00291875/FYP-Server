package com;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class RemoteBluetoothServer {

    public static void main(String[] args) throws Exception {
        Thread waitThread = new Thread(new WaitThread());
        waitThread.start();
//            ExecutorService pool = Executors.newFixedThreadPool(3);
//
//            Set<Future<String>> set = new HashSet<>();
//
//           // Future<Void> future = executor.submit(new Callable<Void>() {
//                public Void call() throws Exception {
//                    return null;
//                }
//            });
        // future.get(); // wait for completion of testA.abc()
    }
}