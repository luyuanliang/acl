import a.MyThread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by luyl on 17-9-18.
 */
public class Test2 {

    public static void main(String[] arg) throws Exception {
        MyThread t1 = new MyThread();
        t1.setThreadname("T1");
        t1.start();
        t1.join(200L);
        System.out.println("aaaaa");
        Lock lock = new ReentrantLock();

    }

}
