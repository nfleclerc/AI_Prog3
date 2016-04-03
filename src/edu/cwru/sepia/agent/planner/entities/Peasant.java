package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;
import edu.cwru.sepia.environment.model.state.ResourceType;
import edu.cwru.sepia.environment.model.state.Unit;
import edu.cwru.sepia.util.Direction;

import java.util.List;

/**
 * This class represents a peasant strips unit.
 */
public class Peasant extends StripsUnit{

    private int cargoAmount;
    private ResourceType cargoType;

    /**
     * Construct a clone of the provided peasant.
     * @param peasant The peasant to clone
     */
    public Peasant(Peasant peasant) {
        super(peasant);
        cargoAmount = peasant.cargoAmount;
        cargoType = peasant.cargoType;
    }

    /**
     * Construct a peasant from a UnitView object.
     * @param unitView The unit view to represent
     */
    public Peasant(Unit.UnitView unitView) {
        super(unitView);
        cargoAmount = unitView.getCargoAmount();
        cargoType = unitView.getCargoType();
    }

    /**
     * Construct a peasant at the specified townhall.
     * @param townhall The townhall from which this peasant will spawn
     * @param peasants A list of preexisting peasants (for ID-ing this one)
     */
    public Peasant(Townhall townhall, List<Peasant> peasants) {
        id = peasants.size() + 1;
        position = new Position(townhall.getPosition().move(Direction.WEST));
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

    /**
     * Get the amount of cargo this peasant is carrying.
     * @return This peasant's cargo amount
     */
    public int getCargoAmount() {
        return cargoAmount;
    }

    /**
     * Get the type of cargo this peasant is carrying.
     * @return This peasant's cargo type
     */
    public ResourceType getCargoType() {
        return cargoType;
    }

    /**
     * Set the amount of cargo this peasant is carrying.
     * @param cargoAmount The amount of cargo
     */
    public void setCargoAmount(int cargoAmount) {
        this.cargoAmount = cargoAmount;
    }

    /**
     * Set the type of cargo this peasant is carrying.
     * @param cargoType The type of cargo
     */
    public void setCargoType(ResourceType cargoType) {
        this.cargoType = cargoType;
    }

    /**
     * Command this peasant to start carrying cargo.
     * @param type   The type of cargo
     * @param amount The amount of cargo
     */
    public void carry(ResourceType type, int amount) {
        setCargoType(type);
        setCargoAmount(amount);
    }

    /**
     * Set the current position of this peasant.
     * @param position The position to place this peasant
     */
    public void setPosition(Position position) {
        this.position = position;
    }

}
