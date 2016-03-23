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

    //todo: some way of keeping track of business and knowing the current action

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
        cargoType = peasant.cargoType;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Peasant &&
                id == ((Peasant) o).id &&
                position.equals(((Peasant) o).getPosition()) &&
                cargoAmount == ((Peasant) o).getCargoAmount() &&
                cargoType == ((Peasant) o).getCargoType();
    }

    public int getCargoAmount() {
        return cargoAmount;
    }

    public Position getPosition() {
        return position;
    }

    public ResourceType getCargoType() {
        return cargoType;
    }

    public void setCargoAmount(int cargoAmount) {
        this.cargoAmount = cargoAmount;
    }

    public void setCargoType(ResourceType cargoType) {
        this.cargoType = cargoType;
    }

    public void carry(ResourceType type, int cargoAmount) {
        setCargoType(type);
        setCargoAmount(cargoAmount);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getID() {
        return id;
    }

    //todo: hashcode
}
