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
import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.n2.chess.beans.hibernate.Game;
import org.n2.chess.model.Board;
import org.n2.chess.model.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.schildbach.game.common.ChessLikeMove;
import de.schildbach.game.exception.ParseException;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@SuppressWarnings("serial")
@Component("board")
@Scope("session")
public class BoardBean implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(BoardBean.class);
    
    private Board board;
    
    private Square square;
    
    private Square source;
    
    private Square dest;
    
    private Game game;
    
    private String colorPlayer;
    
    @Autowired
    private IBoardService boardService;
    
    @Autowired
    private IRuleService ruleService;
    
    /**
     * @return the board
     */
    public Board getBoard() {
        if(board==null) {
            board = createBoard();
        }
        return board;
    }
    
    /**
     * @param string 
     * @return
     */
    private Board createBoard() {
        if(getGame()!=null) {
            this.board = getBoardService().createBoard(getGame(),getColorPlayer());
        }
        return this.board;
    }
    
    private Board createBoard(int moveNumber) {
        if(getGame()!=null) {
            this.board = getBoardService().createBoard(getGame(),getColorPlayer(),moveNumber);
        }
        return this.board;
    }
    
    /**
     * Selects a square on the board. 
     * Method is called when the user clicks the board.
     */
    public void select() {
        if(getSquare()!=null) {
            if(getSource()==null || (getSource()!=null && getDest()!=null)) {
                setSource(getSquare());
                setDest(null);
                getBoard().setSource(getSource());
                hideLastMoves();
                showNextMoves();                       
            } else {
                setDest(getSquare());
                getBoard().setDest(getDest());
                String notation = null;
                try {
                    notation = createNotation();
                    getRuleService().parseMove(notation);
                } catch(ParseException parseException) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Invalid move: " + notation + ", FEN: " + getGame().getFen());
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid move", "Invalid move: " + notation));           
                }
            }
        }
    }

    private void hideLastMoves() {
        getBoard().hideLastMoves();
    }

    private void showNextMoves() {
        Collection<? extends ChessLikeMove> moveList = getRuleService().getAllowedMoves(getSource().getNotation());
        for (ChessLikeMove move : moveList) {
            String notation = move.getTarget().getNotation();
            Square square = getSquare(notation);
            square.setNext(true);
        }
    }
    
    private String printMoveList(Collection<? extends ChessLikeMove> moveList) {
        StringBuilder sb = new StringBuilder();
        if(moveList!=null) {
            for (ChessLikeMove move : moveList) {
                sb.append(move.getTarget().getNotation()).append(", ");
            }
        }
        return sb.toString();
    }

    public void move() {
        getRuleService().parseMove(createNotation());
        castling();
        getBoard().move();       
        getGame().setFen(getBoardService().createFen(getBoard()));
        getRuleService().parsePosition(getGame().getFen());
        hideLastMoves();
    }

    private void castling() {
        if(isCastlingKingsideMove() && isCastlingKingsideValid()) {
            getBoard().castlingKingsideRookMove();
            // rule engine expects a selected rook for castling
            getBoard().getDest().setColumn(6);
        }
        if(isCastlingQueensideMove() && isCastlingQueensideValid()) {
            getBoard().castlingQueensideRookMove();
            // rule engine expects a selected rook for castling
            getBoard().getDest().setColumn(2);
        }
    }
    
    /**
     * Sets the source and the destination square of an move.
     * 
     * @param notation Two letter number combinations: e2e5, b8c6
     */
    public void setSourceAndDest(String notation) {
        String sourceNotation = notation.substring(0,2);
        String destNotation = notation.substring(2,4);
        setSource(getSquare(sourceNotation));
        getBoard().setSource(getSource());
        setDest(getSquare(destNotation));
        getBoard().setDest(getDest());
    }
    
    /**
     * @param notation Letter number notation: e5, d4, a1
     * @return The square for this notation
     */
    protected Square getSquare(String notation) {
        int row = Integer.valueOf(notation.substring(1));
        int col = BoardService.LETTER_NUMBER_MAP.get(notation.substring(0,1));
        return getBoard().getRowMap().get(8-row).getSquareMap().get(col);
    }
    
    public String createNotation() {
        return getBoardService().createNotation(getSource(),getDest(),getColorPlayer());
    }
    
    public String getNotation() {
        return createNotation();
    }
    
    public boolean isCastlingKingsideMove() {
        return getBoardService().isCastlingKingsideMove(getSource(),getDest(),getColorPlayer());
    }
    
    public boolean isCastlingKingsideValid() {
        boolean valid = false;
        if(Board.WHITE.equals(getColorPlayer())) {
            valid = getBoard().getCastling().contains("K");
        }
        if(Board.BLACK.equals(getColorPlayer())) {
            valid = getBoard().getCastling().contains("k");
        }
        return valid;
    }
    
    public boolean isCastlingQueensideMove() {
        return getBoardService().isCastlingQueensideMove(getSource(),getDest(),getColorPlayer());
    }
    
    public boolean isCastlingQueensideValid() {
        boolean valid = false;
        if(Board.WHITE.equals(getColorPlayer())) {
            valid = getBoard().getCastling().contains("Q");
        }
        if(Board.BLACK.equals(getColorPlayer())) {
            valid = getBoard().getCastling().contains("q");
            
        }
        return valid;
    }
    
    /**
     * @return the square
     */
    public Square getSquare() {
        return square;
    }

    /**
     * @param square the square to set
     */
    public void setSquare(Square square) {
        this.square = square;
    }

    

    /**
     * @return the source
     */
    public Square getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Square source) {
        this.source = source;
    }

    /**
     * @return the dest
     */
    public Square getDest() {
        return dest;
    }

    /**
     * @param dest the dest to set
     */
    public void setDest(Square dest) {
        this.dest = dest;
    }

    /**
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * @param game the game to set
     */
    public void setGame(Game game) {
        getRuleService().parsePosition(game.getFen());
        this.game = game;
        board = createBoard();
    }
    
    public void loadHistory(int moveNumber) {
        board = createBoard(moveNumber);
    }

    /**
     * @return the colorPlayer
     */
    public String getColorPlayer() {
        return colorPlayer;
    }

    /**
     * @param colorPlayer the colorPlayer to set
     */
    public void setColorPlayer(String colorPlayer) {
        this.colorPlayer = colorPlayer;
    }

    public IBoardService getBoardService() {
        return boardService;
    }

    public void setBoardService(IBoardService boardService) {
        this.boardService = boardService;
    }

    public IRuleService getRuleService() {
        return ruleService;
    }

    public void setRuleService(IRuleService ruleService) {
        this.ruleService = ruleService;
    }

    

}
