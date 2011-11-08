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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.n2.chess.beans.hibernate.IUserDao;
import org.n2.chess.beans.hibernate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Service("userService")
public class UserService implements IUserService {
    
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    private IUserDao userDao;
    
    /**
     * @return the userDao
     */
    public IUserDao getUserDao() {
        return userDao;
    }

    /**
     * @param userDao the userDao to set
     */
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.IUserService#loadAll()
     */
    @Override
    public List<User> getAll() {
        return getUserDao().loadAll();
    }
    
    public void save(User user) {
        getUserDao().save(user);
    }
    
    /* (non-Javadoc)
     * @see org.n2.chess.beans.IUserService#findUser(java.lang.String)
     */
    @Override
    public User findUser(String username) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("login", username));
        User user = null;
        List<User> result = getUserDao().find(criteria);
        if(result!=null) {
            if(result.size()>1) {
                LOG.error("More than one user found with login: " + username);
                throw new RuntimeException("More than one user found.");
            }
            if(!result.isEmpty()) {
                user = result.get(0);
            }
        }
        return user;
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.IUserService#isUsernameAvailable(java.lang.String)
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        List<User> userList = getUserDao().findByExample(new User(username,null,null,null));
        return userList==null || userList.isEmpty();
    }

    /* (non-Javadoc)
     * @see org.n2.chess.beans.IUserService#isEmailAvailable(java.lang.String)
     */
    @Override
    public boolean isEmailAvailable(String email) {
        List<User> userList = getUserDao().findByExample(new User(null,email,null,null));
        return userList==null || userList.isEmpty();
    }

    

}
