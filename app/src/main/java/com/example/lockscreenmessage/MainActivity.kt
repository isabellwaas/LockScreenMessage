package com.example.lockscreenmessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lockscreenmessage.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.topAppBar.menu.findItem(R.id.about).setOnMenuItemClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }

        val persistentSaver: IPersistentSaver = PersistentSaver(getSharedPreferences("settings", Context.MODE_PRIVATE))
        persistentSaver.writeValue(getString(R.string.lockScreenMessageId), 11223344)

        var serviceRunning: Boolean = false
        activityMainBinding.showSwitch.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked && !serviceRunning)
            {
                if(persistentSaver.readValue(this.getString(com.example.lockscreenmessage.R.string.lockScreenMessageTitle),null).isNullOrBlank() && persistentSaver.readValue(this.getString(com.example.lockscreenmessage.R.string.lockScreenMessageContent), null).isNullOrBlank())
                {
                    MaterialAlertDialogBuilder(this)
                    .setMessage(getString(R.string.alertDialogMessage))
                        .setPositiveButton(getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
                        .show()
                    activityMainBinding.showSwitch.isChecked=false
                }
                else {
                    ContextCompat.startForegroundService(this, Intent(this, NotificationService::class.java))
                    serviceRunning = true
                }
            } else if (!isChecked && serviceRunning) {
                stopService(Intent(this, NotificationService::class.java))
                serviceRunning = false
            }
        }

        activityMainBinding.saveButton.setOnClickListener {
            persistentSaver.writeValue(getString(R.string.lockScreenMessageTitle), activityMainBinding.titleTextInput.editText?.text.toString())
            persistentSaver.writeValue(getString(R.string.lockScreenMessageContent), activityMainBinding.contentTextInput.editText?.text.toString())
            val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activityMainBinding.root.getWindowToken(), 0)
            Snackbar.make(activityMainBinding.root, getString(R.string.messageSaved), Snackbar.LENGTH_SHORT).show()
        }

        activityMainBinding.deleteButton.setOnClickListener {
            activityMainBinding.contentTextInput.editText?.text?.clear()
            activityMainBinding.titleTextInput.editText?.text?.clear()
            activityMainBinding.contentTextInput.editText?.clearFocus()
            activityMainBinding.titleTextInput.editText?.clearFocus()
            persistentSaver.removeValue(getString(R.string.lockScreenMessageTitle))
            persistentSaver.removeValue(getString(R.string.lockScreenMessageContent))
        }

        activityMainBinding.contentTextInput.editText?.addTextChangedListener(getTextWatcher { activityMainBinding.titleTextInput.editText?.text.toString().isBlank()})
        activityMainBinding.titleTextInput.editText?.addTextChangedListener(getTextWatcher { activityMainBinding.contentTextInput.editText?.text.toString().isBlank()})
    }

    fun getTextWatcher(secondCondition:()->Boolean): TextWatcher
    {
        return object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int)
            {
                activityMainBinding.saveButton.isEnabled = !(charSequence.isBlank() && secondCondition.invoke())
                activityMainBinding.deleteButton.isEnabled = !(charSequence.isBlank() && secondCondition.invoke())
            }

            override fun afterTextChanged(editable: Editable) {}
        }
    }

}