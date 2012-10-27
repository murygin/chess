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
public class Board implements Serializable {

    private static final Logger LOG = Logger.getLogger(Board.class);
    
    public static final String BLACK = "b";
    public static final String WHITE = "w";
    
    Map<Integer,Row> rowMap = new Hashtable<Integer, Row>(8);

    Square source, dest;
  
    String number;

    String halfmove;

    String enPassant;

    String castling;

    String active;

    String placement;
    
    String colorPlayer;
    
    
    public void move() {    
        validate(source,dest);
        Square sourceSquare = getRowMap().get(source.getRow()).getSquareMap().get(source.getColumn());
        Square destSquare = getRowMap().get(dest.getRow()).getSquareMap().get(dest.getColumn());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Move, source: " + sourceSquare);
            LOG.debug("Move, dest: " + destSquare);
        }
        Piece piece = sourceSquare.getPiece();
        sourceSquare.setPiece(null);
        destSquare.setPiece(piece);
        setActive(getActive().equals(BLACK) ? WHITE : BLACK);
        setNumber(String.valueOf(Integer.parseInt(getNumber())+1));
        unSelect();
     }
    
    /**
     * @param source2
     * @param dest2
     */
    private void validate(Square source2, Square dest2) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param row
     */
    public void putRow(Row row) {
        rowMap.put(row.getNumber(), row);      
    }
    
    public List<Row> getRows() {
        List<Row> rows = new ArrayList<Row>(rowMap.values());
        if(Board.WHITE.equals(getColorPlayer())) {
            Collections.reverse(rows);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Returning rows.");
        }
        return rows;
    }
    
    /**
     * @return the rowMap
     */
    public Map<Integer, Row> getRowMap() {
        return rowMap;
    }

    /**
     * @param rowMap the rowMap to setSquare sourceSquare = getRowMap().get(source.getRow()).getSquareMap().get(source.getColumn());
        
     */
    public void setRowMap(Map<Integer, Row> rowMap) {
        this.rowMap = rowMap;
    }

    /**
     * @param source
     */
    public void setSource(Square source) {
        unSelect();
        getRowMap().get(source.getRow()).getSquareMap().get(source.getColumn()).setSource(true);
        this.source=source;
    }
    
    /**
     * @return the source
     */
    public Square getSource() {
        return source;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the halfmove
     */
    public String getHalfmove() {
        return halfmove;
    }

    /**
     * @param halfmove the halfmove to set
     */
    public void setHalfmove(String halfmove) {
        this.halfmove = halfmove;
    }

    /**
     * @return the enPassant
     */
    public String getEnPassant() {
        return enPassant;
    }

    /**
     * @param enPassant the enPassant to set
     */
    public void setEnPassant(String enPassant) {
        this.enPassant = enPassant;
    }

    /**
     * @return the castling
     */
    public String getCastling() {
        return castling;
    }

    /**
     * @param castling the castling to set
     */
    public void setCastling(String castling) {
        this.castling = castling;
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
        for (Row row : getRowMap().values()) {
            row.setActive(active);
        } 
    }

    /**
     * @return the placement
     */
    public String getPlacement() {
        return placement;
    }

    /**
     * @param placement the placement to set
     */
    public void setPlacement(String placement) {
        this.placement = placement;
    }

    /**
     * @return the playerColor
     */
    public String getColorPlayer() {
        return colorPlayer;
    }

    /**
     * @param playerColor the playerColor to set
     */
    public void setColorPlayer(String playerColor) {
        this.colorPlayer = playerColor;
    }

    private void unSelect() {
        this.source=null;
        this.dest=null;
        for (Row row : getRowMap().values()) {
            row.unSelect();
        }
        
    }
    
    private void unSource() {
        this.source=null;
        for (Row row : getRowMap().values()) {
            row.unSource();
        }
        
    }

    /**
     * @param source
     */
    public void setDest(Square dest) {
        this.dest=dest;
        getRowMap().get(dest.getRow()).getSquareMap().get(dest.getColumn()).setDest(true);
        
    }
    
    /**
     * @return the dest
     */
    public Square getDest() {
        return dest;
    }

    private void unDest() {
        this.dest=null;
        for (Row row : getRowMap().values()) {
            row.unDest();
        }
        
    }
    
}
