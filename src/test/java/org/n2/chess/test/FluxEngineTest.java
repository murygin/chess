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
package org.n2.chess.test;

import jcpi.data.IllegalNotationException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.n2.chess.flux.NextMove;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class FluxEngineTest {

    private static final Logger LOG = Logger.getLogger(FluxEngineTest.class);
    
    @Test
    public void test() throws IllegalNotationException {
        String fen = "r1bnkbnr/ppp2ppp/8/4p3/8/2N2N2/PPP1PPPP/R1B1KB1R w KQkq - 0 11";
        NextMove nextMove = new NextMove(fen, 1);
        String move = nextMove.caclculateNextMove();
        
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Next move: " + move);
        }
    }
    

}
