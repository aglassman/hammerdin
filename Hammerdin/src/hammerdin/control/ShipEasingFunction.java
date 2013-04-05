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
public interface ShipEasingFunction {
    public float getSlurpFloat(Quaternion currentRotation, Quaternion newRotation,float tpf);
}
