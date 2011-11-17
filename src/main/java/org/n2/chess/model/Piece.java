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

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
public class Piece {

    public final char ROOK_B = 'r';  
    public final char ROOK_W = 'R';
    
    public final char KNIGHT_B = 'n';  
    public final char KNIGHT_W = 'N';

    public final char BISHOP_B = 'b'; 
    public final char BISHOP_W = 'B';

    public final char QUEEN_B = 'q'; 
    public final char QUEEN_W = 'Q';

    public final char KING_B = 'k'; 
    public final char KING_W = 'K';

    public final char PAWN_B = 'p'; 
    public final char PAWN_W = 'P';
    
    private char letter;
    
    private int row;
    
    private int column;
    
    /**
     * „r“ = Rook, „n“ = Knight, „b“ = Bishop „q“ = Queen, „k“ = King, „p“ = Pawn
     * @param c
     * @param c2 
     * @param r 
     * @return
     */
    public static Piece createPiece(char p, int r, int c) {
        return new Piece(p,r,c);
    }
    
    /**
     * @param c
     * @param c2 
     * @param r 
     */
    public Piece(char p, int r, int c) {
        this.letter = p;
        this.row=r;
        this.column=c;
    }

    /**
     * @return the letter
     */
    public char getLetter() {
        return letter;
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


    

}
