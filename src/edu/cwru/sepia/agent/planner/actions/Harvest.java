package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.StateTracker;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Harvest implements StripsAction {

    private Map<Peasant, Resource> peasantResourceMap = new HashMap<>();

    @Override
    public boolean preconditionsMet(GameState state) {
        StateTracker stateTracker = state.getStateTracker();
        stateTracker.getPeasants().stream()
                //peasant is idle and is not carrying anything
                .filter(peasant -> peasant.getCargoAmount() == 0 && peasant.isIdle())
                .forEach(peasant -> stateTracker.getAllResources().stream()
                        //resource has materials to harvest and is next to the peasant
                        .filter(resource -> resource.getAmountRemaining() > 0 &&
                                peasant.getPosition().isAdjacent(resource.getPosition()))
                        .forEach(resource -> peasantResourceMap.put(peasant, resource)));
        return !peasantResourceMap.isEmpty();
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        //peasant is not idle
        //peasant is now carrying stuff
        //mine/forest loses supplies
        return childState;
    }
}
