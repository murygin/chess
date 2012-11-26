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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.n2.chess.beans.hibernate.Game;
import org.n2.chess.beans.hibernate.Move;
import org.n2.chess.beans.hibernate.MoveTuble;
import org.n2.chess.beans.hibernate.User;
import org.n2.chess.model.Board;
import org.n2.chess.model.GameInfo;
import org.n2.chess.model.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.schildbach.game.exception.ParseException;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@SuppressWarnings("serial")
@Component("game")
@Scope("session")
public class GameBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(GameBean.class);
    
    String emailNew;
    
    String colorNew = Board.WHITE;
    
    List<Game> gameList;
    
    List<GameInfo> gameInfoList;
    
    GameInfo selectedGameInfo;
    
    private boolean newGameVisible = false;
    
    @Autowired
    private UserBean userBean;
    
    @Autowired
    private BoardBean boardBean;
    
    @Autowired
    private IGameService gameService;
    
    public List<Game> getGameList() {
        if(gameList==null) {
            gameList = new LinkedList<Game>();
        }
        return gameList;
    }
    
    /**
     * @return the gameInfoList
     */
    public List<GameInfo> getGameInfoList() {
        if(gameInfoList==null) {
            gameInfoList = new LinkedList<GameInfo>();
        }
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
        if(getUserBean().getUser()!=null) {
            gameList = getGameService().loadGames(getUserBean().getUser());
            gameInfoList = new ArrayList<GameInfo>();
            for (Game game : gameList) {
                gameInfoList.add(new GameInfo(game, getUserBean().getUser()));
            }
            Collections.sort(gameInfoList);
            if(gameInfoList!=null && !gameInfoList.isEmpty()) {
                setSelectedGame(gameInfoList.get(0).getGame());
                initBoardBean();
            }          
        }
        getGameList();
    }
    
    public void loadGame() {
        Game game = getGameService().loadGame(getSelectedGame().getId());
        getGameList().remove(getSelectedGameInfo().getGame());
        getGameInfoList().remove(getSelectedGameInfo());
        setSelectedGame(game);
        initBoardBean();
    }

    private void initBoardBean() {
        getBoardBean().setColorPlayer(getMyColor());
        getBoardBean().setGame(getSelectedGame());
        if(getMyTurn()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "It's your turn", "To move click the board."));         
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please wait", "It's not your turn."));
        }
    }
    
    public void create() {
        try {
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
            getGameList().add(newGame);
            getGameInfoList().add(new GameInfo(newGame, getUserBean().getUser()));
            setSelectedGame(newGame);
            loadGame();
            setNewGameVisible(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New game saved", "Opponent: " + oppenent + ", your color: " + color));
        } catch (UserNotFoundException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Game creation failed: " + e.getMessage());
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Game creation failed", e.getMessage()));
        } catch (Exception e) {
            LOG.error("Game creation failed: ", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Game creation failed", "Unknow error."));
        }
    }
    
    public void move() {
        String notation = null;
        try {
            Date date = Calendar.getInstance().getTime();
            notation = getBoardBean().createNotation();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing new move: " + notation + ", FEN: " + getSelectedGame().getFen() + ", game-id: " + getSelectedGame().getId());
            }
            getBoardBean().move();
            Move move = new Move();
            move.setGameId(getSelectedGame().getId());
            move.setN(getSelectedGame().getMoveSet().size() + 1);
            move.setDate(date);
            move.setMove(notation);
            move.setFen(getSelectedGame().getFen());
            getSelectedGame().getMoveSet().add(move);
            getSelectedGame().setStatus(getBoardBean().getBoard().getActive());
            getSelectedGame().setLastMoveDate(date);
            getSelectedGame().setNotifyDate(null);
            getGameService().updateGame(getSelectedGame());
            if (LOG.isDebugEnabled()) {
                LOG.debug("New move saved, new FEN: " + getSelectedGame().getFen() + ", game-id: " + getSelectedGame().getId());
            }
            replaceGameInLists(getSelectedGame());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Move saved", "Notation: " + notation));
        } catch(ParseException parseException) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Invalid move: " + notation + ", FEN: " + getSelectedGame().getFen());
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid move", "Invalid move: " + notation));           
        } catch(Exception e) {
            LOG.error("Moving failed: ", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Moving failed, unknwon error."));           
        }
    }
    
    private void replaceGameInLists(Game selectedGame) {
        getGameList().remove(selectedGame);
        getGameInfoList().remove(new GameInfo(selectedGame, getUserBean().getUser()));
        getGameList().add(selectedGame);
        getGameInfoList().add(new GameInfo(selectedGame, getUserBean().getUser()));
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
        Game game = null;
        if(getSelectedGameInfo()!=null) {
            game = getSelectedGameInfo().getGame();
        }
        return game;
    }
    
    /**
     * @return the selectedGame
     */
    public GameInfo getSelectedGameInfo() {
        return selectedGameInfo;
    }

    /**
     * @param selectedGame the selectedGame to set
     */
    public void setSelectedGameInfo(GameInfo selectedGame) {
        this.selectedGameInfo = selectedGame;
        if(!getGameInfoList().contains(selectedGameInfo)) {
            getGameInfoList().add(selectedGameInfo);
        }
        if(!getGameList().contains(selectedGameInfo.getGame())) {
            getGameList().add(selectedGameInfo.getGame());
        }
    }
    
    /**
     * @param selectedGame the selectedGame to set
     */
    public void setSelectedGame(Game selectedGame) {
        setSelectedGameInfo(new GameInfo(selectedGame, getUserBean().getUser()));       
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
    
}
