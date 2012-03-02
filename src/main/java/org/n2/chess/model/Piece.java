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
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
public class Piece implements Serializable {

    private static final String IMAGE_FOLDER = "image/piece/";
    
    public final static char ROOK_B = 'r';
    public final static String ROOK_B_UNICODE = "&#9820;";
    public final static String ROOK_B_IMAGE = "rook-black.png";
    public final static char ROOK_W = 'R';
    public final static String ROOK_W_UNICODE = "&#9814;";
    public final static String ROOK_W_IMAGE = "rook-white.png";
    
    public final static char KNIGHT_B = 'n';
    public final static String KNIGHT_B_UNICODE = "&#9822;";
    public final static String KNIGHT_B_IMAGE = "knight-black.png";
    public final static char KNIGHT_W = 'N';
    public final static String KNIGHT_W_UNICODE = "&#9816;";
    public final static String KNIGHT_W_IMAGE = "knight-white.png";

    public final static char BISHOP_B = 'b';
    public final static String BISHOP_B_UNICODE = "&#9821;";
    public final static String BISHOP_B_IMAGE = "bishop-black.png";
    public final static char BISHOP_W = 'B';
    public final static String BISHOP_W_UNICODE = "&#9815;";
    public final static String BISHOP_W_IMAGE = "bishop-white.png";

    public final static char QUEEN_B = 'q';
    public final static String QUEEN_B_UNICODE = "&#9819;";
    public final static String QUEEN_B_IMAGE = "queen-black.png";
    public final static char QUEEN_W = 'Q';
    public final static String QUEEN_W_UNICODE = "&#9813;";
    public final static String QUEEN_W_IMAGE = "queen-white.png";

    public final static char KING_B = 'k';
    public final static String KING_B_UNICODE = "&#9818;";
    public final static String KING_B_IMAGE = "king-black.png";
    public final static char KING_W = 'K';
    public final static String KING_W_UNICODE = "&#9812;";
    public final static String KING_W_IMAGE = "king-white.png";

    public final static char PAWN_B = 'p'; 
    public final static String PAWN_B_UNICODE = "&#9823;";
    public final static String PAWN_B_IMAGE = "pawn-black.png";
    public final static char PAWN_W = 'P';
    public final static String PAWN_W_UNICODE = "&#9817;";
    public final static String PAWN_W_IMAGE = "pawn-white.png";
    
    public final static Map<Character, String> UNICODE_MAP;
    public final static Map<Character, String> IMAGE_MAP;
    static {
        UNICODE_MAP = new Hashtable<Character, String>();
        UNICODE_MAP.put(ROOK_B, ROOK_B_UNICODE);
        UNICODE_MAP.put(ROOK_W, ROOK_W_UNICODE);
        UNICODE_MAP.put(KNIGHT_B, KNIGHT_B_UNICODE);
        UNICODE_MAP.put(KNIGHT_W, KNIGHT_W_UNICODE);
        UNICODE_MAP.put(BISHOP_B, BISHOP_B_UNICODE);
        UNICODE_MAP.put(BISHOP_W, BISHOP_W_UNICODE);
        UNICODE_MAP.put(QUEEN_B, QUEEN_B_UNICODE);
        UNICODE_MAP.put(QUEEN_W, QUEEN_W_UNICODE);
        UNICODE_MAP.put(KING_B, KING_B_UNICODE);
        UNICODE_MAP.put(KING_W, KING_W_UNICODE);
        UNICODE_MAP.put(PAWN_B, PAWN_B_UNICODE);
        UNICODE_MAP.put(PAWN_W, PAWN_W_UNICODE);
        IMAGE_MAP = new Hashtable<Character, String>();
        IMAGE_MAP.put(ROOK_B, IMAGE_FOLDER + ROOK_B_IMAGE);
        IMAGE_MAP.put(ROOK_W, IMAGE_FOLDER + ROOK_W_IMAGE);
        IMAGE_MAP.put(KNIGHT_B, IMAGE_FOLDER + KNIGHT_B_IMAGE);
        IMAGE_MAP.put(KNIGHT_W, IMAGE_FOLDER + KNIGHT_W_IMAGE);
        IMAGE_MAP.put(BISHOP_B, IMAGE_FOLDER + BISHOP_B_IMAGE);
        IMAGE_MAP.put(BISHOP_W, IMAGE_FOLDER + BISHOP_W_IMAGE);
        IMAGE_MAP.put(QUEEN_B, IMAGE_FOLDER + QUEEN_B_IMAGE);
        IMAGE_MAP.put(QUEEN_W, IMAGE_FOLDER + QUEEN_W_IMAGE);
        IMAGE_MAP.put(KING_B, IMAGE_FOLDER + KING_B_IMAGE);
        IMAGE_MAP.put(KING_W, IMAGE_FOLDER + KING_W_IMAGE);
        IMAGE_MAP.put(PAWN_B, IMAGE_FOLDER + PAWN_B_IMAGE);
        IMAGE_MAP.put(PAWN_W, IMAGE_FOLDER + PAWN_W_IMAGE);
    }
    
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
    
    public String getUnicode() {
        return UNICODE_MAP.get(getLetter());
    }
    
    public String getImage() {
        return IMAGE_MAP.get(getLetter());
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
