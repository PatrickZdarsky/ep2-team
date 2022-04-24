import codedraw.CodeDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.StreamSupport;

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
    public static final int NUMBER_OF_BODIES = 10_000;
    public static final double OVERALL_SYSTEM_MASS = 10000 * SUN_MASS; // kilograms


    public static Octree octree;

    public static void main(String[] args) {
        octree = new Octree(GALAXY_SIZE, new Vector3());

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

            octree.add(body);
        }

        CodeDraw cd = new CodeDraw(1000, 1000);

        long nano = System.nanoTime();
        int count = 0;
        while(true) {


            octree.advanceSimulation();
            octree.rebuildTree();
            //System.out.println((System.nanoTime()-nano)/1000000+"ms");

            count++;

            if (count > 5) {
                count = 0;

                draw(cd);
                System.out.println((System.nanoTime()-nano)/1000000+"ms");
                nano = System.nanoTime();
            }
        }
    }

    private static void draw(CodeDraw cd) {
        // clear codedraw
        cd.clear(Color.BLACK);

        StreamSupport.stream(octree.spliterator(), false).forEach(body -> body.draw(cd));

        // show updated positions
        cd.show();
    }
}
