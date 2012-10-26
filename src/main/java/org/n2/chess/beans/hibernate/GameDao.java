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
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Repository("gameDao")
@SuppressWarnings("unchecked")
public class GameDao extends CustomHibernateDaoSupport implements IGameDao, Serializable {

    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IUserDao#save(org.n2.chess.beans.hibernate.User)
     */
    @Override
    public void save(Game game) {
        getHibernateTemplate().save(game);
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IUserDao#update(org.n2.chess.beans.hibernate.User)
     */
    @Override
    public void update(Game game) {
        getHibernateTemplate().update(game);
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IUserDao#delete(org.n2.chess.beans.hibernate.User)
     */
    @Override
    public void delete(Game user) {
        getHibernateTemplate().delete(user);
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IUserDao#findAll()
     */
    @Override
    public List<Game> loadAll() {
        return getHibernateTemplate().loadAll(Game.class);
    }
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IGameDao#load(java.lang.Integer)
     */
    @Override
    public Game load(Integer id) {
        return getHibernateTemplate().load(Game.class, id);
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IUserDao#find(java.lang.String, java.lang.Object[])
     */
    @Override
    public List<Game> find(String query, Object... values) {
        return getHibernateTemplate().find(query, values);
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IUserDao#findByExample(org.n2.chess.beans.hibernate.User)
     */
    @Override
    public List<Game> findByExample(Game game) {
        return getHibernateTemplate().findByExample(game);
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.hibernate.IUserDao#find(org.hibernate.criterion.DetachedCriteria)
     */
    @Override
    public List<Game> find(DetachedCriteria criteria) {
        return getHibernateTemplate().findByCriteria(criteria);
    }

}
