public class Main {

    public static void main(String args[]) {
        (new Thread(new HelloRunnable())).start();

        /* creating a Thread obj from our conc class */
        Thread t1 = new Thread(new HelloRunnable());
        Thread t2 = new Thread(new SleepRunnable());


        /* starting the 'run' code block */
        t1.start();
        t2.start();

        /*
        ! there is a .join() method, which makes threads wait. I can't figure it out yet 
         */
    }

}