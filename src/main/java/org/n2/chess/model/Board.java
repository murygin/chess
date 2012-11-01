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

    public static final String CASTLING_KINGSIDE = "castling_kingside";
    public static final String CASTLING_QUEENSIDE = "castling_queenside";

    Map<Integer, Row> rowMap = new Hashtable<Integer, Row>(8);

    Square source, dest;

    String number;

    String halfmove;

    String enPassant;

    String castling;

    String active;

    String placement;

    String colorPlayer;

    public void move() {
        validate(source, dest);
        Square sourceSquare = getRowMap().get(source.getRow()).getSquareMap().get(source.getColumn());
        Square destSquare = getRowMap().get(dest.getRow()).getSquareMap().get(dest.getColumn());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Move, source: " + sourceSquare);
            LOG.debug("Move, dest: " + destSquare);
        }
        Piece piece = sourceSquare.getPiece();
        if (Piece.KING_B == piece.getLetter()) {
            disableBlackCastlings();
        }
        if (Piece.KING_W == piece.getLetter()) {
            disableWhiteCastlings();
        }
        if (Piece.ROOK_B == piece.getLetter() && sourceSquare.getColumn() == 0) {
            disableBlackCastlingQueenside();
        }
        if (Piece.ROOK_B == piece.getLetter() && sourceSquare.getColumn() == 7) {
            disableBlackCastlingKingside();
        }
        if (Piece.ROOK_W == piece.getLetter() && sourceSquare.getColumn() == 0) {
            disableWhiteCastlingQueenside();
        }
        if (Piece.ROOK_W == piece.getLetter() && sourceSquare.getColumn() == 7) {
            disableWhiteCastlingKingside();
        }
        sourceSquare.setPiece(null);
        destSquare.setPiece(piece);
        setActive(getActive().equals(BLACK) ? WHITE : BLACK);
        setNumber(String.valueOf(Integer.parseInt(getNumber()) + 1));
        unSelect();
    }

    private void disableWhiteCastlings() {
        String old = getCastling();
        setCastling("--" + old.substring(2, 4));
    }

    private void disableBlackCastlings() {
        String old = getCastling();
        setCastling(old.substring(0, 2) + "--");
    }

    private void disableBlackCastlingQueenside() {
        String old = getCastling();
        setCastling(old.substring(0, 3) + "-");
    }

    private void disableBlackCastlingKingside() {
        String old = getCastling();
        setCastling(old.substring(0, 2) + "-" + old.substring(3, 4));
    }

    private void disableWhiteCastlingQueenside() {
        String old = getCastling();
        setCastling(old.substring(0, 1) + "-" + old.substring(2, 4));
    }

    private void disableWhiteCastlingKingside() {
        String old = getCastling();
        setCastling("-" + old.substring(1, 4));
    }

    /**
     * @param source2
     * @param dest2
     */
    private void validate(Square source2, Square dest2) {
        // TODO Auto-generated method stub

    }

    public boolean isPieceAtSquare(char piece, int row, int col) {
        Square square = getRowMap().get(row).getSquareMap().get(col);
        return (square != null && square.getPiece() != null && piece == square.getPiece().getLetter());
    }
    
    public void castlingQueensideRookMove() {
        if(getActive().equals(WHITE)) {
            move(7,0,7,3);         
        }
        if(getActive().equals(BLACK)) {
            move(0,0,0,3);         
        }
    }
   
    public void castlingKingsideRookMove() {
        if(getActive().equals(WHITE)) {
            move(7,7,7,5);         
        }
        if(getActive().equals(BLACK)) {
            move(0,7,0,5);         
        }        
    }
    
    private void move(int sx, int sy, int dx, int dy) {
        Square sourceSquare = getRowMap().get(sx).getSquareMap().get(sy);
        Square destSquare = getRowMap().get(dx).getSquareMap().get(dy);
        Piece piece = sourceSquare.getPiece();
        if(piece!=null) {
            sourceSquare.setPiece(null);
            destSquare.setPiece(piece);
        }      
    }

    /**
     * @param row
     */
    public void putRow(Row row) {
        rowMap.put(row.getNumber(), row);
    }

    public List<Row> getRows() {
        List<Row> rows = new ArrayList<Row>(rowMap.values());
        if (Board.WHITE.equals(getColorPlayer())) {
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
     * @param rowMap
     *            the rowMap to setSquare sourceSquare =
     *            getRowMap().get(source.getRow
     *            ()).getSquareMap().get(source.getColumn());
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
        this.source = source;
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
     * @param number
     *            the number to set
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
     * @param halfmove
     *            the halfmove to set
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
     * @param enPassant
     *            the enPassant to set
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
     * @param castling
     *            the castling to set
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
     * @param active
     *            the active to set
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
     * @param placement
     *            the placement to set
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
     * @param playerColor
     *            the playerColor to set
     */
    public void setColorPlayer(String playerColor) {
        this.colorPlayer = playerColor;
    }

    private void unSelect() {
        this.source = null;
        this.dest = null;
        for (Row row : getRowMap().values()) {
            row.unSelect();
        }

    }

    private void unSource() {
        this.source = null;
        for (Row row : getRowMap().values()) {
            row.unSource();
        }

    }

    /**
     * @param source
     */
    public void setDest(Square dest) {
        this.dest = dest;
        getRowMap().get(dest.getRow()).getSquareMap().get(dest.getColumn()).setDest(true);

    }

    /**
     * @return the dest
     */
    public Square getDest() {
        return dest;
    }

    private void unDest() {
        this.dest = null;
        for (Row row : getRowMap().values()) {
            row.unDest();
        }

    }

}
