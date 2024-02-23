package com.example.directoryofgameartifacts.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.directoryofgameartifacts.MainActivity
import com.example.directoryofgameartifacts.R
import com.example.directoryofgameartifacts.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    private lateinit var but_sing: TextView
    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var errorText : TextView
    private lateinit var button_Login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        editTextLogin = findViewById(R.id.editTextLogin)
        editTextPassword = findViewById(R.id.editTextPassword)
        button_Login = findViewById(R.id.buttonLogin)
        errorText = findViewById(R.id.textView3)
        but_sing = findViewById(R.id.textView2)


        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val login_save = sharedPreferences.getString("login", "")


        if (login_save == "true"){
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }

        but_sing.setOnClickListener {
            val intent = Intent(this@Login, Sign_in::class.java)
            startActivity(intent)
        }

        button_Login.setOnClickListener {
            val loginText = editTextLogin?.text?.toString()
            val passwordText = editTextPassword?.text?.toString()

            Log.e("444", "$loginText  $passwordText")

            if (!loginText.isNullOrBlank() || !passwordText.isNullOrBlank()){
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        // Вызываем функцию logIn для выполнения запроса
                        val data = api_resource()
                        val result = data.log_in(loginText.toString(), passwordText.toString())

                        if (result != null) {
                            if (result.message.isNullOrBlank()) {
                                // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                                Log.d("LoginActivity", "Login successful")
                                //Log.d("LoginActivity", "User ID: ${result.user_data.user_id}")
                                errorText.text = result.message

                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                try {
                                    editor.putString("profile_id", result.profile_id.toString())
                                    editor.putString("user_name", loginText)
                                    editor.putString("login", "true")
                                    editor.apply()
                                } catch(e: Exception) {
                                    editor.putString("profile_id", result.profile_id.toString())
                                    editor.putString("login", "true")
                                    editor.apply()
                                }

                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                                //ErrorText.setTextColor(R.color.blue)

                            } else {
                                // Если произошла ошибка, выводим сообщение об ошибке
                                Log.e("LoginActivity", "Login failed")
                                errorText.text = result.message
                                val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("login", "false")
                                editor.apply()
                            }
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            errorText.text = "Ошибка в процессе авторизации ${result.message}"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        errorText.text = "Ошибка входа: Неправильный пароль или профиль не найден"
                        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("login", "false")
                        editor.apply()
                    }
                }
            } else {
                errorText.text = "Введите данные в поля"
            }
        }

    }
    override fun onBackPressed() {

    }
}