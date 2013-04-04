/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitysystem.subsystem.debug;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import entitysystem.SubSystem;
import input.utils.KeySequence;
import input.utils.Keystroke;
import input.utils.KeystrokeSequence;
import java.util.ArrayList;
import java.util.Arrays;
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
            e.process();
            debugRenderer.preRender(e);
        }
        debugRenderer.render();
    }

    public void register(DebugEntity de) {
        debuggableEntities.add(de);
    }
    
    public void turnAllDebug(boolean state)
    {
        for(DebugEntity de : debuggableEntities)
        {
            de.on = state;
        }
    }
    
    public void initDebugKeyboardControl(InputManager im)
    {
        KeySequence keySequence = new KeySequence("Toggle All Debug On.", 
                new Keystroke(KeyInput.KEY_LCONTROL, Keystroke.Type.HELD),
                new Keystroke(KeyInput.KEY_Z, Keystroke.Type.TERMINAL));
        
        KeystrokeSequence ks = new KeystrokeSequence(im, Arrays.asList(keySequence));
        
        ks.callbacks.add(new KeystrokeSequence.Callback() {

            @Override
            public void run() {
                turnAllDebug(true);
            }
        });
        
        KeySequence keySequence2 = new KeySequence("Toggle All Debug Off.", 
                new Keystroke(KeyInput.KEY_LCONTROL, Keystroke.Type.HELD),
                new Keystroke(KeyInput.KEY_X, Keystroke.Type.TERMINAL));
        
        KeystrokeSequence ks2 = new KeystrokeSequence(im, Arrays.asList(keySequence2));
        
        ks2.callbacks.add(new KeystrokeSequence.Callback() {

            @Override
            public void run() {
                turnAllDebug(false);
            }
        });
        
        KeySequence keySequence3 = new KeySequence("Turn Off Debug Renderer.", 
                new Keystroke(KeyInput.KEY_LCONTROL, Keystroke.Type.HELD),
                new Keystroke(KeyInput.KEY_C, Keystroke.Type.TERMINAL));
        
        KeystrokeSequence ks3 = new KeystrokeSequence(im, Arrays.asList(keySequence3));
        
        ks3.callbacks.add(new KeystrokeSequence.Callback() {

            @Override
            public void run() {
                debugRenderer.toggle();
            }
        });
        
    }
    
}
