package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(), ToDoDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest{
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        val taskupdate = Task("uptitle", "description updated")
        taskupdate.id = task.id
        database.taskDao().insertTask(taskupdate)

        val loaded = database.taskDao().getTaskById(task.id)

        assertThat<Task>(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(taskupdate.id))
        assertThat(loaded.title, `is`(taskupdate.title))
        assertThat(loaded.description, `is`(taskupdate.description))
        assertThat(loaded.isCompleted, `is`(taskupdate.isCompleted))
    }

}