package ca.uqam.info.solanum.students.halma.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.halmaboard.api.Board;
import org.halmaboard.api.Field;
import org.halmaboard.api.FieldException;
import org.halmaboard.api.Model;
import org.halmaboard.api.ModelAccessConsistencyException;

/**
 * Implémentation du modèle de jeu Halma.
 */
public class ModelImpl implements Model {
  private String[] playersNames;
  private BoardImpl board;
  private int nbPlayers;
  private int baseSize;
  private int currentPlayer;
  private Set<Field>[] occupiedFields;

  /**
   * Constructeur de l'implémentation du modèle Halma.
   *
   * @param baseSize     La taille de base du plateau.
   * @param playersNames Les noms des joueurs.
   * @param nbPlayers    Le nombre de joueurs.
   */
  public ModelImpl(int baseSize, String[] playersNames, int nbPlayers) {
    this.playersNames = playersNames;
    this.board = new BoardImpl(baseSize, nbPlayers);
    this.nbPlayers = playersNames.length;
    this.baseSize = baseSize;
    // Initialisation du tableau des champs occupés
    this.occupiedFields = new HashSet[nbPlayers];
    for (int i = 0; i < nbPlayers; i++) {
      occupiedFields[i] = new HashSet<>();
      // Initialisation: chaque joueur occupe ses champs de départ
      Set<Field> homeFields = board.getHomeFieldsForPlayer(i);
      if (homeFields != null) {
        occupiedFields[i].addAll(homeFields);
      }
    }
  }

  /**
   * Récupère le plateau de jeu.
   *
   * @return Le plateau de jeu.
   */
  @Override
  public Board getBoard() {
    return this.board;
  }

  /**
   * Récupère les noms des joueurs.
   *
   * @return Les noms des joueurs.
   */
  @Override
  public String[] getPlayerNames() {
    return Arrays.copyOf(this.playersNames, this.playersNames.length);
  }

  /**
   * Récupère les champs occupés par un joueur spécifique.
   *
   * @param playerIndex L'indice du joueur.
   * @return Un ensemble de champs occupés par le joueur.
   */
  @Override
  public Set<Field> getPlayerFields(int playerIndex) {
    //return board.getHomeFieldsForPlayer(playerIndex);
    //return Set.copyOf(occupiedFields[playerIndex]);
    return Collections.unmodifiableSet(occupiedFields[playerIndex]);
  }

  /**
   * Récupère l'indice du joueur actuel.
   *
   * @return L'indice du joueur actuel.
   */
  @Override
  public int getCurrentPlayer() {
    currentPlayer = 1;
    return currentPlayer;
  }

  /**
   * Définit le joueur actuel.
   *
   * @param playerIndex L'indice du joueur.
   */
  @Override
  public void setCurrentPlayer(int playerIndex) throws ModelAccessConsistencyException {
    // Vérifier si l'index du joueur est valide
    if (playerIndex < 0 || playerIndex >= nbPlayers) {
      throw new ModelAccessConsistencyException("L'index du joueur est invalide : " + playerIndex);
    }
    this.currentPlayer = playerIndex;
  }

  /**
   * Vérifie si un champ est clair.
   *
   * @param field Le champ à vérifier.
   * @return true si le champ est clair, false sinon.
   * @throws FieldException Si le champ n'est pas trouvé.
   */
  @Override
  public boolean isClear(Field field) throws FieldException {
    // Vérifier si le champ existe sur le plateau
    if (!board.getAllFields().contains(field)) {
      throw new FieldException("No field found");
    }
    // Vérifier si le champ est occupé par un joueur
    for (int i = 0; i < nbPlayers; i++) {
      if (occupiedFields[i].contains(field)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Occupe un champ spécifique.
   *
   * @param playerIndex L'indice du joueur.
   * @param field       Le champ à occuper.
   * @throws ModelAccessConsistencyException Si le champ ne peut pas être occupé.
   */
  @Override
  public void occupyField(int playerIndex, Field field) throws ModelAccessConsistencyException {
    // Vérifier que l'index du joueur est valide
    if (playerIndex < 0 || playerIndex >= nbPlayers) {
      throw new ModelAccessConsistencyException("Index de joueur invalide: " + playerIndex);
    }
    // Vérifier si le champ existe sur le plateau (sans parcourir tous les champs)
    if (!board.getAllFields().contains(field)) {
      throw new ModelAccessConsistencyException("Le champ spécifié n'existe pas sur le plateau");
    }
    // Vérifier si le champ est déjà occupé
    for (int i = 0; i < nbPlayers; i++) {
      if (occupiedFields[i].contains(field)) {
        throw new FieldException("Le champ est déjà occupé par le joueur " + i);
      }
    }
    // Si tout est valide, occuper le champ
    occupiedFields[playerIndex].add(field);
  }

  /**
   * Vide un champ spécifique.
   *
   * @param field Le champ à vider.
   * @throws ModelAccessConsistencyException Si le champ ne peut pas être vidé.
   */
  @Override
  public void clearField(Field field) throws ModelAccessConsistencyException {
    boolean fieldWasOccupied = false;
    // Vérifier si le champ existe sur le plateau
    if (!board.getAllFields().contains(field)) {
      throw new ModelAccessConsistencyException("Le champ spécifié n'existe pas sur le plateau");
    }
    // Chercher quel joueur occupe le champ et le libérer
    for (int i = 0; i < nbPlayers; i++) {
      if (occupiedFields[i].contains(field)) {
        occupiedFields[i].remove(field);
        fieldWasOccupied = true;
      }
    }
    // Si aucun joueur n'occupe ce champ
    if (!fieldWasOccupied) {
      throw new ModelAccessConsistencyException("Le champ spécifié n'est pas actuellement occupé");
    }
  }

  /**
   * Vérifie l'égalité entre deux objets.
   *
   * @param obj L'objet à comparer.
   * @return true si les objets sont égaux, false sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ModelImpl other = (ModelImpl) obj;
    return baseSize == other.baseSize
        && nbPlayers == other.nbPlayers
        && currentPlayer == other.currentPlayer
        && Arrays.equals(playersNames, other.playersNames)
        && Arrays.deepEquals(occupiedFields, other.occupiedFields);
  }
}


