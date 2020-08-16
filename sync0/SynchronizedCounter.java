public class SynchronizedCounter {
    private int c = 0;

    /* constructors can't be synchronized */

    /* happens-before relationship */
    public synchronized void increment() {
        c++;
    }

    /* happens-before relationship */
    public synchronized void decrement() {
        c--;
    }

    /* happens-before relationship */
    public synchronized int value() {
        return c;
    }
}