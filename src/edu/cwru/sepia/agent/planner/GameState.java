package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.agent.planner.actions.Deposit;
import edu.cwru.sepia.agent.planner.actions.Harvest;
import edu.cwru.sepia.agent.planner.actions.Move;
import edu.cwru.sepia.agent.planner.actions.StripsAction;
import edu.cwru.sepia.agent.planner.entities.Peasant;
import edu.cwru.sepia.agent.planner.entities.Resource;
import edu.cwru.sepia.agent.planner.entities.Townhall;
import edu.cwru.sepia.environment.model.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent the state of the game after applying one of the avaiable actions. It will also
 * track the A* specific information such as the parent pointer and the cost and heuristic function. Remember that
 * unlike the path planning A* from the first assignment the cost of an action may be more than 1. Specifically the cost
 * of executing a compound action such as move can be more than 1. You will need to account for this in your heuristic
 * and your cost function.
 *
 * The first instance is constructed from the StateView object (like in PA2). Implement the methods provided and
 * add any other methods and member variables you need.
 *
 * Some useful API calls for the state view are
 *
 * state.getXExtent() and state.getYExtent() to get the map size
 *
 * I recommend storing the actions that generated the instance of the GameState in this class using whatever
 * class/structure you use to represent actions.
 */
public class GameState implements Comparable<GameState> {

    private StateTracker stateTracker;
    private GameState parent;
    private StripsAction actionFromParentToThis;

    /**
     * Construct a GameState from a stateview object. This is used to construct the initial search node. All other
     * nodes should be constructed from the another constructor you create or by factory functions that you create.
     *
     * @param state The current stateview at the time the plan is being created
     * @param playernum The player number of agent that is planning
     * @param requiredGold The goal amount of gold (e.g. 200 for the small scenario)
     * @param requiredWood The goal amount of wood (e.g. 200 for the small scenario)
     * @param buildPeasants True if the BuildPeasant action should be considered
     */
    public GameState(State.StateView state, int playernum, int requiredGold, int requiredWood, boolean buildPeasants) {
        this.stateTracker = new StateTracker(state, playernum, requiredGold, requiredWood, buildPeasants);
    }

    public GameState(GameState parent, StripsAction actionFromParentToThis){
        this.parent = parent;
        this.actionFromParentToThis = actionFromParentToThis;
        this.stateTracker = new StateTracker(parent.getStateTracker());
    }

    public StateTracker getStateTracker() {
        return stateTracker;
    }

    /**
     * Unlike in the first A* assignment there are many possible goal states. As long as the wood and gold requirements
     * are met the peasants can be at any location and the capacities of the resource locations can be anything. Use
     * this function to check if the goal conditions are met and return true if they are.
     *
     * @return true if the goal conditions are met in this instance of game state.
     */
    public boolean isGoal() {
        return stateTracker.isGoal();
    }

    /**
     * The branching factor of this search graph are much higher than the planning. Generate all of the possible
     * successor states and their associated actions in this method.
     *
     * @return A list of the possible successor states and their associated actions
     */
    public List<GameState> generateChildren() {
        List<GameState> children = new ArrayList<>();
        //for each peasant in this state
        for (Peasant peasant : stateTracker.getPeasants()) {
            //generate List of positions that are hunkey-dorey
            List<Position> viablePositions = generateViablePositions();
            for (Position position : viablePositions){
                Move move = new Move(peasant, position);
                if (move.preconditionsMet(this)){
                    children.add(move.apply(this));
                }
            }
            //add all possible states resulting from deposits
            for (Townhall townhall : stateTracker.getTownhalls()){
                Deposit deposit = new Deposit(peasant, townhall);
                if (deposit.preconditionsMet(this)){
                    children.add(deposit.apply(this));
                }
            }
            //add all possible states resulting from harvests
            for (Resource resource : stateTracker.getAllResources()){
                Harvest harvest = new Harvest(peasant, resource);
                if (harvest.preconditionsMet(this)){
                    children.add(harvest.apply(this));
                }
            }
        }
        return children;
    }

    private List<Position> generateViablePositions() {
        List<Position> viablePositions = new ArrayList<>();
        for (Townhall townhall : stateTracker.getTownhalls()){
            viablePositions.addAll(townhall.getPosition().getAdjacentPositions());
        }
        for (Resource resource : stateTracker.getAllResources()){
            viablePositions.addAll(resource.getPosition().getAdjacentPositions());
        }
        viablePositions.stream()
                .filter(position -> !position.inBounds(stateTracker.getXExtent(), stateTracker.getYExtent()))
                .forEach(viablePositions::remove);
        return viablePositions;

    }

    /**
     * Write your heuristic function here. Remember this must be admissible for the properties of A* to hold. If you
     * can come up with an easy way of computing a consistent heuristic that is even better, but not strictly necessary.
     *
     * Add a description here in your submission explaining your heuristic.
     *
     * @return The value estimated remaining cost to reach a goal state from this state.
     */
    public double heuristic() {
        // TODO: Implement me!
        return 1;
    }

    /**
     *
     * Write the function that computes the current cost to get to this node. This is combined with your heuristic to
     * determine which actions/states are better to explore.
     *
     * @return The current cost to reach this goal
     */

    //todo: make sure to update when generating new states in stripsaction
    public double getCost() {
        // TODO: Implement me!
        return 1;
    }

    /**
     * This is necessary to use your state in the Java priority queue. See the official priority queue and Comparable
     * interface documentation to learn how this function should work.
     *
     * @param o The other game state to compare
     * @return 1 if this state costs more than the other, 0 if equal, -1 otherwise
     */
    @Override
    public int compareTo(GameState o) {
        if (this.getCost() + this.heuristic() > o.getCost() + o.heuristic()) {
            return 1;
        } else if (this.getCost() + this.heuristic() == o.getCost() + o.heuristic()){
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * This will be necessary to use the GameState as a key in a Set or Map.
     *
     * @param o The game state to compare
     * @return True if this state equals the other state, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof GameState && stateTracker.equals((((GameState) o).getStateTracker()));
    }

    /**
     * This is necessary to use the GameState as a key in a HashSet or HashMap. Remember that if two objects are
     * equal they should hash to the same value.
     *
     * @return An integer hashcode that is equal for equal states.
     */
    @Override
    public int hashCode() {
        return stateTracker.hashCode();
    }

    public GameState getParent() {
        return parent;
    }

    public StripsAction getActionFromParentToThis() {
        return actionFromParentToThis;
    }
}
