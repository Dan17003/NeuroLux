package com.cordova.gianella.innovacionneuroluxkids

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayoutResourceId(): Int
    abstract fun getBottomNavigationMenuItemId(): Int

    protected lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        LayoutInflater.from(this).inflate(getLayoutResourceId(), contentFrame, true)

        // --- ¡AQUÍ ESTÁ LA CORRECCIÓN CRUCIAL! ---
        // Debemos llamar a las funciones que configuran el header y el footer.
        setupHeader()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        // Asegurarse de que el ítem correcto esté seleccionado al volver a una pantalla
        if (::bottomNavView.isInitialized) {
            bottomNavView.menu.findItem(getBottomNavigationMenuItemId()).isChecked = true
        }
    }

    private fun setupHeader() {
        // Aunque no haga nada por ahora, la dejamos para futura lógica.
        // val btnSettings: ImageView = findViewById(R.id.btnSettings)
        // btnSettings.setOnClickListener { /* ... */ }
    }

    private fun setupBottomNavigation() {
        bottomNavView = findViewById(R.id.bottom_navigation)
        bottomNavView.selectedItemId = getBottomNavigationMenuItemId()

        bottomNavView.setOnItemSelectedListener { item ->
            if (item.itemId == bottomNavView.selectedItemId) {
                return@setOnItemSelectedListener false
            }

            val intent = when (item.itemId) {
                R.id.nav_home -> Intent(this, HomeActivity::class.java)
                R.id.nav_control -> Intent(this, ControlLuzActivity::class.java)
                R.id.nav_routines -> Intent(this, TerceraPaginaActivity::class.java)
                R.id.nav_profile -> Intent(this, ProfileActivity::class.java)
                else -> null
            }

            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(it)
                overridePendingTransition(0, 0)
            }
            true
        }
    }
}