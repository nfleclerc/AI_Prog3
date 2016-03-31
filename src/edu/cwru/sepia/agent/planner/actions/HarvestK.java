package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;
import java.util.List;

public class HarvestK extends StripsAction {

    private List<Resource> resources;
    private List<Peasant> peasants;


    public HarvestK(List<Peasant> units, List<Position> positions){
        super(units);
        this.peasants = units;
        this.resources = resources;
        this.type = SepiaActionType.HARVEST;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Peasant peasant : peasants){
            if(!(peasant.getCargoAmount() == 0 &&
                    resources.get(peasants.indexOf(peasant)).getAmountRemaining() > 0 &&
                    peasant.getPosition().isAdjacent(resources.get(peasants.indexOf(peasant)).getPosition()))){
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
            Resource childResource = childState.getStateTracker().getResourceById(resources.get(peasants.indexOf(peasant)).getID());
            childPeasant.carry(childResource.getType(), 100);
            childResource.harvestAmount(100);
            if (resources.get(peasants.indexOf(peasant)).getAmountRemaining() == 0) {
                childState.removeResource(resources.get(peasants.indexOf(peasant)));
            }
        }
        return childState;
    }

    @Override
    public Position targetPosition(int index) {
        return resources.get(index).getPosition();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Harvest:");
        for (Peasant peasant : peasants){
            sb.append("\n\t(" + peasant.getID() + ", " + resources.get(peasants.indexOf(peasant)).getID() + ")");
        }
        return sb.toString();
    }
}
