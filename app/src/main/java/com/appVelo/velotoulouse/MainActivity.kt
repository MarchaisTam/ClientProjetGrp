package com.appVelo.velotoulouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appVelo.velotoulouse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //redirection sur mapsActivity quand on clique sur "c'est parti "
        binding.button.setOnClickListener {
            //Création de l'intent
            val intent = Intent(this, MapsActivity::class.java)

            // Lance le workflow de changement d'écran
            startActivity(intent)
        }
    }
}