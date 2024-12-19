package ru.aston.polechudes

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random

/**
Создать Custom View - Барабан состоящий из цветов радуги.
Каждый цвет означает либо картинку либо текст.
При нажатии на барабан или на кнопку где нибудь рядом,
он начинается вращаться и останавливается рандомно каждый раз.

Когда барабан останавливается на определенном цвете:
- красный - текст
- оранжевый - картинка
- жёлтый - текст
- зелёный - картинка
- голубой - текст
- синий - картинка
- фиолетовый - текст

если цвет был картинкой - то грузим картинку из сервиса https://loremipsum.io/ru/21-of-the-best-placeholder-image-generators/
и показываем ее в определенном поле,
если цвет был текстом - то РИСУЕМ текст с помощью Custom View и показываем эту View.
Так же должна быть кнопка Reset, которая чистит экран от картинок и текста
Для усложнения:
- Сбоку барабана сделать ползунок от 0 до 100, по дефолту он должен иметь число 50
- При увеличении ползунка размер барабана увеличивается динамически
- При уменьшении ползунка размер барабана уменьшается динамически
- Эффект должен корректно увеличивать/уменьшать размер при вращении
 **/

class MainActivity : AppCompatActivity() {

    private lateinit var drumView: Baraban
    private lateinit var buttonStart: Button
    private lateinit var buttonReset: FloatingActionButton
    private lateinit var drumSizeBar: SeekBar
    private lateinit var drumResultView: DrumResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drumView = findViewById(R.id.drumView)
        buttonStart = findViewById(R.id.buttonStart)
        buttonReset = findViewById(R.id.buttonReset)
        drumSizeBar = findViewById(R.id.seekBarSize)
        drumResultView = findViewById(R.id.drumResultView)

        buttonStart.setOnClickListener { startDrum() }
        buttonReset.setOnClickListener { resetView() }

        drumSizeBar.min = 0
        drumSizeBar.max = 100

        drumView.setOnSpinCompleteListener { color ->
            val isEven = DrumColors.colors.indexOf(color) % 2 == 0
            drumResultView.setContent(color, isEven)
            buttonStart.setBackgroundColor(color)
        }

        drumSizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val scale = progress / 100f + 0.5f
                val newSize = 200f * scale
                drumView.setSize(newSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun startDrum() {
        val randomSections = Random.nextInt(1, DrumColors.colors.size + 1)
        Log.d("startDrum","randomSections: $randomSections")
        drumView.spinTo(randomSections)
    }

    private fun resetView() {
        drumResultView.resetContent()
    }
}
