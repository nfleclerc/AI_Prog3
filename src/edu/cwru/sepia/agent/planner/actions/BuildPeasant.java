package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.StripsUnit;
import edu.cwru.sepia.agent.planner.entities.Townhall;
import java.util.*;

/**
 * Created by nathaniel on 3/27/16.
 */
public class BuildPeasant extends StripsAction {

    private final Townhall townhall;
    private GameState gameState;

    public BuildPeasant(List<Townhall> townhall){
        super(townhall);
        this.townhall = townhall.get(0);
        type = SepiaActionType.BUILD;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        return state.getStateTracker().getCurrentGold() >= 400 && state.getStateTracker().getCurrentFood() < 3;
    }

    @Override
    public GameState apply(GameState state) {
        gameState = state;
        GameState childState = new GameState(state, this);
        childState.getStateTracker().buyPeasant();
        List<Peasant> childPeasants = childState.getStateTracker().getPeasants();
        childPeasants.add(new Peasant(townhall, childPeasants));
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
