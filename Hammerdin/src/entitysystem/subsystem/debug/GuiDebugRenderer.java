/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitysystem.subsystem.debug;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import java.util.List;

/**
 *
 * @author aglassman
 */
public class GuiDebugRenderer implements DebugRenderer{

    BitmapText debugText = null;
    public GuiDebugRenderer(SimpleApplication app)
    {
        BitmapFont debugFont = app.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
        int size = debugFont.getCharSet().getRenderedSize()+5;
        int width = 5;
        int height = app.getCamera().getHeight() - 5;
        debugText = new BitmapText(debugFont, false);
        debugText.setSize(size);
        debugText.setColor(ColorRGBA.Blue);
        debugText.setText("No Debug Text");
        debugText.setLocalTranslation(width,height,1);
        app.getGuiNode().attachChild(debugText);
    }
    
   StringBuilder sb = null;
    
    @Override
    public void preRender(DebugEntity e) {
        if(sb == null)
            sb = new StringBuilder();
        
        List<String> debugInfo = e.getDebugInfo();
        
        if(debugInfo == null)
            return;
        sb.append(String.format("Debug: Entity Id: %s\n",e.id));
        for(String s : debugInfo)
        {
            sb.append(s).append("\n");
        }
        sb.append("\n");
    }

    @Override
    public void render() {
        if(sb == null)
            return;
        
        if(debugText != null)
            debugText.setText(sb.toString());
        
        sb = null;
    }
        
}
