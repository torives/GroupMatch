package br.com.yves.groupmatch.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		setSupportActionBar(findViewById(R.id.toolbar))
		setupNavigation()
		GroupMatchAuth.setActivity(this)
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
