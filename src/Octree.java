import java.util.Iterator;
import java.util.stream.StreamSupport;

public class Octree implements Iterable<Body>{

    protected TreeNode root;

    protected int size = 0;

    //Welcome to the most useless class in this project

    public Octree(double length, Vector3 center) {
        root = new TreeNode(center, length);
    }

    public boolean add(Body body) {
        if (Math.abs(body.getPosition().getX() - root.getCenterPosition().getX()) > root.getLength()/2
            || Math.abs(body.getPosition().getY() - root.getCenterPosition().getY()) > root.getLength()/2
            || Math.abs(body.getPosition().getZ() - root.getCenterPosition().getZ()) > root.getLength()/2)
            return false;


        root.add(body);
        size++;

        return true;
    }

    public void calculateForces(Body body) {
        root.calculateForces(body);
    }

    public void advanceSimulation() {
        StreamSupport.stream(this.spliterator(), true).forEach(this::calculateForces);
        StreamSupport.stream(this.spliterator(), true).forEach(Body::move);
    }

    public void rebuildTree() {
        var oldRoot = root;
        clear();

        StreamSupport.stream(oldRoot.spliterator(), true).forEach(this::add);
    }

    public void clear() {
        root = new TreeNode(root.getCenterPosition(), root.getLength());
    }

    @Override
    public Iterator<Body> iterator() {
        return root.iterator();
    }
}
