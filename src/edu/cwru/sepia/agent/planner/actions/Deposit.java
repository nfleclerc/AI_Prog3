package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.StateTracker;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Townhall;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Deposit implements StripsAction {

    private Map<Peasant, Townhall> peasantTownhallMap = new HashMap<>();

    @Override
    public boolean preconditionsMet(GameState state) {
        StateTracker stateTracker = state.getStateTracker();
        stateTracker.getPeasants().stream()
                .filter(peasant -> peasant.isIdle() && peasant.getCargoAmount() > 0)
                .forEach(peasant -> stateTracker.getTownhalls().stream()
                        .filter(townhall -> peasant.getPosition().isAdjacent(townhall.getPosition()))
                        .forEach(townhall -> peasantTownhallMap.put(peasant, townhall)));
        return !peasantTownhallMap.isEmpty();
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        //peasant is not idle
        //peasant has no cargo anymore
        //current amt of resource is increased
        return childState;
    }
}
