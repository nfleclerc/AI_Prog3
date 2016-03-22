package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;

/**
 * Created by nathaniel on 3/21/16.
 *
 */
public class Forest {

    private int id;
    private int amountRemaining;
    private Position position;
    private ResourceType type = ResourceType.WOOD;


    public Forest(ResourceNode.ResourceView resource) {
        id = resource.getID();
        amountRemaining = resource.getAmountRemaining();
        position = new Position(resource.getXPosition(), resource.getYPosition());
    }

    public Forest(Forest forest) {
        id = forest.id;
        amountRemaining = forest.amountRemaining;
        position = new Position(forest.position);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Forest &&
                id == ((Forest) o).id &&
                position.equals(((Forest) o).position);
    }
}
