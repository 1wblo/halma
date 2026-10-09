package ca.uqam.info.solanum.students.halma.controller;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.halmaboard.api.Board;
import org.halmaboard.api.Field;
import org.halmaboard.api.Model;
import org.halmaboard.api.ModelAccessConsistencyException;
import org.halmaboard.api.ModelReadOnly;
import org.halmaboard.api.Move;

/**
 * Controleur d'une partie de Halma : coups possibles, execution d'un coup, tours et victoire.
 */
public class HalmaController {
  private static final Comparator<Field> FIELD_ORDER =
      Comparator.comparingInt(Field::y).thenComparingInt(Field::x);
  private static final Comparator<Move> MOVE_ORDER =
      Comparator.comparing(Move::origin, FIELD_ORDER).thenComparing(Move::target, FIELD_ORDER);

  private final Model model;

  /**
   * Cree un controleur pour le modele donne.
   *
   * @param model modele de la partie a piloter.
   */
  public HalmaController(Model model) {
    this.model = model;
  }

  /**
   * Retourne le modele de la partie en lecture seule.
   *
   * @return le modele pilote par ce controleur.
   */
  public ModelReadOnly getModel() {
    return model;
  }

  /**
   * Calcule tous les coups permis pour le joueur courant. Un pion peut avancer sur une case
   * voisine libre, ou sauter par-dessus un pion voisin vers la case libre situee juste derriere ;
   * les sauts peuvent s'enchainer.
   *
   * @return les coups permis, tries par case de depart puis par case d'arrivee.
   */
  public List<Move> getPlayerMoves() {
    List<Move> moves = new ArrayList<>();
    if (isGameOver()) {
      return moves;
    }
    Board board = model.getBoard();
    for (Field origin : model.getPlayerFields(model.getCurrentPlayer())) {
      for (Field neighbour : board.getNeighbours(origin)) {
        if (model.isClear(neighbour)) {
          moves.add(new Move(origin, neighbour, false));
        }
      }
      for (Field target : jumpTargets(origin)) {
        moves.add(new Move(origin, target, true));
      }
    }
    moves.sort(MOVE_ORDER);
    return moves;
  }

  /**
   * Cherche toutes les cases atteignables depuis une case par une suite de sauts.
   *
   * @param origin case de depart du pion.
   * @return les cases d'arrivee possibles, sans la case de depart.
   */
  private Set<Field> jumpTargets(Field origin) {
    Board board = model.getBoard();
    Set<Field> reached = new HashSet<>();
    Deque<Field> toVisit = new ArrayDeque<>();
    toVisit.add(origin);
    while (!toVisit.isEmpty()) {
      Field from = toVisit.poll();
      for (Field over : board.getNeighbours(from)) {
        if (model.isClear(over)) {
          continue;
        }
        Field landing = board.getExtendedNeighbour(from, over);
        if (landing != null && !landing.equals(origin) && model.isClear(landing)
            && reached.add(landing)) {
          toVisit.add(landing);
        }
      }
    }
    return reached;
  }

  /**
   * Execute un coup du joueur courant, puis passe au joueur suivant.
   *
   * @param move coup a jouer, parmi ceux de {@link #getPlayerMoves()}.
   * @throws IllegalArgumentException        si le coup n'est pas permis.
   * @throws ModelAccessConsistencyException si le modele refuse le deplacement.
   */
  public void performMove(Move move) throws ModelAccessConsistencyException {
    if (!getPlayerMoves().contains(move)) {
      throw new IllegalArgumentException("Coup non permis : " + move);
    }
    int player = model.getCurrentPlayer();
    model.clearField(move.origin());
    model.occupyField(player, move.target());
    if (!isGameOver()) {
      passTurn();
    }
  }

  /**
   * Passe au joueur suivant sans jouer, par exemple quand le joueur courant est bloque.
   *
   * @throws ModelAccessConsistencyException si le modele refuse le changement de joueur.
   */
  public void passTurn() throws ModelAccessConsistencyException {
    int nbPlayers = model.getPlayerNames().length;
    model.setCurrentPlayer((model.getCurrentPlayer() + 1) % nbPlayers);
  }

  /**
   * Indique si un joueur a gagne.
   *
   * @return true si la partie est terminee.
   */
  public boolean isGameOver() {
    return getWinner() >= 0;
  }

  /**
   * Retourne le gagnant : le joueur dont tous les pions occupent sa zone cible.
   *
   * @return l'indice du gagnant, ou -1 si personne n'a encore gagne.
   */
  public int getWinner() {
    for (int player = 0; player < model.getPlayerNames().length; player++) {
      Set<Field> targets = model.getBoard().getTargetFieldsForPlayer(player);
      if (!targets.isEmpty() && model.getPlayerFields(player).equals(targets)) {
        return player;
      }
    }
    return -1;
  }
}
