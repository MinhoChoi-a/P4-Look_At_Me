package text.foryou.viewmodel

import android.content.Context
import androidx.room.Room
import androidx.test.runner.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Before
import org.junit.runner.RunWith
import text.foryou.data.AppDatabase
import text.foryou.data.dao.NoteDAO
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Test
import text.foryou.data.model.NoteEntity

@RunWith(AndroidJUnit4::class)
class NoteDataTest: TestCase() {

    private lateinit var noteDao: NoteDAO
    private lateinit var db: AppDatabase

    //set database into the memory
    @Before
    public override fun setUp() {
        val context:Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDao = db.noteDao()!!
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun firstCountNote()  {
        val count = noteDao.getCount()
        assertTrue(count === 0)
    }

    @Test
    fun addNote()  {
        val newNote = NoteEntity("Test", "Font Style", "Font Color", 1, "Background Resource")
        noteDao.insertNote(newNote)
        val getNote = noteDao.getNoteById(1)
        assertEquals("Test", getNote?.text)
    }

}