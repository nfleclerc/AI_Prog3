package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Townhall;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the deposit strips action.
 */
public class DepositK extends StripsAction {

    private final Townhall townhall;
    private List<Peasant> peasants;
    private List<Peasant> peasantsToDeposit;

    /**
     * Construct a deposit strips action.
     *
     * @param peasants A list of peasants to do the depositing
     * @param townhall The townhall into which the peasants will deposit
     */
    public DepositK(List<Peasant> peasants, Townhall townhall){
        super(peasants);
        this.peasants = peasants;
        this.townhall = townhall;
        this.peasantsToDeposit = new ArrayList<>();
        this.type = SepiaActionType.DEPOSIT;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Peasant peasant : peasants){
            if((peasant.getCargoAmount() > 0 &&
                    peasant.getPosition().isAdjacent(townhall.getPosition()))){
                peasantsToDeposit.add(peasant);
            }
        }
        return !peasantsToDeposit.isEmpty();
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        for (Peasant peasant : peasantsToDeposit) {
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
    public int getK() {
        return peasantsToDeposit.size();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Deposit:");
        for (Peasant peasant : peasantsToDeposit){
            sb.append("\t(" + peasant.getID() + ", " + townhall.getID() + ")");
        }
        return sb.toString();
    }
}
