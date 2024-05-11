package com.example.projeto2bimestre

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var profileRef: DatabaseReference
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var preferencesEditText: EditText
    private lateinit var allergiesEditText: EditText
    private lateinit var likesEditText: EditText
    private lateinit var dislikesEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização do Firebase
        database = FirebaseDatabase.getInstance()
        profileRef = database.getReference("profiles")

        // Inicialização do AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        nameEditText = findViewById(R.id.editTextName)
        passwordEditText = findViewById(R.id.EditTextSenha)
        ageEditText = findViewById(R.id.editTextAge)
        preferencesEditText = findViewById(R.id.editTextPreferences)
        allergiesEditText = findViewById(R.id.editTextAlergias)
        likesEditText = findViewById(R.id.editTextGostos)
        dislikesEditText = findViewById(R.id.editTextDesgostos)

        val buttonSaveProfile = findViewById<Button>(R.id.buttonSaveProfile)
        buttonSaveProfile.setOnClickListener {
            saveProfile()
        }

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // Configurar o alarme para vibrar a cada 12 horas
        //setupAlarm()
    }

    // Salva o perfil do usuário no Firebase
    private fun saveProfile() {
        val name = nameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val age = ageEditText.text.toString()
        val preferences = preferencesEditText.text.toString()
        val allergies = allergiesEditText.text.toString()
        val likes = likesEditText.text.toString()
        val dislikes = dislikesEditText.text.toString()

        // Verificar se os campos estão vazios
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(age) || TextUtils.isEmpty(preferences) || TextUtils.isEmpty(allergies) || TextUtils.isEmpty(likes) || TextUtils.isEmpty(dislikes)) {
            Toast.makeText(baseContext, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crie um objeto HashMap para armazenar os dados do perfil
        val profileData = hashMapOf(
            "name" to name,
            "password" to password,
            "age" to age,
            "preferences" to preferences,
            "allergies" to allergies,
            "likes" to likes,
            "dislikes" to dislikes
        )

        // Adicione o perfil ao Firebase
        val key = profileRef.push().key ?: return
        profileRef.child(key).setValue(profileData)

        // Abra a próxima atividade, por exemplo, a atividade do Desafio de 30 Dias de Bondade
        val intent = Intent(this, Desafio::class.java)
        startActivity(intent)
    }

    // Configurar o alarme para vibrar a cada 12 horas
    private fun setupAlarm() {
        val intent = Intent(this, Alarme::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Definir o intervalo de repetição para 12 horas
        val intervalMillis = 12 * 60 * 60 * 1000L // 12 horas em milissegundos

        // Definir o tempo inicial do alarme
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MILLISECOND, intervalMillis.toInt())

        // Agendar o alarme
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intervalMillis, pendingIntent)
    }
}
