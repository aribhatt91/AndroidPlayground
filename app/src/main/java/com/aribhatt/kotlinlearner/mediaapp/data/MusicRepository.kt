package com.aribhatt.kotlinlearner.mediaapp.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.aribhatt.kotlinlearner.mediaapp.data.model.Song

class MusicRepository(context: Context) {
    private val c = context
    companion object {

    }
    fun musicFiles():MutableList<Song>{
        // Initialize an empty mutable list of music
        val list:MutableList<Song> = mutableListOf()

        // Get the external storage media store audio uri
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        //val uri: Uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI

        // IS_MUSIC : Non-zero if the audio file is music
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"

        // Sort the musics
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        //val sortOrder = MediaStore.Audio.Media.TITLE + " DESC"

        // Query the external storage for music files
        val cursor: Cursor? = c.contentResolver.query(
            uri, // Uri
            null, // Projection
            selection, // Selection
            null, // Selection arguments
            sortOrder // Sort order
        )

        // If query result is not empty
        if (cursor!= null && cursor.moveToFirst()){
            val id:Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title:Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val album: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val artist: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val duration:Int = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val trackNumber:Int = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)
            val genre:Int = cursor.getColumnIndex(MediaStore.Audio.Media.GENRE)

            // Now loop through the music files
            do {
                // Add the current music to the list
                list.add(Song(cursor.getLong(id),cursor.getString(title), cursor.getString(album), cursor.getString(artist), cursor.getLong(duration)))
            }while (cursor.moveToNext())
        }

        // Finally, return the music files list
        return  list
    }
}