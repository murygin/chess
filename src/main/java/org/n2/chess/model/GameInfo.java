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
package org.n2.chess.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.n2.chess.beans.hibernate.Game;
import org.n2.chess.beans.hibernate.User;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@SuppressWarnings("serial")
public class GameInfo implements Serializable, Comparable<GameInfo>{

    public static final String MOVE = "move";
    public static final String WAIT = "wait";
    public static final String WIN = "win";
    public static final String DRAW = "draw";
    public static final String LOSS = "loss";
    
    public static final String STYLE_CURRENT = "current";
    
    private static final String IMAGE_FOLDER = "image/";
    
    private static Map<String, Integer> STATUS_SORT_MAP;
    static {
        STATUS_SORT_MAP = new Hashtable<String, Integer>();
        STATUS_SORT_MAP.put(MOVE, 0);
        STATUS_SORT_MAP.put(WAIT, 1);
        STATUS_SORT_MAP.put(WIN, 2);
        STATUS_SORT_MAP.put(DRAW, 3);
        STATUS_SORT_MAP.put(LOSS, 4);
    }
    
    private static Map<String, String> STATUS_IMAGE_MAP;
    static {
        STATUS_IMAGE_MAP = new Hashtable<String, String>();
        STATUS_IMAGE_MAP.put(MOVE, "");
        STATUS_IMAGE_MAP.put(WAIT, IMAGE_FOLDER + "pause.gif");
        STATUS_IMAGE_MAP.put(WIN, "");
        STATUS_IMAGE_MAP.put(DRAW, "");
        STATUS_IMAGE_MAP.put(LOSS, "");
    }
    
    private static Map<String, String> STATUS_MESSAGE_MAP;
    static {
        STATUS_MESSAGE_MAP = new Hashtable<String, String>();
        STATUS_MESSAGE_MAP.put(MOVE, "Your turn");
        STATUS_MESSAGE_MAP.put(WAIT, "Wait");
        STATUS_MESSAGE_MAP.put(WIN, "You won");
        STATUS_MESSAGE_MAP.put(DRAW, "Draw");
        STATUS_MESSAGE_MAP.put(LOSS, "You lost");
    }
    
    Game game;
    
    String opponent;
    
    String color;
    
    String lastMove;
    
    String status;
    
    String style;
    
    public GameInfo(Game game, User user) {
        this.game = game;
        if(game.getPlayerBlack().equals(user)) {
            color = Board.BLACK;
            opponent = game.getPlayerWhite().getLogin();
        }
        if(game.getPlayerWhite().equals(user)) {
            color = Board.WHITE;
            opponent = game.getPlayerBlack().getLogin();
        }
        Date date = (game.getLastMoveDate()!=null) ? game.getLastMoveDate() : game.getStartDate();
        lastMove = getHumanRedableTime(Calendar.getInstance().getTimeInMillis() - date.getTime());
        status = (game.getStatus().equals(color)) ? MOVE : WAIT;
        if(Game.DRAW.equals(game.getStatus())) {
            status = DRAW;
        }
        if(Game.WHITE_WIN.equals(game.getStatus()) && Board.BLACK.equals(color)) {
            status = LOSS;
        }
        if(Game.WHITE_WIN.equals(game.getStatus()) && Board.WHITE.equals(color)) {
            status = WIN;
        }
        if(Game.BLACK_WIN.equals(game.getStatus()) && Board.BLACK.equals(color)) {
            status = WIN;
        }
        if(Game.BLACK_WIN.equals(game.getStatus()) && Board.WHITE.equals(color)) {
            status = LOSS;
        }
    }
    
    /**
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return the opponent
     */
    public String getOpponent() {
        return opponent;
    }
    
    public String getOpponentShort() {
        String name = getOpponent();
        if(name!=null && name.length()>18) {
            StringBuilder sb = new StringBuilder(name.substring(0,15));
            name = sb.append("..").toString();
        }
        return name;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @return the lastMove
     */
    public String getLastMove() {
        return lastMove;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    public String getStyle() {
        if(style==null) {
            return "";
        }
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStatusImage() {
        return STATUS_IMAGE_MAP.get(getStatus());
    }
    
    public String getStatusMessage() {
        return STATUS_MESSAGE_MAP.get(getStatus());
    }

    public static String getHumanRedableTime(long ms) {
        double x = ms / 1000.0;
        long seconds = Math.round(x % 60);
        x /= 60;
        long minutes = Math.round(x % 60);
        x /= 60;
        long hours = Math.round(x % 24);
        x /= 24;
        long days = Math.round(x);
        StringBuilder sb = new StringBuilder();
        if(days>0) {
            sb.append(days).append(" d");
        }
        if(hours>0) {
            if(sb.length()>0) {
                sb.append(", ");
            }
            sb.append(hours).append(" h");
        }
        if(minutes>0 && days<1) {
            if(sb.length()>0) {
                sb.append(", ");
            }
            sb.append(minutes).append(" m");
        }
        if(seconds>0 && hours<1) {
            if(sb.length()>0) {
                sb.append(", ");
            }
            sb.append(seconds).append(" s");
        }
        String time = sb.toString();
        if(time.isEmpty()) {
            time = "just now";
        }
        return time;     
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(GameInfo game) {
        int EQUAL = 0;
        int result = EQUAL;
        if(this.getStatus()!=null && game.getStatus()!=null) {
            result = STATUS_SORT_MAP.get(this.getStatus()).compareTo(STATUS_SORT_MAP.get(game.getStatus()));
        }
        if(EQUAL==result) {
            Date d1 = (this.getGame().getLastMoveDate()!=null) ? this.getGame().getLastMoveDate() : this.getGame().getStartDate();
            Date d2 = (game.getGame().getLastMoveDate()!=null) ? game.getGame().getLastMoveDate() : game.getGame().getStartDate();
            result = d1.compareTo(d2);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((game == null) ? 0 : game.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameInfo other = (GameInfo) obj;
        if (game == null) {
            if (other.game != null)
                return false;
        } else if (!game.equals(other.game))
            return false;
        return true;
    }
    
    
}
