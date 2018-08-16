package br.com.yves.groupmatch.scenes.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.main_activity.*

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
