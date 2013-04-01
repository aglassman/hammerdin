/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package input.utils;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aglassman
 */
public class KeystrokeSequence {
    
    public final List<Callback> callbacks = new ArrayList<Callback>();
    
    public KeystrokeSequence(InputManager im, List<KeySequence> inputSequences)
    {

        for(final KeySequence ks : inputSequences)
        {
            String[] mappingNames = new String[ks.sequence.length];
            for(int index = 0; index < ks.sequence.length; index++)
            {
                System.out.println("added mapping: " + ks.sequence[index]);
                im.addMapping(ks.sequence[index].toString(), new KeyTrigger(ks.sequence[index].key));
                mappingNames[index] = ks.sequence[index].toString();
            }
           
            System.out.println("MappingNames: " + Arrays.toString(mappingNames));
            
            im.addListener(new ActionListener() {

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
               for(KeySequenceType kst : ks.sequence)
                {
                    if(kst.currentValue == 0 && !Integer.toString(kst.key).equals(name))
                    {
                        System.out.println("A");
                        //A previous key was not activated, sequence failed.
                        break;
                    }
                    
                    //If key pressed is current key, do some evaluation
                    if(Integer.toString(kst.key).equals(name))
                    {
                        System.out.println("B");
                        
                       
                        //If it is pressed, and is of hold type, set to one and continue
                        if(isPressed && kst.sequenceType.equals(SequenceType.HELD))
                        {
                            System.out.println("B1");
                            kst.currentValue = 1;
                            break;
                        }
                        
                        //If it is released and is of held type, no longer valid, break.
                        if(!isPressed && kst.sequenceType.equals(SequenceType.HELD))
                        {
                            System.out.println("B2");
                            resetSequence(ks);
                            break;
                        }
                        
                        //If it is pressed, yet already set, fail.
                        if(isPressed && kst.currentValue == 1 && kst.sequenceType.equals(SequenceType.PRESSED))
                        {
                            System.out.println("B3");
                            resetSequence(ks);
                            break;
                        }
                        
                        //If it is released, and type was pressed, set to valid.
                        if(!isPressed && kst.sequenceType.equals(SequenceType.PRESSED))
                        {
                            System.out.println("B4");
                            kst.currentValue = 1;
                            break;
                        }
                        
                        //If it is pressed, and is a terminal, and it made it this far, 
                        // the sequence was completed correctly.
                        if(isPressed && kst.sequenceType.equals(SequenceType.TERMINAL))
                        {
                            System.out.println("B5");
                            resetSequenceAndRunCallback(ks);
                            break;
                        }
                    }
                }
                System.out.println("Action: " + name + " isPressed: " + isPressed +  " state: " + ks.getState());
                
            }
            },  mappingNames);
            
            
        }
        
    }
    
    public void resetSequenceAndRunCallback(KeySequence ks)
    {
        System.out.println("Sequence Completed: " + ks.toString());
        
        for(Callback c: callbacks)
            c.run();
        
        resetSequence(ks);
        
    }
    
    public void resetSequence(KeySequence ks)
    {
        System.out.println("Sequence Reset");
        
        for(KeySequenceType kst : ks.sequence)
            kst.currentValue = 0;
    }
           
    public interface Callback
    {
        public void run();
    }
    
    public enum SequenceType
    {
        HELD,PRESSED,TERMINAL
    }
    

    

}
