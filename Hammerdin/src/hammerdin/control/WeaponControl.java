/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.control;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import entitysystem.subsystem.debug.DebugProxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aglassman
 */
public class WeaponControl extends AbstractControl implements DebugProxy{

    public static String SPACE = "SPACEBAR";
    public PhysicsSpace ps;
    private Material stone_mat;
    private static Sphere sphere;
    private SimpleApplication app;
    private RigidBodyControl ball_phy  = null;
    public WeaponControl(SimpleApplication app,PhysicsSpace ps)
    {
        super();
        init(app);
        this.ps = ps;
        this.app = app;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    void init(SimpleApplication app)
    {
        app.getInputManager().addListener(new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if(isPressed)
                {
                    makeAndAttachBullet();
                }
            }
        }, SPACE);
        
        app.getInputManager().addMapping(SPACE,new  KeyTrigger(KeyInput.KEY_SPACE));
        
        stone_mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        stone_mat.setColor("Color", ColorRGBA.Red);
        sphere = new Sphere(32, 32, 1f, true, false);
        sphere.setTextureMode(TextureMode.Projected);
    }
    int count = 0;
    void makeAndAttachBullet()
    {
        count++;
         
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        app.getRootNode().attachChild(ball_geo);
        /** Position the cannon ball  */
        Vector3f ballPos = spatial.getLocalTranslation().clone();
        ballPos.y +=20;
        ball_geo.setLocalTranslation(ballPos);
        /** Make the ball physcial with a mass > 0.0f */
        ball_phy = new RigidBodyControl(1f);
        /** Add physical ball to physics space. */
        ball_geo.addControl(ball_phy);
        ps.getPhysicsSpace().add(ball_phy);
        ps.getPhysicsSpace().setWorldMax(new Vector3f(50, 50,50));
        /** Accelerate the physcial ball to shoot it. */
        if(false)
        {
            //aim at mouse
            Vector3f worldPostionOfMouse = app.getCamera().getWorldCoordinates(app.getInputManager().getCursorPosition(), 0f);
            worldPostionOfMouse = worldPostionOfMouse.subtract(spatial.getLocalTranslation());
            worldPostionOfMouse.y = 0;
            ball_phy.setLinearVelocity(
                    worldPostionOfMouse.normalize().mult(100)
                    .add(spatial.getControl(ShipControl4.class).getLinearVelocity()));
        }
        else
        {
            //aim in direction of ship nose
            ball_phy.setLinearVelocity(
                    spatial.getControl(ShipControl4.class).getPhysicsRotation().getRotationColumn(2).mult(100)
                    .add(spatial.getControl(ShipControl4.class).getLinearVelocity()));
        }
        
         
    }

    @Override
    public List<String> getDebugInfo() {
        if(ball_phy == null)
            return new ArrayList<String>();
        return Arrays.asList("Last Bullet's Info: ",
                "Number of bullets: " + count,
                "LinearVelocity: " + ball_phy.getLinearVelocity(),
                "Position: " + ball_phy.getPhysicsLocation());
    }
}
