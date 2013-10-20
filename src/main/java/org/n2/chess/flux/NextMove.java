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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jcpi.AbstractEngine;
import jcpi.commands.EngineQuitCommand;
import jcpi.commands.EngineStopCalculatingCommand;

import org.apache.log4j.Logger;

import com.fluxchess.Flux;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class NextMove {
    
    private static final Logger LOG = Logger.getLogger(NextMove.class);
    
    public String caclculateNextMove(String fen, long seconds) {
        String nextMove = null;
        try {
            NextMoveComunication comunication = new NextMoveComunication(fen);
            
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new EngineTestThread(comunication));
            //Thread thread = new Thread(new EngineTestThread(comunication));
            //thread.start();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Engine thread startet, fen: " + fen);
            }
          
            Thread.sleep(seconds*1000);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending stop and quit command...");
            }
            comunication.addCommand(new EngineStopCalculatingCommand());
            comunication.addCommand(new EngineQuitCommand());
            
            shutdownAndAwaitTermination(executor);                
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Next move: " + comunication.getNextMove());
            }
            nextMove = comunication.getNextMove();
        } catch (InterruptedException e) {
            LOG.error("Interrupted", e);
        }
        return nextMove;
    }
    
    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
          // Wait a while for existing tasks to terminate
          if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            pool.shutdownNow(); // Cancel currently executing tasks
            // Wait a while for tasks to respond to being cancelled
            if (!pool.awaitTermination(2, TimeUnit.SECONDS))
                System.err.println("Pool did not terminate");
          }
        } catch (InterruptedException ie) {
          // (Re-)Cancel if current thread also interrupted
          pool.shutdownNow();
          // Preserve interrupt status
          Thread.currentThread().interrupt();
        }
      }
    
    public class EngineTestThread implements Runnable {
        
        NextMoveComunication comunication;

        /**
         * @param comunication
         */
        public EngineTestThread(NextMoveComunication comunication) {
            this.comunication = comunication;
        }

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            AbstractEngine engine = new Flux(comunication);
            engine.run();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Engine thread finished");
            }
        }
    }
}
