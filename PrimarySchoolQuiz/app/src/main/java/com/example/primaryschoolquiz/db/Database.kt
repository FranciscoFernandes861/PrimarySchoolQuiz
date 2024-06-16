package com.example.primaryschoolquiz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Quiz::class, Question::class], version = 2, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table for questions
                database.execSQL("""
                    CREATE TABLE `question_table` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `quizId` INTEGER NOT NULL,
                        `question` TEXT NOT NULL,
                        `option1` TEXT NOT NULL,
                        `option2` TEXT NOT NULL,
                        `option3` TEXT NOT NULL,
                        `option4` TEXT NOT NULL,
                        `correctOption` INTEGER NOT NULL,
                        FOREIGN KEY(`quizId`) REFERENCES `quiz_table`(`id`) ON DELETE CASCADE
                    )
                """)

                // Ensure the quiz_table has the correct schema (if changed)
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `quiz_table_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL
                    )
                """)
                database.execSQL("INSERT INTO `quiz_table_new` (`id`, `name`) SELECT `id`, `name` FROM `quiz_table`")
                database.execSQL("DROP TABLE `quiz_table`")
                database.execSQL("ALTER TABLE `quiz_table_new` RENAME TO `quiz_table`")
            }
        }

        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
