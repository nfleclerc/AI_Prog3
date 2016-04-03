package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;

/**
 * This class represents a resource strips unit.
 */
public abstract class Resource extends StripsUnit {

    protected int id;
    protected int amountRemaining;
    protected Position position;
    protected ResourceType type;

    /**
     * Construct a resource from a ResourceView object.
     * @param resource The resource view to represent
     */
    public Resource(ResourceNode.ResourceView resource){
        id = resource.getID();
        amountRemaining = resource.getAmountRemaining();
        position = new Position(resource.getXPosition(), resource.getYPosition());
    }

    /**
     * Construct a clone of the provided resource.
     * @param resource The resource to clone
     */
    public Resource(Resource resource){
        id = resource.id;
        amountRemaining = resource.amountRemaining;
        position = new Position(resource.position);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Resource
                && id == ((Resource) o).id
                && position.equals(((Resource) o).position)
                && type.equals(((Resource)o).getType())
                && amountRemaining == ((Resource)o).amountRemaining;
    }

    @Override
    public int hashCode(){
        return id + position.hashCode() + type.hashCode() + amountRemaining;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getID() {
        return id;
    }

    /**
     * Get the amount of this resource that is remaining.
     * @return The amount remaining
     */
    public int getAmountRemaining() {
        return amountRemaining;
    }

    /**
     * Get the resource type of this resource.
     * @return The type of resource
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Deplete this resource by the specified amount.
     * @param harvestAmount The amount of this resource to harvest
     */
    public void harvestAmount(int harvestAmount) {
        amountRemaining -= harvestAmount;
    }

}
