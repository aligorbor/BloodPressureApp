package ru.geekbrains.android2.bloodpressureapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.android2.bloodpressureapp.R
import ru.geekbrains.android2.bloodpressureapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        val view = binding.getRoot()
        setContentView(view)
        savedInstanceState ?: run {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MeasuresFragment.newInstance())
                .commitNow()
        }
    }
}