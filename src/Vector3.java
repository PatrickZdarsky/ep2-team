import codedraw.CodeDraw;

// This class represents vectors in a 3D vector space.
public class Vector3 implements Cloneable{

    public static final Vector3 ZERO_VECTOR = new Vector3();

    private double x;
    private double y;
    private double z;

    public Vector3() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public double getX() {
        return x;
    }

    void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    void setZ(double z) {
        this.z = z;
    }

    public Vector3 setValue(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3 selfPlus(Vector3 vector3) {
        x += vector3.x;
        y += vector3.y;
        z += vector3.z;

        return this;
    }

    public Vector3 selfTimes(double d) {
        x *= d;
        y *= d;
        z *= d;

        return this;
    }

    public Vector3 selfMinus(Vector3 vector3) {
        x -= vector3.x;
        y -= vector3.y;
        z -= vector3.z;

        return this;
    }


    // Returns the sum of this vector and vector 'v'.
    public Vector3 plus(Vector3 v) {
        return new Vector3(x+v.x, y+v.y, z+v.z);
    }

    // Returns the product of this vector and 'd'.
    public Vector3 times(double d) {
        return new Vector3(x*d, y*d, z*d);
    }

    // Returns the sum of this vector and -1*v.
    public Vector3 minus(Vector3 v) {
        return plus(v.times(-1));
    }

    // Returns the Euclidean distance of this vector
    // to the specified vector 'v'.
    public double distanceTo(Vector3 v) {
        double dX = x - v.x;
        double dY = y - v.y;
        double dZ = z - v.z;

        return Math.sqrt(dX*dX + dY*dY + dZ*dZ);
    }

    // Returns the length (norm) of this vector.
    public double length() {
        return distanceTo(ZERO_VECTOR);
    }

    // Normalizes this vector: changes the length of this vector such that it becomes 1.
    // The direction and orientation of the vector is not affected.
    public Vector3 normalize() {
        double length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }

    public Vector3 clone() {
        return new Vector3(this);
    }

    public void drawAsFilledCircle(CodeDraw cd, double radius) {

        double x = cd.getWidth() * (this.x + Simulation.GALAXY_SIZE / 2) / Simulation.GALAXY_SIZE;
        double y = cd.getWidth() * (this.y + Simulation.GALAXY_SIZE / 2) / Simulation.GALAXY_SIZE;
        radius = cd.getWidth() * radius / Simulation.GALAXY_SIZE;
        cd.fillCircle(x, y, Math.max(radius, 1.5));
    }


    // Returns the coordinates of this vector in brackets as a string
    // in the form "[x,y,z]", e.g., "[1.48E11,0.0,0.0]".
    public String toString() {
        return "["+x+","+y+","+z+"]";
    }

}

