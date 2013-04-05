/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.control;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import entitysystem.subsystem.debug.DebugProxy;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aglassman
 */
public class AdvancedEasingFunction implements ShipEasingFunction, DebugProxy{

    float theta = 0;
    float invSinTheta = 0;
    float result = 0;
    float x = 0;
    float returnval = 0;
    @Override
    public float getSlurpFloat(Quaternion currentRotation, Quaternion newRotation, float tpf) {
        result =  (currentRotation.getX() * newRotation.getX()) + 
                        (currentRotation.getY() * newRotation.getY()) + 
                        (currentRotation.getZ() * newRotation.getZ()) +
                        (currentRotation.getW() * newRotation.getW());

        if (result < 0.0f) {
            newRotation.set(-newRotation.getX(), -newRotation.getY(), -newRotation.getZ(), -newRotation.getW());
            result = -result;
        }
        
        theta = FastMath.acos(result);
        //invSinTheta = 1f / FastMath.sin(theta);
        
        //x = 1 - result;
        //returnval = -1 * FastMath.pow(x, 2) +2* x+1.5f;
        
        return 1;
    }

    @Override
    public List<String> getDebugInfo() {
        return Arrays.asList(
                "result: " + result,
                "theta: " + theta);
        
    }
    
}
