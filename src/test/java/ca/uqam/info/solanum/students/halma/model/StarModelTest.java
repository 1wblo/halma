package ca.uqam.info.solanum.students.halma.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import ca.uqam.info.solanum.students.halma.controller.StarModelFactory;
import java.util.HashSet;
import java.util.Set;
import org.halmaboard.api.Field;
import org.halmaboard.api.FieldException;
import org.halmaboard.api.Model;
import org.halmaboard.api.ModelAccessConsistencyException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests de l'etat initial et des operations d'un modele cree par StarModelFactory.
 */
public class StarModelTest {

  private static final String[] PLAYERS = {"Alice", "Bob", "Charlie"};
  private static final int BASE_SIZE = 2;

  private Model model;

  @Before
  public void setUp() {
    model = new StarModelFactory().createModel(BASE_SIZE, PLAYERS);
  }

  /** Retourne une case du plateau qu'aucun joueur n'occupe. */
  private Field anyFreeField() {
    Set<Field> free = new HashSet<>(model.getBoard().getAllFields());
    for (int player = 0; player < PLAYERS.length; player++) {
      free.removeAll(model.getPlayerFields(player));
    }
    assertFalse("Le plateau initial doit avoir des cases libres", free.isEmpty());
    return free.iterator().next();
  }

  @Test
  public void testHomeFieldsAreNotClearInitially() {
    for (int player = 0; player < PLAYERS.length; player++) {
      for (Field field : model.getPlayerFields(player)) {
        assertFalse(model.isClear(field));
      }
    }
  }

  @Test
  public void testFreeFieldIsClear() {
    assertTrue(model.isClear(anyFreeField()));
  }

  @Test
  public void testIsClearOnUnknownFieldThrows() {
    assertThrows(FieldException.class, () -> model.isClear(new Field(999, 999)));
  }

  @Test
  public void testOccupyThenClearField() throws ModelAccessConsistencyException {
    Field field = anyFreeField();
    model.occupyField(0, field);
    assertTrue(model.getPlayerFields(0).contains(field));
    assertFalse(model.isClear(field));

    model.clearField(field);
    assertFalse(model.getPlayerFields(0).contains(field));
    assertTrue(model.isClear(field));
  }

  @Test
  public void testClearFreeFieldThrows() {
    Field field = anyFreeField();
    assertThrows(ModelAccessConsistencyException.class, () -> model.clearField(field));
  }

  @Test
  public void testClearUnknownFieldThrows() {
    assertThrows(ModelAccessConsistencyException.class,
        () -> model.clearField(new Field(999, 999)));
  }

  @Test
  public void testFirstPlayerIsCurrentInitially() {
    assertEquals(0, model.getCurrentPlayer());
  }

  @Test
  public void testSetCurrentPlayerChangesCurrentPlayer() throws ModelAccessConsistencyException {
    model.setCurrentPlayer(2);
    assertEquals(2, model.getCurrentPlayer());
    model.setCurrentPlayer(0);
    assertEquals(0, model.getCurrentPlayer());
  }

  @Test
  public void testInvalidIndexLeavesCurrentPlayerUnchanged()
      throws ModelAccessConsistencyException {
    model.setCurrentPlayer(1);
    assertThrows(ModelAccessConsistencyException.class, () -> model.setCurrentPlayer(-1));
    assertEquals(1, model.getCurrentPlayer());
  }

  @Test
  public void testSetCurrentPlayerWithInvalidIndexThrows() {
    assertThrows(ModelAccessConsistencyException.class, () -> model.setCurrentPlayer(-1));
    assertThrows(ModelAccessConsistencyException.class,
        () -> model.setCurrentPlayer(PLAYERS.length));
  }

  @Test
  public void testPlayerFieldsCannotBeModifiedFromOutside() {
    assertThrows(UnsupportedOperationException.class,
        () -> model.getPlayerFields(0).add(new Field(999, 999)));
  }

  @Test
  public void testPlayerNamesAreDefensivelyCopied() {
    model.getPlayerNames()[0] = "Intrus";
    assertEquals("Alice", model.getPlayerNames()[0]);
  }

  @Test
  public void testModelsWithSameParametersAreEqual() {
    assertEquals(model, new StarModelFactory().createModel(BASE_SIZE, PLAYERS));
  }

  @Test
  public void testModelDiffersAfterMove() throws ModelAccessConsistencyException {
    model.occupyField(0, anyFreeField());
    assertNotEquals(model, new StarModelFactory().createModel(BASE_SIZE, PLAYERS));
  }
}
