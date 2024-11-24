package tn.esprit.el_coach.data.sharedpref

import android.content.Context

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Save token, rememberMe flag, image, and fullName
    fun saveToken(token: String, rememberMe: Boolean, image: String, fullName: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putBoolean("rememberMe", rememberMe)
        editor.putString("image", image)
        editor.putString("fullName", fullName)
        editor.apply()
    }

    // Get the stored token
    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    // Get the rememberMe flag
    fun getRememberMe(): Boolean {
        return sharedPreferences.getBoolean("rememberMe", false)
    }

    // Get the stored image
    fun getImage(): String? {
        return sharedPreferences.getString("image", null)
    }

    // Get the stored fullName
    fun getFullName(): String? {
        return sharedPreferences.getString("fullName", null)
    }

    // Clear token, rememberMe flag, image, and fullName
    fun clearToken() {
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.remove("rememberMe")
        editor.remove("image")
        editor.remove("fullName")
        editor.apply()
    }
}
