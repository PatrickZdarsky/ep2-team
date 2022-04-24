import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class OverkillOctTree extends Octree {

    private final ThreadPoolExecutor[] quadrantExecutors;

    public OverkillOctTree(double length, Vector3 center) {
        super(length, center);

        quadrantExecutors = new ThreadPoolExecutor[4];
        for (int i = 0; i < quadrantExecutors.length; i++) {
            quadrantExecutors[i] = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
        }
    }

    @Override
    public void add(Body body) {
        byte octant = root.getOctant(body);

        quadrantExecutors[octant%4].execute(() -> super.add(body));
    }

    public void awaitAllInsertions() {
        for (ThreadPoolExecutor executorService : quadrantExecutors) {
            while (!executorService.getQueue().isEmpty()) {

            }
        }
    }

    public void shutdown() {
        for (ThreadPoolExecutor executorService : quadrantExecutors) {
            executorService.shutdown();
        }
    }
}
