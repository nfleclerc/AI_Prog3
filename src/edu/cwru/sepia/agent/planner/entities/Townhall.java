package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.agent.planner.Position;

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

    @Override
    public boolean equals(Object o) {
        return o instanceof Townhall &&
                id == ((Townhall) o).id &&
                position.equals(((Townhall) o).position);
    }

}
