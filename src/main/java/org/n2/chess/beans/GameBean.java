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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.n2.chess.beans.hibernate.Game;
import org.n2.chess.beans.hibernate.Move;
import org.n2.chess.beans.hibernate.MoveTuble;
import org.n2.chess.beans.hibernate.User;
import org.n2.chess.model.Board;
import org.n2.chess.model.GameInfo;
import org.n2.chess.model.Piece;
import org.n2.chess.model.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Component("game")
@Scope("session")
public class GameBean implements Serializable{

    String emailNew;
    
    String colorNew = Board.WHITE;
    
    List<Game> gameList;
    
    List<GameInfo> gameInfoList;
    
    Game selectedGame;
    
    private boolean newGameVisible = false;
    
    @Autowired
    private UserBean userBean;
    
    @Autowired
    private BoardBean boardBean;
    
    @Autowired
    private IGameService gameService;
    
    @Autowired
    private IMailService mailService;
    
    public List<Game> getGameList() {
        return gameList;
    }
    
    /**
     * @return the gameInfoList
     */
    public List<GameInfo> getGameInfoList() {
        return gameInfoList;
    }

    public List<MoveTuble> getMoveList() {
        List<MoveTuble> moveList = Collections.emptyList();
        if(getSelectedGame()!=null) {
            moveList = new ArrayList<MoveTuble>();
            Set<Move> moves = getSelectedGame().getMoveSet();
            Move white = null;
            if(moves!=null) {
                for (Move move : moves) {
                    if(white==null) {
                        white = move;
                    } else {
                        moveList.add(new MoveTuble(white.getN(), white, move));
                        white = null;
                    }
                }
                if(white!=null) {
                    moveList.add(new MoveTuble(white.getN(), white, null));
                }
            }
        }
        return moveList;
    }
    
    public void init() {
        if(gameList==null && getUserBean().getUser()!=null) {
            gameList = getGameService().loadGames(getUserBean().getUser());
            gameInfoList = new ArrayList<GameInfo>();
            for (Game game : gameList) {
                gameInfoList.add(new GameInfo(game, getUserBean().getUser()));
            }
            if(gameList!=null && !gameList.isEmpty()) {
                setSelectedGame(gameList.get(0));
            }          
        }
        getGameList();
    }
    
    public void create() {
        Game newGame = null;
        String oppenent = null;
        String color;
        if(Board.WHITE.equals(getColorNew())) {
            newGame = getGameService().create(getUserBean().getUser(),getEmailNew());
            oppenent = newGame.getPlayerBlack().getLogin();
            color = "white";
        } else {
            newGame = getGameService().create(getEmailNew(),getUserBean().getUser());
            oppenent = newGame.getPlayerWhite().getLogin();
            color = "black";
        }
        gameList.add(newGame);
        gameInfoList.add(new GameInfo(newGame, getUserBean().getUser()));
        setSelectedGame(newGame);
        setNewGameVisible(false);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New game saved", "Opponent: " + oppenent + ", your color: " + color));
    }
    
    public void move() {
        Date date = Calendar.getInstance().getTime();
        String notation = getBoardBean().createNotation();
        getBoardBean().move();
        Move move = new Move();
        move.setGameId(getSelectedGame().getId());
        move.setN(1);
        move.setDate(date);
        move.setMove(notation);
        move.setFen(getSelectedGame().getFen());
        getSelectedGame().getMoveSet().add(move);
        getSelectedGame().setStatus(getBoardBean().getBoard().getActive());
        getSelectedGame().setLastMoveDate(date);
        getGameService().updateGame(getSelectedGame());     
        getMailService().sendMail(null, getOpponent().getEmail(), Messages.getString("GameBean.0"), Messages.getString("GameBean.1", getOpponent().getLogin(), getUserBean().getUser().getLogin(), notation)); //$NON-NLS-1$ //$NON-NLS-2$
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Move saved", "Notation: " + notation));
    }
    
    /**
     * @return
     */
    private User getOpponent() {
        return (getMyColor().equals(Board.WHITE) ? getSelectedGame().getPlayerBlack() : getSelectedGame().getPlayerWhite());
    }

    public boolean getMyTurn() {
        boolean myTurn = false;
        String myColor = getMyColor();
        if(myColor!=null && getBoardBean().getBoard()!=null) {
            myTurn = myColor.equals(getBoardBean().getBoard().getActive());
        }
        return myTurn;
    }
    
    public String getMyColor() {
        String myColor = null;
        if(getSelectedGame()!=null && getUserBean().getUser()!=null) {
            if(getSelectedGame().getPlayerBlack().getLogin().equals(getUserBean().getUser().getLogin())) {
                myColor=Board.BLACK;
            }
            if(getSelectedGame().getPlayerWhite().getLogin().equals(getUserBean().getUser().getLogin())) {
                myColor=Board.WHITE;
            }
        }
        return myColor;
    }
    
    /**
     * @return the selectedGame
     */
    public Game getSelectedGame() {
        return selectedGame;
    }

    /**
     * @param selectedGame the selectedGame to set
     */
    public void setSelectedGame(Game selectedGame) {
        this.selectedGame = selectedGame;
        getBoardBean().setColorPlayer(getMyColor());
        getBoardBean().setGame(selectedGame);
        if(getMyTurn()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "It's your turn", "To move click the board."));         
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please wait", "It's not your turn."));
        }
    }

    /**
     * @return the newGameVisible
     */
    public boolean getNewGameVisible() {
        return newGameVisible;
    }

    /**
     * @param newGameVisible the newGameVisible to set
     */
    public void setNewGameVisible(boolean newGameVisible) {
        this.newGameVisible = newGameVisible;
    }
    
    public void toggleNewGame() {
        this.newGameVisible = !this.newGameVisible;
    }

    /**
     * @return the emailNew
     */
    public String getEmailNew() {
        return emailNew;
    }
    /**
     * @param emailNew the emailNew to set
     */
    public void setEmailNew(String emailNew) {
        this.emailNew = emailNew;
    }
    /**
     * @return the colorNew
     */
    public String getColorNew() {
        return colorNew;
    }

    /**
     * @param colorNew the colorNew to set
     */
    public void setColorNew(String colorNew) {
        this.colorNew = colorNew;
    }

    /**
     * @return the userBean
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * @param userBean the userBean to set
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * @return the boardBean
     */
    public BoardBean getBoardBean() {
        return boardBean;
    }

    /**
     * @param boardBean the boardBean to set
     */
    public void setBoardBean(BoardBean boardBean) {
        this.boardBean = boardBean;
    }

    /**
     * @return the gameService
     */
    public IGameService getGameService() {
        return gameService;
    }

    /**
     * @param gameService the gameService to set
     */
    public void setGameService(IGameService gameService) {
        this.gameService = gameService;
    }

    /**
     * @return the mailService
     */
    public IMailService getMailService() {
        return mailService;
    }

    /**
     * @param mailService the mailService to set
     */
    public void setMailService(IMailService mailService) {
        this.mailService = mailService;
    }

    
}
