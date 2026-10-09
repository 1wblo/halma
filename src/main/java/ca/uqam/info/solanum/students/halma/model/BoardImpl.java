package ca.uqam.info.solanum.students.halma.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.halmaboard.api.Board;
import org.halmaboard.api.Field;
import org.halmaboard.api.FieldException;
import org.halmaboard.api.ModelInitializationException;

/**
 * Classe représentant le plateau du jeu Halma.
 */
public class BoardImpl implements Board {
  private final Set<Field> allFields = new HashSet<>();
  private final Set<Field> allHomeFields = new HashSet<>();
  private final Set<Field> homeFieldsForPlayer0 = new HashSet<>();
  private final Set<Field> homeFieldsForPlayer1 = new HashSet<>();
  private final Set<Field> homeFieldsForPlayer2 = new HashSet<>();
  private final Set<Field> homeFieldsForPlayer3 = new HashSet<>();
  private final Set<Field> homeFieldsForPlayer4 = new HashSet<>();
  private final Set<Field> homeFieldsForPlayer5 = new HashSet<>();
  private final Set<Field> targetFieldsForPlayer0 = new HashSet<>();
  private final Set<Field> targetFieldsForPlayer1 = new HashSet<>();
  private final Set<Field> targetFieldsForPlayer2 = new HashSet<>();
  private final Set<Field> targetFieldsForPlayer3 = new HashSet<>();
  private final Set<Field> targetFieldsForPlayer4 = new HashSet<>();
  private final Set<Field> targetFieldsForPlayer5 = new HashSet<>();
  private int baseSize;

  /**
   * Initialise le plateau de jeu avec une taille de base et un nombre de joueurs.
   *
   * @param baseSize  La taille de base du plateau.
   * @param nbPlayers Le nombre de joueurs (doit être 3 ou 6).
   * @throws IllegalArgumentException     Si la taille de base est inférieure ou égale à zéro.
   * @throws ModelInitializationException Si le nombre de joueurs n'est pas 3 ou 6.
   */
  public BoardImpl(int baseSize, int nbPlayers) {
    if (baseSize <= 0) {
      throw new IllegalArgumentException(
          "La base doit être supérieure à zéro et le nombre de joueurs doit être égal à 3 "
              + "ou 6.");
    }
    if ((nbPlayers != 3) && (nbPlayers != 6)) {
      throw new ModelInitializationException("Nombre de joueurs invalide");
    }
    this.baseSize = baseSize;
    initializeBoard();
  }

  /**
   * Initialise le plateau de jeu.
   */
  private void initializeBoard() {
    // Pour chaque segment de l'étoile (6 segments au total)
    for (int segment = 0; segment < 6; segment++) {
      int startX;
      int startY;
      int directionX;
      int directionY;
      // Calcul des positions de départ et des directions selon le segment
      switch (segment) {
        case 0: // Middle Left
          startX = baseSize - 1;
          startY = baseSize * 2;
          directionX = -1;
          directionY = 1;
          break;
        case 1: // Top Right
          startX = baseSize * 3;
          startY = 0;
          directionX = -1;
          directionY = -1;
          break;
        case 2: // Bottom Right
          startX = baseSize * 3;
          startY = baseSize * 4 + 1;
          directionX = -1;
          directionY = 1;
          break;
        case 3: // Middle Right
          startX = baseSize * 3 + 1;
          startY = baseSize * 2;
          directionX = 1;
          directionY = 1;
          break;
        case 4: // Top Left
          startX = baseSize;
          startY = 0;
          directionX = 1;
          directionY = -1;
          break;
        case 5: // Bottom Left
          startX = baseSize;
          startY = baseSize * 4 + 1;
          directionX = 1;
          directionY = 1;
          break;
        default:
          continue;
      }
      // Créer un segment de triangle
      int endTriangle = 0;
      for (int x = startX; (directionX == 1) ? x < startX + baseSize : x > startX - baseSize
          ; x += directionX) {
        endTriangle++;
        int minY = (directionY == 1) ? startY + endTriangle : endTriangle - 1;
        int maxX = (directionY == 1) ? startY + baseSize * 2 - endTriangle : baseSize * 2
            - endTriangle;
        for (int y = minY; y <= maxX; y += 2) {
          Field field = new Field(x, y);
          allFields.add(field);
          allHomeFields.add(field);
          // Ajouter aux champs de départ et d'arrivée selon le segment
          if (segment < 6) { // Exclure l'hexagone central
            allHomeFields.add(field);
            // Le segment est la zone de départ d'un joueur et la zone d'arrivée du joueur
            // placé dans la branche opposée (0 et 3, 1 et 5, 2 et 4)
            switch (segment) {
              case 0:
                homeFieldsForPlayer0.add(field);
                targetFieldsForPlayer3.add(field);
                break;
              case 1:
                homeFieldsForPlayer1.add(field);
                targetFieldsForPlayer5.add(field);
                break;
              case 2:
                homeFieldsForPlayer2.add(field);
                targetFieldsForPlayer4.add(field);
                break;
              case 3:
                homeFieldsForPlayer3.add(field);
                targetFieldsForPlayer0.add(field);
                break;
              case 4:
                homeFieldsForPlayer4.add(field);
                targetFieldsForPlayer2.add(field);
                break;
              case 5:
                homeFieldsForPlayer5.add(field);
                targetFieldsForPlayer1.add(field);
                break;
              default:
            }
          }
        }
      }
    }
    // Ajouter l'hexagone central
    addCenterHexagon(baseSize);
  }

  /**
   * Ajoute l'hexagone central au plateau.
   *
   * @param baseSize La taille de base du plateau.
   */
  private void addCenterHexagon(int baseSize) {
    int middleOfHexagon = baseSize * 2;
    for (int x = middleOfHexagon; x >= baseSize; x--) {
      int offset = middleOfHexagon - x;
      for (int y = baseSize + offset; y < baseSize * 5 + 2 - offset; y += 2) {
        allFields.add(new Field(x, y));
      }
    }
    for (int x = middleOfHexagon + 1; x <= baseSize * 3; x++) {
      int offset = x - middleOfHexagon;
      for (int y = baseSize + offset; y < baseSize * 5 + 2 - offset; y += 2) {
        allFields.add(new Field(x, y));
      }
    }
  }

  @Override
  public Set<Field> getAllFields() {
    return Collections.unmodifiableSet(allFields);
  }

  @Override
  public Set<Field> getHomeFieldsForPlayer(int playerIndex) {
    switch (playerIndex) {
      case 0:
        return Collections.unmodifiableSet(homeFieldsForPlayer0);
      case 1:
        return Collections.unmodifiableSet(homeFieldsForPlayer1);
      case 2:
        return Collections.unmodifiableSet(homeFieldsForPlayer2);
      case 3:
        return Collections.unmodifiableSet(homeFieldsForPlayer3);
      case 4:
        return Collections.unmodifiableSet(homeFieldsForPlayer4);
      case 5:
        return Collections.unmodifiableSet(homeFieldsForPlayer5);
      default:
        return Collections.emptySet();
    }
  }

  @Override
  public Set<Field> getAllHomeFields() {
    return Collections.unmodifiableSet(allHomeFields);
  }

  @Override
  public Field getExtendedNeighbour(Field origin, Field neighbour) throws FieldException {
    // Vérifier que les champs existent sur le plateau
    if (!allFields.contains(origin)) {
      throw new FieldException("Le champ d'origine n'existe pas sur le plateau");
    }
    if (!allFields.contains(neighbour)) {
      throw new FieldException("Le champ voisin n'existe pas sur le plateau");
    }

    // Vérifier que le voisin est bien un voisin direct
    Set<Field> neighbours = getNeighbours(origin);
    if (!neighbours.contains(neighbour)) {
      throw new FieldException("Le champ spécifié n'est pas un voisin direct");
    }

    // Calculer la direction entre l'origine et le voisin
    int dirX = neighbour.x() - origin.x();
    int dirY = neighbour.y() - origin.y();

    // Calculer les coordonnées du voisin étendu en suivant la même direction
    int extendedX = neighbour.x() + dirX;
    int extendedY = neighbour.y() + dirY;
    Field extendedNeighbour = new Field(extendedX, extendedY);

    // Vérifier si le voisin étendu existe sur le plateau
    if (allFields.contains(extendedNeighbour)) {
      return extendedNeighbour;
    } else {
      return null; // Le voisin étendu n'existe pas sur le plateau
    }
  }

  @Override
  public Set<Field> getNeighbours(Field field) throws FieldException {
    // Vérifier que le champ existe sur le plateau
    if (!allFields.contains(field)) {
      throw new FieldException("Le champ spécifié n'existe pas sur le plateau");
    }
    Set<Field> neighbours = new HashSet<>();
    int x = field.x(); // Utilisation de x() au lieu de getX()
    int y = field.y(); // Utilisation de y() au lieu de getY()
    // Définition des 6 directions possibles sur une grille hexagonale
    int[][] directions = {
        {-1, -1}, {1, 1},  // Diagonale gauche-haut et droite-bas
        {-1, 1}, {1, -1},  // Diagonale gauche-bas et droite-haut
        {0, -2}, {0, 2}    // Haut et bas
    };
    // Vérifier chaque direction et ajouter le voisin s'il est valide
    for (int[] dir : directions) {
      Field neighbour = new Field(x + dir[0], y + dir[1]);
      if (allFields.contains(neighbour)) { // Vérifier si le voisin existe sur le plateau
        neighbours.add(neighbour);
      }
    }
    return Set.copyOf(neighbours); // Retourner un set immuable
  }

  @Override
  public Set<Field> getTargetFieldsForPlayer(int playerIndex) {
    switch (playerIndex) {
      case 0:
        return Collections.unmodifiableSet(targetFieldsForPlayer0);
      case 1:
        return Collections.unmodifiableSet(targetFieldsForPlayer1);
      case 2:
        return Collections.unmodifiableSet(targetFieldsForPlayer2);
      case 3:
        return Collections.unmodifiableSet(targetFieldsForPlayer3);
      case 4:
        return Collections.unmodifiableSet(targetFieldsForPlayer4);
      case 5:
        return Collections.unmodifiableSet(targetFieldsForPlayer5);
      default:
        return Collections.emptySet();
    }
  }
}
