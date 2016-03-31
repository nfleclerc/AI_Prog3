package edu.cwru.sepia.agent.planner.actions;

import java.util.*;
import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.StateTracker;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;
import javafx.geometry.Pos;

public class MoveK extends StripsAction {
    Position position;
    List<Peasant> peasants;

    public MoveK(List<Peasant> units, Position position){
        super(units);
        this.peasants = units;
        this.position = position;
        this.type = SepiaActionType.MOVE;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        return position.inBounds(state.getStateTracker().getXExtent(),
                state.getStateTracker().getYExtent());
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        for(Peasant peasant : peasants) {
            Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
            childPeasant.setPosition(new Position(position));
        }
        return childState;
    }

    //gets overall cost
    @Override
    public double getCost(){
        double cost = 0.0;
        for(Peasant peasant : peasants){
            cost += (peasant.getPosition().chebyshevDistance(position));
        }
        return cost;
    }

    @Override
    public Position targetPosition() {
        return position;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Move:");
        for (Peasant peasant : peasants){
            sb.append("\n\t(" + peasant.getID() + ", " + position + ")");
        }
        return sb.toString();
    }
}
