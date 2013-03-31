package hammerdin;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import hammerdin.appstate.ShipControlTestAppState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        AppState a = new ShipControlTestAppState();
        a.setEnabled(true);
        this.getStateManager().attach(a);
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        try {
            Thread.currentThread().sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
