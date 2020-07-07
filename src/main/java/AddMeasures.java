package org.nlogo.extensions.music;


import org.nlogo.api.*;
import org.nlogo.api.Command;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import org.nlogo.nvm.Workspace;
import org.nlogo.nvm.ExtensionContext;

import org.nlogo.agent.World;
import org.nlogo.agent.Patch;
import java.util.ArrayList;


public class AddMeasures implements Command {


    public Syntax getSyntax() {
        /** Add N measures to existing world. */
        return SyntaxJ.commandSyntax(new int[]{Syntax.NumberType()

                }
        );
    }



    public void perform(Argument args[], Context context)
            throws ExtensionException {

        int num;

        try {
            num = args[0].getIntValue();
        } catch (LogoException e) {
            throw new ExtensionException(e.getMessage());
        }

        if (num <= 0 || num > 32)
            throw new ExtensionException("Number of measures added must be between 1 and 32");

        ExtensionContext ec = (ExtensionContext) context;
        Workspace ws = ec.workspace();
        World w = ws.world();

        Init.stashPatches(w); // store all patch info

        P.NMEASURES = P.NMEASURES + num;
        P.XMAX = P.NMEASURES * P.MAXNOTESPERMEASURE;

        Init.resizeWorld(context);

        Init.initDrawing(ec);
        Init.fixAgents(w);
        Init.restorePatches(w);  // restore patch info
    }


}


