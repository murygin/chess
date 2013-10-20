/*******************************************************************************
 * Copyright (c) 2013 Daniel Murygin.
 *
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,    
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murygin <dm[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package org.n2.chess.flux;

import jcpi.ICommunication;
import jcpi.commands.GuiBestMoveCommand;
import jcpi.commands.GuiInformationCommand;
import jcpi.commands.GuiInitializeAnswerCommand;
import jcpi.commands.GuiReadyAnswerCommand;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class ResultReader implements ICommunication {

    private String result;
    
    /* (non-Javadoc)
     * @see jcpi.ICommunication#visit(jcpi.commands.GuiInitializeAnswerCommand)
     */
    @Override
    public void visit(GuiInitializeAnswerCommand command) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see jcpi.ICommunication#visit(jcpi.commands.GuiReadyAnswerCommand)
     */
    @Override
    public void visit(GuiReadyAnswerCommand command) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see jcpi.ICommunication#visit(jcpi.commands.GuiBestMoveCommand)
     */
    @Override
    public void visit(GuiBestMoveCommand command) {
        result = command.bestMove.toString();

    }

    /* (non-Javadoc)
     * @see jcpi.ICommunication#visit(jcpi.commands.GuiInformationCommand)
     */
    @Override
    public void visit(GuiInformationCommand command) {
        // TODO Auto-generated method stub

    }

    public String getResult() {
        return result;
    }

}
