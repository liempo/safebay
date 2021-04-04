package wtf.liempo.safebay.data

import android.content.SharedPreferences
import androidx.core.content.edit
import wtf.liempo.safebay.models.Type

// TODO Fix these with DI
class TypeRepository {

    fun setType(pref: SharedPreferences, type: Type) {
        pref.edit { putString(PREF_KEY_TYPE, type.toString()) }
    }

    fun getType(pref: SharedPreferences): Type {
        val value = pref.getString(PREF_KEY_TYPE, null)
            ?: return Type.STANDARD
        return Type.valueOf(value)
    }

    companion object {
        private const val PREF_KEY_TYPE = "auth_type"
    }
}
