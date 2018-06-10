package br.com.yves.groupmatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import br.com.yves.groupmatch.ui.main.MainFragment
import br.com.yves.groupmatch.ui.main.MainView
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), MainView, BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            displayCalendarScene()
        }

        this.bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }


    /*
        MainView Implementation
     */

    override fun displayCalendarScene() = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, MainFragment.newInstance()).commitNow()

    override fun displaySearchScene() = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, SearchDevicesFragment.newInstance()).commitNow()

    override fun displaySettingsScene() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
       BottomNavigationView.OnNavigationItemSelectedListener Implementation
    */

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.calendarTabIcon -> this.displayCalendarScene()
            R.id.searchTabIcon -> this.displaySearchScene()
        }
        return true
    }
}
