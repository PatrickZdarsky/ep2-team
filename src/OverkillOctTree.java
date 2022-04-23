import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class OverkillOctTree extends Octree {

    private ThreadPoolExecutor[] executorServices;

    public OverkillOctTree(double length, Vector3 center) {
        super(length, center);

        executorServices = new ThreadPoolExecutor[4];
        for (int i = 0; i < executorServices.length; i++) {
            executorServices[i] = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
        }
    }

    @Override
    public void add(Body body) {
        byte quadrant = root.getQuadrant(body);

        executorServices[quadrant%4].execute(() -> super.add(body));
    }

    public void awaitAll() {
        for (ThreadPoolExecutor executorService : executorServices) {
            while (!executorService.getQueue().isEmpty()) {

            }
        }
    }

    public void shutdown() {
        for (ThreadPoolExecutor executorService : executorServices) {
            executorService.shutdown();
        }
    }
}
