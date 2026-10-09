package org.halmaboard.api;

/**
 * Cree un modele de jeu Halma pour les parametres donnes.
 */
public interface ModelFactory {
  /**
   * Cree un modele dans son etat initial.
   *
   * @param baseSize taille de base du plateau.
   * @param players  noms des joueurs.
   * @return le modele initialise.
   */
  Model createModel(int baseSize, String[] players);
}
