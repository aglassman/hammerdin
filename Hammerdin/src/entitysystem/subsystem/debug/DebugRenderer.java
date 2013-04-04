/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitysystem.subsystem.debug;

/**
 *
 * @author aglassman
 */
public interface DebugRenderer {

    public void preRender(DebugEntity e);

    public void render();

    public void toggle();
    
    public boolean isOn();
    
}
