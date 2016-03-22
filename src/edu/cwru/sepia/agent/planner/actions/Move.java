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
        //peasant is idle
        //place to move is not blocked
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        return null;
        //peasant is not idle
        //peasant moves towards the indicated position
    }
}
