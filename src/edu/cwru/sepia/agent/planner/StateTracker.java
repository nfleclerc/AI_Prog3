package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.agent.planner.entities.*;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.State;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private boolean buildPeasants;
    private List<Peasant> peasants = new ArrayList<>();
    private List<GoldMine> goldMines = new ArrayList<>();
    private List<Forest> forests = new ArrayList<>();
    private List<Townhall> townhalls = new ArrayList<>();

    /**
     * This constructor initializes this state tracker using another state tracker
     *
     * @param stateTracker The state tracker to represent by this tracker
     */
    public StateTracker(StateTracker stateTracker) {
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
        townhalls.addAll(stateTracker.townhalls.stream().map(Townhall::new).collect(Collectors.toList()));
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
        peasants.addAll(state.getAllUnits().stream().map(Peasant::new).collect(Collectors.toList()));
        for (ResourceNode.ResourceView resource : state.getAllResourceNodes()) {
            if (resource.getType() == ResourceNode.Type.GOLD_MINE) {
                goldMines.add(new GoldMine(resource));
            } else {
                forests.add(new Forest(resource));
            }
        }
    }

    public boolean isGoal() {
        return requiredWood == currentWood && requiredGold == currentGold;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof StateTracker &&
                playerNum == ((StateTracker) o).playerNum &&
                currentWood == ((StateTracker) o).currentWood &&
                currentGold == ((StateTracker) o).currentGold &&
                peasants.equals(((StateTracker) o).peasants) &&
                goldMines.equals(((StateTracker) o).goldMines) &&
                forests.equals(((StateTracker) o).forests) &&
                townhalls.equals(((StateTracker) o).townhalls);
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

    public List<Townhall> getTownhalls() {
        return townhalls;
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
}