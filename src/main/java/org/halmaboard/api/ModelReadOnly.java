package org.halmaboard.api;

import java.util.Set;

/**
 * Acces en lecture seule a l'etat du modele de jeu.
 */
public interface ModelReadOnly {
  /**
   * Retourne le plateau de jeu.
   *
   * @return le plateau.
   */
  Board getBoard();

  /**
   * Retourne les noms des joueurs.
   *
   * @return les noms, dans l'ordre des indices de joueur.
   */
  String[] getPlayerNames();

  /**
   * Retourne les cases occupees par un joueur.
   *
   * @param playerIndex indice du joueur.
   * @return les cases occupees par ce joueur.
   */
  Set<Field> getPlayerFields(int playerIndex);

  /**
   * Retourne l'indice du joueur courant.
   *
   * @return l'indice du joueur dont c'est le tour.
   */
  int getCurrentPlayer();

  /**
   * Indique si une case est libre.
   *
   * @param field case a verifier.
   * @return true si aucun joueur n'occupe la case.
   * @throws FieldException si la case n'existe pas sur le plateau.
   */
  boolean isClear(Field field) throws FieldException;
}
