package org.halmaboard.api;

/**
 * Acces en lecture-ecriture a l'etat du modele de jeu.
 */
public interface Model extends ModelReadOnly {
  /**
   * Definit le joueur courant.
   *
   * @param playerIndex indice du joueur.
   * @throws ModelAccessConsistencyException si l'indice est invalide.
   */
  void setCurrentPlayer(int playerIndex) throws ModelAccessConsistencyException;

  /**
   * Fait occuper une case par un joueur.
   *
   * @param playerIndex indice du joueur.
   * @param field       case a occuper.
   * @throws ModelAccessConsistencyException si l'operation est incoherente.
   */
  void occupyField(int playerIndex, Field field) throws ModelAccessConsistencyException;

  /**
   * Libere une case occupee.
   *
   * @param field case a liberer.
   * @throws ModelAccessConsistencyException si la case n'existe pas ou n'est pas occupee.
   */
  void clearField(Field field) throws ModelAccessConsistencyException;
}
