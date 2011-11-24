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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n2.chess.beans.hibernate.Game;
import org.n2.chess.model.Board;
import org.n2.chess.model.Piece;
import org.n2.chess.model.Row;
import org.n2.chess.model.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.security.action.GetBooleanAction;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Service("boardService")
public class BoardService implements IBoardService, Serializable {

    public static String[] LETTERS_COLUMN = {"a","b","c","d","e","f","g","h"};
    
    @Autowired
    private IFenParser parser;
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IBoardService#createBoard(org.n2.chess.beans.hibernate.Game)
     */
    @Override
    public Board createBoard(Game game, String colorPlayer) {
        String fen = game.getFen();
        getParser().parse(fen);
        Board board = new Board();
        board.setColorPlayer(colorPlayer);
        board.setActive(getParser().getActive());
        board.setCastling(getParser().getCastling());
        board.setEnPassant(getParser().getEnPassant());
        board.setHalfmove(getParser().getHalfmove());
        board.setNumber(getParser().getNumber());
        int r = 0;
        for (String fenRow : getParser().getRows()) {
           Row row = new Row(r,board.getActive(),colorPlayer);
           int c = 0;
           for (int i = 0; i < fenRow.length(); i++) {
               char p = fenRow.charAt(i);
               Integer n = getNumber(p);
               if(n==null) {
                   row.putPiece(Piece.createPiece(p,r,c));
                   c++;
               } else {
                   for (int j = 0; j < n; j++) {
                       row.putSquare(new Square(r,c));
                       c++;
                   }                 
               } 
           }
           board.putRow(row);
           r++;
        }
        return board;
    }
    
    public String createFen(Board board) {
        StringBuilder fen = new StringBuilder();
        boolean first = true;
        List<Row> rows = new ArrayList<Row>(board.getRowMap().values());
        Collections.reverse(rows);
        for (Row row : rows) {
            if(!first) {
                fen.append("/");
            }
            int empty=0;
            List<Square> squares = new ArrayList<Square>(row.getSquareMap().values());
            Collections.reverse(squares);
            for (Square square : squares) {
                Piece piece = square.getPiece();
                if(piece!=null) {
                    if(empty>0) {
                        fen.append(empty);
                        empty=0;
                    }
                    fen.append(piece.getLetter());
                } else {
                    empty++;
                }
            }
            if(empty>0) {
                fen.append(empty);
            }
            first = false;
        }
        fen.append(" ").append(board.getActive());
        fen.append(" ").append(board.getCastling());
        fen.append(" ").append(board.getEnPassant());
        fen.append(" ").append(board.getHalfmove());
        fen.append(" ").append(board.getNumber());
        return fen.toString();
    }
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IBoardService#createNotation(org.n2.chess.model.Square, org.n2.chess.model.Square)
     */
    @Override
    public String createNotation(Square source, Square dest, String colorPlayer) {
        StringBuilder sb = new StringBuilder();
        if(source!=null && source.getPiece()!=null && dest!=null) {
            Piece piece = source.getPiece();
            if(piece.getLetter()!=Piece.PAWN_B && piece.getLetter()!=Piece.PAWN_W) {
                sb.append(String.valueOf(piece.getLetter()).toUpperCase());
            }
            sb.append(LETTERS_COLUMN[source.getColumn()]);
            //if(Board.WHITE.equals(colorPlayer)) {
               //sb.append((source.getRow()+1));
            //} else {
                sb.append((8-source.getRow()));
            //}
            if(dest.getPiece()!=null) {
                sb.append("x");
            } else {
                sb.append("-");
            }
            sb.append(LETTERS_COLUMN[dest.getColumn()]);
            //if(Board.WHITE.equals(colorPlayer)) {
                //sb.append((dest.getRow()+1));
            //} else {
                sb.append((8-dest.getRow()));
            //}
        }
        return sb.toString();
    }
   

    private static Integer getNumber(char c) {
        try {
            return Integer.parseInt(String.valueOf(c));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * @return the parser
     */
    public IFenParser getParser() {
        return parser;
    }

    /**
     * @param parser the parser to set
     */
    public void setParser(IFenParser parser) {
        this.parser = parser;
    }
    
    public static boolean isEven(int n) {
        return ((n % 2) == 0) ? true : false;
    }

}
