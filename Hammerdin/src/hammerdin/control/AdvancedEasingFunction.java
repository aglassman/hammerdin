/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.control;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;

/**
 *
 * @author aglassman
 */
public class AdvancedEasingFunction implements ShipEasingFunction{

    @Override
    public float getSlurpFloat(Quaternion currentRotation, Quaternion newRotation) {
        float result =  (currentRotation.getX() * newRotation.getX()) + 
                        (currentRotation.getY() * newRotation.getY()) + 
                        (currentRotation.getZ() * newRotation.getZ()) +
                        (currentRotation.getW() * newRotation.getW());

        if (result < 0.0f) {
            newRotation.set(-newRotation.getX(), -newRotation.getY(), -newRotation.getZ(), -newRotation.getW());
            result = -result;
        }
        float theta = 0;
        float invSinTheta = 0;
        
        theta = FastMath.acos(result);
        invSinTheta = 1f / FastMath.sin(theta);
         
        return 5f;
    }
    
}
