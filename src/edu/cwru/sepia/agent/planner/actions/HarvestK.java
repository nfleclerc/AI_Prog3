package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HarvestK extends StripsAction {

    private final List<Peasant> peasants;
    private Resource childResource;
    private Map<Peasant, Resource> peasantsAtResource;


    public HarvestK(List<Peasant> units, Map<Peasant, Resource> peasantsAtResource){
        super(units);
        this.peasants = units;
        this.peasantsAtResource = peasantsAtResource;
        this.type = SepiaActionType.HARVEST;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Peasant peasant : peasantsAtResource.keySet()) {
            if (!(peasant.getCargoAmount() == 0 &&
                    peasantsAtResource.get(peasant).getAmountRemaining() > 0 &&
                    peasant.getPosition().isAdjacent(peasantsAtResource.get(peasant).getPosition()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        for(Peasant peasant : peasantsAtResource.keySet()) {
                Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
                childResource = childState.getStateTracker().getResourceById(
                        peasantsAtResource.get(peasant).getID());
                if (childResource != null) {
                    childPeasant.carry(childResource.getType(), 100);
                    childResource.harvestAmount(100);
                    if (childResource.getAmountRemaining() <= 0) {
                        childState.removeResource(childResource);
                    }
                }
        }
        return childState;
    }

    @Override
    public Position targetPosition() {
        return null;
    }

    public Position targetPosition(int index){
        return peasantsAtResource.get(peasants.get(index)).getPosition();
    }


    @Override
    public int getK() {
        return peasantsAtResource.size();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Harvest:");
        for (Peasant peasant : peasantsAtResource.keySet()){
            sb.append("\t(" + peasant.getID() + ", " + peasantsAtResource.get(peasant).getID() + ")");
        }
        return sb.toString();
    }

}
