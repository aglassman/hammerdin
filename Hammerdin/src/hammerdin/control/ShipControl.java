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
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
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
    public float velocity = 0f;
    public float angVelocity = 0f;
    public float direction = 0f;
    public Quaternion quat = new Quaternion(0, 1, 0, 1);
    
    public float maxVelocity = 5f;
    
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
                        angVelocity = angVelocity <= 2*FastMath.PI ? angVelocity += 6*tpf : angVelocity;
                        break;
                    case  SHIP_RIGHT:
                        angVelocity = angVelocity >= -2*FastMath.PI ? angVelocity -= 6*tpf : angVelocity;
                        break;
                    case  SHIP_FORWARD:
                        velocity = velocity <= maxVelocity ? velocity +=  (tpf*1) : velocity;
                        break;
                    case  SHIP_REVERSE:
                         velocity = velocity >= -maxVelocity ? velocity -= (tpf*1) : velocity;
                        break;
                    case SHIP_STOP:
                        velocity = velocity / (1.1f);
                        angVelocity = angVelocity / (1.06f);
                       
                        break;
                }
                System.out.println(
                        String.format("v: %s d: %s tpf: %s, val: %s name: %s"
                            ,velocity,direction,tpf, value, name));
            }
        }, SHIP_LEFT,SHIP_RIGHT,SHIP_FORWARD,SHIP_REVERSE,SHIP_STOP);
        
       
        inputManager.addMapping(SHIP_LEFT,new  KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(SHIP_RIGHT,new  KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(SHIP_FORWARD,new  KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(SHIP_REVERSE,new  KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(SHIP_STOP, new KeyTrigger(KeyInput.KEY_SPACE));
    }
    int count = 0;
    @Override
    protected void controlUpdate(float tpf) {
        direction += angVelocity*tpf;
        count++;
        Quaternion newDirection = new Quaternion().fromAngleAxis(direction, Vector3f.UNIT_Y);
        //get scaled bank based on angVelocity, and max angVelocity of PI
        Quaternion bank = new Quaternion().fromAngleAxis((-angVelocity/(2*FastMath.PI) * FastMath.HALF_PI), Vector3f.UNIT_Z);
        Vector3f velocityMod = newDirection.mult(Vector3f.UNIT_Z).mult(velocity);
        newDirection = newDirection.mult(bank);
        spatial.setLocalRotation(newDirection);
        
        spatial.setLocalTranslation(spatial.getLocalTranslation().add(velocityMod));
        
        if(count == 60)
        {
            System.out.println("newDir: " + newDirection);
            System.out.println("velMod: " + velocityMod);
            count = 0;
        }
        
    }
    
}
