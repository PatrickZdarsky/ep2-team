import java.util.Iterator;

public class Octree implements Iterable<Body>{

    protected TreeNode root;

    //Welcome to the most useless class in this project

    public Octree(double length, Vector3 center) {
        root = new TreeNode(center, length);
    }

    public void add(Body body) {
        root.add(body);
    }

    public void calculateForces(Body body) {
        root.calculateForces(body);
    }

    public void advanceSimulation() {
        for (Body body : this) {
            calculateForces(body);
            //System.out.println("Calculated forces for "+body);
        }

//        for (Body body : this)
//            body.move();
    }

    public void clear() {
        root = new TreeNode(root.getCenterPosition(), root.getLength());
    }

    @Override
    public Iterator<Body> iterator() {
        return root.iterator();
    }
}
