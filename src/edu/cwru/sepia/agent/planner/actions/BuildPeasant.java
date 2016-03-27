package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.StripsUnit;
import edu.cwru.sepia.agent.planner.entities.Townhall;

/**
 * Created by nathaniel on 3/27/16.
 */
public class BuildPeasant extends StripsAction {

    public BuildPeasant(Townhall townhall) {
        super(townhall);
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        return false;
    }

    @Override
    public GameState apply(GameState state) {
        return null;
    }

    @Override
    public Position targetPosition() {
        return null;
    }
}
