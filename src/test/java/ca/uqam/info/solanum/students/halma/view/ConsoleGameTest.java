package ca.uqam.info.solanum.students.halma.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.uqam.info.solanum.students.halma.controller.HalmaController;
import ca.uqam.info.solanum.students.halma.controller.StarModelFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import org.halmaboard.api.Field;
import org.halmaboard.api.Model;
import org.halmaboard.api.ModelAccessConsistencyException;
import org.halmaboard.api.Move;
import org.halmaboard.view.SimpleConsoleVisualizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests de la partie console, pilotee par une entree scriptee.
 */
public class ConsoleGameTest {

  private static final String[] PLAYERS = {"Alice", "Bob", "Charlie"};

  private Model model;

  @Before
  public void setUp() {
    model = new StarModelFactory().createModel(3, PLAYERS);
  }

  /** Joue une partie avec l'entree donnee et retourne tout ce qui a ete affiche. */
  private String play(String script) throws ModelAccessConsistencyException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(buffer, true, StandardCharsets.UTF_8);
    new ConsoleGame(new HalmaController(model), new SimpleConsoleVisualizer(false),
        new Scanner(script), output).play();
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @Test
  public void testQuitStopsGameWithoutMoving() throws ModelAccessConsistencyException {
    String output = play("q\n");
    assertTrue(output.contains("Coups permis"));
    assertTrue(output.contains("Partie interrompue."));
    assertEquals(0, model.getCurrentPlayer());
  }

  @Test
  public void testEndOfInputStopsGame() throws ModelAccessConsistencyException {
    assertTrue(play("").contains("Partie interrompue."));
  }

  @Test
  public void testChosenMoveIsPlayedAndTurnPasses() throws ModelAccessConsistencyException {
    Field firstOrigin = new HalmaController(model).getPlayerMoves().get(0).origin();
    String output = play("1\nq\n");
    assertTrue(model.isClear(firstOrigin));
    assertEquals(1, model.getCurrentPlayer());
    assertTrue(output.contains("Bob, ton coup"));
  }

  @Test
  public void testInvalidChoicesAreRefused() throws ModelAccessConsistencyException {
    String output = play("abc\n0\n999\nq\n");
    assertEquals(3, output.split("Choix invalide", -1).length - 1);
    assertEquals(0, model.getCurrentPlayer());
  }

  @Test
  public void testWinnerIsAnnounced() throws ModelAccessConsistencyException {
    // Bob est a un coup de la victoire : tous ses pions sont dans sa zone cible sauf un,
    // place sur une case voisine de la derniere case cible libre.
    for (Field field : new HashSet<>(model.getPlayerFields(1))) {
      model.clearField(field);
    }
    Field lastTarget = null;
    Field waiting = null;
    for (Field target : model.getBoard().getTargetFieldsForPlayer(1)) {
      for (Field neighbour : model.getBoard().getNeighbours(target)) {
        if (!model.getBoard().getTargetFieldsForPlayer(1).contains(neighbour)
            && model.isClear(neighbour)) {
          lastTarget = target;
          waiting = neighbour;
        }
      }
    }
    for (Field target : model.getBoard().getTargetFieldsForPlayer(1)) {
      if (!target.equals(lastTarget)) {
        model.occupyField(1, target);
      }
    }
    model.occupyField(1, waiting);
    model.setCurrentPlayer(1);

    HalmaController controller = new HalmaController(model);
    int winningChoice = -1;
    for (int i = 0; i < controller.getPlayerMoves().size(); i++) {
      Move move = controller.getPlayerMoves().get(i);
      if (move.origin().equals(waiting) && move.target().equals(lastTarget)) {
        winningChoice = i + 1;
      }
    }
    assertFalse("Le coup gagnant doit etre propose", winningChoice < 0);
    String output = play(winningChoice + "\n");
    assertTrue(output.contains("Victoire de Bob !"));
  }
}
