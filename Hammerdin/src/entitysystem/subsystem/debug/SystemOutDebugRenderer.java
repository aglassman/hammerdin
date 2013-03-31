/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitysystem.subsystem.debug;

import java.util.List;

/**
 *
 * @author aglassman
 */
public class SystemOutDebugRenderer implements DebugRenderer{

    StringBuilder sb = null;
    
    @Override
    public void preRender(DebugEntity e) {
        if(sb == null)
            sb = new StringBuilder();
        
        List<String> debugInfo = e.getDebugInfo();
        
        if(debugInfo == null)
            return;
        sb.append(String.format("Debug: Entity Id: %s",e.id));
        for(String s : debugInfo)
        {
            sb.append(s).append("\n");
        }
        sb.append("\n");
    }

    @Override
    public void render() {
        if(sb == null)
            return;
        
        System.out.println(sb.toString());
        sb = null;
    }

    
}
