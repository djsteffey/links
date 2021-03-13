package halfbyte.game.links;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import halfbyte.game.links.board.Board;

public class GameState implements Serializable {
    // const
    private static final long serialVersionUID = 1L;
    private static final int NUM_LEVELS = 1000;

    // enum
    public enum EStatus{
        COMPLETE,
        INCOMPLETE
    }

    // variables
    private Map<Board.EType, List<EStatus>> m_status;
    private long m_coins;

    // methods
    public GameState(){
        this.m_status = new EnumMap<Board.EType, List<EStatus>>(Board.EType.class);
        this.m_status.put(Board.EType.SIZE_4x4, new ArrayList<>());
        this.m_status.put(Board.EType.SIZE_5x5, new ArrayList<>());
        this.m_status.put(Board.EType.SIZE_6x6, new ArrayList<>());
        for (int i = 0; i < NUM_LEVELS; ++i) {
            this.m_status.get(Board.EType.SIZE_4x4).add(EStatus.INCOMPLETE);
            this.m_status.get(Board.EType.SIZE_5x5).add(EStatus.INCOMPLETE);
            this.m_status.get(Board.EType.SIZE_6x6).add(EStatus.INCOMPLETE);
        }
        this.m_coins = 5;
    }

    public int getNumComplete(Board.EType type){
        int count = 0;
        for (int i = 0; i < NUM_LEVELS; ++i){
            if (this.m_status.get(type).get(i) == EStatus.COMPLETE){
                count += 1;
            }
        }
        return count;
    }

    public int getFirstIncomplete(Board.EType type){
        for (int i = 0; i < NUM_LEVELS; ++i){
            if (this.m_status.get(type).get(i) == EStatus.INCOMPLETE){
                return i;
            }
        }
        return -1;
    }

    public float getPercentComplete(Board.EType type){
        int count = this.getNumComplete(type);
        return (float)(count) / NUM_LEVELS;
    }

    public void setStatus(Board.EType type, int level, EStatus status){
        this.m_status.get(type).set(level, status);
    }

    public long getCoins(){
        return this.m_coins;
    }

    public void addCoins(long amount){
        this.m_coins += amount;
    }

    public void removeCoins(long amount){
        this.m_coins -= amount;
    }
}
