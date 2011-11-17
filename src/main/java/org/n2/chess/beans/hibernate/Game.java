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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    private Integer id;
    private User playerWhite;
    private User playerBlack;
    private Date startDate;
    private Date lastMoveDate;
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
}
