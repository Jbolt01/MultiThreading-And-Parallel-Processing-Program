import java.util.*;
import java.util.concurrent.locks.*;

public class ProjectTwo{

    static Set<Integer> set = new HashSet<Integer>();
    static Lock lock = new ReentrantLock(true);
    static int range = 1000;

    public static void main(String[] args){

        Thread t2 = new T2();
        Thread t1 = new T1();
        t1.start();
        t2.start();
    }

    public static class T1 extends Thread {
        public void run(){
            while (set.size() != range) {
                lock.lock();
                set.add((int) (Math.random() * range + 1));
                lock.unlock();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Thread 1 interrupted.");
                    break;
                }
            }
        }
    }

    public static class T2 extends Thread {
        public void run(){
            while (set.size() != range) {
                lock.lock();
                Iterator<?> iter = set.iterator();
                System.out.println("Elements in set: ");
                while (iter.hasNext()) {
                    System.out.print(iter.next() + " ");
                }
                System.out.println();
                lock.unlock();

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Thread 2 interrupted.");
                    break;
                }
            }
        }
    }

}
