package model.Score;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreBoard {

    private final List<ScoreEntry> entries = new ArrayList<>();
    private final int maxEntries;
    private final FileHandle fileHandle;
    private final ScoreFormula scoreFormula;
    private final Json json;

    private static final String DEFAULT_LOCATION = "scoreboard/score.txt";

    public ScoreBoard(int maxEntries, FileHandle fileHandle, ScoreFormula scoreFormula) {
        this.fileHandle = fileHandle;
        this.maxEntries = maxEntries;
        this.scoreFormula = scoreFormula;
        json = new Json();
        json.setSerializer(ScoreEntry.class, new Serializer<>() {
            @Override
            public void write(Json json, ScoreEntry entry, Class knownType) {
                json.writeObjectStart();
                json.writeValue("name", entry.name());
                json.writeValue("score", entry.score());
                json.writeObjectEnd();
            }

            @Override
            public ScoreEntry read(Json json, JsonValue jsonData, Class type) {
                String name = jsonData.getString("name");
                int score = jsonData.getInt("score");
                return new ScoreEntry(name, score);
            }
        });
        ensureFileExists();
        load();
    }

    public ScoreBoard(int maxEntries, ScoreFormula scoreFormula) {
        this(maxEntries, Gdx.files.local(DEFAULT_LOCATION), scoreFormula);
    }

    public List<ScoreEntry> getHighScores() {
        return List.copyOf(entries);
    }

    public void submitScore(String playerName, GameStats gameStats) {
        ScoreEntry scoreEntry = new ScoreEntry(playerName, scoreFormula.calculateScore(gameStats));
        entries.add(scoreEntry);
        entries.sort(Comparator.comparingInt(ScoreEntry::score).reversed());
        if (entries.size() > maxEntries) {
            entries.remove(entries.size() - 1);
        }
        save();
    }

    private FileHandle getFileHandle() {
        return fileHandle;
    }

    private void clear() {
        getFileHandle().writeString("", false);
        entries.clear();
    }

    private void save() {

        String data = json.toJson(entries.subList(0, entries.size()));
        getFileHandle().writeString(data, false);
    }

    private void load() {
        if (getFileHandle().exists()) {
            try {
                ScoreEntry[] array = json.fromJson(ScoreEntry[].class, getFileHandle());
                entries.clear();
                if (array != null) {
                    entries.addAll(List.of(array));
                }
            } catch (Exception e) {
                clear();
            }
        }
    }

    private void ensureFileExists() {
        FileHandle file = getFileHandle();
        if (!file.parent().exists()) {
            file.parent().mkdirs();
        }

        if (!file.exists()) {
            file.writeString("[]", false);
        }
    }


}

