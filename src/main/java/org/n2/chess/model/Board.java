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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
public class Board implements Serializable {

    Map<Integer,Row> rowMap = new Hashtable<Integer, Row>(8);

    
    /**
     * @param row
     */
    public void putRow(Row row) {
        rowMap.put(row.getNumber(), row);      
    }
    
    public List<Row> getRows() {
        return new ArrayList<Row>(rowMap.values());
    }
    
    /**
     * @return the rowMap
     */
    public Map<Integer, Row> getRowMap() {
        return rowMap;
    }

    /**
     * @param rowMap the rowMap to set
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
        
    }
    
    private void unSelect() {
        for (Row row : getRowMap().values()) {
            row.unSelect();
        }
        
    }
    
    private void unSource() {
        for (Row row : getRowMap().values()) {
            row.unSource();
        }
        
    }

    /**
     * @param source
     */
    public void setDest(Square dest) {
        getRowMap().get(dest.getRow()).getSquareMap().get(dest.getColumn()).setDest(true);
        
    }
    
    private void unDest() {
        for (Row row : getRowMap().values()) {
            row.unDest();
        }
        
    }

    

    
}
