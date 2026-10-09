package ca.uqam.info.solanum.students.halma.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.halmaboard.api.Field;
import org.halmaboard.api.Model;
import org.halmaboard.api.ModelAccessConsistencyException;
import org.halmaboard.api.Move;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests des regles de jeu portees par HalmaController.
 */
public class HalmaControllerTest {

  private static final String[] PLAYERS = {"Alice", "Bob", "Charlie"};

  private Model model;
  private HalmaController controller;

  @Before
  public void setUp() {
    model = new StarModelFactory().createModel(3, PLAYERS);
    controller = new HalmaController(model);
  }

  @Test
  public void testInitialMovesBelongToCurrentPlayer() {
    List<Move> moves = controller.getPlayerMoves();
    assertFalse(moves.isEmpty());
    for (Move move : moves) {
      assertTrue(model.getPlayerFields(0).contains(move.origin()));
    }
  }

  @Test
  public void testEveryMoveLandsOnFreeField() {
    for (Move move : controller.getPlayerMoves()) {
      assertTrue(model.isClear(move.target()));
    }
  }

  @Test
  public void testSimpleMovesGoToNeighbourAndJumpsDoNot() {
    for (Move move : controller.getPlayerMoves()) {
      boolean adjacent = model.getBoard().getNeighbours(move.origin()).contains(move.target());
      assertEquals(!move.jump(), adjacent);
    }
  }

  @Test
  public void testJumpOverAdjacentPawnIsOffered() {
    List<Move> moves = controller.getPlayerMoves();
    int expectedJumps = 0;
    for (Field origin : model.getPlayerFields(0)) {
      for (Field over : model.getBoard().getNeighbours(origin)) {
        if (model.isClear(over)) {
          continue;
        }
        Field landing = model.getBoard().getExtendedNeighbour(origin, over);
        if (landing != null && model.isClear(landing)) {
          expectedJumps++;
          assertTrue(moves.contains(new Move(origin, landing, true)));
        }
      }
    }
    assertTrue("La position initiale doit permettre au moins un saut", expectedJumps > 0);
  }

  @Test
  public void testJumpsCanBeChained() throws ModelAccessConsistencyException {
    // Deux pions adverses alignes, separes par une case libre : le pion doit pouvoir
    // enchainer deux sauts dans la meme direction.
    Field origin = null;
    Field farLanding = null;
    for (Field candidate : model.getPlayerFields(0)) {
      Field landing = new Field(candidate.x() + 2, candidate.y() + 2);
      Field far = new Field(candidate.x() + 4, candidate.y() + 4);
      Field first = new Field(candidate.x() + 1, candidate.y() + 1);
      Field second = new Field(candidate.x() + 3, candidate.y() + 3);
      Set<Field> all = model.getBoard().getAllFields();
      if (all.contains(first) && all.contains(landing) && all.contains(second)
          && all.contains(far) && model.isClear(first) && model.isClear(landing)
          && model.isClear(second) && model.isClear(far)) {
        model.occupyField(1, first);
        model.occupyField(1, second);
        origin = candidate;
        farLanding = far;
        break;
      }
    }
    assertTrue("Aucune position de test trouvee", origin != null);
    assertTrue(controller.getPlayerMoves().contains(new Move(origin, farLanding, true)));
  }

  @Test
  public void testPerformMoveMovesPawnAndPassesTurn() throws ModelAccessConsistencyException {
    Move move = controller.getPlayerMoves().get(0);
    controller.performMove(move);
    assertTrue(model.isClear(move.origin()));
    assertTrue(model.getPlayerFields(0).contains(move.target()));
    assertEquals(1, model.getCurrentPlayer());
  }

  @Test
  public void testTurnComesBackToFirstPlayer() throws ModelAccessConsistencyException {
    for (int turn = 0; turn < PLAYERS.length; turn++) {
      assertEquals(turn, model.getCurrentPlayer());
      controller.performMove(controller.getPlayerMoves().get(0));
    }
    assertEquals(0, model.getCurrentPlayer());
  }

  @Test
  public void testIllegalMoveIsRejected() {
    Field origin = model.getPlayerFields(0).iterator().next();
    Move illegal = new Move(origin, new Field(999, 999), false);
    assertThrows(IllegalArgumentException.class, () -> controller.performMove(illegal));
    assertEquals(0, model.getCurrentPlayer());
  }

  @Test
  public void testMovingOpponentPawnIsRejected() {
    Field opponentPawn = model.getPlayerFields(1).iterator().next();
    Field free = null;
    for (Field neighbour : model.getBoard().getNeighbours(opponentPawn)) {
      if (model.isClear(neighbour)) {
        free = neighbour;
      }
    }
    Move illegal = new Move(opponentPawn, free, false);
    assertThrows(IllegalArgumentException.class, () -> controller.performMove(illegal));
  }

  @Test
  public void testNoWinnerAtStart() {
    assertFalse(controller.isGameOver());
    assertEquals(-1, controller.getWinner());
  }

  @Test
  public void testPlayerWinsWhenTargetZoneIsFull() throws ModelAccessConsistencyException {
    for (Field field : new HashSet<>(model.getPlayerFields(1))) {
      model.clearField(field);
    }
    for (Field field : model.getBoard().getTargetFieldsForPlayer(1)) {
      model.occupyField(1, field);
    }
    assertTrue(controller.isGameOver());
    assertEquals(1, controller.getWinner());
    assertTrue(controller.getPlayerMoves().isEmpty());
  }
}
