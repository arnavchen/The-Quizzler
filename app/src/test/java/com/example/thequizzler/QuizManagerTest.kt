import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuizManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class QuizManagerTest {

    private lateinit var sampleQuestions: List<GeneratedQuestion>

    @Before
    fun setup() {
        sampleQuestions = listOf(
            GeneratedQuestion(
                "Question 1", listOf("A", "B"), { it == "A" }, "A", 10
            ),
            GeneratedQuestion(
                "Question 2", listOf("C", "D"), { it == "D" },"D", 10
            )
        )
    }

    @Test
    fun `submitAnswer with correct answer increases score`() {
        val quizManager = QuizManager(sampleQuestions)
        val wasCorrect = quizManager.submitAnswer("A", 2000L) // 2 seconds

        assertTrue(wasCorrect)

        assertEquals(0, quizManager.currentQuestionIndex)
        assertTrue("Score should be greater than 0 for a correct answer", quizManager.score > 0)
    }

    @Test
    fun `submitAnswer with incorrect answer does not increase score`() {
        val quizManager = QuizManager(sampleQuestions)
        val wasCorrect = quizManager.submitAnswer("B", 2000L)

        assertFalse(wasCorrect)

        assertEquals(0, quizManager.currentQuestionIndex)
        assertEquals(0, quizManager.score)
    }

    @Test
    fun `advanceToNextQuestion increments question index`() {
        val quizManager = QuizManager(sampleQuestions)
        assertEquals(0, quizManager.currentQuestionIndex)

        quizManager.advanceToNextQuestion()

        assertEquals(1, quizManager.currentQuestionIndex)
        assertFalse("Quiz should not be finished after one advancement", quizManager.isFinished)
    }

    @Test
    fun `isFinished becomes true after advancing past the last question`() {
        val quizManager = QuizManager(sampleQuestions)

        quizManager.submitAnswer("A", 1000L)
        quizManager.advanceToNextQuestion()
        assertFalse("Quiz should not be finished on the second question", quizManager.isFinished)
        assertEquals(1, quizManager.currentQuestionIndex)

        quizManager.submitAnswer("D", 1000L)
        quizManager.advanceToNextQuestion()
        assertTrue("Quiz should be finished after advancing past the last question", quizManager.isFinished)
        assertEquals(2, quizManager.currentQuestionIndex)
    }

    @Test
    fun `score is higher for faster correct answer`() {
        val managerFast = QuizManager(sampleQuestions)
        val managerSlow = QuizManager(sampleQuestions)

        managerFast.submitAnswer("A", 1000L)
        managerSlow.submitAnswer("A", 5000L)

        assertTrue(managerFast.score > managerSlow.score)
    }
}
