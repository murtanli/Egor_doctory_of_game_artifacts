package com.example.directoryofgameartifacts.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.directoryofgameartifacts.R
import com.example.directoryofgameartifacts.api.api_resource
import com.example.directoryofgameartifacts.auth.Login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Artivact_page : AppCompatActivity() {

    private lateinit var image_ico_block: ImageView
    private lateinit var textview_block: TextView
    private lateinit var titleview_block: TextView
    private lateinit var save_but_block: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artivact_page)

        // Добавляем кнопку "Назад" (стрелку) на панель инструментов
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        image_ico_block = findViewById(R.id.image_ico)
        textview_block = findViewById(R.id.text_block)
        titleview_block = findViewById(R.id.title_block)
        save_but_block = findViewById(R.id.save_but)

        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val profile_id = sharedPreferences.getString("profile_id", "")
        val idpr = profile_id?.toIntOrNull()

        val artifact_id = intent.getStringExtra("id")
        val artifact_text = intent.getStringExtra("text")
        val artifact_title = intent.getStringExtra("title")
        val status = intent.getStringExtra("status")
        val id = artifact_id?.toIntOrNull()

        if (status == "true"){
            save_but_block.setTextColor(ContextCompat.getColor(this@Artivact_page, R.color.green))
            save_but_block.text = "Удалить"
        } else {
            save_but_block.setTextColor(ContextCompat.getColor(this@Artivact_page, R.color.black))
            save_but_block.text = "Сохранить !"
        }

        Log.e("666", id.toString())

        supportActionBar?.title = artifact_title

        textview_block.text = artifact_text
        titleview_block.text = artifact_title

        when (id){
            1 -> image_ico_block.setImageResource(R.drawable.one)

            2 -> image_ico_block.setImageResource(R.drawable.two)

            3 -> image_ico_block.setImageResource(R.drawable.three)

            4 -> image_ico_block.setImageResource(R.drawable.four)

            5 -> image_ico_block.setImageResource(R.drawable.five)

            6 -> image_ico_block.setImageResource(R.drawable.six)

            7 -> image_ico_block.setImageResource(R.drawable.seven)

            8 -> image_ico_block.setImageResource(R.drawable.eight)

            9 -> image_ico_block.setImageResource(R.drawable.nine)
        }



        save_but_block.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(300)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
            if (status == "false") {
                GlobalScope.launch(Dispatchers.Main) {
                    try {

                        val data = api_resource()
                        val result = data.save_artifact(
                            idpr,
                            id)

                        if (result != null) {
                            save_but_block.setTextColor(ContextCompat.getColor(this@Artivact_page, R.color.green))
                            save_but_block.text = "Сохранено !"
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                    }
                }
            } else if (status == "true") {
                GlobalScope.launch(Dispatchers.Main) {
                    try {

                        val data = api_resource()
                        val result = data.delete_artifact(
                            idpr,
                            id)

                        if (result != null) {
                            save_but_block.setTextColor(ContextCompat.getColor(this@Artivact_page, R.color.black))
                            save_but_block.text = "Сохранено !"
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                    }
                }
            }

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
