public class SleepRunnable implements Runnable {

    /* code block that a thread runs */
    public void run() {
        for (int i = 0; i < 4; i++) {
            System.out.println(String.format("msg %d", i));

            /* 
                > sleep for 1000 ms -> 1sec (not 100% percise)
                > gives other threads the chance to run
            */
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                /* it is standard practice for a thread to terminate on interuption */
                return;
            }

            /*
                > checking if there has been an asynchronous interuption
                > clears the state of the interuption flag. isInterrupted() doesn't clear state
            */
            if (Thread.interrupted()) {
                return;
            }
        }
    }

}