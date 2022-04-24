import codedraw.CodeDraw;

// This class represents celestial bodies like stars, planets, asteroids, etc..
public class Body extends PointObject {

    private Vector3 currentMovement;
    private Vector3 appliedForce;

    public Body(double mass, Vector3 massCenter, Vector3 currentMovement) {
        super(massCenter, mass);
        this.currentMovement = currentMovement;
    }



    // Moves this body to a new position, according to the specified force vector 'force' exerted
    // on it, and updates the current movement accordingly.
    // (Movement depends on the mass of this body, its current movement and the exerted force.)
    // Hint: see simulation loop in Simulation.java to find out how this is done.
    public void move() {
        Vector3 newPosition = currentMovement.plus(
                position.plus(appliedForce == null ? Vector3.ZERO_VECTOR : appliedForce.times(1 / mass)));
                        // F = m*a -> a = F/m

        // new minus old position.
        Vector3 newMovement = newPosition.minus(position);

        // update body state
        position = newPosition;
        currentMovement = newMovement;
    }

    // Returns the approximate radius of this body.
    // (It is assumed that the radius r is related to the mass m of the body by r = m ^ 0.5,
    // where m and r measured in solar units.)
    public double radius() {
        return SpaceDraw.massToRadius(mass);
    }

    // Returns a vector representing the gravitational force exerted by 'b' on this body.
    // The gravitational Force F is calculated by F = G*(m1*m2)/(r*r), with m1 and m2 being the
    // masses of the objects interacting, r being the distance between the centers of the masses
    // and G being the gravitational constant.
    // Hint: see simulation loop in Simulation.java to find out how this is done.
    public Vector3 gravitationalForce(PointObject b) {
        Vector3 direction = b.getPosition().clone().selfMinus(position);
        double distance = direction.length();
        if (distance == 0)
            return direction.setValue(0, 0, 0); //re-use the vector and set its value to zero

        double force = Simulation.G * mass * b.getMass() / (distance * distance);

        return direction.normalize().selfTimes(force);
    }

    public void addForce(Vector3 force) {
        if (appliedForce == null)
            appliedForce = force;
        else
            appliedForce.selfPlus(force);
    }

    public void draw(CodeDraw cd) {
        cd.setColor(SpaceDraw.massToColor(this.mass));

        //cheap 3d illusion, nothing fancy
        double perspectiveMultiplicative = map(getPosition().getZ() / (Simulation.GALAXY_SIZE/2), 0, 2, 0, 5);
        position.drawAsFilledCircle(cd, perspectiveMultiplicative * SpaceDraw.massToRadius(this.mass));
    }

    // Returns a string with the information about this body including
    // mass, position (mass center) and current movement. Example:
    // "5.972E24 kg, position: [1.48E11,0.0,0.0] m, movement: [0.0,29290.0,0.0] m/s."
    public String toString() {
        return String.format("%e kg, position: %s m, movement: %s m/s, force: %s", mass, position, currentMovement, appliedForce);
    }

    private double map(double val, double min, double max, double newMin, double newMax) {
        return newMin + val * ((newMax-newMin) / (max-min));
    }
}

