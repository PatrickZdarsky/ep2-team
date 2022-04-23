/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MassCenter implements IPointObject {

    private final Vector3 position;
    private double mass;

    public MassCenter(IPointObject pointObject) {
        position = new Vector3(pointObject.getPosition());
        mass = pointObject.getMass();
    }

    public void merge(IPointObject pointObject) {
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

    @Override
    public String toString() {
        return "MassCenter{" +
                "position=" + position +
                ", mass=" + mass +
                '}';
    }
}
