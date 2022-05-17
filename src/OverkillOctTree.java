import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class OverkillOctTree extends Octree {

    private final ThreadPoolExecutor[] quadrantExecutors;
    private final ZeroFiringSignal insertionSignal;

    private final ExecutorService simulationExecutor;
    private final ZeroFiringSignal simulationSignal;

    public OverkillOctTree(double length, Vector3 center) {
        super(length, center);

        quadrantExecutors = new ThreadPoolExecutor[4];
        for (int i = 0; i < quadrantExecutors.length; i++) {
            quadrantExecutors[i] = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
        }
        insertionSignal = new ZeroFiringSignal();

        simulationExecutor = Executors.newWorkStealingPool(4);
        simulationSignal = new ZeroFiringSignal();
    }

    @Override
    public boolean add(Body body) {
        byte octant = root.getOctant(body);
        insertionSignal.acquire();

        quadrantExecutors[octant%4].execute(() -> {
            super.add(body);
            insertionSignal.release();
        });

        return true;
    }

    @Override
    public void rebuildTree() {
        super.rebuildTree();

        awaitAllInsertions();
    }

    //    @Override
//    public void advanceSimulation() {
//        for (Body body : this) {
//            simulationSignal.acquire();
//            simulationExecutor.execute(() -> {
//
//                calculateForces(body);
//
//                simulationSignal.release();
//            });
//        }
//        simulationSignal.waitUntilZero();
//
//        for (Body body : this) {
//            simulationSignal.acquire();
//            simulationExecutor.execute(() -> {
//                body.move();
//
//                simulationSignal.release();
//            });
//        }
//        simulationSignal.waitUntilZero();
//    }

    public void awaitAllInsertions() {
        insertionSignal.waitUntilZero();
    }

    public void shutdown() {
        for (ThreadPoolExecutor executorService : quadrantExecutors) {
            executorService.shutdown();
        }
    }
}
