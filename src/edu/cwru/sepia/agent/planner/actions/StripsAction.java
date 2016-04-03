package edu.cwru.sepia.agent.planner.actions;

import edu.cwru.sepia.agent.planner.GameState;
import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.agent.planner.entities.StripsUnit;

import java.util.List;

/**
 * A useful start of an interface representing strips actions. You may add new methods to this interface if needed, but
 * you should implement the ones provided. You may also find it useful to specify a method that returns the effects
 * of a StripsAction.
 */
public abstract class StripsAction {

    protected SepiaActionType type;
    protected final List<? extends StripsUnit> units;


    public StripsAction(List<? extends StripsUnit> units){
        this.units = units;
    }


    /**
     * Returns true if the provided GameState meets all of the necessary conditions for this action to successfully
     * execute.
     *
     * As an example consider a Move action that moves peasant 1 in the NORTH direction. The partial game state might
     * specify that peasant 1 is at location (3, 3). In this case the game state shows that nothing is at location (3, 2)
     * and (3, 2) is within bounds. So the method returns true.
     *
     * If the peasant were at (3, 0) this method would return false because the peasant cannot move to (3, -1).
     *
     * @param state GameState to check if action is applicable
     * @return true if apply can be called, false otherwise
     */
    public abstract boolean preconditionsMet(GameState state);

    /**
     * Applies the action instance to the given GameState producing a new GameState in the process.
     *
     * As an example consider a Move action that moves peasant 1 in the NORTH direction. The partial game state
     * might specify that peasant 1 is at location (3, 3). The returned GameState should specify
     * peasant 1 at location (3, 2).
     *
     * In the process of updating the peasant state you should also update the GameState's cost and parent pointers.
     *
     * @param state State to apply action to
     * @return State resulting from successful action application.
     */
    public abstract GameState apply(GameState state);

    /**
     * Get the enumerated action type associated with this strips action.
     *
     * @return The action type
     */
    public SepiaActionType getType() {
        return type;
    }

    /**
     * Get the StripsUnit object at the specified index.
     *
     * @param index The index
     * @return The strips unit of interest
     */
    public StripsUnit getUnit(int index) {
        return units.get(index);
    }

    /**
     * Get the target position of this action.
     *
     * @return The target position
     */
    public abstract Position targetPosition();

    /**
     * Get the cost of performing this action.
     *
     * @return The cost
     */
    public double getCost(){
        return 0;
    }

    /**
     * Get the parallelization parameter of this action.
     *
     * @return K - the number of peasants performing this action in parallel
     */
    public abstract int getK();
}
