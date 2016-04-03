package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.agent.planner.entities.*;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.State;
import edu.cwru.sepia.environment.model.state.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is used as a lightweight shell for pseudo-tracking an actual game state object
 */
public class StateTracker {

    private int playerNum;
    private int turnNumber;
    private double xExtent;
    private double yExtent;
    private int currentWood;
    private int currentGold;
    private int requiredWood;
    private int requiredGold;
    private int currentFood;
    private boolean buildPeasants;
    private List<Peasant> peasants = new ArrayList<>();
    private List<Goldmine> goldmines = new ArrayList<>();
    private List<Forest> forests = new ArrayList<>();
    private Townhall townhall;
    private GameState parent;

    /**
     * Construct a StateTracker object from another state tracker and parent state.
     *
     * @param stateTracker A state tracker to reconstruct into this one
     * @param parent The parent game state for making comparisons
     */
    public StateTracker(StateTracker stateTracker, GameState parent) {
        xExtent = stateTracker.xExtent;
        yExtent = stateTracker.yExtent;
        turnNumber = stateTracker.turnNumber;
        playerNum = stateTracker.playerNum;
        requiredGold = stateTracker.requiredGold;
        requiredWood = stateTracker.requiredWood;
        buildPeasants = stateTracker.buildPeasants;
        currentWood = stateTracker.currentWood;
        currentGold = stateTracker.currentGold;
        peasants.addAll(stateTracker.peasants.stream().map(Peasant::new).collect(Collectors.toList()));
        forests.addAll(stateTracker.forests.stream().map(Forest::new).collect(Collectors.toList()));
        goldmines.addAll(stateTracker.goldmines.stream().map(Goldmine::new).collect(Collectors.toList()));
        townhall = new Townhall(stateTracker.getTownhall());
        this.parent = parent;
        currentFood = stateTracker.currentFood;
    }

    /**
     * Construct a StateTracker object from the provided stateview and world specifications.
     * @param state
     * @param playerNum     The player number associated with this state
     * @param requiredWood  The amount of wood required to achieve victory in this world
     * @param requiredGold  The amount of gold required to achieve victory in this world
     * @param buildPeasants Whether building peasants is allowed in this world
     */
    public StateTracker(State.StateView state, int playerNum, int requiredWood, int requiredGold, boolean buildPeasants) {
        xExtent = state.getXExtent();
        yExtent = state.getYExtent();
        turnNumber = state.getTurnNumber();
        this.playerNum = playerNum;
        this.requiredGold = requiredGold;
        this.requiredWood = requiredWood;
        this.buildPeasants = buildPeasants;
        currentGold = 0;
        currentWood = 0;
        currentFood = 1;
        for (ResourceNode.ResourceView resource : state.getAllResourceNodes()) {
            if (resource.getType() == ResourceNode.Type.GOLD_MINE)
                goldmines.add(new Goldmine(resource));
            else
                forests.add(new Forest(resource));
        }
        for (Unit.UnitView unit : state.getAllUnits()) {
            if (unit.getTemplateView().getName().equals("Peasant"))
                peasants.add(new Peasant(unit));
            else
                townhall = new Townhall(unit);
        }
    }

    /**
     * Determine whether this state tracker represents the goal state.
     *
     * @return <code>true</code> if the demand for resources has been met; <code>false</code> if more work is needed
     */
    public boolean isGoal() {
        return currentWood >= requiredWood && currentGold >= requiredGold;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof StateTracker &&
                currentWood == ((StateTracker) o).currentWood &&
                currentGold == ((StateTracker) o).currentGold &&
                peasants.equals(((StateTracker) o).peasants) &&
                goldmines.equals(((StateTracker) o).goldmines) &&
                forests.equals(((StateTracker) o).forests) &&
                townhall.equals(((StateTracker) o).townhall);
    }

    @Override
    public int hashCode(){
        return currentWood +
                currentGold +
                peasants.hashCode() +
                goldmines.hashCode() +
                forests.hashCode() +
                townhall.hashCode();
    }

    /**
     * Get all the peasants in the world.
     *
     * @return A list of all peasants currently present in this game state
     */
    public List<Peasant> getPeasants() {
        return peasants;
    }

    /**
     * Get all the resources in the world.
     *
     * @return A list of all resources currently present in this game state
     */
    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        resources.addAll(goldmines);
        resources.addAll(forests);
        return resources;
    }

    /**
     * Get the townhall in the world.
     *
     * @return A Townhall object representing the townhall currently present in this game state
     */
    public Townhall getTownhall() {
        return townhall;
    }

    public void addResource(ResourceType cargoType, int cargoAmount) {
        switch (cargoType){
            case GOLD:
                currentGold += cargoAmount;
                break;
            case WOOD:
                currentWood += cargoAmount;
        }
    }

    /**
     * Get the x-extent of the world.
     *
     * @return The number of 'columns' in the world
     */
    public double getXExtent() {
        return xExtent;
    }

    /**
     * Get the y-extent of the world.
     *
     * @return The number of 'rows' in the world
     */
    public double getYExtent() {
        return yExtent;
    }

    /**
     * Get the peasant with the specified ID.
     *
     * @param id The unit ID of the peasant
     * @return The identified peasant
     */
    public Peasant getPeasantById(int id){
        for (Peasant peasant : peasants){
            if (peasant.getID() == id)
                return peasant;
        }
        return null;
    }

    /**
     * Assess the quality of the game state represented by this state tracker.
     *
     * This heuristic function takes into account several factors pertaining to the overall quality:
     *   - current resources
     *   -
     *
     * @return The heuristic value computed by this function
     */
    public double heuristic() {
        double heuristic = requiredGold + requiredWood;
        heuristic -= currentGold;
        heuristic -= currentWood;
        for (Peasant peasant : peasants) {
            if (goldNeeded()) {
                // If there is a demand for gold, seek out the closest goldmine
                if (peasant.getCargoAmount() == 0) {
                    if (!goldmines.isEmpty()) {
                        // Compute distance to nearest nonempty goldmine, add to heuristic
                        Goldmine goldmine = findClosestGoldMine(peasant);
                        heuristic += goldmine.getPosition().chebyshevDistance(peasant.getPosition()) * 2;
                    }
                } else {
                    // Compute distance to townhall, add to heuristic
                    heuristic += townhall.getPosition().chebyshevDistance(peasant.getPosition()) * .5;
                }
            } else {
                // If there is a demand for wood, seek out the closest forest
                if (peasant.getCargoAmount() == 0) {
                    if (!forests.isEmpty()) {
                        // Compute distance to nearest nonempty forest, add to heuristic
                        Forest forest = findClosestForest(peasant);
                        heuristic += forest.getPosition().chebyshevDistance(peasant.getPosition()) * 2;
                    }
                } else {
                    // Compute distance to townhall, add to heuristic
                    heuristic += townhall.getPosition().chebyshevDistance(peasant.getPosition()) * .5;
                }
            }
        }
        // Sweeten the deal if there are more peasants in this state than in the parent state
        if (peasants.size() > parent.getStateTracker().getPeasants().size()){
            heuristic *= 1000;
        }
        return heuristic;
    }

    /**
     * Find the forest closest to the specified peasant.
     *
     * @param peasant The peasant of interest
     * @return The closest forest
     */
    public Forest findClosestForest(Peasant peasant) {
        Forest closestForest = forests.get(0);
        int thisDistance, closestDistance;

        for (Forest forest : forests) {

            thisDistance = forest.getPosition().chebyshevDistance(peasant.getPosition());
            closestDistance = closestForest.getPosition().chebyshevDistance(peasant.getPosition());

            if (thisDistance < closestDistance) closestForest = forest;
        }
        return closestForest;
    }

    /**
     * Find the goldmine closest to the specified peasant.
     *
     * @param peasant The peasant of interest
     * @return The closest goldmine
     */
    public Goldmine findClosestGoldMine(Peasant peasant) {
        Goldmine closestGoldmine = goldmines.get(0);
        int thisDistance, closestDistance;

        for (Goldmine goldmine : goldmines) {

            thisDistance = goldmine.getPosition().chebyshevDistance(peasant.getPosition());
            closestDistance = closestGoldmine.getPosition().chebyshevDistance(peasant.getPosition());

            if (thisDistance < closestDistance) closestGoldmine = goldmine;
        }
        return closestGoldmine;
    }

    /**
     * Remove the specified resource from the list of tracked resources.
     *
     * @param resource The resource to remove
     */
    public void removeResource(Resource resource) {
        switch (resource.getType()){
            case WOOD:
                forests.remove(resource);
                break;
            case GOLD:
                goldmines.remove(resource);
                break;
        }
    }

    /**
     * Get the resource with the specified resource ID.
     *
     * @param id The resource ID
     * @return The identified resource
     */
    public Resource getResourceById(int id) {
        for (Resource resource : getAllResources()){
            if (resource.getID() == id){
                return resource;
            }
        }
        return null;
    }

    /**
     * Get all the goldmines in the world.
     *
     * @return A list of goldmines
     */
    public List<Goldmine> getGoldmines() {
        return goldmines;
    }

    /**
     * Get all the forests in the world.
     * @return A list of forests
     */
    public List<Forest> getForests() {
        return forests;
    }

    /**
     * Determine whether gold is needed.
     *
     * @return <code>true</code> if gold is needed; <code>false</code> otherwise
     */
    public boolean goldNeeded() {
        return currentGold < requiredGold;
    }

    /**
     * Determine whether wood is needed.
     *
     * @return <code>true</code> if wood is needed; <code>false</code> otherwise
     */
    public boolean woodNeeded() {
        return currentWood < requiredWood;
    }

    /**
     * Get the current amount of gold.
     *
     * @return current gold
     */
    public int getCurrentGold() {
        return currentGold;
    }

    /**
     * Get the current amount of food.
     *
     * @return current food
     */
    public int getCurrentFood() {
        return currentFood;
    }

    /**
     * Adjust the amount of resources that account for building a peasant.
     */
    public void buyPeasant() {
        currentGold -= 400;
        currentFood += 1;
    }

    /**
     * Determine whether it is necessary in this state to build peasants.
     *
     * @return <code>true</code> if peasants are needed; <code>false</code> otherwise
     */
    public boolean mustBuildPeasants() {
        return buildPeasants;
    }

}