package ca.uqam.info.solanum.students.halma.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelAccessConsistencyException;
import ca.uqam.info.solanum.inf2050.f24halma.model.FieldException;
import java.util.Iterator;

public class ModelImplTest {

     private ModelImpl model;
     private Field field1;
     private Field field2;

     @Before
     public void setUp() {
          String[] playerNames = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};
          model = new ModelImpl(8, playerNames, 6); // Initialiser le modèle avec 6 joueurs

          // Récupérer un champ valide depuis le plateau de jeu
          Iterator<Field> fieldIterator = model.getBoard().getAllFields().iterator();
          if (fieldIterator.hasNext()) {
               field1 = fieldIterator.next();
          }
          field2 = new Field(1, 2);
     }

     @Test
     public void testOccupyFieldWithValidPlayerAndField() {
          try {
               model.occupyField(0, field1);
          } catch (Exception e) {
               fail("Exception ne devait pas être levée");
          }
          assertTrue(model.getPlayerFields(0).contains(field1));
     }

     @Test
     public void testOccupyFieldWithInvalidPlayerIndex() {
          Exception exception = assertThrows(ModelAccessConsistencyException.class, () -> {
               model.occupyField(-1, field1);
          });
          assertEquals("Index de joueur invalide: -1", exception.getMessage());
     }

     @Test
     public void testOccupyNonExistentField() {
          Field invalidField = new Field(999, 999); // Champ inexistant
          Exception exception = assertThrows(ModelAccessConsistencyException.class, () -> {
               model.occupyField(0, invalidField);
          });
          assertEquals("Le champ spécifié n'existe pas sur le plateau", exception.getMessage());
     }

     @Test
     public void testOccupyAlreadyOccupiedField() {
          try {
               model.occupyField(0, field1);
          } catch (Exception e) {
               fail("Exception ne devait pas être levée");
          }
          Exception exception = assertThrows(FieldException.class, () -> {
               model.occupyField(1, field1);
          });
          assertEquals("Le champ est déjà occupé par le joueur 0", exception.getMessage());
     }
}
