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
    
    private String message = "Hello from a UserService instance.";

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
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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

}
