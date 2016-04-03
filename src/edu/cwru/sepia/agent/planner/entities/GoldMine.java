package edu.cwru.sepia.agent.planner.entities;

import edu.cwru.sepia.environment.model.state.ResourceNode;
import edu.cwru.sepia.environment.model.state.ResourceType;

/**
 * This class represents a goldmine resource entity.
 */
public class Goldmine extends Resource{

    /**
     * Construct a goldmine from a ResourceView object.
     * @param resource The resource to represent
     */
    public Goldmine(ResourceNode.ResourceView resource) {
        super(resource);
        type = ResourceType.GOLD;
    }

    /**
     * Construct a goldmine from a Resource object.
     * @param resource The resource to specialize
     */
    public Goldmine(Resource resource) {
        super(resource);
        type = ResourceType.GOLD;
    }

}
