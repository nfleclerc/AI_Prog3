package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;

/**
 * Created by nathaniel on 3/22/16.
 */
public abstract class Resource {

    protected int id;
    protected int amountRemaining;
    protected Position position;
    protected ResourceType type;

    public Resource(ResourceNode.ResourceView resource){
        id = resource.getID();
        amountRemaining = resource.getAmountRemaining();
        position = new Position(resource.getXPosition(), resource.getYPosition());
    }

    public Resource(Resource resource){
        id = resource.id;
        amountRemaining = resource.amountRemaining;
        position = new Position(resource.position);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Resource &&
                id == ((Resource) o).id &&
                position.equals(((Resource) o).position) &&
                type.equals(((Resource)o).getType()) &&
                amountRemaining == ((Resource)o).amountRemaining;
    }

    public Position getPosition() {
        return position;
    }

    public int getAmountRemaining() {
        return amountRemaining;
    }

    public ResourceType getType() {
        return type;
    }

    public void harvestAmount(int harvestAmount) {
        amountRemaining -= harvestAmount;
    }

    public int getID() {
        return id;
    }
}
