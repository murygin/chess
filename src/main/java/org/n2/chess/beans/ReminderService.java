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
    @Scheduled(cron="0 */5 * * * ?")
    //every two hours@Scheduled(cron="0 0 0/2 * * ?")
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
        Date date = Calendar.getInstance().getTime();
        for (Game game : gameList) {
            String email = game.getPlayerBlack().getEmail();
            String name = game.getPlayerBlack().getLogin();
            String opponent = game.getPlayerWhite().getLogin();
            if(Game.WHITE.equals(game.getStatus())) {
                email = game.getPlayerWhite().getEmail();
                name = game.getPlayerWhite().getLogin();
                opponent = game.getPlayerBlack().getLogin();
            }
            getMailService().sendMail(null, email, Messages.getString("GameBean.0"), Messages.getString("GameBean.1", name, opponent)); //$NON-NLS-1$ //$NON-NLS-2$
            game.setNotifyDate(date);
            getGameDao().update(game);
            if (LOG.isInfoEnabled()) {
                LOG.info("Reminder send to: " + name);
            }
        }
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
