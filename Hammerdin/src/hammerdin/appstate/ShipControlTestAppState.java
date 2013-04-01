/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.appstate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import entitysystem.EntitySystem;
import entitysystem.subsystem.debug.DebugEntity;
import entitysystem.subsystem.debug.DebugSubSystem;
import entitysystem.subsystem.debug.GuiDebugRenderer;
import hammerdin.control.AdvancedEasingFunction;
import hammerdin.control.ShipControl;
import hammerdin.control.ShipControl2;
import hammerdin.control.ShipControl3;

/**
 *
 * @author aglassman
 */
public class ShipControlTestAppState extends AbstractAppState {
    
    SimpleApplication app;
    EntitySystem es = new EntitySystem();
    DebugSubSystem dss = null;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication)app;
                   
        dss = new DebugSubSystem(new GuiDebugRenderer(this.app));
        es.registerSubSystem(dss);
        
        initState();
    }
    
    private void initState()
    {
        final int testControl = 3;
        System.out.println("inited");
        Spatial scene = app.getAssetManager().loadModel("Scenes/ShipControlTestScene.j3o");
        app.getRootNode().attachChild(scene);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        app.getRootNode().addLight(sun);
        app.getRootNode().depthFirstTraversal(new SceneGraphVisitor() {

            public void visit(Spatial spatial) {
                if(spatial.getName().equals("ship"))
                {
                    if(testControl == 1)
                        setupControl1(spatial);
                    else if(testControl == 2)
                        setupControl2(spatial);
                    else if(testControl == 3)
                        setupControl3(spatial);
                }
            }
        });
        
    }
    
    @Override
    public boolean isEnabled()
    {
        return super.isEnabled() && app != null;
    }
    
    @Override
    public void update(float tpf) {
        es.process();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    private void setupControl1(Spatial spatial)
    {
        System.out.println("found");
                    Node container = ((Node)spatial);
                    Spatial ship =  container.getChildren().get(0);
                    ShipControl shipControl = new ShipControl();
                    shipControl.setContainerNode(container);
                    shipControl.initKeyMapping(app.getInputManager());
                    ship.addControl(shipControl);
                    
                    app.getFlyByCamera().setEnabled(false);
        
                    //create the camera Node
                    CameraNode camNode = new CameraNode("Camera Node", app.getCamera());
                    //This mode means that camera copies the movements of the target:
                    camNode.setControlDir(ControlDirection.SpatialToCamera);
                    //Attach the camNode to the target:
                    container.attachChild(camNode);
                    //Move camNode, e.g. behind and above the target:
                    camNode.setLocalTranslation(new Vector3f(0, 100, 0));
                    //Rotate the camNode to look at the target:
                    camNode.lookAt(((Node)spatial).getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    private void setupControl2(Spatial spatial)
    {
        ShipControl2 shipControl = new ShipControl2();
                    shipControl.initKeyMapping(app.getInputManager());
                    spatial.addControl(shipControl);
        app.getCamera().setLocation(new Vector3f(0, 100, 0));
        app.getCamera().lookAt(app.getRootNode().getWorldTranslation(), Vector3f.UNIT_Z);
        
    }
    
    private void setupControl3(Spatial spatial)
    {
        System.out.println("found");
        Node container = ((Node)spatial);
        Spatial ship =  container.getChildren().get(0);
        ShipControl3 shipControl = new ShipControl3();
        shipControl.setContainerNode(container);
        shipControl.initKeyMapping(app.getInputManager());
        ship.addControl(shipControl);

        app.getFlyByCamera().setEnabled(false);

        //create the camera Node
        CameraNode camNode = new CameraNode("Camera Node", app.getCamera());
        //This mode means that camera copies the movements of the target:
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        //Attach the camNode to the target:
        container.attachChild(camNode);
        //Move camNode, e.g. behind and above the target:
        camNode.setLocalTranslation(new Vector3f(0, 100, 0));
        //Rotate the camNode to look at the target:
        camNode.lookAt(((Node)spatial).getLocalTranslation(), Vector3f.UNIT_Y);
        app.getInputManager().setCursorVisible(true);
        shipControl.setCam(camNode.getCamera());
        shipControl.setShipEasingFunction(new AdvancedEasingFunction());
        
        DebugEntity de = new DebugEntity(1l, shipControl);
        de.updateOnCount = 100;
        de.on = true;
        dss.register(de);
        
        dss.initDebugKeyboardControl(app.getInputManager());
        
    }
}
