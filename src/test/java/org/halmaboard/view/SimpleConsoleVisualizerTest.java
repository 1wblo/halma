package org.halmaboard.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.uqam.info.solanum.students.halma.controller.StarModelFactory;
import org.halmaboard.api.Model;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests du visualiseur console.
 */
public class SimpleConsoleVisualizerTest {

  private static final String[] PLAYERS = {"Alice", "Bob", "Charlie"};

  private Model model;
  private String output;

  @Before
  public void setUp() {
    model = new StarModelFactory().createModel(3, PLAYERS);
    output = new SimpleConsoleVisualizer(false).stringifyModel(model);
  }

  /** Retourne uniquement le dessin du plateau, sans le resume qui le precede. */
  private String boardPart() {
    return output.substring(output.indexOf("\n\n") + 2);
  }

  private static long count(String text, char symbol) {
    return text.chars().filter(c -> c == symbol).count();
  }

  @Test
  public void testSummaryListsEveryPlayer() {
    for (String name : PLAYERS) {
      assertTrue(output.contains(name));
    }
    assertTrue(output.contains("Joueur courant : Alice"));
  }

  @Test
  public void testBoardDrawsOneSymbolPerField() {
    String board = boardPart();
    long drawn = count(board, '.');
    for (int player = 0; player < PLAYERS.length; player++) {
      drawn += count(board, Character.forDigit(player, 10));
    }
    assertEquals(model.getBoard().getAllFields().size(), drawn);
  }

  @Test
  public void testBoardDrawsEveryPawnOfEachPlayer() {
    String board = boardPart();
    for (int player = 0; player < PLAYERS.length; player++) {
      assertEquals(model.getPlayerFields(player).size(),
          count(board, Character.forDigit(player, 10)));
    }
  }

  @Test
  public void testColoursAreOnlyUsedWhenRequested() {
    assertFalse(output.contains("\u001B["));
    assertTrue(new SimpleConsoleVisualizer(true).stringifyModel(model).contains("\u001B["));
  }
}
