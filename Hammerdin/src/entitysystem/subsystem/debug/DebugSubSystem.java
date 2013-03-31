/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitysystem.subsystem.debug;

import entitysystem.Entity;
import entitysystem.SubSystem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aglassman
 */
public class DebugSubSystem extends SubSystem{

    List<DebugEntity> debuggableEntities = new ArrayList<DebugEntity>();
    DebugRenderer debugRenderer = null;
    
    public DebugSubSystem(DebugRenderer debugRenderer)
    {
        this.debugRenderer = debugRenderer;
    }
    
    
    @Override
    public void process() {
        for(DebugEntity e : debuggableEntities)
        {
            if(e.process())
            {
                debugRenderer.preRender(e);
            }
        }
        debugRenderer.render();
    }

    public void register(DebugEntity de) {
        debuggableEntities.add(de);
    }
    
    
    
}
