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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.n2.chess.beans.BoardService;


/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Entity
@Table(name = "move")
public class Move implements Serializable {

    private Integer id;
    private Integer gameId;
    private Integer n;
    private String move;
    private Date date;
    private String fen;
    private String css;
    private int sourceX, sourceY, destX, destY;

    public Move() {
        super();
    }
    
    public void calculateCoordinates() {
        if(move==null) {
            return;
        }
        if(move.contains("0")) {
            calculateCastlingCoordinates();
            return;
        }
        String move2 = move;
        if(move2.length()>5) {
            move2 = move2.substring(1);
        }
        String source = move2.substring(0, 2);
        String dest = move2.substring(3, 5);
        setSourceY(BoardService.LETTER_NUMBER_MAP.get(source.substring(0, 1)));
        setSourceX(Integer.valueOf(source.substring(1)));
        setDestY(BoardService.LETTER_NUMBER_MAP.get(dest.substring(0, 1)));
        setDestX(Integer.valueOf(dest.substring(1)));
    }
    
    /**
     * @return
     */
    private void calculateCastlingCoordinates() {
        if(move==null || !move.contains("0")) {
            return;
        }
    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * @return the gameId
     */
    @Column(name = "game_id", nullable = false)
    public Integer getGameId() {
        return gameId;
    }

    /**
     * @param gameId the gameId to set
     */
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    /**
     * @return the id
     */
    @Column(name = "number", nullable = false)
    public Integer getN() {
        return n;
    }
    /**
     * @param id the id to set
     */
    public void setN(Integer n) {
        this.n = n;
    }

   

    /**
     * @return the move
     */
    @Column(name = "move", nullable = true, length = 10)
    public String getMove() {
        return move;
    }

    /**
     * @param move the move to set
     */
    public void setMove(String move) {
        this.move = move;
    }

    /**
     * @return the startDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "movedate", nullable = false)
    public Date getDate() {
        return date;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the salt
     */
    @Column(name = "fen", nullable = true, length = 70)
    public String getFen() {
        return fen;
    }

    /**
     * @param salt the salt to set
     */   
    public void setFen(String fen) {
        this.fen = fen;
    }
    
    @Transient
    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    @Transient
    public int getSourceX() {
        return sourceX;
    }

    public void setSourceX(int sourceX) {
        this.sourceX = sourceX;
    }

    @Transient
    public int getSourceY() {
        return sourceY;
    }

    public void setSourceY(int sourceY) {
        this.sourceY = sourceY;
    }

    @Transient
    public int getDestX() {
        return destX;
    }

    public void setDestX(int destX) {
        this.destX = destX;
    }

    @Transient
    public int getDestY() {
        return destY;
    }

    public void setDestY(int destY) {
        this.destY = destY;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Move other = (Move) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
