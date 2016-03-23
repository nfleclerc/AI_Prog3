package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.Unit;

/**
 * Created by nathaniel on 3/21/16.
 */
public class Townhall {

    private int id;
    private Position position;

    public Townhall(Townhall townhall){
        id = townhall.id;
        position = new Position(townhall.position);
    }

    public Townhall(Unit.UnitView unitView){
        id = unitView.getID();
        position = new Position(unitView.getXPosition(), unitView.getYPosition());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Townhall &&
                id == ((Townhall) o).id &&
                position.equals(((Townhall) o).position);
    }

    public Position getPosition() {
        return position;
    }

    public int getID() {
        return id;
    }

    @Override
    public int hashCode(){
        return id + position.hashCode();
    }
}
