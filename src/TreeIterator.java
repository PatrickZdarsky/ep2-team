import java.util.Iterator;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class TreeIterator implements Iterator<Body>{

    private final TreeNode node;
    Iterator<Body> currentIterator;

    private byte index = 0;
    private Body nextBody;

    public TreeIterator(TreeNode node) {
        this.node = node;

        nextBody = retrieveNextBody();
    }

    private Body retrieveNextBody() {

        //Check the current iterator, if we have one
        if (currentIterator != null) {
            if (currentIterator.hasNext()) {
                return currentIterator.next();
            } else {
                currentIterator = null;
            }
        }

        while(index < 8){
            //Get PointObject at this index, and increase the index
            IPointObject current = node.entries[index++];

            if (current == null) {
                //Empty, skip
                continue;
            }

            if (current instanceof Body) {
                return (Body) current;
            }

            //Current is another node => Get that iterator
            currentIterator = ((TreeNode) current).iterator();

            //Because of the nature of this implementation, this node MUST have at least 2 bodies attached to it,
            // so we can just get one without further checks
            return currentIterator.next();
        }

        return null;
    }

    @Override
    public boolean hasNext() {
        return nextBody != null;
    }

    @Override
    public Body next() {
        Body currentNext = nextBody;
        nextBody = retrieveNextBody();

        return currentNext;
    }
}
