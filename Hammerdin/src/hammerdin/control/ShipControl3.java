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
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author aglassman
 */
public class ShipControl3 extends AbstractControl{

    public static final String SHIP_LEFT = "SHIP_LEFT";
    public static final String SHIP_RIGHT = "SHIP_RIGHT";
    public static final String SHIP_FORWARD = "SHIP_FORWARD";
    public static final String SHIP_REVERSE = "SHIP_REVERSE";
    public static final String SHIP_STOP = "SHIP_STOP";
    public static final String M_X = "MOUSE_MOVEMENT_X";
    public static final String M_Y = "MOUSE_MOVEMENT_Y";
    
    public float velocity = 0f;
    //public float angVelocity = 0f;
    public float direction = 0f;
    public Quaternion quat = new Quaternion(0, 1, 0, 1);
    private Node containerNode;
    private Vector3f lookAtMouseVector = new Vector3f();
    public float maxVelocity = 5f;
    private Camera cam = null;
    
    public void setCam(Camera cam)
    {
        this.cam = cam;
    }
    
    
     @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
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

    public void initKeyMapping(final InputManager inputManager) {
        System.out.println("mapped");
        inputManager.addListener(new AnalogListener() {

           
            @Override
            public void onAnalog(String name, float value, float tpf) {
                switch(name){
                     
                    case SHIP_LEFT:
                        direction = direction += 6*tpf;
                        break;
                    case  SHIP_RIGHT:
                        direction = direction -= 6*tpf;
                        break;
                    case  SHIP_FORWARD:
                        velocity = velocity <= maxVelocity ? velocity +=  (tpf*1) : velocity;
                        break;
                    case  SHIP_REVERSE:
                         velocity = velocity >= -maxVelocity ? velocity -= (tpf*1) : velocity;
                        break;
                    case SHIP_STOP:
                        velocity = velocity / (1.1f);
                        //angVelocity = angVelocity / (1.06f);
                       
                        break;
                }
                System.out.println(
                        String.format("v: %s d: %s tpf: %s, val: %s name: %s"
                            ,velocity,direction,tpf, value, name));
            }
        }, SHIP_LEFT,SHIP_RIGHT,SHIP_FORWARD,SHIP_REVERSE,SHIP_STOP);
        
        
        inputManager.addListener(new AnalogListener() {

            @Override
            public void onAnalog(String name, float value, float tpf) {
                Vector2f click2d = inputManager.getCursorPosition();
                lookAtMouseVector = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                lookAtMouseVector = lookAtMouseVector.mult(new Vector3f(1,0,1));
                System.out.println("click2d: " + click2d);
                System.out.println("click3d: " + lookAtMouseVector );
                System.out.println("x: " + spatial.getLocalTransform().getTranslation().x);
                System.out.println("z: " + spatial.getLocalTransform().getTranslation().z+ "\n");
            }
        }, M_X,M_Y);
        
        inputManager.addMapping(M_X, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(M_Y, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(SHIP_LEFT,new  KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(SHIP_RIGHT,new  KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(SHIP_FORWARD,new  KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(SHIP_REVERSE,new  KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(SHIP_STOP, new KeyTrigger(KeyInput.KEY_SPACE));
    }
    int count = 0;
    boolean countOn = true;
    @Override
    protected void controlUpdate(float tpf) {
        //direction += angVelocity*tpf;
        count++;
        Quaternion newDirection = new Quaternion().fromAngleAxis(direction, Vector3f.UNIT_Y);
        //get scaled bank based on angVelocity, and max angVelocity of PI
        //Quaternion bank = new Quaternion().fromAngleAxis((direction/(2*FastMath.PI) * FastMath.HALF_PI), Vector3f.UNIT_Z);
        Vector3f velocityMod = newDirection.mult(Vector3f.UNIT_Z).mult(velocity);
        //newDirection = newDirection.mult(bank);
       
        
        //spatial.setLocalTranslation(spatial.getLocalTranslation().add(velocityMod));
     
          Quaternion q = new Quaternion();
         lookAtMouseVector =  lookAtMouseVector.normalize();
       Vector3f a = Vector3f.UNIT_Z.cross(lookAtMouseVector);
       q = new Quaternion(a.x, a.y, a.z, 
               FastMath.sqrt(FastMath.sqr(Vector3f.UNIT_Z.length()) * FastMath.sqr(lookAtMouseVector.length())));// + Vector3f.UNIT_Z.mult(lookAtMouseVector));
        
        spatial.setLocalRotation(q);
        
        if(countOn && count == 60)
        {
            //System.out.println("newDir: " + newDirection);
            //System.out.println("velMod: " + velocityMod);
            System.out.println("quat: " + q);
            count = 0;
        }
        containerNode.setLocalTranslation(containerNode.getLocalTranslation().add(velocityMod));
        
     
        
    }

    public void setContainerNode(Node container) {
        containerNode = container;
    }
    
}
