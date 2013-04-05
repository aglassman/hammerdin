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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import entitysystem.subsystem.debug.DebugProxy;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aglassman
 */
public class ShipControl3 extends AbstractControl implements DebugProxy{

    public static final String SHIP_LEFT = "SHIP_LEFT";
    public static final String SHIP_RIGHT = "SHIP_RIGHT";
    public static final String SHIP_FORWARD = "SHIP_FORWARD";
    public static final String SHIP_REVERSE = "SHIP_REVERSE";
    public static final String SHIP_STOP = "SHIP_STOP";
    public static final String MOUSE_MOVE = "MOUSE_MOVE";
    public static final String DEBUG = "DEBUG";
    private InputManager inputManager = null;
    public Vector3f velocity = new Vector3f();
    
    private ShipEasingFunction sef;
    
    
    private Quaternion direction = new Quaternion();
    
    private Node containerNode;
    public float maxVelocity = 5f;
    private Camera cam = null;
    
    public void setCam(Camera cam)
    {
        this.cam = cam;
    }
    
    public void setShipEasingFunction(ShipEasingFunction sef)
    {
        this.sef = sef;
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
                        velocity.x += 90*tpf;
                        break;
                    //minus x = right
                    case  SHIP_RIGHT:
                        velocity.x -= 90*tpf;
                        break;
                    //plus z = up                   
                    case  SHIP_FORWARD:
                        velocity.z += 90*tpf;
                        break;
                    //minus z = down
                    case  SHIP_REVERSE:
                         velocity.z -= 90*tpf;
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
    }
    
    /**
     * This sets the direction quaternion to look at the current 
     * mouse position from the ship position.
     */
    private void calculateNewDirection()
    {
        //get cursor position in world coordinates.
        Vector3f worldPostionOfMouse = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0f);
        
        //Treat position as vector, and translate it to the origin so the quaternion can be calcualted correctly.
        worldPostionOfMouse = worldPostionOfMouse.subtract(containerNode.getLocalTranslation());
        worldPostionOfMouse.y = 0;//worldPostionOfMouse = worldPostionOfMouse.mult(new Vector3f(1,0,1));
        direction.lookAt(worldPostionOfMouse, Vector3f.UNIT_Y);
    }
    
    int count = 0;
    boolean debugOn = false;
    @Override
    protected void controlUpdate(float tpf) {
        /* This is what makes the ship rotate slower.
         * We will probably have to use some sort of function to determine
         * what float value to use in the slerp command.  Different functions
         * will give different weightly feels to the ship control.
         */
        //spatial.getLocalRotation().0nhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
        Quaternion newShipRot =  spatial.getLocalRotation();
        newShipRot.slerp(direction, sef.getSlurpFloat(spatial.getLocalRotation(), direction,tpf));
        //spatial.setLocalRotation(newShipRot);
        //sef.getSlurpFloat(spatial.getLocalRotation(), direction,tpf);
         //Add to the local translation based on the current velocity
        containerNode.setLocalTranslation(containerNode.getLocalTranslation().add(velocity.mult(tpf)));
        
        //debug loop
        count++;
        if(debugOn && count == 120)
        {
            //System.out.println("cam: " + camVec);
            //System.out.println("mouse: " + lookAtMouseVector);
            //System.out.println("quat: " + q);
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

    @Override
    public List<String> getDebugInfo() {
        return Arrays.asList(
                "Ship - Hello World",
                "direction: " + direction.toString(),
                "velocity: " + velocity.toString());
    }
    
}
