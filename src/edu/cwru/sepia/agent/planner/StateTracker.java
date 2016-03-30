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
    private List<GoldMine> goldMines = new ArrayList<>();
    private List<Forest> forests = new ArrayList<>();
    private Townhall townhall;
    private GameState parent;

    /**
     * This constructor initializes this state tracker using another state tracker
     *
     * @param stateTracker The state tracker to represent by this tracker
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
        goldMines.addAll(stateTracker.goldMines.stream().map(GoldMine::new).collect(Collectors.toList()));
        townhall = new Townhall(stateTracker.getTownhall());
        this.parent = parent;
        currentFood = stateTracker.currentFood;
    }

    public StateTracker(State.StateView state, int playerNum, int requiredGold, int requiredWood, boolean buildPeasants) {
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
            if (resource.getType() == ResourceNode.Type.GOLD_MINE) {
                goldMines.add(new GoldMine(resource));
            } else {
                forests.add(new Forest(resource));
            }
        }
        for (Unit.UnitView unit : state.getAllUnits()){
            if (unit.getTemplateView().getName().equals("Peasant")){
                peasants.add(new Peasant(unit));
            } else {
                townhall = new Townhall(unit);
            }
        }
    }

    public boolean isGoal() {
        return requiredWood == currentWood && requiredGold == currentGold;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof StateTracker &&
                currentWood == ((StateTracker) o).currentWood &&
                currentGold == ((StateTracker) o).currentGold &&
                peasants.equals(((StateTracker) o).peasants) &&
                goldMines.equals(((StateTracker) o).goldMines) &&
                forests.equals(((StateTracker) o).forests) &&
                townhall.equals(((StateTracker) o).townhall);
    }

    @Override
    public int hashCode(){
        return currentWood +
                currentGold +
                peasants.hashCode() +
                goldMines.hashCode() +
                forests.hashCode() +
                townhall.hashCode();
    }


    public List<Peasant> getPeasants() {
        return peasants;
    }

    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        resources.addAll(goldMines);
        resources.addAll(forests);
        return resources;
    }

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

    public double getXExtent() {
        return xExtent;
    }

    public double getYExtent() {
        return yExtent;
    }

    public Peasant getPeasantById(int id){
        for (Peasant peasant : peasants){
            if (peasant.getID() == id){
                return peasant;
            }
        }
        return null;
    }

    public double heuristic() {
        double heuristic = requiredGold + requiredWood;
        heuristic -= currentGold;
        heuristic -= currentWood;
        for (Peasant peasant : peasants) {
            if (currentGold <= currentWood) {
                //if no gold is had, go to nearest nonempty goldmine
                if (peasant.getCargoAmount() == 0) {
                    //find nearest nonempty goldmine
                    GoldMine goldMine = findClosestGoldMine(peasant);
                    //find distance to it
                    //add the distance to this heuristic
                    heuristic += goldMine.getPosition().chebyshevDistance(peasant.getPosition()) * 2;
                } else {
                    //if next to a goldmine harvest
                    //if gold is had, go to the townhall
                    //add distance to this heuristic
                    heuristic += townhall.getPosition().chebyshevDistance(peasant.getPosition()) * .5;
                }
            } else {
                //if no wood is had, go to nearest nonempty forest
                if (peasant.getCargoAmount() == 0) {
                    //find nearest nonempty forest
                    Forest forest = findClosestForest(peasant);
                    //find distance to it
                    //add the distance to this heuristic
                    heuristic += forest.getPosition().chebyshevDistance(peasant.getPosition()) * 2;
                } else {
                    //if next to a goldmine harvest
                    //if gold is had, go to the townhall
                    //add distance to this heuristic
                    heuristic += townhall.getPosition().chebyshevDistance(peasant.getPosition()) * .5;
                }
            }
        }
        if (peasants.size() > parent.getStateTracker().getPeasants().size()){
            return 0;
        }
        return heuristic;
    }

    private Forest findClosestForest(Peasant peasant) {
        Forest closestForest = forests.get(0);
        for (Forest forest : forests) {
            if (forest.getPosition().chebyshevDistance(peasant.getPosition()) <
                    closestForest.getPosition().chebyshevDistance(peasant.getPosition())) {
                closestForest = forest;
            }
        }
        return closestForest;
    }

    private GoldMine findClosestGoldMine(Peasant peasant) {
        GoldMine closestGoldMine = goldMines.get(0);
        for (GoldMine goldMine : goldMines) {
            if (goldMine.getPosition().chebyshevDistance(peasant.getPosition()) <
                    closestGoldMine.getPosition().chebyshevDistance(peasant.getPosition())) {
                closestGoldMine = goldMine;
            }
        }
        return closestGoldMine;
    }

    public void removeResource(Resource resource) {
        switch (resource.getType()){
            case WOOD:
                forests.remove(resource);
                break;
            case GOLD:
                goldMines.remove(resource);
                break;
        }
    }

    public Resource getResourceById(int id) {
        for (Resource resource : getAllResources()){
            if (resource.getID() == id){
                return resource;
            }
        }
        return null;
    }

    public List<GoldMine> getGoldMines() {
        return goldMines;
    }

    public List<Forest> getForests() {
        return forests;
    }

    public boolean goldNeeded() {
        return currentGold < currentWood;
    }

    public int getCurrentGold() {
        return currentGold;
    }

    public int getCurrentFood() {
        return currentFood;
    }

    public void buyPeasant() {
        currentGold -= 400;
        currentFood += 1;
    }

    public boolean mustBuildPeasants() {
        return buildPeasants;
    }
}