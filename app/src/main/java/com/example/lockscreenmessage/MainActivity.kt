package com.example.lockscreenmessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.lockscreenmessage.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.function.Predicate


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

        var serviceRunning: Boolean = false
        activityMainBinding.showSwitch.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked && !serviceRunning)
            {
                if(activityMainBinding.titleTextInput.editText?.text.toString().isBlank() && activityMainBinding.contentTextInput.editText?.text.toString().isBlank())
                {
                    MaterialAlertDialogBuilder(this)
                    .setMessage(getString(R.string.alertDialogMessage))
                        .setPositiveButton(getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
                        .show()
                    activityMainBinding.showSwitch.isChecked=false
                }
                else
                {
                    ContextCompat.startForegroundService(this, Intent(this, NotificationService::class.java))
                    serviceRunning = true
                }
            } else if (!isChecked && serviceRunning) {
                stopService(Intent(this, NotificationService::class.java))
                serviceRunning = false
            }
        }

        val persistentSaver: IPersistentSaver = PersistentSaver(getSharedPreferences("settings", Context.MODE_PRIVATE))
        persistentSaver.writeValue(getString(R.string.lockScreenMessageId), 11223344)

        activityMainBinding.saveButton.setOnClickListener {
            persistentSaver.writeValue(getString(R.string.lockScreenMessageTitle), activityMainBinding.titleTextInput.editText?.text.toString())
            persistentSaver.writeValue(getString(R.string.lockScreenMessageContent), activityMainBinding.contentTextInput.editText?.text.toString())
        }

        activityMainBinding.deleteButton.setOnClickListener {
            activityMainBinding.contentTextInput.editText?.text?.clear()
            activityMainBinding.titleTextInput.editText?.text?.clear()
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