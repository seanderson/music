package org.nlogo.extensions.music;

import org.nlogo.api.*;
import org.nlogo.api.Command;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;
// SEA
import org.nlogo.api.Context;

import org.nlogo.nvm.Workspace;
import org.nlogo.nvm.ExtensionContext;

import org.nlogo.agent.World;
import org.nlogo.agent.Patch;

/**
 * Copy notes from voice1 to voice2.
 * USAGE: delay-voice voice measure num-measures amount-to-delay
 */
public class DelayVoice implements Command {

    public Syntax getSyntax() {
        return SyntaxJ.commandSyntax(new int[]{
                Syntax.NumberType(),
                Syntax.NumberType(),
                Syntax.NumberType(),
                Syntax.NumberType()
               }
        );
    }

    public void perform(Argument args[], Context context)
            throws ExtensionException {
        int src;
        int meas;
        int offset;
        int nmeas;

        try {
            src = args[0].getIntValue();
            meas = args[1].getIntValue();
            nmeas = args[2].getIntValue();
            offset = args[3].getIntValue();

        } catch (LogoException e) {
            throw new ExtensionException("Error in DelayVoice: " + e.getMessage());
        }

        if (src < 0 || src >= P.NVOICES)
            throw new ExtensionException("Bad voice ID: " + src);
        if (offset <= 0)
            throw new ExtensionException("Delay must be positive");
        delay((ExtensionContext) context, src, meas, nmeas, offset);

    }


    /*
      Temporally delay a voice by a number of smallest temporal unit.
      Will drop any notes extending beyond existing measures.
    */
    public static void delay(ExtensionContext ec,
                            int src, int meas,
                            int nmeas, int offset)
            throws ExtensionException {
        Workspace ws = ec.workspace();
        World w = ws.world();
        int pcoloridx = w.patchesOwnIndexOf("PCOLOR");
        Patch p = null;
        Patch pdest = null;
        try {
            int y0_src = P.PATCHESPERVOICE * (1 + src); // 1 is rhythm offset
            int xmax = P.MAXNOTESPERMEASURE * (meas + nmeas);
            for (int x = (meas + nmeas)*P.MAXNOTESPERMEASURE - 1;x >= meas * P.MAXNOTESPERMEASURE; x--) {
                int destx = x + offset;
                for (int y = y0_src; y < y0_src + P.PATCHESPERVOICE; y++) {
                    p = w.getPatchAt(x, y);
                    if (p.pcolor().equals(P.BLACK)) continue; // ignore empties

                    // clear all destination patches at this x
                    for (int yd = y0_src; yd < y0_src + P.PATCHESPERVOICE; yd++) {
                        pdest = w.getPatchAt(destx, yd);
                        pdest.setPatchVariable(pcoloridx, P.DBLACK);
                    }
                    // validate new y patch for destination voice


                    pdest = w.getPatchAt(destx, y);
                    pdest.setPatchVariable(pcoloridx, P.vcolor[src]);
                }
            }
        } catch (org.nlogo.api.AgentException ex) {
            throw new org.nlogo.api.ExtensionException("Bad patch in music.");
        }


    }

}
