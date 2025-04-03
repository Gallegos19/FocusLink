package com.example.focuslink.core.data

import android.content.Context
import android.content.SharedPreferences

object GlobalStorage {
    private const val PREFS_NAME = "app_prefs"
    private const val ID_ACTIVITY_KEY = "id_activity"
    private const val USER_ID_KEY = "user_id"
    private const val GROUP_ID_KEY = "group_id"
    private const val UPLOAD_DATA = "upload_data"

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    private fun checkInitialization() {
        checkNotNull(sharedPreferences) { "GlobalStorage no ha sido inicializado. Llama a init(context) antes de usarlo." }
    }

    // Guardar ID de actividad
    fun saveIdActivity(activityId: Int) {
        checkInitialization()
        sharedPreferences?.edit()?.putInt(ID_ACTIVITY_KEY, activityId)?.apply()
    }

    // Obtener ID de actividad (retorna null si no está almacenado)
    fun getIdActivity(): Int? {
        checkInitialization()
        return sharedPreferences?.getInt(ID_ACTIVITY_KEY, -1)?.takeIf { it != -1 }
    }

    // Guardar ID de usuario
    fun saveUserId(userId: String) {
        checkInitialization()
        sharedPreferences?.edit()?.putString(USER_ID_KEY, userId)?.apply()
    }

    // Obtener ID de usuario
    fun getUserId(): String? {
        checkInitialization()
        return sharedPreferences?.getString(USER_ID_KEY, null)
    }

    // Guardar ID de grupo
    fun saveGroupId(groupId: Int) {
        checkInitialization()
        sharedPreferences?.edit()?.putInt(GROUP_ID_KEY, groupId)?.apply()
    }

    // Obtener ID de grupo
    fun getGroupId(): Int? {
        checkInitialization()
        return sharedPreferences?.getInt(GROUP_ID_KEY, -1)?.takeIf { it != -1 }
    }

    // Guardar Upload Data
    fun saveUploadData(upload: Boolean) {
        checkInitialization()
        sharedPreferences?.edit()?.putBoolean(UPLOAD_DATA, upload)?.apply()
    }

    // Obtener Upload Data
    fun getUploadData(): Boolean {
        checkInitialization()
        return sharedPreferences?.getBoolean(UPLOAD_DATA, false) ?: false
    }

    // Borrar solo el ID de actividad
    fun clearActivityId() {
        checkInitialization()
        sharedPreferences?.edit()?.remove(ID_ACTIVITY_KEY)?.apply()
    }

    // Borrar solo el ID de grupo
    fun clearGroupId() {
        checkInitialization()
        sharedPreferences?.edit()?.remove(GROUP_ID_KEY)?.apply()
    }

    // Borrar toda la sesión (actividad, usuario, grupo, etc.)
    fun clearSession() {
        checkInitialization()
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
