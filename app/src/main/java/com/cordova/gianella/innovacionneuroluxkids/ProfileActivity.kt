package com.cordova.gianella.innovacionneuroluxkids

import android.os.Bundle

class ProfileActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int {
        return R.layout.content_profile
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.nav_profile
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lógica del botón de cerrar sesión, etc.
    }
}