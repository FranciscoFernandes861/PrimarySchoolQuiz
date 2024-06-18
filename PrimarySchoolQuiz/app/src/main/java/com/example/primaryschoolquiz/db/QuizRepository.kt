package com.example.primaryschoolquiz.db

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class QuizRepository(private val quizDao: QuizDao) {

    fun getAllQuizzes(userId: String): LiveData<List<QuizWithQuestions>> {
        return quizDao.getAllQuizzes(userId)
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

                // Logging to verify upload
                println("Uploaded QR code to: ${storageRef.path}")
                println("Download URL: ${downloadUrl}")

                // Create a copy of the quiz object with the QR code URL
                val quizWithQRCode = quiz.copy(id = quizId, qrCodeUrl = downloadUrl.toString())

                // Save the quiz to Firestore
                db.collection("quizzes").document(quizId).set(quizWithQRCode).await()

                // Logging to verify Firestore save
                println("Quiz saved to Firestore with ID: $quizId")

                // Insert the quiz and questions into Room database
                quizDao.insertQuiz(quizWithQRCode)
                questions.forEach { question ->
                    quizDao.insertQuestion(question.copy(quizId = quizWithQRCode.id))
                }
                println("Quiz and questions saved to Room database")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                println("Exception during quiz submission: ${e.message}")
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
