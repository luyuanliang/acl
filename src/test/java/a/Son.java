package a;

/**
 * Created by luyl on 17-9-18.
 */
public class Son extends Thread {
    @Override
    public void run() {
        try {
           // Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("son");
    }

    public static void main(String arg[]){
        int a = 021;
        System.out.println(a);
    }
}
