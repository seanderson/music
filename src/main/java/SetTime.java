package org.nlogo.extensions.music;

import org.nlogo.api.*;
import org.nlogo.api.Command;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import org.nlogo.api.Context;

/**
 * Set scale/tonic for a voice.
 */
public class SetTime implements Command {


    public Syntax getSyntax() {
        return SyntaxJ.commandSyntax(new int[]{
                        Syntax.NumberType()
                }
        );
    }

    public void perform(Argument args[], Context context)
            throws ExtensionException {
        int t;


        try {
            t = args[0].getIntValue();

        } catch (LogoException e) {
            throw new ExtensionException("Error in SetTime: " + e.getMessage());
        }

        try {
            for (int i = 0; i < P.NDRUMS; i++) {
                P.drums[i].agent.xcor(t);
            }
            for (int i = 0; i < P.NVOICES; i++) {
                P.voices[i].agent.xcor(t);
            }
        } catch (org.nlogo.api.AgentException ex) {
            throw new org.nlogo.api.ExtensionException("Bad agent in SetTime."
                    + ex.getMessage());
        }
    }

}
