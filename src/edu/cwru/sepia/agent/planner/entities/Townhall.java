package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.Unit;

/**
 * Created by nathaniel on 3/21/16.
 */
public class Townhall extends StripsUnit{


    public Townhall(Townhall townhall) {
        super(townhall);
    }

    public Townhall(Unit.UnitView unitView) {
        super(unitView);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Townhall &&
                id == ((Townhall) o).id &&
                position.equals(((Townhall) o).position);
    }

    @Override
    public int hashCode(){
        return id + position.hashCode();
    }
}
