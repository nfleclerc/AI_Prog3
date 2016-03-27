package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Harvest extends StripsAction {

    private final Resource resource;

    public Harvest(Peasant peasant, Resource resource){
        super(peasant);
        this.resource = resource;
        this.type = SepiaActionType.HARVEST;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        return peasant.getCargoAmount() == 0 &&
                resource.getAmountRemaining() > 0 &&
                peasant.getPosition().isAdjacent(resource.getPosition());
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
        childPeasant.carry(resource.getType(), 100);
        resource.harvestAmount(100);
        return childState;
    }

    @Override
    public Position targetPosition() {
        return resource.getPosition();
    }

    @Override
    public String toString(){
        return "Harvest(" + peasant.getID() + ", " + resource.getID() + ")";
    }
}
