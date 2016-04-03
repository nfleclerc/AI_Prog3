package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.Unit;

/**
 * This class represents a unit which can perform a strips action.
 */
public abstract class StripsUnit {

    protected int id;
    protected Position position;

    /**
     * Construct a clone of the provided strips unit.
     * @param stripsUnit The strips unit to reconstruct
     */
    public StripsUnit(StripsUnit stripsUnit){
        this.id = stripsUnit.id;
        this.position = new Position(stripsUnit.getPosition());
    }

    /**
     * Construct a strips unit from a UnitView object.
     * @param unitView The unit view to represent
     */
    public StripsUnit(Unit.UnitView unitView){
        id = unitView.getID();
        position = new Position(unitView.getXPosition(), unitView.getYPosition());
    }

    /**
     * Construct a default strips unit.
     */
    protected StripsUnit() {

    }

    /**
     * Get the position of this strips unit.
     * @return The current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the ID of this strips unit.
     * @return The unit ID
     */
    public int getID() {
        return id;
    }
}
