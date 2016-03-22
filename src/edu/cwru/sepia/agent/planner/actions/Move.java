package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.StateTracker;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;

/**
 * Created by nathaniel on 3/20/16.
 */
public class Move extends StripsAction {

    private final Position position;

    public Move(Peasant peasant, Position position){
        super(peasant);
        this.position = position;
        this.type = SepiaActionType.MOVE;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        //place to move is valid
        return position.inBounds(state.getStateTracker().getXExtent(),
                state.getStateTracker().getYExtent());
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
        childPeasant.setPosition(position);
        return null;
    }

    @Override
    public Position targetPosition() {
        return position;
    }

    @Override
    public String toString(){
        return getClass().getName() + "(" + peasant.getID() + ", " + position.toString() + ")";
    }
}
