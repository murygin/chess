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

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.fluxchess.flux.Flux;
import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineNewGameCommand;
import com.fluxchess.jcpi.commands.EngineQuitCommand;
import com.fluxchess.jcpi.commands.EngineReadyRequestCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.commands.IEngineCommand;
import com.fluxchess.jcpi.commands.ProtocolBestMoveCommand;
import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.fluxchess.jcpi.commands.ProtocolInitializeAnswerCommand;
import com.fluxchess.jcpi.commands.ProtocolReadyAnswerCommand;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.protocols.IProtocolHandler;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class NextMove  implements IProtocolHandler {
    
    private static final Logger LOG = Logger.getLogger(NextMove.class);
    
    private final BlockingQueue<IEngineCommand> engineCommandQueue = new LinkedBlockingQueue<IEngineCommand>();
    
    private long seconds;
    private GenericMove result;

    public NextMove(String fen, long seconds) {
        this.seconds = seconds;
        
        this.engineCommandQueue.add(new EngineReadyRequestCommand());
        this.engineCommandQueue.add(new EngineNewGameCommand());
        try {
            this.engineCommandQueue.add(new EngineAnalyzeCommand(new GenericBoard(fen), new ArrayList<GenericMove>()));
        } catch (IllegalNotationException e) {
            LOG.error("Illegal notation: " + fen, e);
        }
        EngineStartCalculatingCommand engineCommand = new EngineStartCalculatingCommand();
        engineCommand.setMoveTime(seconds * 1000);
        this.engineCommandQueue.add(engineCommand);
    }

    public String caclculateNextMove() {
        AbstractEngine engine = new Flux(this);
        engine.run();

        return result.toString();
    }

    @Override
    public IEngineCommand receive() {
        try {
            // Wait 5 seconds longer than the engine calculates before giving up waiting
			return this.engineCommandQueue.poll(seconds + 5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return new EngineQuitCommand();
		}
    }


    /* (non-Javadoc)
     * @see com.fluxchess.jcpi.commands.IProtocol#send(com.fluxchess.jcpi.commands.ProtocolInitializeAnswerCommand)
     */
    @Override
    public void send(ProtocolInitializeAnswerCommand command) {
        command.accept(this);
    }

    /* (non-Javadoc)
     * @see com.fluxchess.jcpi.commands.IProtocol#send(com.fluxchess.jcpi.commands.ProtocolReadyAnswerCommand)
     */
    @Override
    public void send(ProtocolReadyAnswerCommand command) {
        
    }

    /* (non-Javadoc)
     * @see com.fluxchess.jcpi.commands.IProtocol#send(com.fluxchess.jcpi.commands.ProtocolBestMoveCommand)
     */
    @Override
    public void send(ProtocolBestMoveCommand command) {
        result = command.bestMove;       
        engineCommandQueue.add(new EngineQuitCommand());
    }

    /* (non-Javadoc)
     * @see com.fluxchess.jcpi.commands.IProtocol#send(com.fluxchess.jcpi.commands.ProtocolInformationCommand)
     */
    @Override
    public void send(ProtocolInformationCommand command) {
    }


}
