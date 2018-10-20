package br.com.yves.groupmatch.presentation.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.main_activity.*
import br.com.yves.groupmatch.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = navHost.findNavController()

        bottomNavigationView.setupWithNavController(navController)

        //FIXME: Descobrir como esconder o Up Button nos primeiros fragments das abas
//        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() =
            navHost.findNavController().navigateUp()
}
