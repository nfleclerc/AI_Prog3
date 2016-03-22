package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.Unit;

/**
 * Created by nathaniel on 3/21/16.
 *
 */
public class Peasant {

    private int id;
    private Position position;
    private int cargoAmount;
    private ResourceType cargoType;
    private boolean idle;

    public Peasant(Unit.UnitView unit){
        id = unit.getID();
        position = new Position(unit.getXPosition(), unit.getYPosition());
        cargoAmount = unit.getCargoAmount();
        cargoType = unit.getCargoType();
    }

    public Peasant(Peasant peasant){
        id = peasant.id;
        position = new Position(peasant.position);
        cargoAmount = peasant.cargoAmount;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Peasant &&
                id == ((Peasant) o).id;
    }

    public int getCargoAmount() {
        return cargoAmount;
    }

    public boolean isIdle() {
        return idle;
    }

    public Position getPosition() {
        return position;
    }
}
