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

import org.n2.chess.beans.hibernate.Game;
import org.n2.chess.model.Board;
import org.n2.chess.model.Piece;
import org.n2.chess.model.Row;
import org.n2.chess.model.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Service("boardService")
public class BoardService implements IBoardService, Serializable {

    @Autowired
    private IFenParser parser;
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IBoardService#createBoard(org.n2.chess.beans.hibernate.Game)
     */
    @Override
    public Board createBoard(Game game) {
        String fen = game.getFen();
        getParser().parse(fen);
        
        Board board = new Board();
        int r = 0;
        for (String fenRow : getParser().getRows()) {
           Row row = new Row(r);
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
