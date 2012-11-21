/*******************************************************************************
 * Copyright (c) 2012 Daniel Murygin.
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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.schildbach.game.Board;
import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.chess.ChessRules;
import de.schildbach.game.common.ChessLikeMove;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
@Component("ruleService")
@Scope("prototype")
public class RuleService implements IRuleService, Serializable {

    transient ChessRules rules = null;
    GamePosition position = null;
    Board initialBoard = null;
    Collection<? extends ChessLikeMove> allowedMoves;
    
    public void parsePosition(String fen) {
        position = getRules().parsePosition(fen);
        allowedMoves = getRules().allowedMoves(position, getInitialBoard());
    }
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IRuleService#parseMove(java.lang.String)
     */
    @Override
    public GameMove parseMove(String notation) {
        if(notation.length()>5) {
            notation = notation.substring(1);
        }
        notation = notation.replace("x", "-");
        return getRules().parseMove(notation, Locale.ENGLISH, position, getInitialBoard());
    }
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IRuleService#getAllowedMoves()
     */
    @Override
    public Collection<? extends ChessLikeMove> getAllowedMoves() {
        if(allowedMoves==null) {
            allowedMoves = getRules().allowedMoves(position, getInitialBoard());
        }
        return allowedMoves;
    }
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IRuleService#getAllowedMoves(java.lang.String)
     */
    @Override
    public Collection<? extends ChessLikeMove> getAllowedMoves(String notationSource) {
        List<ChessLikeMove> moveList = new LinkedList<ChessLikeMove>();
        if(notationSource!=null) {
            for (ChessLikeMove move : getAllowedMoves()) {
                if(notationSource.equals(move.getSource().getNotation())) {
                    moveList.add(move);
                }
            }
        }
        return moveList;
    }
    

    public ChessRules getRules() {
        if(rules==null) {
            rules = new ChessRules(null);
        }
        return rules;
    }
    
    
    
    public Board getInitialBoard() {
        if(initialBoard==null) {
            initialBoard = getRules().initialPositionFromBoard(null).getBoard();
        }
        return initialBoard;
    }

   
    
    
}
