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
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
public class Row implements Serializable {

    Map<Integer,Square> squareMap = new Hashtable<Integer, Square>(8);

    int number;
    /**
     * @param r
     */
    public Row(int r) {
        this.number = r;
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
        return new ArrayList<Square>(squareMap.values());
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
