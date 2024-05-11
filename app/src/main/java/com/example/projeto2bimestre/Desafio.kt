package com.example.projeto2bimestre
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Desafio : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var reviewRef: DatabaseReference

    private val challengeMap = mapOf(

    "Deixe um bilhete carinhoso para o seu parceiro" to "Expresse seu amor e gratidão em palavras escritas.",
    "Faça uma caminhada de 30 minutos ao ar livre" to "Aproveite o ar fresco e a natureza ao seu redor para relaxar e rejuvenescer.",
    "Cozinhem juntos uma nova receita de culinária" to "Experimente sabores novos e expanda seu repertório culinário.",
    "Tirem um tempo para meditar por 10 minutos" to "Acalme sua mente e pratique a atenção plena para reduzir o estresse.",
    "Limpem e organizem um espaço da sua casa" to "Crie um ambiente mais harmonioso e produtivo ao seu redor.",
    "Tentem aprender uma nova habilidade, como tocar um instrumento musical" to "Desenvolva uma nova habilidade que traga alegria e satisfação à sua vida.",
    "Faça um jantar romântico em casa para o seu parceiro" to "Prepare uma refeição especial e crie um ambiente aconchegante com velas e música suave.",
    "Assistam juntos ao pôr do sol em um local tranquilo" to "Desfrutem de um momento tranquilo e romântico enquanto observam as cores do pôr do sol juntos.",
    "Escrevam juntos uma lista de sonhos e objetivos para o futuro" to "Compartilhem suas aspirações e construam planos em conjunto para o futuro.",
    "Façam um piquenique em um parque ou praia" to "Preparem uma cesta de alimentos deliciosos e desfrutem de uma refeição ao ar livre em meio à natureza.",
    "Escolham um livro para ler juntos e discutam sobre ele depois" to "Compartilhem o prazer da leitura e troquem ideias e reflexões sobre a história.",
    "Façam uma lista de reprodução de músicas que são significativas para vocês" to "Relembrem momentos especiais e criem novas memórias ao som das músicas que amam.",
    "Visitem um local turístico na cidade onde nunca foram juntos" to "Descubram novos lugares e explorem juntos a beleza e a história da sua cidade."
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

        // Inicialização do Firebase
        database = FirebaseDatabase.getInstance()
        reviewRef = database.getReference("reviews")
        val textViewReminder: TextView = findViewById(R.id.textViewReminder)
        textViewReminder.visibility = View.VISIBLE
        // Obtenha um desafio aleatório da lista
        val challengeEntry = challengeMap.entries.random()
        val challenge = challengeEntry.key
        val challengeTip = challengeEntry.value

        // Exiba o desafio e a dica na tela
        val textViewChallenge: TextView = findViewById(R.id.textViewChallenge)
        textViewChallenge.text = challenge

        val textViewTip: TextView = findViewById(R.id.textViewTip)
        textViewTip.text = "Dica: $challengeTip"

        // Configurar botão de envio de avaliação
        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            submitReview()
        }
    }

    private fun submitReview() {
        // Obter avaliação do usuário
        val editTextReview: EditText = findViewById(R.id.editTextReview)
        val review = editTextReview.text.toString()

        // Obter nota do usuário
        val ratingBar: RatingBar = findViewById(R.id.ratingBar)
        val rating = ratingBar.rating

        // Enviar avaliação para o Firebase
        sendReviewToFirebase(review, rating)
    }

    private fun sendReviewToFirebase(review: String, rating: Float) {
        // Crie um objeto HashMap para armazenar os dados da avaliação
        val reviewData = hashMapOf(
            "review" to review,
            "rating" to rating
        )

        // Adicione a avaliação ao Firebase
        val key = reviewRef.push().key ?: return
        reviewRef.child(key).setValue(reviewData)
    }
}
