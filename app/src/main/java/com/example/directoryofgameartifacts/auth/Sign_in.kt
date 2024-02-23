package com.example.directoryofgameartifacts.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.directoryofgameartifacts.R
import com.example.directoryofgameartifacts.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Sign_in : AppCompatActivity() {

    private lateinit var editTextLogin_block: EditText
    private lateinit var editTextPassword_block: EditText
    private lateinit var editTextPassword2_block: EditText
    private lateinit var buttonLogin_block: Button
    private lateinit var textView6_block: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        editTextLogin_block = findViewById(R.id.editTextLogin)
        editTextPassword_block = findViewById(R.id.editTextPassword)
        editTextPassword2_block = findViewById(R.id.editTextPassword2)
        buttonLogin_block = findViewById(R.id.buttonLogin)
        textView6_block = findViewById(R.id.textView6)

        supportActionBar?.hide()

        buttonLogin_block.setOnClickListener {
            val loginText = editTextLogin_block?.text?.toString()
            val passwordText = editTextPassword_block?.text?.toString()
            val passwordText2 = editTextPassword2_block?.text?.toString()
            if (!loginText.isNullOrBlank() && !passwordText.isNullOrBlank() && !passwordText2.isNullOrBlank() && passwordText == passwordText2) {
                GlobalScope.launch(Dispatchers.Main) {
                    try {

                        val data = api_resource()
                        val result = data.Sign_in(
                            loginText.toString(),
                            passwordText.toString())

                        if (result != null) {
                            val intent = Intent(this@Sign_in, Login::class.java)
                            startActivity(intent)
                            textView6_block.text = result.message

                            val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("profile_id", result.profile_id.toString())
                            editor.putString("login", loginText)
                            editor.putString("login", "false")


                            editor.apply()

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            textView6_block.text = "Ошибка в процессе авторизации ${result.message}"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        textView6_block.text = "Ошибка входа: Неправильный пароль или профиль уже существует"
                    }
                }
            } else {
                textView6_block.text = "Заполните все поля или пароли не совпадают"
            }
        }

    }
}