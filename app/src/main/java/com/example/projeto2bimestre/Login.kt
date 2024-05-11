package com.example.projeto2bimestre
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto2bimestre.Desafio
import com.example.projeto2bimestre.MainActivity
import com.example.projeto2bimestre.Profile
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    private lateinit var profileRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicialização do Firebase
        val database = FirebaseDatabase.getInstance()
        profileRef = database.getReference("profiles")

        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLoginSubmit)
        val backButton = findViewById<Button>(R.id.buttonBackToMain)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(username, password)
        }

        // Listener para o botão de voltar para MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Fechar a atividade de login para que o usuário não possa voltar para ela pressionando o botão de voltar
        }
    }

    private fun loginUser(username: String, password: String) {
        profileRef.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (profileSnapshot in dataSnapshot.children) {
                        val profile = profileSnapshot.getValue(Profile::class.java)
                        if (profile != null && profile.password == password) {
                            // Login bem-sucedido, abrir a próxima atividade
                            val intent = Intent(this@Login, Desafio::class.java)
                            startActivity(intent)
                            finish() // Fechar a atividade de login para que o usuário não possa voltar para ela pressionando o botão de voltar
                            return
                        }
                    }
                    // Nome de usuário encontrado, mas senha incorreta
                    Toast.makeText(this@Login, "Senha incorreta", Toast.LENGTH_SHORT).show()
                } else {
                    // Nome de usuário não encontrado
                    Toast.makeText(this@Login, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Tratamento de erro
                Toast.makeText(this@Login, "Erro de banco de dados", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
