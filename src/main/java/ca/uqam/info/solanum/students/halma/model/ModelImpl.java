package ca.uqam.info.solanum.students.halma.model;
import ca.uqam.info.solanum.inf2050.f24halma.model.*;

import java.util.HashSet;
import java.util.Set;


import java.util.*;

/**
 * blalala
 */
public class ModelImpl implements Model {
    private String[] playersNames;
    private BoardImpl board;
    private int nbPlayers;
    private int baseSize;
    private int currentPlayer;

    /**
     * blallala
     * @param baseSize blala
     * @param playersNames blala
     * @param nbPlayers blala
     */
   public ModelImpl(int baseSize, String[] playersNames, int nbPlayers) {
       this.playersNames = playersNames;
       this.board = new BoardImpl(baseSize, nbPlayers);
       this.nbPlayers = playersNames.length;
       this.baseSize = baseSize;
   }

    @Override
    public Board getBoard() {
        return this.board;
    }

    @Override
    public String[] getPlayerNames() {
        return this.playersNames;
    }

    @Override
    public Set<Field> getPlayerFields(int playerIndex) {
        return board.getHomeFieldsForPlayer(playerIndex);
    }

    @Override
    public int getCurrentPlayer(){
       int currentPlayer = 0;
        return currentPlayer;
    }
    @Override
    public boolean isClear(Field field) throws FieldException {
        boolean isClear = false;
        boolean isPresence = false ;
        try {
            for (Field neutral : board.getAllFields()) {
                if (neutral.equals(field)) {
                    isPresence = true;
                }
                for (Field base : board.getAllHomeFields()) {
                    if (neutral.equals(field) && !(base.equals(field))) {
                        isClear = true;
                        break;
                    }
                }
            }
            if (!isPresence) {
                throw new FieldException("no field found");
            }

        }catch (FieldException e) {
            System.out.println(e.getMessage());
        }
        return isClear;
    }

    @Override
    public void occupyField(int i, Field field) throws FieldException {
    }

    @Override
    public void clearField(Field field) throws FieldException {
    }

    @Override
    public void setCurrentPlayer(int i) {
        this.currentPlayer = i;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }
}