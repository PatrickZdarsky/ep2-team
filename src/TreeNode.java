import java.util.Iterator;

public class TreeNode extends PointObject implements Iterable<Body> {

    public static final double BARNES_HUT_THRESHOLD = 1;

    PointObject[] entries;

    //Position and size of this sector
    private final Vector3 centerPosition;
    private final double length;

    public TreeNode(Vector3 centerPosition, double length) {
        this.centerPosition = centerPosition;
        this.length = length;
        entries = new PointObject[8];
    }

    /**
     * Adds a body to this node and updates the massCenter and mass accordingly
     * @param pointObject The pointObject to add
     */
    public void add(PointObject pointObject) {
        merge(pointObject);

        int quadrant = getOctant(pointObject);

        PointObject currentEntry = entries[quadrant];
        if (currentEntry == null) {
            entries[quadrant] = pointObject;
        } else if (currentEntry instanceof TreeNode){
            ((TreeNode) currentEntry).add(pointObject);
        } else {
            //Convert leaf to node
            var node = new TreeNode(getQuadrantCenter(quadrant), length/2);
            node.add(currentEntry);
            node.add(pointObject);

            entries[quadrant] = node;
        }
    }

    public void calculateForces(Body body) {
        // if sector matches barnes hut approximation criteria, use the sector approximation
        if (length / body.getPosition().distanceTo(position) < BARNES_HUT_THRESHOLD){
            body.addForce(body.gravitationalForce(this));
            return;
        }

        for (PointObject pointObject : entries) {
            if (pointObject == null)
                continue;

            if (pointObject instanceof Body) {
                body.addForce(((Body) pointObject).gravitationalForce(body));

            } else {
                ((TreeNode) pointObject).calculateForces(body);
            }
        }
    }

    byte getOctant(PointObject pointObject) {
        byte octant = 0;

        // 0 - 3 lower; 4-7 upper
        octant += checkValue(pointObject.getPosition().getZ(), centerPosition.getZ()) ? 4 : 0;
        octant += checkValue(pointObject.getPosition().getY(), centerPosition.getY()) ? 2 : 0;
        octant += checkValue(pointObject.getPosition().getX(), centerPosition.getX()) ? 1 : 0;

//        var z = checkValue(pointObject.getPosition().getZ(), centerPosition.getZ());
//        var y = checkValue(pointObject.getPosition().getY(), centerPosition.getY());
//        var x = checkValue(pointObject.getPosition().getX(), centerPosition.getX());

        return octant;
    }

    private Vector3 getQuadrantCenter(int quadrant) {
        return new Vector3(
                centerPosition.getX() + ((length / 4) * (quadrant % 2 == 0 ? -1 : 1)),
                centerPosition.getY() + ((length / 4) * (quadrant == 0 || quadrant == 1 || quadrant == 4 || quadrant == 5 ? -1 : 1)),
                centerPosition.getZ() + ((length / 4) * (quadrant > 3 ? 1 : -1)));
    }

    private boolean checkValue(double coordinate, double nodeCoordinate) {
        double diff = coordinate - nodeCoordinate;
//        if (Math.abs(diff) > length/2)
//            throw new IllegalStateException("Entry should not be in this node! diff: "+diff+" length: "+length/2);

        return diff > 0;
    }

    public double getLength() {
        return length;
    }

    public Vector3 getCenterPosition() {
        return centerPosition;
    }

    @Override
    public Iterator<Body> iterator() {
        return new TreeIterator(this);
    }
}
