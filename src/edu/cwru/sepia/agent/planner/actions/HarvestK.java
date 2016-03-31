package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;
import java.util.List;

public class HarvestK extends StripsAction {

    private Resource resource;
    private List<Peasant> peasants;


    public HarvestK(List<Peasant> units, Resource resource){
        super(units);
        this.peasants = units;
        this.resource = resource;
        this.type = SepiaActionType.HARVEST;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Peasant peasant : peasants) {
            if ((peasant.getCargoAmount() != 0 ||
                    resource.getAmountRemaining() <= 0 ||
                    !peasant.getPosition().isAdjacent(resource.getPosition()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        for(Peasant peasant : peasants) {
                Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
                Resource childResource = childState.getStateTracker().getResourceById(resource.getID());
                childPeasant.carry(resource.getType(), 100);
                childResource.harvestAmount(100);
                if (resource.getAmountRemaining() <= 0) {
                    childState.removeResource(resource);

                }
        }
        return childState;
    }

    @Override
    public Position targetPosition() {
        return resource.getPosition();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Harvest:");
        for (Peasant peasant : peasants){
            sb.append("\n\t(" + peasant.getID() + ", " + resource.getID() + ")");
        }
        return sb.toString();
    }
}
