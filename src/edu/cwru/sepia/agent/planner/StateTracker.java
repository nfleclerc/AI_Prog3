package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.State;
import edu.cwru.sepia.environment.model.state.Unit;
import edu.cwru.sepia.util.Direction;
import edu.cwru.sepia.util.DistanceMetrics;

import java.awt.*;
import java.util.*;
import java.util.List;

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
    private Map<Integer, Position> peasants;
    private Map<Integer, Position> forests;
    private Map<Integer, Position> goldMines;

    /**
     * This constructor initializes this state tracker using another state tracker
     * @param stateTracker The state tracker to represent by this tracker
     */
    public StateTracker(StateTracker stateTracker) {
        xExtent = stateTracker.getXExtent();
        yExtent = stateTracker.getYExtent();
        turnNumber = stateTracker.getTurnNumber();
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
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public double getXExtent() {
        return xExtent;
    }

    public double getYExtent() {
        return yExtent;
    }

    public boolean isGoal() {
        return requiredWood == currentWood && requiredGold == currentGold;
    }
}