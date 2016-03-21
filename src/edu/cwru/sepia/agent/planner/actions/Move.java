package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.StateTracker;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Move implements StripsAction {
    @Override
    public boolean preconditionsMet(GameState state) {
        StateTracker stateTracker = state.getStateTracker();
        return false;
    }

    @Override
    public GameState apply(GameState state) {
        return null;
    }
}
