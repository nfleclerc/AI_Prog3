package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.agent.planner.actions.*;
import edu.cwru.sepia.agent.planner.entities.*;
import edu.cwru.sepia.environment.model.state.State;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is used to represent the state of the game after applying one of the available actions. It will also
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
     * @param state         The current stateview at the time the plan is being created
     * @param playernum     The player number of agent that is planning
     * @param requiredGold  The goal amount of gold (e.g. 200 for the small scenario)
     * @param requiredWood  The goal amount of wood (e.g. 200 for the small scenario)
     * @param buildPeasants True if the BuildPeasant action should be considered
     */
    public GameState(State.StateView state, int playernum, int requiredGold, int requiredWood, boolean buildPeasants) {
        this.stateTracker = new StateTracker(state, playernum, requiredGold, requiredWood, buildPeasants);
    }

    /**
     * Construct a GameState from its parent and the action that is taken to create this child.
     *
     * @param parent                 The parent of this state
     * @param actionFromParentToThis The action that is performed to create this state from its parent
     */
    public GameState(GameState parent, StripsAction actionFromParentToThis) {
        this.parent = parent;
        this.actionFromParentToThis = actionFromParentToThis;
        this.stateTracker = new StateTracker(parent.getStateTracker(), parent);
    }

    /**
     * Get the StateTracker object that represents this state.
     *
     * @return The tracker managing this state
     */
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
        // Generate a state resulting from building a peasant, if possible
        if (stateTracker.mustBuildPeasants()) {
            List<Townhall> townhall = new ArrayList<>();
            townhall.add(stateTracker.getTownhall());
            BuildPeasant buildPeasant = new BuildPeasant(townhall);
            if (buildPeasant.preconditionsMet(this)) {
                children.add(buildPeasant.apply(this));
                return children;
            }
        }
        return generateSomeStuff();  // default child state generation method
    }

    /**
     * Generate all possible states resulting from performing various actions.
     *
     * @return A list of children of this state
     */
    private List<GameState> generateSomeStuff() {
        List<GameState> children = new ArrayList<>();
        // Add all possible states resulting from performing moves
        MoveK move = new MoveK(stateTracker.getPeasants(), generatePositions());
        if (move.preconditionsMet(this)) {
            children.add(move.apply(this));
        }
        // Add all possible states resulting from performing deposits
        DepositK deposit = new DepositK(stateTracker.getPeasants(), stateTracker.getTownhall());
        if (deposit.preconditionsMet(this)) {
            children.add(deposit.apply(this));
        }
        //add all possible states resulting from harvests
        HarvestK harvest = new HarvestK(stateTracker.getPeasants(), generateResources());
        if (harvest.preconditionsMet(this)) {
            children.add(harvest.apply(this));
        }
        return children;
    }

    private Map<Peasant, Resource> generateResources() {
        Map<Peasant, Resource> resourceMap = new HashMap<>();
        for (Peasant peasant : stateTracker.getPeasants()) {
            for (Position position : peasant.getPosition().getAdjacentPositions()) {
                stateTracker.getAllResources().stream().filter(resource
                        -> resource.getPosition().equals(position)).forEach(resource
                        -> resourceMap.put(peasant, resource));
            }
        }
        return resourceMap;
    }

    /**
     * Generate the best position for each peasant in this state
     *
     * @return A map of positions to peasants
     */
    private Map<Peasant, Position> generatePositions() {
        Map<Peasant, Position> peasantPositionMap = new HashMap<>();
        List<Position> closedPositions = new ArrayList<>();
        for (Peasant peasant : stateTracker.getPeasants()) {
            Position position = generateViablePosition(peasant, closedPositions);
            peasantPositionMap.put(peasant, position);
            closedPositions.add(position);
        }
        return peasantPositionMap;
    }

    /**
     * Generate a viable position for a peasant to move to.
     *
     * @param peasant         The peasant of interest
     * @param closedPositions A list of closed positions
     * @return The most viable position available to the peasant
     */
    private Position generateViablePosition(Peasant peasant, List<Position> closedPositions) {
        List<Position> positions = new ArrayList<>();
        if (peasant.getCargoAmount() == 0) {


            if (stateTracker.woodNeeded()) {
                positions.addAll(stateTracker.getForests().stream()
                        .filter(resource -> resource.getAmountRemaining() > 0)
                        .map(resource -> getBestPosition(peasant,
                                resource.getPosition().getAdjacentPositions(),
                                closedPositions)).collect(Collectors.toList()));
            } else {
                positions.addAll(stateTracker.getGoldMines().stream()
                        .filter(resource -> resource.getAmountRemaining() > 0)
                        .map(resource -> getBestPosition(peasant,
                                resource.getPosition().getAdjacentPositions(),
                                closedPositions)).collect(Collectors.toList()));
            }

        } else {
            positions.add(getBestPosition(peasant,
                    stateTracker.getTownhall().getPosition().getAdjacentPositions(),
                    closedPositions));

        }
        System.out.println(positions);

        return (getBestPosition(peasant, positions, closedPositions));
    }

    /**
     * Get the best position available to the peasant.
     *
     * @param peasant         The peasant of interest
     * @param positions       A list of positions
     * @param closedPositions
     * @return
     */
    private Position getBestPosition(Peasant peasant, List<Position> positions, List<Position> closedPositions) {
        Position currentPosition = peasant.getPosition();
        if (positions.isEmpty()) return peasant.getPosition();
        Position bestPosition = positions.get(0);
        if (closedPositions.contains(bestPosition)) bestPosition = positions.get(1);
        for (Position position : positions) {
            if (position.chebyshevDistance(currentPosition) < bestPosition.chebyshevDistance(currentPosition)
                    && !closedPositions.contains(position)) {
                bestPosition = position;
            }
        }
        return bestPosition;
    }

    /*
     * Write your heuristic function here. Remember this must be admissible for the properties of A* to hold. If you
     * can come up with an easy way of computing a consistent heuristic that is even better, but not strictly necessary.
     *
     * Add a description here in your submission explaining your heuristic.
     *
     * @return The value estimated remaining cost to reach a goal state from this state.
     */
    public double heuristic() {
        return stateTracker.heuristic();
    }

    /**
     * Write the function that computes the current cost to get to this node. This is combined with your heuristic to
     * determine which actions/states are better to explore.
     *
     * @return The current cost to reach this goal
     */

    public double getCost() {
        return actionFromParentToThis == null && parent == null ?
                0.0 :
                parent.getCost() + actionFromParentToThis.getCost();
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
        } else if (this.getCost() + this.heuristic() == o.getCost() + o.heuristic()) {
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

    public void removeResource(Resource resource) {
        stateTracker.removeResource(resource);
    }
}
