package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;

/**
 * Created by nathaniel on 3/21/16.
 *
 */
public class GoldMine {

    private int id;
    private int amountRemaining;
    private Position position;
    private ResourceType type = ResourceType.GOLD;

    public GoldMine(ResourceNode.ResourceView resource){
        id = resource.getID();
        amountRemaining = resource.getAmountRemaining();
        position = new Position(resource.getXPosition(), resource.getYPosition());
    }

    public GoldMine(GoldMine goldMine){
        id = goldMine.id;
        amountRemaining = goldMine.amountRemaining;
        position = new Position(goldMine.position);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GoldMine &&
                id == ((GoldMine) o).id &&
                position.equals(((GoldMine) o).position);
    }

}
