package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Townhall;
import java.util.List;

public class DepositK extends StripsAction {

    private final Townhall townhall;
    private List<Peasant> peasants;

    public DepositK(List<Peasant> peasants, Townhall townhall){
        super(peasants);
        this.peasants = peasants;
        this.townhall = townhall;
        this.type = SepiaActionType.DEPOSIT;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Peasant peasant : peasants){
            if((peasant.getCargoAmount() > 0 &&
                    peasant.getPosition().isAdjacent(townhall.getPosition()))){
                return true;
            }
        }
        return false;
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        for (Peasant peasant : peasants) {
            Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
            childState.getStateTracker()
                    .addResource(childPeasant.getCargoType(), childPeasant.getCargoAmount());
            childPeasant.carry(null, 0);
            //this is assuming the peasants and positions are added to their respective lists in the same order
        }
        return childState;
    }

    @Override
    public Position targetPosition() {
        return townhall.getPosition();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Deposit:");
        for (Peasant peasant : peasants){
            sb.append("\n\t(" + peasant.getID() + ", " + townhall.getID() + ")");
        }
        return sb.toString();
    }
}
