/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entitysystem.subsystem.debug;

import java.util.List;

/**
 *
 * @author aglassman
 */
public class DebugEntity {
    
    public final long id;
    public int updateOnCount = 1;
    private int count = 0;
    public boolean on = false;
    private DebugProxy proxy;
    private boolean getNewInfo;
    private List<String> currentInfo;
    public DebugEntity(long id,DebugProxy proxy)
    {
        this.id = id;
        this.proxy = proxy;
    }
    
    /**
     * 
     * @return true if there is info to display
     */
    public boolean process()
    {
        if(!on)
            return false;
        
        count++;
        if(count >= updateOnCount)
        {
            count = 0;
            getNewInfo = true;
            return true;
        }
        
        return false;
    }
    
    public List<String> getDebugInfo()
    {
        if(getNewInfo)
        {
            currentInfo = proxy.getDebugInfo();
            getNewInfo = false;
        }
        return currentInfo;
    }
}
