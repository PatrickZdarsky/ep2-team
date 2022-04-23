import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // one light year
    public static final double LY = 9.461e15; // meters

    public static final double SUN_MASS = 1.989e30; // kilograms
    public static final double SUN_RADIUS = 696340e3; // meters

    // set some system parameters
    public static final double GALAXY_SIZE = 2 * AU; // the length of the galaxy
    public static final int NUMBER_OF_BODIES = 100_000;
    public static final double OVERALL_SYSTEM_MASS = 20 * SUN_MASS; // kilograms


    public static Octree octree;

    public static void main(String[] args) {
        octree = new OverkillOctTree(GALAXY_SIZE, new Vector3());

        setup();
        int times = 100;
        double sum = 0;
        for (int i = 0; i < times; i++) {
            double time = setup();
            sum += time;
            System.out.println(i+": "+time+"ms");
        }

        System.out.println("Avg: "+(sum/times)+"ms");

        if (octree instanceof OverkillOctTree)
            ((OverkillOctTree) octree).shutdown();
    }

    private static double setup() {
        List<Body> bodies = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < NUMBER_OF_BODIES; i++) {
            Body body = new Body(Math.abs(random.nextGaussian()) * Simulation.OVERALL_SYSTEM_MASS / Simulation.NUMBER_OF_BODIES, // kg
                    new Vector3(0.2 * random.nextGaussian() * (GALAXY_SIZE/2), 0.2 * random.nextGaussian() * (GALAXY_SIZE/2), 0.2 * random.nextGaussian() * (GALAXY_SIZE/2)),
                    new Vector3(0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3, 0 + random.nextGaussian() * 5e3));

            if (Math.abs(body.getPosition().getX()) > (GALAXY_SIZE/2) || Math.abs(body.getPosition().getY()) > (GALAXY_SIZE/2) || Math.abs(body.getPosition().getZ()) > (GALAXY_SIZE/2)) {
               // System.out.println("Invalid position!");
                i--;
                continue;
            }

            bodies.add(body);
        }

        octree.clear();

//        System.out.println(bodies.get(0).gravitationalForce(bodies.get(1)));
//
//        if (true)
//            return 0;

        long time = System.nanoTime();

        for (Body body : bodies) {
            octree.add(body);
        }

        if (octree instanceof OverkillOctTree)
            ((OverkillOctTree) octree).awaitAll();


        int count = 0;
        for (Body body : octree) {
            count++;
            body.setAppliedForce(octree.calculateForces(body));
        }
        System.out.println(count);


        return (System.nanoTime() - time)/1000000.0;
    }

}
