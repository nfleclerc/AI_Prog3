package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Harvest implements StripsAction {
    @Override
    public boolean preconditionsMet(GameState state) {
        return false;
        //peasant is idle
        //peasant is next to mine/forest
        //peasant is not carrying anything
        //mine/forest has materials to harvest
    }

    @Override
    public GameState apply(GameState state) {
        return null;
        //peasant is not idle
        //peasant is now carrying stuff
        //mine/forest loses supplies
    }
}
