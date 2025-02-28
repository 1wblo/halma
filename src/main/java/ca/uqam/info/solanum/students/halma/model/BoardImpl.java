package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.model.Board;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelInitializationException;

import java.util.HashSet;
import java.util.Set;

import java.util.*;

/**
 * Cette classe blalala
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
     * blalalala
     * @param baseSize blalalala
     * @param nbPlayers blalalala
     */
   public BoardImpl(int baseSize,int nbPlayers) {
       if (baseSize <= 0) {
           throw new IllegalArgumentException("La base doit etre superieure a zero et le nombre de joueurs doit etre egale a 3 ou 6.");
       }
       if ((nbPlayers != 3) && (nbPlayers != 6)) {
           throw new ModelInitializationException("Invalid number of players");
       }
       this.baseSize = baseSize;
       initializeBoard(/*baseSize*/);
   }
 //public void initializeBoard() {
     private void initializeBoard(/*int baseSize*/) {
         // For each segment of the star (6 segments total)
         for (int segment = 0; segment < 6; segment++) {
             int startX, startY;
             int xDirection, yStart;
            // boolean isVertical = (segment % 2 == 0);

             // Calculate starting positions and directions based on segment
             switch (segment) {
                 case 0: // Middle Left
                     startX = baseSize - 1;
                     startY = baseSize * 2;
                     xDirection = -1;
                     yStart = 1;
                     break;
                 case 1: // Top Right
                     startX = baseSize * 3;
                     startY = 0;
                     xDirection = -1;
                     yStart = -1;
                     break;
                 case 2: // Bottom Right
                     startX = baseSize * 3;
                     startY = baseSize * 4 + 1;
                     xDirection = -1;
                     yStart = 1;
                     break;
                 case 3: // Middle Right
                     startX = baseSize * 3 + 1;
                     startY = baseSize * 2;
                     xDirection = 1;
                     yStart = 1;
                     break;
                 case 4: // Top Left
                     startX = baseSize;
                     startY = 0;
                     xDirection = 1;
                     yStart = -1;
                     break;
                 case 5: // Bottom Left
                     startX = baseSize;
                     startY = baseSize * 4 + 1;
                     xDirection = 1;
                     yStart = 1;
                     break;
                 default:
                     continue;
             }

             // Create triangle segment
             int endTriangle = 0;
             for (int x = startX;
                  (xDirection == 1) ? x < startX + baseSize : x > startX - baseSize;
                  x += xDirection) {

                 endTriangle++;
                 int yMin = (yStart == 1) ? startY + endTriangle : endTriangle - 1;
                 int yMax = (yStart == 1) ? startY + baseSize * 2 - endTriangle : baseSize * 2 - endTriangle;

                 for (int y = yMin; y <= yMax; y += 2) {
                     Field field = new Field(x, y);
                     allFields.add(field);
                     allHomeFields.add(field);

                     // Add to home and target fields based on segment
                     if (segment < 6) { // Exclude center hexagon
                         allHomeFields.add(field);

                         // Assign home and target fields based on segment
                         switch (segment) {
                             case 0:
                                 homeFieldsForPlayer0.add(field);
                                 targetFieldsForPlayer0.add(field);
                                 break;
                             case 1:
                                 homeFieldsForPlayer1.add(field);
                                 targetFieldsForPlayer1.add(field);
                                 break;
                             case 2:
                                 homeFieldsForPlayer2.add(field);
                                 targetFieldsForPlayer2.add(field);
                                 break;
                             case 3:
                                 homeFieldsForPlayer3.add(field);
                                 targetFieldsForPlayer3.add(field);
                                 break;
                             case 4:
                                 homeFieldsForPlayer4.add(field);
                                 targetFieldsForPlayer4.add(field);
                                 break;
                             case 5:
                                 homeFieldsForPlayer5.add(field);
                                 targetFieldsForPlayer5.add(field);
                                 break;
                         }
                     }
                 }
             }
         }

         // Add center hexagon
         addCenterHexagon(baseSize);
     }

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
                return null;
        }
    }

    @Override
    public Set<Field> getAllHomeFields() {
        return Collections.unmodifiableSet(allHomeFields);
    }

    @Override
    public Field getExtendedNeighbour(Field origin, Field neighbour) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Set<Field> getNeighbours(Field field) {
        throw new RuntimeException("Not implemented yet");
    }
    @Override
    public Set<Field> getTargetFieldsForPlayer(int playerIndex) {
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
                return null;
        }
    }
}
