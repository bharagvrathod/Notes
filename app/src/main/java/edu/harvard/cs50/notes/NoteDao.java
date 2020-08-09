package edu.harvard.cs50.notes;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("INSERT INTO notes (content) VALUES ('New note')")
    long create(String content);

    @Query("SELECT * FROM notes")
    List<Note> getAll();

    @Query("UPDATE notes SET content = :content WHERE id = :id")
    void save(String content, long id);

    @Query("DELETE FROM notes WHERE id = :id")
    void delete(long id);

    @Query("SELECT content FROM notes WHERE id = :id")
    String getcontent(long id);

}

