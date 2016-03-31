package edu.cwru.sepia.agent.planner.actions;

import java.util.*;
import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.StateTracker;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;

public class MoveK extends StripsAction {
    List<Position> positions;
    List<Peasant> peasants;

    public MoveK(List<Peasant> units, List<Position> positions){
        super(units);
        this.peasants = units;
        this.positions = positions;
        this.type = SepiaActionType.MOVE;
    }

    @Override
    public boolean preconditionsMet(GameState state) {
        for (Position position : positions) {
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
        for(Peasant peasant : peasants) {
            Peasant childPeasant = childState.getStateTracker().getPeasantById(peasant.getID());
            System.out.println("old pos: " + childPeasant.getPosition() + " new pos: " +
            positions.get(peasants.indexOf(peasant)));
            childPeasant.setPosition(new Position(positions.get(peasants.indexOf(peasant))));
            //this is assuming the peasants and positions are added to their respective lists in the same order
        }
        return childState;
    }

    //gets overall cost
    @Override
    public double getCost(){
        double cost = 0.0;
        for(Position position : positions){
            cost += (position.chebyshevDistance(position));
        }
        return cost;
    }

    @Override
    public Position targetPosition(int index) {
        return positions.get(index);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Move:");
        for (Peasant peasant : peasants){
            sb.append("\n\t(" + peasant.getID() + ", " + positions.get(peasants.indexOf(peasant)).toString() + ")");
        }
        return sb.toString();
    }
}
