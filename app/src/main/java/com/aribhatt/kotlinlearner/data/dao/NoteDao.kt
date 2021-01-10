package com.aribhatt.kotlinlearner.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aribhatt.kotlinlearner.data.entities.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(notes: List<NoteEntity>)

    @Delete
    fun deleteNote(note: NoteEntity):Int

    @Delete
    fun deleteNotes(notes: List<NoteEntity>):Int

    @Query("DELETE FROM notes")
    fun deleteAllNotes():Int

    @Query("SELECT * FROM notes ORDER BY date ASC")
    fun getAll(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): LiveData<NoteEntity>

    @Query("SELECT COUNT(*) FROM notes")
    fun getCount(): Int
}