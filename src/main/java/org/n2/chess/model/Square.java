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

import org.apache.log4j.Logger;
import org.n2.chess.beans.BoardService;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@SuppressWarnings("serial")
public class Square implements Serializable {

    private static final Logger LOG = Logger.getLogger(Square.class);
    
    public static final String WHITE = "white";
    
    public static final String BLACK = "black";
    
    String color;
    
    int row;
    
    int column;
    
    Piece piece;
    
    boolean source = false;
    
    boolean dest = false;

    /**
     * @param color
     */
    public Square(String color) {
        super();
        this.color = color;
    }
   
    /**
     * @param row
     * @param column
     */
    public Square(int row, int column) {
        super();
        this.row = row;
        this.column = column;
        this.color = createColor();      
    }

    /**
     * @param color
     * @param piece
     */
    public Square(Piece piece) {
        super();
        this.row = piece.getRow();
        this.column = piece.getColumn();
        this.color = createColor();
        this.piece = piece;
    }


    /**
     * @return
     */
    private String createColor() {
        return ((BoardService.isEven(row) && BoardService.isEven(column)) 
                || (!BoardService.isEven(row) && !BoardService.isEven(column))) ? Square.WHITE : Square.BLACK;
    }
    
    public String getStyle() {
        StringBuilder sb = new StringBuilder();
        sb.append(getColor());
        if(getSource()) {
            sb.append("-source");
        }
        if(getDest()) {
            sb.append("-dest");
        }
        return sb.toString();
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * @return the piece
     */
    public Piece getPiece() {
        if (LOG.isDebugEnabled() && piece!=null) {
            LOG.debug("Returning piece for " + toString());
        }
        return piece;
    }

    /**
     * @param piece the piece to set
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * @return the dest
     */
    public boolean getDest() {
        return dest;
    }

    /**
     * @param dest the dest to set
     */
    public void setDest(boolean dest) {
        this.dest = dest;
    }

    /**
     * @return the source
     */
    public boolean getSource() {
        return source;
    }

    /**
     * @param b
     */
    public void setSource(boolean b) {
        this.source=b;    
    }

    @Override
    public String toString() {
        return "Square [color=" + color + ", row=" + row + ", column=" + column + ", piece=" + piece + "]";
    }
    
    
}
