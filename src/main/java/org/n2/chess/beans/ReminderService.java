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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.n2.chess.beans.hibernate.Game;
import org.n2.chess.beans.hibernate.IGameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
@Service("reminderService")
public class ReminderService {

    private static final Logger LOG = Logger.getLogger(ReminderService.class);
    
    @Autowired
    IGameDao gameDao;
    
    @Autowired
    private IMailService mailService;
    
    //every 5 minutes
    //@Scheduled(cron="0 */5 * * * ?")
    //every hour
    @Scheduled(cron="0 0 0/1 * * ?")
    public void send() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending reminder...");
        }
        DetachedCriteria crit = DetachedCriteria.forClass(Game.class);
        crit.add(Restrictions.isNull("notifyDate"));
        crit.setFetchMode("playerWhite", FetchMode.JOIN);
        crit.setFetchMode("playerBlack", FetchMode.JOIN);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Game> gameList = getGameDao().find(crit);      
        for (Game game : gameList) {
            sendReminder(game);
        }
    }

    private void sendReminder(Game game) {
        Date date = Calendar.getInstance().getTime();
        boolean drawReminder = false;
        boolean resignReminder = false;
        String email = game.getPlayerBlack().getEmail();
        String name = game.getPlayerBlack().getLogin();
        String opponent = game.getPlayerWhite().getLogin();
        String status = game.getStatus();
        if(Game.WHITE.equals(status)) {
            email = game.getPlayerWhite().getEmail();
            name = game.getPlayerWhite().getLogin();
            opponent = game.getPlayerBlack().getLogin();                       
        } else if(Game.DRAW.equals(status)) {
            drawReminder = true;
            if(Game.WHITE.equals(game.getDrawOffer())) {
                email = game.getPlayerWhite().getEmail();
                name = game.getPlayerWhite().getLogin();
                opponent = game.getPlayerBlack().getLogin();
            } else {
                email = game.getPlayerBlack().getEmail();
                name = game.getPlayerBlack().getLogin();
                opponent = game.getPlayerWhite().getLogin();
            }
        } else if(Game.BLACK_WIN.equals(status)) {
            resignReminder = true;
            email = game.getPlayerBlack().getEmail();
            name = game.getPlayerBlack().getLogin();
            opponent = game.getPlayerWhite().getLogin();     
        } else if(Game.WHITE_WIN.equals(status)) {
            resignReminder = true;
            email = game.getPlayerWhite().getEmail();
            name = game.getPlayerWhite().getLogin();
            opponent = game.getPlayerBlack().getLogin();
        }
        if(drawReminder) {
            sendDrawReminder(email, name, opponent);
            if (LOG.isInfoEnabled()) {
                LOG.info("Draw reminder send to: " + name);
            }
        } else if(resignReminder) {
            sendResignReminder(email, name, opponent);
            if (LOG.isInfoEnabled()) {
                LOG.info("Resign reminder send to: " + name);
            }
        } else {
            sendMoveReminder(email, name, opponent);
            if (LOG.isInfoEnabled()) {
                LOG.info("Move reminder send to: " + name);
            }
        }
        game.setNotifyDate(date);
        getGameDao().update(game);        
    }

    
    
    private void sendDrawReminder(String email, String name, String opponent) {
        getMailService().sendMail(
                null, 
                email, 
                Messages.getString("GameBean.4"), //$NON-NLS-1$
                Messages.getString("GameBean.5", name, opponent)); //$NON-NLS-1$
        
    }
    
    private void sendResignReminder(String email, String name, String opponent) {
        getMailService().sendMail(
                null, 
                email, 
                Messages.getString("GameBean.6"), //$NON-NLS-1$
                Messages.getString("GameBean.7", name, opponent)); //$NON-NLS-1$
        
    }

    private void sendMoveReminder(String email, String name, String opponent) {
        getMailService().sendMail(
                null, 
                email, 
                Messages.getString("GameBean.0"), //$NON-NLS-1$
                Messages.getString("GameBean.1", name, opponent)); //$NON-NLS-1$ 
    }

    public IGameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(IGameDao gameDao) {
        this.gameDao = gameDao;
    }

    public IMailService getMailService() {
        return mailService;
    }

    public void setMailService(IMailService mailService) {
        this.mailService = mailService;
    }
}
