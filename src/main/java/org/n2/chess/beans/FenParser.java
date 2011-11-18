/*******************************************************************************
 * Copyright (c) 2011 Daniel Murygin.
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
package org.n2.chess.beans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Service("fenParser")
public class FenParser implements IFenParser, Serializable {

    private static final Logger LOG = Logger.getLogger(FenParser.class);
    
    String placement;
    String active;
    String castling;
    String enPassant;
    String halfmove;
    String number;
    List<String> rows;
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IFenParser#parse(java.lang.String)
     */
    @Override
    public void parse(String fen) {
        StringTokenizer st = new StringTokenizer(fen, " ");
        try {
            placement = st.nextToken();
            active = st.nextToken();
            castling = st.nextToken();
            enPassant = st.nextToken();
            halfmove = st.nextToken();
            number = st.nextToken();
        
            st = new StringTokenizer(placement, "/");
            rows = new ArrayList<String>(8);
            for (int i = 0; i < 8; i++) {
                rows.add(st.nextToken());             
            }
            if(rows.size()!=8) {
                malformedFen(fen);
            }
        } catch(NoSuchElementException e) {
            malformedFen(fen);
        }
    }

    /**
     * @return the rows
     */
    public List<String> getRows() {
        return rows;
    }

    /**
     * @return the placement
     */
    @Override
    public String getPlacement() {
        return placement;
    }

    /**
     * @return the active
     */
    @Override
    public String getActive() {
        return active;
    }

    /**
     * @return the castling
     */
    @Override
    public String getCastling() {
        return castling;
    }

    /**
     * @return the enPassant
     */
    @Override
    public String getEnPassant() {
        return enPassant;
    }

    /**
     * @return the halfmove
     */
    @Override
    public String getHalfmove() {
        return halfmove;
    }

    /**
     * @return the number
     */
    @Override
    public String getNumber() {
        return number;
    }
    
    private void malformedFen(String fen) {
        String message = "Malformed fen: " + fen;
        LOG.error(message);
        throw new RuntimeException(message);
    }

}
