package com.example.directoryofgameartifacts.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.directoryofgameartifacts.MainActivity
import com.example.directoryofgameartifacts.R
import com.example.directoryofgameartifacts.api.api_resource
import com.example.directoryofgameartifacts.auth.Login
import com.example.directoryofgameartifacts.databinding.FragmentProfileBinding
import com.example.directoryofgameartifacts.ui.home.Artivact_page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var exit: Button

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        exit = binding.buttonExit
        val linearContainer = binding.savedArtifacts
        val text_name = binding.name
        //(activity as? MainActivity)?.act_bar()
        (activity as? MainActivity)?.title = "Сохраненные артифакты"

        exit.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("profile_id")
            editor.remove("user_name")
            editor.putString("login", "false")
            editor.apply()

            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val profile_id = sharedPreferences.getString("profile_id", "")
        val user_name = sharedPreferences.getString("user_name", "")
        val id = profile_id?.toIntOrNull()

        text_name.text = user_name

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_allartifact(id)
                if (result.isNotEmpty()) {
                    for (info in result) {
                        if (info.bookmarked == "true") {
                            val blockLayout = LinearLayout(requireContext())
                            blockLayout.orientation = LinearLayout.HORIZONTAL
                            blockLayout.setPadding(0, 16, 0, 40) // Устанавливаем отступы

                            val image = ImageView(requireContext())

                            when (info.id){
                                1 -> image.setImageResource(R.drawable.one)
                                2 -> image.setImageResource(R.drawable.two)
                                3 -> image.setImageResource(R.drawable.three)
                                4 -> image.setImageResource(R.drawable.four)
                                5 -> image.setImageResource(R.drawable.five)
                                6 -> image.setImageResource(R.drawable.six)
                                7 -> image.setImageResource(R.drawable.seven)
                                8 -> image.setImageResource(R.drawable.eight)
                                9 -> image.setImageResource(R.drawable.nine)
                            }


                            // Установите изображение из info.image в ImageView
                            // image.setImageResource(R.drawable.image_resource) // Пример

                            val text = TextView(requireContext())
                            text.text = info.title
                            text.setTextColor(Color.BLACK) // Устанавливаем черный цвет текста
                            text.textSize = 18f // Устанавливаем размер текста

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
                                val intent = Intent(requireContext(), Artivact_page::class.java)
                                intent.putExtra("title", info.title)
                                intent.putExtra("text", info.text)
                                intent.putExtra("status", info.bookmarked)
                                intent.putExtra("id", info.id.toString())
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
                            layoutParams_title.leftMargin = 100
                            layoutParams.marginStart = 30 // Устанавливаем отступ слева
                            text.layoutParams = layoutParams_title
                            image.layoutParams = layoutParams
                            layoutParams.leftMargin = 200
                            layoutParams.topMargin = 100

                            blockLayout.addView(image)
                            blockLayout.addView(text)

                            linearContainer.setPadding(0, 0, 0, 200)
                            linearContainer.addView(blockLayout)
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



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}