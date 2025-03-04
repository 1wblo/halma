package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.students.halma.model.BoardImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.FieldException;
import java.util.Set;

public class BoardImplTest {

     private BoardImpl board;
     private int baseSize = 8;  // Vous pouvez ajuster la taille de base selon vos besoins
     private int nbPlayers = 6;

     @Before
     public void setUp() {
          board = new BoardImpl(baseSize, nbPlayers);
     }

     @Test
     public void testInitializeBoard() {
          Set<Field> allFields = board.getAllFields();
          assertNotNull(allFields);
          assertFalse(allFields.isEmpty());
     }

     @Test
     public void testHomeFieldsForPlayer() {
          Set<Field> homeFieldsForPlayer0 = board.getHomeFieldsForPlayer(0);
          assertNotNull(homeFieldsForPlayer0);
          assertFalse(homeFieldsForPlayer0.isEmpty());

          Set<Field> homeFieldsForPlayer1 = board.getHomeFieldsForPlayer(1);
          assertNotNull(homeFieldsForPlayer1);
          assertFalse(homeFieldsForPlayer1.isEmpty());
     }

     @Test(expected = FieldException.class)
     public void testGetNeighboursWithInvalidField() throws FieldException {
          Field invalidField = new Field(999, 999);
          board.getNeighbours(invalidField);
     }

     @Test
     public void testGetNeighbours() throws FieldException {
          Field field = new Field(baseSize, baseSize);
          Set<Field> neighbours = board.getNeighbours(field);
          assertNotNull(neighbours);
          assertFalse(neighbours.isEmpty());
     }

     @Test(expected = FieldException.class)
     public void testGetExtendedNeighbourWithInvalidOrigin() throws FieldException {
          Field invalidField = new Field(999, 999);
          Field validNeighbour = new Field(baseSize, baseSize + 2);
          board.getExtendedNeighbour(invalidField, validNeighbour);
     }

     @Test(expected = FieldException.class)
     public void testGetExtendedNeighbourWithInvalidNeighbour() throws FieldException {
          Field validField = new Field(baseSize, baseSize);
          Field invalidNeighbour = new Field(999, 999);
          board.getExtendedNeighbour(validField, invalidNeighbour);
     }

     @Test
     public void testGetExtendedNeighbour() throws FieldException {
          Field origin = new Field(baseSize, baseSize);
          Field neighbour = new Field(baseSize, baseSize + 2);
          Field extendedNeighbour = board.getExtendedNeighbour(origin, neighbour);
          assertNotNull(extendedNeighbour);
     }
}
