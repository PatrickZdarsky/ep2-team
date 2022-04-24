/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ZeroFiringSignal {

    private int count = 0;

    public synchronized void acquire() {
        count++;
    }

    public synchronized void release() {
        count--;

        if (count == 0)
            notifyAll();
    }

    public synchronized void waitUntilZero() {
        while (count > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
