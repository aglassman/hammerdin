/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import entitysystem.subsystem.debug.DebugProxy;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aglassman
 */
public class ShipControl4 extends RigidBodyControl implements DebugProxy{
    
    public static final String SHIP_LEFT = "A";
    public static final String SHIP_RIGHT = "B";
    public static final String SHIP_FORWARD = "C";
    public static final String SHIP_REVERSE = "D";
    public static final String SHIP_STOP = "E";
    public static final String MOUSE_MOVE = "F";
    public static final String ROTATE_L = "G";
    public static final String ROTATE_R = "H";
    
    public ShipControl4(float mass)
    {
        super(mass);
    }
    
    
   
    
   
    
    public Control cloneForSpatial(Spatial spatial) {
        ShipControl3 control = new ShipControl3();
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

    private int constant = 100;
    private Vector3f left = Vector3f.UNIT_X.mult(constant);
    private Vector3f right = Vector3f.UNIT_X.mult(-constant);
    private Vector3f up = Vector3f.UNIT_Z.mult(constant);
    private Vector3f down = Vector3f.UNIT_Z.mult(-constant);
    
    public void initKeyMapping(final InputManager inputManager) {
        setAngularDamping(0f);
        inputManager.addListener(new AnalogListener() {

           
            @Override
            public void onAnalog(String name, float value, float tpf) {
                Vector3f t = spatial.getLocalTranslation();
                switch(name){
                     
                    //plus x = left
                    case SHIP_LEFT:
                        applyForce(left, Vector3f.ZERO);
                        break;
                    //minus x = right
                    case  SHIP_RIGHT:
                        applyForce(right, Vector3f.ZERO);
                        break;
                    //plus z = up                   
                    case  SHIP_FORWARD:
                        applyForce(up, Vector3f.ZERO);
                        break;
                    //minus z = down
                    case  SHIP_REVERSE:
                         applyForce(down, Vector3f.ZERO);
                        break;
                    case ROTATE_L:
                        applyTorqueImpulse(Vector3f.UNIT_Y.mult(2f));
                        break;
                        
                    case ROTATE_R:
                        applyTorqueImpulse(Vector3f.UNIT_Y.mult(-2f));
                        break;
                    //slow ship to a stop                   
                    case SHIP_STOP:
                          clearForces();
                        break;
                }
               
            }
        }, SHIP_LEFT,SHIP_RIGHT,SHIP_FORWARD,ROTATE_L,ROTATE_R,SHIP_REVERSE,SHIP_STOP);
        
        
        inputManager.addListener(new AnalogListener() {

            @Override
            public void onAnalog(String name, float value, float tpf) {
                calculateNewDirection();
            }
        }, MOUSE_MOVE);
        
        inputManager.addMapping(MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(SHIP_LEFT,new  KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(SHIP_RIGHT,new  KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(SHIP_FORWARD,new  KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(SHIP_REVERSE,new  KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(SHIP_STOP, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(ROTATE_L, new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(ROTATE_R, new KeyTrigger(KeyInput.KEY_RIGHT));
    }
    
    /**
     * This sets the direction quaternion to look at the current 
     * mouse position from the ship position.
     */
    private void calculateNewDirection()
    {
        //Do nothing atm.
    }
     
    
    @Override
    public void update(float tpf) {
        super.update(tpf);//update rigid body control
    }

   
    @Override
    public List<String> getDebugInfo() {
        return Arrays.asList(
                "Ship - Hello World",
                "location: " + getPhysicsLocation(),
                "physicsRotation: " + getPhysicsRotation(),
                "linearVelocity: " + getLinearVelocity(),
                "angularVelocity: " + getAngularVelocity());
    }
}
