package org.halmaboard.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.halmaboard.api.Field;
import org.halmaboard.api.ModelReadOnly;

/**
 * Produit une vue textuelle d'une partie pour la console : resume des joueurs et dessin du plateau.
 */
public class SimpleConsoleVisualizer {
  private static final char FREE_FIELD = '.';
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
   * Decrit le modele : joueurs, joueur courant, nombre de cases occupees par joueur, puis
   * dessin du plateau. Chaque pion est represente par l'indice de son joueur et chaque case
   * libre par un point.
   *
   * @param model modele a decrire.
   * @return le resume textuel suivi du dessin du plateau.
   */
  public String stringifyModel(ModelReadOnly model) {
    return stringifyModel(model, false);
  }

  /**
   * Decrit le modele comme {@link #stringifyModel(ModelReadOnly)}, avec en option les
   * coordonnees des cases : x en haut du plateau, y a gauche.
   *
   * @param model           modele a decrire.
   * @param showCoordinates true pour graduer le plateau avec les coordonnees x et y.
   * @return le resume textuel suivi du dessin du plateau.
   */
  public String stringifyModel(ModelReadOnly model, boolean showCoordinates) {
    String[] names = model.getPlayerNames();
    int current = model.getCurrentPlayer();
    StringBuilder sb = new StringBuilder();
    sb.append("Halma - ").append(names.length).append(" joueurs, ")
        .append(model.getBoard().getAllFields().size()).append(" cases\n");
    for (int i = 0; i < names.length; i++) {
      sb.append(i == current ? "> " : "  ")
          .append('[').append(i).append("] ")
          .append(colourise(i, names[i]))
          .append(" : ")
          .append(model.getPlayerFields(i).size())
          .append(" cases occupees\n");
    }
    if (current >= 0 && current < names.length) {
      sb.append("Joueur courant : ").append(colourise(current, names[current])).append('\n');
    }
    sb.append('\n').append(stringifyBoard(model, showCoordinates));
    return sb.toString();
  }

  /**
   * Dessine le plateau ligne par ligne a partir des coordonnees des cases.
   *
   * @param model           modele dont on dessine le plateau.
   * @param showCoordinates true pour ajouter les coordonnees x (en haut) et y (a gauche).
   * @return le dessin du plateau, une ligne de texte par rangee de cases.
   */
  private String stringifyBoard(ModelReadOnly model, boolean showCoordinates) {
    Set<Field> fields = model.getBoard().getAllFields();
    if (fields.isEmpty()) {
      return "";
    }
    Map<Field, Integer> owners = new HashMap<>();
    for (int i = 0; i < model.getPlayerNames().length; i++) {
      for (Field field : model.getPlayerFields(i)) {
        owners.put(field, i);
      }
    }
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (Field field : fields) {
      minX = Math.min(minX, field.x());
      maxX = Math.max(maxX, field.x());
      minY = Math.min(minY, field.y());
      maxY = Math.max(maxY, field.y());
    }
    StringBuilder sb = new StringBuilder();
    if (showCoordinates) {
      StringBuilder header = new StringBuilder("y\\x  ");
      for (int x = minX; x <= maxX; x++) {
        header.append(String.format("%-3d", x));
      }
      sb.append(header.toString().stripTrailing()).append('\n');
    }
    for (int y = minY; y <= maxY; y++) {
      StringBuilder line = new StringBuilder();
      if (showCoordinates) {
        line.append(String.format("%3d   ", y));
      }
      for (int x = minX; x <= maxX; x++) {
        line.append(symbolFor(new Field(x, y), fields, owners)).append("  ");
      }
      sb.append(line.toString().stripTrailing()).append('\n');
    }
    return sb.toString();
  }

  private String symbolFor(Field field, Set<Field> fields, Map<Field, Integer> owners) {
    if (!fields.contains(field)) {
      return " ";
    }
    Integer owner = owners.get(field);
    if (owner == null) {
      return String.valueOf(FREE_FIELD);
    }
    return colourise(owner, String.valueOf(owner));
  }

  private String colourise(int playerIndex, String name) {
    if (!useColours) {
      return name;
    }
    return COLOURS[playerIndex % COLOURS.length] + name + RESET;
  }
}
