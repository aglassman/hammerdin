/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.appstate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import hammerdin.control.ShipControl;

/**
 *
 * @author aglassman
 */
public class ShipControlTestAppState extends AbstractAppState {
    
    SimpleApplication app;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication)app;
            initState();
    }
    
    private void initState()
    {
        System.out.println("inited");
        app.getRootNode().depthFirstTraversal(new SceneGraphVisitor() {

            public void visit(Spatial spatial) {
                System.out.println("visited: " );
                ShipControl shipControl = spatial.getControl(ShipControl.class);
                if(shipControl != null)
                {
                    System.out.println("found");
                    shipControl.initKeyMapping(app.getInputManager());
                    app.getCamera().lookAt(spatial.getWorldTranslation(), Vector3f.UNIT_X);
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
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
