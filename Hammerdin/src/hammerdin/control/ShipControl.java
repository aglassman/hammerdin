/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author aglassman
 */
public class ShipControl extends AbstractControl{

    public static final String SHIP_LEFT = "SHIP_LEFT";
    public static final String SHIP_RIGHT = "SHIP_RIGHT";
    public static final String SHIP_FORWARD = "SHIP_FORWARD";
    public static final String SHIP_REVERSE = "SHIP_REVERSE";
    public static final String SHIP_STOP = "SHIP_STOP";
    public Vector3f velocity = new Vector3f();
    
    
     @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        ShipControl control = new ShipControl();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }

    public void initKeyMapping(InputManager inputManager) {
        System.out.println("mapped");
        inputManager.addListener(new AnalogListener() {

           
            @Override
            public void onAnalog(String name, float value, float tpf) {
                switch(name){
                     
                    case SHIP_LEFT:
                        velocity.z = velocity.z - (tpf*50);
                        break;
                    case  SHIP_RIGHT:
                        velocity.z = velocity.z + (tpf*50);
                        break;
                    case  SHIP_FORWARD:
                        velocity.x = velocity.x + (tpf*50);
                        break;
                    case  SHIP_REVERSE:
                         velocity.x = velocity.x - (tpf*50);
                        break;
                    case SHIP_STOP:
                        velocity.x = velocity.x / (1.1f);
                        velocity.z = velocity.z / (1.1f);
                        break;
                }
            }
        }, SHIP_LEFT,SHIP_RIGHT,SHIP_FORWARD,SHIP_REVERSE,SHIP_STOP);
        
       
        inputManager.addMapping(SHIP_LEFT,new  KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(SHIP_RIGHT,new  KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(SHIP_FORWARD,new  KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(SHIP_REVERSE,new  KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(SHIP_STOP, new KeyTrigger(KeyInput.KEY_SPACE));
    }

    @Override
    protected void controlUpdate(float tpf) {
        spatial.setLocalTranslation(spatial.getLocalTranslation().add(velocity.mult(tpf)));
        
    }
    
}
