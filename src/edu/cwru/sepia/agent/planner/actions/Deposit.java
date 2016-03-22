package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.StateTracker;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Townhall;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Deposit extends StripsAction {


    private final Townhall townhall;

    public Deposit(Peasant peasant, Townhall townhall){
        super(peasant);
        this.townhall = townhall;
        this.type = SepiaActionType.DEPOSIT;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        return peasant.getCargoAmount() > 0 &&
                peasant.getPosition().isAdjacent(townhall.getPosition());
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        childState.getStateTracker().getPeasants().stream()
                .filter(this.peasant::equals)
                .forEach(peasant -> peasant.carry(null, 0));
        return childState;
    }

    @Override
    public Position targetPosition() {
        return townhall.getPosition();
    }
}
