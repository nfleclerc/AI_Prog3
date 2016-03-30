package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.Unit;

/**
 * Created by nathaniel on 3/27/16.
 */
public abstract class StripsUnit {
    protected int id;
    protected Position position;

    public StripsUnit(StripsUnit stripsUnit){
        this.id = stripsUnit.id;
        this.position = new Position(stripsUnit.getPosition());
    }

    public StripsUnit(Unit.UnitView unitView){
        id = unitView.getID();
        position = new Position(unitView.getXPosition(), unitView.getYPosition());
    }

    protected StripsUnit() {
    }

    public Position getPosition() {
        return position;
    }

    public int getID() {
        return id;
    }
}
