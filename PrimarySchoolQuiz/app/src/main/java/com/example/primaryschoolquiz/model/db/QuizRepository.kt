package com.example.primaryschoolquiz.model.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import com.example.primaryschoolquiz.model.Question
import com.example.primaryschoolquiz.model.Quiz
import com.example.primaryschoolquiz.model.QuizWithQuestions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import com.example.primaryschoolquiz.view.ui.utils.createNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class QuizRepository(private val quizDao: QuizDao, private val context: Context) {

    fun getAllQuizzes(userId: String): LiveData<List<QuizWithQuestions>> {
        return quizDao.getAllQuizzes(userId)
    }

    fun getQuizWithQuestions(quizId: String): Flow<QuizWithQuestions?> {
        println("Repository fetching quiz with ID: $quizId")
        return quizDao.getQuizWithQuestions(quizId).onEach {
            println("Fetched quiz from DAO: $it")
        }
    }

    suspend fun insertQuiz(quiz: Quiz): Long {
        return quizDao.insertQuiz(quiz)
    }

    suspend fun insertQuestion(question: Question) {
        quizDao.insertQuestion(question)
    }

    suspend fun submitQuiz(quiz: Quiz, questions: List<Question>): Boolean {
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val quizId = db.collection("quizzes").document().id

        val qrCode = generateQRCode(quizId)
        val storageRef = storage.reference.child("qr_codes/$quizId.png")
        val baos = ByteArrayOutputStream()
        qrCode.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        return withContext(Dispatchers.IO) {
            try {
                // Upload QR code to Firebase Storage
                val uploadTask = storageRef.putBytes(data).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()

                // Create a copy of the quiz object with the QR code URL and the generated ID
                val quizWithQRCode = quiz.copy(id = quizId, qrCodeUrl = downloadUrl.toString())

                // Save the quiz to Firestore
                db.collection("quizzes").document(quizId).set(quizWithQRCode).await()

                // Insert the quiz and questions into Room database
                println("Inserting quiz into Room database: $quizWithQRCode")
                quizDao.insertQuiz(quizWithQRCode)
                questions.forEach { question ->
                    val questionWithQuizId = question.copy(quizId = quizWithQRCode.id)
                    println("Inserting question into Room database: $questionWithQuizId")
                    quizDao.insertQuestion(questionWithQuizId)
                }

                createNotification(context)

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }




    private fun generateQRCode(content: String): Bitmap {
        val writer = com.google.zxing.qrcode.QRCodeWriter()
        val bitMatrix = writer.encode(content, com.google.zxing.BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }
}
