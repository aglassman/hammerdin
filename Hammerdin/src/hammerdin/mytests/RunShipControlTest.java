package hammerdin.mytests;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import hammerdin.appstate.ShipControlTestAppState;

/**
 * test
 * @author normenhansen
 */
public class RunShipControlTest extends SimpleApplication {

    
    
    public static void main(String[] args) {
        RunShipControlTest app = new RunShipControlTest();
        app.start();
    }
    
     private static RunShipControlTest currentInstance;
     protected Geometry player;
     Boolean isRunning=true;
    
     
     
    @Override
    public void simpleInitApp() {
        Spatial scene = assetManager.loadModel("Scenes/ShipControlTestScene.j3o");
        rootNode.attachChild(scene);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
        AppState a = new ShipControlTestAppState();
        a.setEnabled(true);
        this.getStateManager().attach(a);
        
        getCamera().setLocation(new Vector3f(0, 100, 0));
       
        
        
    }
 /** Custom Keybinding: Map named actions to inputs. */
  private void initKeys() {
    // You can map one or several inputs to one named action
   
    inputManager.clearMappings();
      
    //inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
   //inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
     //                                 new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    // Add the names to the action listener.
   //inputManager.addListener(analogListener, new String[]{"Left", "Right", "Rotate"});
 
  }
 
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("Pause") && !keyPressed) {
        isRunning = !isRunning;
      }
    }
  };
 
  private AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
      if (isRunning) {
        if (name.equals("Rotate")) {
          player.rotate(0, value*speed, 0);
        }
        if (name.equals("Right")) {
          Vector3f v = player.getLocalTranslation();
          player.setLocalTranslation(v.x + value*speed, v.y, v.z);
        }
        if (name.equals("Left")) {
          Vector3f v = player.getLocalTranslation();
          player.setLocalTranslation(v.x - value*speed, v.y, v.z);
        }
      } else {
        System.out.println("Press P to unpause.");
      }
    }
  };
}
