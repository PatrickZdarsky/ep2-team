package galaxy;

// This class represents celestial bodies like stars, planets, asteroids, etc..
public class Body implements IPointObject {

    private double mass;
    private Vector3 massCenter; // position of the mass center.
    private Vector3 currentMovement;

    public Body(double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.mass = mass;
        this.massCenter = massCenter;
        this.currentMovement = currentMovement;
    }

    public double getMass() {
        return mass;
    }

    public Vector3 getPosition() {
        return massCenter;
    }

    // Returns the distance between the mass centers of this body and the specified body 'b'.
    public double distanceTo(Body b) {
        return massCenter.distanceTo(b.massCenter);
    }



    // Moves this body to a new position, according to the specified force vector 'force' exerted
    // on it, and updates the current movement accordingly.
    // (Movement depends on the mass of this body, its current movement and the exerted force.)
    // Hint: see simulation loop in Simulation.java to find out how this is done.
    public void move(Vector3 force) {
        Vector3 newPosition = currentMovement.plus(
                    massCenter.plus(force.times(1 / mass)));
                        // F = m*a -> a = F/m

        // new minus old position.
        Vector3 newMovement = newPosition.minus(massCenter);

        // update body state
        massCenter = newPosition;
        currentMovement = newMovement;
    }

    // Returns the approximate radius of this body.
    // (It is assumed that the radius r is related to the mass m of the body by r = m ^ 0.5,
    // where m and r measured in solar units.)
    public double radius() {
        return SpaceDraw.massToRadius(mass);
    }

    // Returns a new body that is formed by the collision of this body and 'b'. The impulse
    // of the returned body is the sum of the impulses of 'this' and 'b'.
    public Body merge(Body b) {
        double mass = this.mass + b.mass;
        Vector3 massCenter = b.massCenter.times(b.mass)
                .plus(this.massCenter.times(this.mass))
                .times(1/mass);
        Vector3 movement = b.currentMovement.times(b.mass)
                .plus(this.currentMovement.times(this.mass))
                .times(1.0/mass);

        return new Body(mass, massCenter, movement);
    }

    // Returns a string with the information about this body including
    // mass, position (mass center) and current movement. Example:
    // "5.972E24 kg, position: [1.48E11,0.0,0.0] m, movement: [0.0,29290.0,0.0] m/s."
    public String toString() {
        return String.format("%e kg, position: %s m, movement: %s m/s", mass, massCenter, currentMovement);
    }

}

