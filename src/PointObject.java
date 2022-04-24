/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class PointObject {

    protected Vector3 position;

    protected double mass;

    public PointObject(Vector3 position, double mass) {
        this.position = position;
        this.mass = mass;
    }

    public PointObject() {
        position = new Vector3();
        mass = 0;
    }

    public void merge(PointObject pointObject) {
        double massSum = mass + pointObject.getMass();
        position.setX((mass*position.getX() + pointObject.getMass()*pointObject.getPosition().getX()) / massSum);
        position.setY((mass*position.getY() + pointObject.getMass()*pointObject.getPosition().getY()) / massSum);
        position.setZ((mass*position.getZ() + pointObject.getMass()*pointObject.getPosition().getZ()) / massSum);

        mass += pointObject.getMass();
    }

    public Vector3 getPosition() {
        return position;
    }

    public double getMass() {
        return mass;
    }
}
