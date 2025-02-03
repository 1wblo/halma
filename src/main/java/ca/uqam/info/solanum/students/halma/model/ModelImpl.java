package ca.uqam.info.solanum.students.halma.model;
import ca.uqam.info.solanum.inf2050.f24halma.model.*;

import java.util.HashSet;
import java.util.Set;


import java.util.*;

public class ModelImpl implements Model {
    private final Board board;
    private final String[] playerNames;
    private final int currentPlayer;
    // Stocke les champs occupés
    private final Map<Field, Integer> occupiedFields;

    public ModelImpl(Board board, String[] players) {
        this.board = board;
        this.playerNames = Arrays.copyOf(players, players.length);
        this.currentPlayer = 0;
        this.occupiedFields = new HashMap<>();


        initializePlayerPieces();
    }

    /**
     *  Initialise les pièces des joueurs en les plaçant dans leur zone de départ.
     *  Pour chaque joueur, récupère les champs de départ et les assigne à ce joueur.
     */
    private void initializePlayerPieces() {
        // Place chque joueur dans leur zone de départ
        for (int i = 0; i < playerNames.length; i++) {
            Set<Field> homeFields = board.getHomeFieldsForPlayer(i);
            for (Field field : homeFields) {
                occupiedFields.put(field, i);
            }
        }
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public String[] getPlayerNames() {
        //retourne une copie
        return Arrays.copyOf(playerNames, playerNames.length);
    }

    @Override
    public Set<Field> getPlayerFields(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= playerNames.length) {
            throw new IllegalArgumentException("Invalid player index: " + playerIndex);
        }

        //Collecte tous les champs occupés par le joueur indiqué

        Set<Field> fields = new HashSet<>();
        for (Map.Entry<Field, Integer> entry : occupiedFields.entrySet()) {
            if (entry.getValue() == playerIndex) {
                fields.add(entry.getKey());
            }
        }

        return Collections.unmodifiableSet(fields);
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public boolean isClear(Field field) throws FieldException {

        if (!board.getAllFields().contains(field)) {
            throw new FieldException("Invalid field position");
        }

        return !occupiedFields.containsKey(field);
    }


    @Override
    public void occupyField(int playerIndex, Field field) throws FieldException {
        throw new RuntimeException("");
    }

    @Override
    public void clearField(Field field) throws FieldException {
        throw new RuntimeException("");
    }

    @Override
    public void setCurrentPlayer(int playerIndex) {
        throw new RuntimeException("");
    }
}
