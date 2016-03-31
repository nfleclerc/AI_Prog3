package edu.cwru.sepia.agent.planner.actions;

import java.util.*;
import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.StateTracker;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;
import javafx.geometry.Pos;

public class MoveK extends StripsAction {
    Map<Peasant, Position> positions;
    List<Peasant> peasants;

    public MoveK(List<Peasant> units, Map<Peasant, Position> positions){
        super(units);
        this.peasants = units;
        this.positions = positions;
        this.type = SepiaActionType.MOVE;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Position position : positions.values()){
            if (!position.inBounds(state.getStateTracker().getXExtent(),
                                    state.getStateTracker().getYExtent())){
                return false;
            }
        }
        return true;
    }

    @Override
    public GameState apply(GameState state) {
        GameState childState = new GameState(state, this);
        for(Peasant peasant : positions.keySet()) {
            Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
            childPeasant.setPosition(new Position(positions.get(peasant)));
        }
        return childState;
    }

    //gets overall cost
    @Override
    public double getCost(){
        double cost = 0.0;
        for(Peasant peasant : positions.keySet()){
            cost += (peasant.getPosition().chebyshevDistance(positions.get(peasant)));
        }
        return cost;
    }

    @Override
    public Position targetPosition() {
        return null;
    }

    public Position targetPosition(int index){
        return positions.get(peasants.get(index));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Move:");
        for (Peasant peasant : peasants){
            sb.append("\t(" + peasant.getID() + ", " + positions.get(peasant) + ")");
        }
        return sb.toString();
    }
}
