package com.aldomar.firebasesenati

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistryActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRepeatPassword: EditText
    private lateinit var buttonSaveRegistry: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar!!.title = "Registros"
        initViews()
        buttonSaveRegistry.setOnClickListener() {
            if (validateInput()) {
                registerUser(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
            }
        }
    }

    private fun initViews() {
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword)
        buttonSaveRegistry = findViewById(R.id.buttonRegistry)
        auth = FirebaseAuth.getInstance()
    }

    private fun validateInput(): Boolean {
        val username = editTextUsername.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val repeatPassword = editTextRepeatPassword.text.toString()

        if (username.isEmpty()) {
            Toast.makeText(this, "Ingrese su nombre de usuario", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
        } else if (repeatPassword.isEmpty()) {
            Toast.makeText(this, "Repita su contraseña", Toast.LENGTH_SHORT).show()
        } else if (password != repeatPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var uid: String = auth.currentUser!!.uid
                    databaseReference =
                        FirebaseDatabase.getInstance().getReference("users").child(uid)
                    var hashMap = HashMap<String, Any>()
                    val username = editTextUsername.text.toString()
                    val email = editTextEmail.text.toString()
                    val password = editTextPassword.text.toString()

                    hashMap["uid"] = uid
                    hashMap["username"] = username
                    hashMap["email"] = email
                    hashMap["password"] = password
                    hashMap["perfil"] = "image"

                    databaseReference.updateChildren(hashMap)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                val intent = Intent(this@RegistryActivity, MainActivity::class.java)
                                Toast.makeText(
                                    this, "Usuario registrado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "El registro ha fallado", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast.makeText(this, "Error al autentificarse", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener() { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}