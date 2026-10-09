package org.halmaboard.view;

import org.halmaboard.api.ModelReadOnly;

/**
 * Produit un resume textuel de l'etat d'une partie pour la console.
 */
public class SimpleConsoleVisualizer {
  private static final String RESET = "\u001B[0m";
  private static final String[] COLOURS = {
      "\u001B[31m", "\u001B[32m", "\u001B[34m", "\u001B[33m", "\u001B[35m", "\u001B[36m"
  };

  private final boolean useColours;

  /**
   * Cree un visualiseur.
   *
   * @param useColours true pour colorer le nom de chaque joueur avec des codes ANSI.
   */
  public SimpleConsoleVisualizer(boolean useColours) {
    this.useColours = useColours;
  }

  /**
   * Decrit le modele : joueurs, joueur courant et nombre de cases occupees par joueur.
   *
   * @param model modele a decrire.
   * @return le resume textuel, une ligne par joueur.
   */
  public String stringifyModel(ModelReadOnly model) {
    String[] names = model.getPlayerNames();
    int current = model.getCurrentPlayer();
    StringBuilder sb = new StringBuilder();
    sb.append("Halma - ").append(names.length).append(" joueurs, ")
        .append(model.getBoard().getAllFields().size()).append(" cases\n");
    for (int i = 0; i < names.length; i++) {
      sb.append(i == current ? "> " : "  ")
          .append(colourise(i, names[i]))
          .append(" : ")
          .append(model.getPlayerFields(i).size())
          .append(" cases occupees\n");
    }
    if (current >= 0 && current < names.length) {
      sb.append("Joueur courant : ").append(colourise(current, names[current])).append('\n');
    }
    return sb.toString();
  }

  private String colourise(int playerIndex, String name) {
    if (!useColours) {
      return name;
    }
    return COLOURS[playerIndex % COLOURS.length] + name + RESET;
  }
}
