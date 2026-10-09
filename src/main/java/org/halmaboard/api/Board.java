package org.halmaboard.api;

import java.util.Set;

/**
 * Plateau de jeu : positions, cases de depart et cases cibles par joueur.
 */
public interface Board {
  /**
   * Retourne toutes les cases du plateau.
   *
   * @return l'ensemble des cases du plateau.
   */
  Set<Field> getAllFields();

  /**
   * Retourne les cases de depart de tous les joueurs.
   *
   * @return l'ensemble des cases de depart.
   */
  Set<Field> getAllHomeFields();

  /**
   * Retourne les cases de depart d'un joueur.
   *
   * @param playerIndex indice du joueur.
   * @return les cases de depart de ce joueur.
   */
  Set<Field> getHomeFieldsForPlayer(int playerIndex);

  /**
   * Retourne les cases cibles d'un joueur.
   *
   * @param playerIndex indice du joueur.
   * @return les cases que ce joueur doit atteindre.
   */
  Set<Field> getTargetFieldsForPlayer(int playerIndex);

  /**
   * Retourne les cases voisines d'une case.
   *
   * @param field case dont on cherche les voisines.
   * @return les cases directement voisines.
   * @throws FieldException si la case n'existe pas sur le plateau.
   */
  Set<Field> getNeighbours(Field field) throws FieldException;

  /**
   * Retourne la case situee au-dela d'une voisine, dans le prolongement de l'origine.
   *
   * @param origin    case de depart.
   * @param neighbour case voisine de l'origine.
   * @return la case situee derriere la voisine.
   * @throws FieldException si une des cases est invalide.
   */
  Field getExtendedNeighbour(Field origin, Field neighbour) throws FieldException;
}
