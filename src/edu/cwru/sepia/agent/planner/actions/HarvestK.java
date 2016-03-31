package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HarvestK extends StripsAction {

    private Resource resource;
    private List<Peasant> peasants;
    private Map<Peasant, Resource> peasantsAtResource;


    public HarvestK(List<Peasant> units, Resource resource){
        super(units);
        this.peasants = units;
        this.resource = resource;
        this.peasantsAtResource = new HashMap<>();
        this.type = SepiaActionType.HARVEST;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Peasant peasant : peasants) {
            if ((peasant.getCargoAmount() == 0 &&
                    resource.getAmountRemaining() > 0 &&
                    peasant.getPosition().isAdjacent(resource.getPosition()))) {
                        peasantsAtResource.put(peasant, resource);
                    }
            }
        return !peasantsAtResource.isEmpty();
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        for(Peasant peasant : peasantsAtResource.keySet()) {
                Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
                Resource childResource = childState.getStateTracker().getResourceById(
                        peasantsAtResource.get(peasant).getID());
                childPeasant.carry(childResource.getType(), 100);
                childResource.harvestAmount(100);
                if (childResource.getAmountRemaining() <= 0) {
                    childState.removeResource(childResource);
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
            sb.append("\t(" + peasant.getID() + ", " + resource.getID() + ")");
        }
        return sb.toString();
    }
}
