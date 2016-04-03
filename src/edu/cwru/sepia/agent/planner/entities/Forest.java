package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;

/**
 * This class represents a forest resource entity.
 */
public class Forest extends Resource {

    /**
     * Construct a forest from a ResourceView object.
     * @param resource The resource to represent
     */
    public Forest(ResourceNode.ResourceView resource) {
        super(resource);
        type = ResourceType.WOOD;
    }

    /**
     * Construct a forest from a Resource object.
     * @param resource The resource to specialize
     */
    public Forest(Resource resource) {
        super(resource);
        type = ResourceType.WOOD;
    }
}
