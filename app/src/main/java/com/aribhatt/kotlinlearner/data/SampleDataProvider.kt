package com.aribhatt.kotlinlearner.data

import com.aribhatt.kotlinlearner.data.entities.NoteEntity
import java.util.*

class SampleDataProvider {

    companion object {
        private val sampleText1 = "A simple note"
        private val sampleText2 = "A rather longer text"
        private val sampleText3 = "A yet longerrrrrr rrrr rrr text"

        private fun getDate(diff: Long): Date {
            return Date(Date().time + diff)
        }

        fun getNotes() = arrayListOf<NoteEntity>(
            NoteEntity(1, getDate(1), sampleText1),
            NoteEntity(2, getDate(2), sampleText2),
            NoteEntity(3, getDate(3), sampleText3)
        )
    }
}