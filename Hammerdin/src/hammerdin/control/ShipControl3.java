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
import com.jme3.input.controls.ActionListener;
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
    public static final String DEBUG = "DEBUG";
    private InputManager inputManager = null;
    public Vector3f velocity = new Vector3f();
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
        this.inputManager = inputManager;
        System.out.println("mapped");
        inputManager.addListener(new AnalogListener() {

           
            @Override
            public void onAnalog(String name, float value, float tpf) {
                Vector3f t = spatial.getLocalTranslation();
                switch(name){
                     
                    //plus x = left
                    case SHIP_LEFT:
                        velocity.x += 1*tpf;
                        break;
                    //minus x = right
                    case  SHIP_RIGHT:
                        velocity.x -= 1*tpf;
                        break;
                    //plus z = up                   
                    case  SHIP_FORWARD:
                        velocity.z += 1*tpf;
                        break;
                    //minus z = down
                    case  SHIP_REVERSE:
                         velocity.z -= 1*tpf;
                        break;
                    //slow ship to a stop                   
                    case SHIP_STOP:
                          velocity.x = velocity.x/(1.1f);
                          velocity.z = velocity.z/(1.1f);
                        break;
                    case DEBUG:
                        debugOn = !debugOn;
                        break;
                }
               
            }
        }, SHIP_LEFT,SHIP_RIGHT,SHIP_FORWARD,SHIP_REVERSE,SHIP_STOP);
        
        
//        inputManager.addListener(new AnalogListener() {
//
//            @Override
//            public void onAnalog(String name, float value, float tpf) {
//                lookAtMouseVector = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0f);
//                lookAtMouseVector = lookAtMouseVector.mult(new Vector3f(1,0,1));
//                //System.out.println("click2d: " + click2d);
//                //System.out.println("click3d: " + lookAtMouseVector );
//                //System.out.println("x: " + spatial.getLocalTransform().getTranslation().x);
//                //System.out.println("z: " + spatial.getLocalTransform().getTranslation().z+ "\n");
//            }
//        }, M_X,M_Y);
//        
//        inputManager.addMapping(M_X, new MouseAxisTrigger(MouseInput.AXIS_X, false));
//        inputManager.addMapping(M_Y, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(SHIP_LEFT,new  KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(SHIP_RIGHT,new  KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(SHIP_FORWARD,new  KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(SHIP_REVERSE,new  KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(SHIP_STOP, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(DEBUG, new KeyTrigger(KeyInput.KEY_I));
    }
    int count = 0;
    boolean debugOn = false;
    @Override
    protected void controlUpdate(float tpf) {
        Vector3f camVec = cam.getLocation();
        
        //This creates a Quaternion that will point at the mouse.
        lookAtMouseVector = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0f);
        
        //Translate the vector to the origin so the quaternion can be calcualted correctly.
        lookAtMouseVector = lookAtMouseVector.subtract(containerNode.getLocalTranslation());
        lookAtMouseVector = lookAtMouseVector.mult(new Vector3f(1,0,1));
        Quaternion q = new Quaternion();
        q.lookAt(lookAtMouseVector, Vector3f.UNIT_Y);
        
        //Set the rotation
        spatial.setLocalRotation(q);
        
         //Add to the local translation based on the current velocity
        containerNode.setLocalTranslation(containerNode.getLocalTranslation().add(velocity));
        
        //debug loop
        count++;
        if(debugOn && count == 120)
        {
            System.out.println("cam: " + camVec);
            System.out.println("mouse: " + lookAtMouseVector);
            System.out.println("quat: " + q);
            System.out.println();
            count = 0;
        }
    }

    /**
     * Container node is what the camera is attached to.  This
     * allows the camera to stay still despite what the ship may do later on.
     * @param container 
     */
    public void setContainerNode(Node container) {
        containerNode = container;
    }
    
}
