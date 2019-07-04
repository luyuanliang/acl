package a;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by luyl on 17-9-18.
 */
@Getter
@Setter
public class MyThread extends Thread {

    private String threadname;

    public static int count = 0;


    @Override
    public void run() {
//        Son son = new Son();
//        son.start();
//        try {
//            //son.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            Thread.sleep(2000L);
        }catch (Exception e){

        }

        for (int i = 0; i < 10; i++) {
            System.out.println(threadname + count++);
        }

    }
}
