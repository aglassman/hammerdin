package entitysystem;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aglassman
 */
public class EntitySystem {
        
    List<SubSystem> subSystems = new ArrayList<SubSystem>();
    
    public void registerSubSystem(SubSystem subSystem)
    {
        subSystems.add(subSystem);
    }
    
    public void process()
    {
        for(SubSystem ss : subSystems)
        {
            ss.process();
        }
    }
}
