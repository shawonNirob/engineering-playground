//package org.build.webserver;
//
//import java.util.concurrent.*;
//
//public class ThreadPoolWebServer {
//
//    private static final String WEB_ROOT = "www";
//    private static final int PORT = 8099;
//
//    private static final int CORE_POOL_SIZE = Math.min(2, Runtime.getRuntime().availableProcessors());
//    private static final int MAX_POOL_SIZEC = 200;
//    private static final int QUEUE_CAPACITY = 1000;
//    private static final long KEEP_ALIVE_SECONDS = 60L;
//
//    public static void main(String[] args) {
//        BlockingDeque<Runnable> workQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
//
//        RejectedExecutionHandler rejectionHandler = (r, executor) ->{
//            if(r instanceof ClientHandler){
//                ((ClientHandler), r).sendServiceUnavailable();
//            }
//        };
//
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                CORE_POOL_SIZE,
//                MAX_POOL_SIZEC,
//                KEEP_ALIVE_SECONDS,
//                TimeUnit.SECONDS,
//                workQueue,
//                new ThreadPoolExecutor.AbortPolicy()
//        );
//        executor.setRejectedExecutionHandler(rejectionHandler);
//
//
//
//    }
//
//    static class ClientHandler implements Runnable{
//
//        public void sendServiceUnavailable(){
//
//        }
//
//        @Override
//        public void run() {
//
//        }
//
//    }
//}
