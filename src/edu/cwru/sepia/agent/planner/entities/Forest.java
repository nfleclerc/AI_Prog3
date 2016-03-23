package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;

/**
 * Created by nathaniel on 3/21/16.
 *
 */
public class Forest extends Resource{

    public Forest(ResourceNode.ResourceView resource) {
        super(resource);
        type = ResourceType.WOOD;
    }

    public Forest(Resource resource) {
        super(resource);
        type = ResourceType.WOOD;
    }
}
