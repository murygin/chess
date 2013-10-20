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
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import jcpi.AbstractCommunication;
import jcpi.commands.EngineAnalyzeCommand;
import jcpi.commands.EngineNewGameCommand;
import jcpi.commands.EngineReadyRequestCommand;
import jcpi.commands.EngineStartCalculatingCommand;
import jcpi.commands.IEngineCommand;
import jcpi.commands.IGuiCommand;
import jcpi.data.GenericBoard;
import jcpi.data.GenericMove;
import jcpi.data.IllegalNotationException;

import org.apache.log4j.Logger;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class NextMoveComunication extends AbstractCommunication {

    private static final Logger LOG = Logger.getLogger(NextMoveComunication.class);
    
    /**
     * The engine command queue.
     */
    private final Queue<IEngineCommand> engineCommandQueue = new LinkedList<IEngineCommand>();
    
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    
    /**
     * The protocol.
     */
    private ResultReader communication = new ResultReader();
    
    
    private String nextMove;
    
    /**
     * 
     */
    public NextMoveComunication(String fen) {
        super();
        this.engineCommandQueue.add(new EngineReadyRequestCommand());
        this.engineCommandQueue.add(new EngineNewGameCommand());
        try {
            this.engineCommandQueue.add(new EngineAnalyzeCommand(new GenericBoard(fen), new ArrayList<GenericMove>()));
        } catch (IllegalNotationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        EngineStartCalculatingCommand engineCommand = new EngineStartCalculatingCommand();
        engineCommand.setInfinite();
        this.engineCommandQueue.add(engineCommand);
    }

    /* (non-Javadoc)
     * @see jcpi.AbstractCommunication#receive()
     */
    @Override
    protected IEngineCommand receive() {
        IEngineCommand engineCommand = null;
        while (engineCommand == null) {
            readLock.lock();
            try {
                engineCommand = this.engineCommandQueue.poll();
            } finally {
                readLock.unlock(); 
            }        
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Returning command: " + engineCommand.getClass().getSimpleName());
        }
        return engineCommand;
    }

    /* (non-Javadoc)
     * @see jcpi.AbstractCommunication#send(jcpi.commands.IGuiCommand)
     */
    @Override
    public void send(IGuiCommand guiCommand) {
        guiCommand.accept(communication);
        nextMove = communication.getResult();
    }
     
    public void addCommand(IEngineCommand command) {
        // Block all other threads before writing the file
        writeLock.lock();
        try {
            this.engineCommandQueue.add(command);          
        } finally {
            writeLock.unlock();
        }
    }

    public String getNextMove() {
        return nextMove;
    }

}
