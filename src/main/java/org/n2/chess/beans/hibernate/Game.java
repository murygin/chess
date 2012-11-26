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
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Entity
@Table(name = "game", catalog = "cchess")
public class Game implements Serializable {

    public static final String BLACK = "b";
    public static final String WHITE = "w";
    public static final String DRAW = "draw";
    public static final String BLACK_WIN = "bwin";
    public static final String WHITE_WIN = "wwin";
    
    private Integer id;
    private User playerWhite;
    private User playerBlack;
    private String status; 
    private Set<Move> moveSet;
    private Date startDate;
    private Date lastMoveDate;
    private Date notifyDate;
    private String fen;

    public Game() {
        super();
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
     * @return the playerWhite
     */
    @ManyToOne( targetEntity=User.class )
    @JoinColumn(name="white")
    public User getPlayerWhite() {
        return playerWhite;
    }

    /**
     * @param playerWhite the playerWhite to set
     */ 
    public void setPlayerWhite(User playerWhite) {
        this.playerWhite = playerWhite;
    }
    
    /**
     * @return the playerBlack
     */
    @ManyToOne( targetEntity=User.class )
    @JoinColumn(name="black")
    public User getPlayerBlack() {
        return playerBlack;
    }
   
    /**
     * @param playerBlack the playerBlack to set
     */   
    public void setPlayerBlack(User playerBlack) {
        this.playerBlack = playerBlack;
    }
    
   
    /**
     * @return the status
     */
    @Column(name = "status", nullable = false, length = 10)
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @OneToMany(fetch=FetchType.EAGER,cascade = {CascadeType.ALL})
    @JoinColumn(name="game_id")
    @OrderBy("date")
    public Set<Move> getMoveSet() {
        return moveSet;
    }
    
    /**
     * @param moveSet the moveSet to set
     */
    public void setMoveSet(Set<Move> moveSet) {
        this.moveSet = moveSet;
    }

    /**
     * @return the startDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start")
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the lastMoveDate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_move")
    public Date getLastMoveDate() {
        return lastMoveDate;
    }

    /**
     * @param lastMoveDate the lastMoveDate to set
     */  
    public void setLastMoveDate(Date lastMoveDate) {
        this.lastMoveDate = lastMoveDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notified")
    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    /**
     * @return the salt
     */
    @Column(name = "fen", nullable = false, length = 70)
    public String getFen() {
        return fen;
    }

    /**
     * @param salt the salt to set
     */   
    public void setFen(String fen) {
        this.fen = fen;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((playerBlack == null) ? 0 : playerBlack.hashCode());
        result = prime * result + ((playerWhite == null) ? 0 : playerWhite.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
        Game other = (Game) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (playerBlack == null) {
            if (other.playerBlack != null)
                return false;
        } else if (!playerBlack.equals(other.playerBlack))
            return false;
        if (playerWhite == null) {
            if (other.playerWhite != null)
                return false;
        } else if (!playerWhite.equals(other.playerWhite))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        return true;
    }
    
    
}
