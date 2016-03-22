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

    private final Resource resource;
    private final Peasant peasant;

    public Harvest(Peasant peasant, Resource resource){
        this.peasant = peasant;
        this.resource = resource;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        return peasant.getCargoAmount() == 0 &&
                resource.getAmountRemaining() > 0 &&
                peasant.getPosition().isAdjacent(resource.getPosition());
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        return childState;
    }
}
