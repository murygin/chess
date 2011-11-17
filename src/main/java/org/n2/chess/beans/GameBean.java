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
import java.util.List;

import org.n2.chess.beans.hibernate.Game;
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
    
    List<Game> gameList;
    
    Game selectedGame;
    
    @Autowired
    private UserBean userBean;
    
    @Autowired
    private IGameService gameService;
    
    public List<Game> getGameList() {
        if(gameList==null && getUserBean().getUser()!=null) {
            gameList = getGameService().loadGames(getUserBean().getUser());
            if(gameList!=null && !gameList.isEmpty()) {
                setSelectedGame(gameList.get(0));
            }
        }
        return gameList;
    }
    
    public void create() {
        Game newGame = getGameService().create(getUserBean().getUser(),getEmailNew());
        gameList.add(newGame);
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
