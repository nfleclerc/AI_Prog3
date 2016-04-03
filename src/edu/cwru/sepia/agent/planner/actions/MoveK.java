package edu.cwru.sepia.agent.planner.actions;

import java.util.*;
import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.Peasant;

/**
 * This class represents the move strips action.
 */
public class MoveK extends StripsAction {

    Map<Peasant, Position> positions;
    List<Peasant> peasants;

    /**
     * Construct a move strips action.
     *
     * @param units     A list of peasants to move
     * @param positions A map of goal positions to their respective peasants
     */
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

    @Override
    public double getCost(){
        double cost = 0.0;
        for(Peasant peasant : positions.keySet()){
            cost += (peasant.getPosition().chebyshevDistance(positions.get(peasant)));
        }
        return cost;
    }

    @Override
    public int getK() {
        return peasants.size();
    }

    @Override
    public Position targetPosition() {
        return null;
    }

    /**
     * Get the target position of this move.
     *
     * @param index The index of a peasant
     * @return The target position of the peasant
     */
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
