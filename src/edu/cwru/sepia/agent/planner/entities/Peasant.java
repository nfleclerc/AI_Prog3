package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.Unit;
import edu.cwru.sepia.util.Direction;

/**
 * Created by nathaniel on 3/21/16.
 *
 */
public class Peasant extends StripsUnit{

    private int cargoAmount;
    private ResourceType cargoType;

    public Peasant(Peasant peasant) {
        super(peasant);
        cargoAmount = peasant.cargoAmount;
        cargoType = peasant.cargoType;
    }

    public Peasant(Unit.UnitView unitView) {
        super(unitView);
        cargoAmount = unitView.getCargoAmount();
        cargoType = unitView.getCargoType();
    }

    public Peasant(Townhall townhall) {
        id = 2;
        position = new Position(townhall.getPosition().move(Direction.SOUTH));
        cargoAmount = 0;
        cargoType = null;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Peasant &&
                id == ((Peasant) o).id &&
                position.equals(((Peasant) o).getPosition()) &&
                cargoAmount == ((Peasant) o).getCargoAmount() &&
                cargoType == ((Peasant) o).getCargoType();
    }

    @Override
    public int hashCode(){
        return id +
                position.hashCode() +
                cargoAmount;
    }

    public int getCargoAmount() {
        return cargoAmount;
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

}
