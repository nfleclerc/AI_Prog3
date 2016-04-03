package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.environment.model.state.Unit;

/**
 * This class represents a townhall strips unit.
 */
public class Townhall extends StripsUnit{

    /**
     * Construct a clone of the provided townhall.
     * @param townhall The townhall to clone
     */
    public Townhall(Townhall townhall) {
        super(townhall);
    }

    /**
     * Construct a townhall from a UnitView object.
     * @param unitView The unit view to represent
     */
    public Townhall(Unit.UnitView unitView) {
        super(unitView);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Townhall
                && id == ((Townhall) o).id
                && position.equals(((Townhall) o).position);
    }

    @Override
    public int hashCode(){
        return id + position.hashCode();
    }
}
