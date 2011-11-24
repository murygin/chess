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
package org.n2.chess.beans.hibernate;

import java.io.Serializable;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
public class MoveTuble implements Serializable {

    private int number;
    
    private Move white;
    
    private Move black;

    
    
    /**
     * @param number
     * @param white
     * @param black
     */
    public MoveTuble(int number, Move white, Move black) {
        super();
        this.number = number;
        this.white = white;
        this.black = black;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

   
    public Move getWhite() {
        return white;
    }

    public void setWhite(Move white) {
        this.white = white;
    }

   
    public Move getBlack() {
        return black;
    }

    public void setBlack(Move black) {
        this.black = black;
    }
}
