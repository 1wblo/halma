package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.model.Board;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import java.util.HashSet;
import java.util.Set;

import java.util.*;

public class BoardImpl implements Board {
    private final Set<Field> allFields;
    private final Map<Integer, Set<Field>> homeFields;
    private final Map<Integer, Set<Field>> targetFields;

    public BoardImpl(Set<Field> allFields, Map<Integer, Set<Field>> homeFields, Map<Integer, Set<Field>> targetFields) {
        // Stocke les champs fournis les rendre non modifiables."
        this.allFields = Collections.unmodifiableSet(new HashSet<>(allFields));

        // Créé une copie non modifiable
        Map<Integer, Set<Field>> unmodifiableHomeFields = new HashMap<>();
        Map<Integer, Set<Field>> unmodifiableTargetFields = new HashMap<>();

        for (Map.Entry<Integer, Set<Field>> entry : homeFields.entrySet()) {
            unmodifiableHomeFields.put(entry.getKey(), Collections.unmodifiableSet(new HashSet<>(entry.getValue())));
        }

        for (Map.Entry<Integer, Set<Field>> entry : targetFields.entrySet()) {
            unmodifiableTargetFields.put(entry.getKey(), Collections.unmodifiableSet(new HashSet<>(entry.getValue())));
        }

        this.homeFields = Collections.unmodifiableMap(unmodifiableHomeFields);
        this.targetFields = Collections.unmodifiableMap(unmodifiableTargetFields);
    }

    @Override
    public Set<Field> getAllFields() {
        return allFields;
    }

    @Override
    public Set<Field> getAllHomeFields() {
        Set<Field> result = new HashSet<>();
        for (Set<Field> fields : homeFields.values()) {
            result.addAll(fields);
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    public Set<Field> getHomeFieldsForPlayer(int playerIndex) {
        Set<Field> fields = homeFields.get(playerIndex);
        if (fields == null) {
            throw new IllegalArgumentException("Indice Invalide: " + playerIndex);
        }
        return fields;
    }

    @Override
    public Set<Field> getTargetFieldsForPlayer(int playerIndex) {
        Set<Field> fields = targetFields.get(playerIndex);
        if (fields == null) {
            throw new IllegalArgumentException("Indice invalide: " + playerIndex);
        }
        return fields;
    }

    @Override
    public Set<Field> getNeighbours(Field field) {
        throw new RuntimeException("");
    }

    @Override
    public Field getExtendedNeighbour(Field origin, Field neighbour) {
        throw new RuntimeException("");
    }
}
