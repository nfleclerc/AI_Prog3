package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Deposit implements StripsAction {

    @Override
    public boolean preconditionsMet(GameState state) {
        return false;
    }

    @Override
    public GameState apply(GameState state) {
        return null;
    }
}
