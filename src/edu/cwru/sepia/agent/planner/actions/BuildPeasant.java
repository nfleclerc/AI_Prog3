package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Townhall;

/**
 * Created by nathaniel on 3/27/16.
 */
public class BuildPeasant extends StripsAction {

    private final Townhall townhall;

    public BuildPeasant(Townhall townhall) {
        super(townhall);
        this.townhall = townhall;
        type = SepiaActionType.BUILD;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        return state.getStateTracker().getCurrentGold() >= 400 && state.getStateTracker().getCurrentFood() >= 1;
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        childState.getStateTracker().buyPeasant();
        childState.getStateTracker().getPeasants().add(new Peasant(townhall));
        return childState;
    }

    @Override
    public Position targetPosition() {
        return townhall.getPosition();
    }

    @Override
    public String toString(){
        return "BuildPeasant(" + townhall.getID() + ")";
    }

}
