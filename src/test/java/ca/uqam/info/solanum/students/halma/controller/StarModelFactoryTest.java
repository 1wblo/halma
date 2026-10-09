package ca.uqam.info.solanum.students.halma.controller;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.halmaboard.api.Field;
import org.halmaboard.api.Model;
import org.halmaboard.api.ModelFactory;
import org.halmaboard.api.ModelInitializationException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests de la fabrique StarModelFactory.
 */
public class StarModelFactoryTest {

  private static final String[] THREE_PLAYERS = {"Alice", "Bob", "Charlie"};
  private static final String[] SIX_PLAYERS = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};

  private ModelFactory factory;

  @Before
  public void setUp() {
    factory = new StarModelFactory();
  }

  @Test
  public void testCreateModelReturnsModelWithThreePlayers() {
    Model model = factory.createModel(2, THREE_PLAYERS);
    assertNotNull(model);
    assertArrayEquals(THREE_PLAYERS, model.getPlayerNames());
  }

  @Test
  public void testCreateModelReturnsModelWithSixPlayers() {
    Model model = factory.createModel(2, SIX_PLAYERS);
    assertNotNull(model);
    assertArrayEquals(SIX_PLAYERS, model.getPlayerNames());
  }

  @Test
  public void testCreatedModelHasBoardWithFields() {
    Model model = factory.createModel(3, THREE_PLAYERS);
    assertNotNull(model.getBoard());
    assertFalse(model.getBoard().getAllFields().isEmpty());
  }

  @Test
  public void testEveryPlayerStartsOnItsHomeFields() {
    Model model = factory.createModel(3, SIX_PLAYERS);
    for (int player = 0; player < SIX_PLAYERS.length; player++) {
      Set<Field> occupied = model.getPlayerFields(player);
      assertFalse("Le joueur " + player + " doit occuper des cases", occupied.isEmpty());
      assertEquals(model.getBoard().getHomeFieldsForPlayer(player), occupied);
    }
  }

  @Test
  public void testPlayersStartWithSameNumberOfDistinctFields() {
    Model model = factory.createModel(3, SIX_PLAYERS);
    int expected = model.getPlayerFields(0).size();
    Set<Field> all = new HashSet<>();
    for (int player = 0; player < SIX_PLAYERS.length; player++) {
      assertEquals(expected, model.getPlayerFields(player).size());
      all.addAll(model.getPlayerFields(player));
    }
    assertEquals(expected * SIX_PLAYERS.length, all.size());
    assertTrue(model.getBoard().getAllFields().containsAll(all));
  }

  @Test
  public void testLargerBaseSizeCreatesLargerBoard() {
    int small = factory.createModel(1, THREE_PLAYERS).getBoard().getAllFields().size();
    int large = factory.createModel(3, THREE_PLAYERS).getBoard().getAllFields().size();
    assertTrue(large > small);
  }

  @Test
  public void testUnsupportedNumberOfPlayersIsRejected() {
    assertThrows(ModelInitializationException.class,
        () -> factory.createModel(2, new String[] {"Alice", "Bob"}));
  }

  @Test
  public void testNonPositiveBaseSizeIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> factory.createModel(0, THREE_PLAYERS));
  }
}
