package com.example.directoryofgameartifacts.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.directoryofgameartifacts.MainActivity
import com.example.directoryofgameartifacts.R
import com.example.directoryofgameartifacts.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Second_page : AppCompatActivity() {
    private lateinit var Container: LinearLayout
    private lateinit var button_exit_block: Button
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)

        supportActionBar?.hide()

        Container = findViewById(R.id.Container_right)
        button_exit_block = findViewById(R.id.button_exit)

        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val profile_id = sharedPreferences.getString("profile_id", "")
        val id = profile_id?.toIntOrNull()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_allartifact(id)
                var iter = 0
                if (result.isNotEmpty()) {
                    for (info in result) {
                        iter += 1
                        if (iter > 5) {
                            val blockLayout = LinearLayout(this@Second_page)
                            blockLayout.orientation = LinearLayout.HORIZONTAL
                            blockLayout.setPadding(0, 40, 0, 40) // Устанавливаем отступы

                            val image = ImageView(this@Second_page)
                            val text = TextView(this@Second_page)
                            when (info.id){
                                6 -> {
                                    image.setImageResource(R.drawable.six)
                                }
                                7 -> {
                                    image.setImageResource(R.drawable.seven)
                                }
                                8 -> {
                                    image.setImageResource(R.drawable.eight)
                                }
                                9 -> {
                                    image.setImageResource(R.drawable.nine)

                                }

                            }

                            text.text = info.title
                            // Установите изображение из info.image в ImageView
                            // image.setImageResource(R.drawable.image_resource) // Пример


                            text.setTextColor(Color.BLACK) // Устанавливаем черный цвет текста
                            text.textSize = 18f // Устанавливаем размер текста

                            if (info.bookmarked == "true") {
                                text.setTextColor(ContextCompat.getColor(this@Second_page, R.color.green))
                            }



                            text.setOnClickListener {
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
                                val intent = Intent(this@Second_page, Artivact_page::class.java)
                                intent.putExtra("title", info.title)
                                intent.putExtra("text", info.text)
                                intent.putExtra("id", info.id.toString())
                                intent.putExtra("status", info.bookmarked)
                                startActivity(intent)
                            }

                            val layoutParams_title = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            layoutParams_title.marginStart


                            val layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            layoutParams_title.topMargin = 200
                            layoutParams_title.leftMargin = 50
                            layoutParams.marginStart = 30 // Устанавливаем отступ слева
                            text.layoutParams = layoutParams_title
                            image.layoutParams = layoutParams

                            layoutParams.leftMargin = 100
                            layoutParams.topMargin = 200

                            blockLayout.addView(image)
                            blockLayout.addView(text)

                            Container.addView(blockLayout)
                        }
                    }
                } else {
                    // Обработка случая, когда список пуст
                    Log.e("BusActivity", "Response failed - result is empty")
                }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("BusActivity", "Error during response", e)
                e.printStackTrace()
            }
        }

        button_exit_block.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {

    }
}