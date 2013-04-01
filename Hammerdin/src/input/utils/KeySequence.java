/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package input.utils;

import java.util.Arrays;

/**
 *
 * @author aglassman
 */
public class KeySequence
{
    public final String name;
    public final KeySequenceType[] sequence;
    public KeySequence(String name,KeySequenceType... sequence)
    {
        this.name = name;
        this.sequence = sequence;
    }

    @Override
    public String toString()
    {
        return String.format("SequenceName: %s, Sequence: [%s]",name,Arrays.toString(sequence));
    }

    String getState() {
        StringBuilder sb = new StringBuilder();
        for(KeySequenceType kst : sequence)
        {
            sb.append(String.format("(name:%s state: %s) ",kst.key, kst.currentValue));
        }
        return sb.toString();
    }
}
