package map;


import galaxy.Body;
import galaxy.IPointObject;
import galaxy.MassCenter;
import galaxy.Vector3;

import java.util.ArrayList;
import java.util.List;

public class MapNode implements IPointObject {

    private List<Body> bodies;

    private MassCenter massCenter;

    public MapNode() {
        bodies = new ArrayList<>(8);
    }

    /**
     * Adds a body to this node and updates the massCenter and mass accordingly
     * @param body The body to add
     */
    public void add(Body body) {
        //Calculate massCenter
        massCenter.merge(body);


        //Todo
    }

    @Override
    public Vector3 getPosition() {
        return massCenter.getPosition();
    }

    @Override
    public double getMass() {
        return massCenter.getMass();
    }
}
