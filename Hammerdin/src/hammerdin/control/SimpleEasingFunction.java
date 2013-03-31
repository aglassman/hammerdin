/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hammerdin.control;

import com.jme3.math.Quaternion;

/**
 *
 * @author aglassman
 */
public class SimpleEasingFunction implements ShipEasingFunction{

    @Override
    public float getSlurpFloat(Quaternion currentRotation, Quaternion newRotation) {
        return 3f;
    }
    
}
