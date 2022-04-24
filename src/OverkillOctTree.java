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

    private final Phaser insertionPhaser;

    public OverkillOctTree(double length, Vector3 center) {
        super(length, center);

        quadrantExecutors = new ThreadPoolExecutor[4];
        for (int i = 0; i < quadrantExecutors.length; i++) {
            quadrantExecutors[i] = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
        }
        insertionSignal = new ZeroFiringSignal();
        insertionPhaser = new Phaser();

        simulationExecutor = Executors.newWorkStealingPool(4);
        simulationSignal = new ZeroFiringSignal();
    }

    @Override
    public boolean add(Body body) {
        byte octant = root.getOctant(body);
        insertionPhaser.register();

        quadrantExecutors[octant%4].execute(() -> {
            insertionPhaser.arriveAndAwaitAdvance();
            super.add(body);
            insertionPhaser.arriveAndDeregister();
        });

        return true;
    }

    @Override
    public void advanceSimulation() {
        for (Body body : this) {
            simulationExecutor.execute(() -> {
                simulationSignal.acquire();

                calculateForces(body);

                simulationSignal.release();
            });
        }

        simulationSignal.waitUntilZero();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Body body : this)
            simulationExecutor.execute(() -> {
                simulationSignal.acquire();

                body.move();

                simulationSignal.release();
            });
        simulationSignal.waitUntilZero();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void awaitAllInsertions() {
        //insertionSignal.waitUntilZero();
        insertionPhaser.awaitAdvance(insertionPhaser.arrive());
    }

    public void shutdown() {
        for (ThreadPoolExecutor executorService : quadrantExecutors) {
            executorService.shutdown();
        }
    }
}
