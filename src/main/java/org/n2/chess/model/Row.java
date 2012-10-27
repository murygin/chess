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
package org.n2.chess.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@SuppressWarnings("serial")
public class Row implements Serializable {

    private static final Logger LOG = Logger.getLogger(Row.class);
    
    Map<Integer,Square> squareMap = new Hashtable<Integer, Square>(8);

    int number;
    
    String active;
    
    String colorPlayer;
    
    /**
     * @param r
     */
    public Row(int r) {
        this.number = r;
        this.active = Board.WHITE;
    }
    
    /**
     * @param r
     * @param active2
     */
    public Row(int r, String active) {
        this.number = r;
        this.active = active;
    }

    /**
     * @param r
     * @param active2
     * @param colorPlayer2
     */
    public Row(int r, String active, String colorPlayer) {
        this.number = r;
        this.active = active;
        this.colorPlayer = colorPlayer;
    }

    /**
     * @param createPiece
     */
    public void putPiece(Piece piece) {
        squareMap.put(piece.getColumn(), new Square(piece));     
    }
    
    public void putSquare(Square square) {
        squareMap.put(square.getColumn(), square);     
    }
    
    public List<Square> getSquares() {
        List<Square> squares = new ArrayList<Square>(squareMap.values());
        if(Board.WHITE.equals(getColorPlayer())) {
            Collections.reverse(squares);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Returning squares");
        }
        return squares;
    }

    /**
     * @return the pieceMap
     */
    public Map<Integer, Square> getSquareMap() {
        return squareMap;
    }

    /**
     * @param squareMap the pieceMap to set
     */
    public void setPieceMap(Map<Integer, Square> squareMap) {
        this.squareMap = squareMap;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }
    
    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the colorPlayer
     */
    public String getColorPlayer() {
        return colorPlayer;
    }

    /**
     * @param colorPlayer the colorPlayer to set
     */
    public void setColorPlayer(String colorPlayer) {
        this.colorPlayer = colorPlayer;
    }

    public void unSelect() {
        for (Square square : getSquareMap().values()) {
            square.setSource(false);
            square.setDest(false);
        }   
    }
 
    public void unSource() {
        for (Square square : getSquareMap().values()) {
            square.setSource(false);
        }   
    }

    public void unDest() {
        for (Square square : getSquareMap().values()) {
            square.setDest(false);
        }
    }
    
}
