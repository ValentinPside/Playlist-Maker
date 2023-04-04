package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Реализация анонимный класс
        val but1 = findViewById<Button>(R.id.button1)
        val but1ClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск!", Toast.LENGTH_SHORT).show()
         }
        }
        but1.setOnClickListener(but1ClickListener)
        val but2 = findViewById<Button>(R.id.button2)
        val but2ClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Медиатека!", Toast.LENGTH_SHORT).show()
        }
        }
        but2.setOnClickListener(but2ClickListener)
        val but3 = findViewById<Button>(R.id.button3)
        val but3ClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Настройки!", Toast.LENGTH_SHORT).show()
        }
        }
        but3.setOnClickListener(but3ClickListener)

        //Реализация через лямбду
        but1.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск!", Toast.LENGTH_SHORT).show()
        }
        but2.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Медиатека!", Toast.LENGTH_SHORT).show()
        }
        but3.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Настройки!", Toast.LENGTH_SHORT).show()
        }
    }
}