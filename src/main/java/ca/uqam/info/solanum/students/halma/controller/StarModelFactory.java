package ca.uqam.info.solanum.students.halma.controller;

import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.students.halma.model.ModelImpl;

/**
 * Factory pour la création du modèle de jeu Halma avec une configuration d'étoile.
 */
public class StarModelFactory implements ModelFactory {
  /**
   * Le constructeur par défaut de StarModelFactory.
   */
  public StarModelFactory() {
  }

  /**
   * Crée un modèle de jeu Halma avec une configuration d'étoile.
   *
   * @param baseSize La taille de base du plateau de jeu.
   * @param players  Tableau des noms des joueurs.
   * @return Un objet ModelImpl représentant le modèle de jeu.
   */
  @Override
  public Model createModel(int baseSize, String[] players) {
    int nbPlayers = players.length;
    return new ModelImpl(baseSize, players, nbPlayers);
  }
}