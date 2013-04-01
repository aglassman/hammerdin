/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package input.utils;

/**
 *
 * @author aglassman
 */
public class KeySequenceType
{
    public final int key;
    public final KeystrokeSequence.SequenceType sequenceType;
    int currentValue = 0;
    public KeySequenceType(int key, KeystrokeSequence.SequenceType sequenceType)
    {
        this.key = key;
        this.sequenceType = sequenceType;
    }

    @Override
    public String toString()
    {
        return Integer.toString(key);
    }
}
