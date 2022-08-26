package com.example.lockscreenmessage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

        val persistentSaver: IPersistentSaver = PersistentSaver(getSharedPreferences("settings", Context.MODE_PRIVATE))
        persistentSaver.writeValue(getString(R.string.lock_screen_message_id), 11223344)

        val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        //Set toolbar icon action
        activityMainBinding.topAppBar.menu.findItem(R.id.about).setOnMenuItemClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }

        //Set switch action and default state
        activityMainBinding.showSwitch.setOnCheckedChangeListener { view, isChecked ->
            if (!isChecked && persistentSaver.readValue(getString(R.string.lock_screen_message_enabled), false)) {
                stopService(Intent(this, NotificationService::class.java))
                persistentSaver.writeValue(getString(R.string.lock_screen_message_enabled), false)
            }
            else if (isChecked && !persistentSaver.readValue(getString(R.string.lock_screen_message_enabled), false))
            {
                if(persistentSaver.readValue(this.getString(com.example.lockscreenmessage.R.string.lock_screen_message_title),null).isNullOrBlank() && persistentSaver.readValue(this.getString(com.example.lockscreenmessage.R.string.lock_screen_message_content), null).isNullOrBlank())
                {
                    MaterialAlertDialogBuilder(this)
                    .setMessage(getString(R.string.alert_dialog_message))
                        .setPositiveButton(getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
                        .show()
                    activityMainBinding.showSwitch.isChecked=false
                    persistentSaver.writeValue(getString(R.string.lock_screen_message_enabled), false)
                }
                else
                {
                    ContextCompat.startForegroundService(this, Intent(this, NotificationService::class.java))
                    inputMethodManager.hideSoftInputFromWindow(activityMainBinding.root.getWindowToken(), 0)
                    Snackbar.make(activityMainBinding.root, getString(R.string.switch_hint), Snackbar.LENGTH_SHORT).show()
                    persistentSaver.writeValue(getString(R.string.lock_screen_message_enabled), true)
                }
            }
        }
        if(persistentSaver.readValue(getString(R.string.lock_screen_message_enabled), false)) { activityMainBinding.showSwitch.isChecked=true }

        //Set save button action
        activityMainBinding.saveButton.setOnClickListener {
            persistentSaver.writeValue(getString(R.string.lock_screen_message_title), activityMainBinding.titleTextInput.editText?.text.toString())
            persistentSaver.writeValue(getString(R.string.lock_screen_message_content), activityMainBinding.contentTextInput.editText?.text.toString())
            inputMethodManager.hideSoftInputFromWindow(activityMainBinding.root.getWindowToken(), 0)
            Snackbar.make(activityMainBinding.root, getString(R.string.message_saved), Snackbar.LENGTH_SHORT).show()
            it.isEnabled=false
        }

        //Set delete button action
        activityMainBinding.deleteButton.setOnClickListener {
            activityMainBinding.contentTextInput.editText?.text?.clear()
            activityMainBinding.titleTextInput.editText?.text?.clear()
            activityMainBinding.contentTextInput.editText?.clearFocus()
            activityMainBinding.titleTextInput.editText?.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(activityMainBinding.root.getWindowToken(), 0)
            activityMainBinding.showSwitch.isChecked=false
            persistentSaver.removeValue(getString(R.string.lock_screen_message_title))
            persistentSaver.removeValue(getString(R.string.lock_screen_message_content))
        }

        //Set edit text watchers and default state
        activityMainBinding.contentTextInput.editText?.addTextChangedListener(getTextWatcher(activityMainBinding.titleTextInput.editText, persistentSaver, com.example.lockscreenmessage.R.string.lock_screen_message_content, com.example.lockscreenmessage.R.string.lock_screen_message_title))
        activityMainBinding.titleTextInput.editText?.addTextChangedListener(getTextWatcher(activityMainBinding.contentTextInput.editText, persistentSaver, com.example.lockscreenmessage.R.string.lock_screen_message_title, com.example.lockscreenmessage.R.string.lock_screen_message_content))

        val currentTitle: String? = persistentSaver.readValue(getString(R.string.lock_screen_message_title), null)
        if(!(currentTitle.isNullOrBlank())) activityMainBinding.titleTextInput.editText?.setText(currentTitle)
        val currentContent: String? = persistentSaver.readValue(getString(R.string.lock_screen_message_content), null)
        if(!(currentContent.isNullOrBlank())) activityMainBinding.contentTextInput.editText?.setText(currentContent)


        //causes error: BLASTBufferItemConsumer::onDisconnect()
        activityMainBinding.contentTextInput.editText?.filters= listOf<InputFilter>(object: InputFilter{
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                return source?.replace(Regex("\n"), " ")
            }
        },
            InputFilter.LengthFilter(240)).toTypedArray()
    }

    fun getTextWatcher(secondEditText: EditText?, persistentSaver: IPersistentSaver, persistentFirstKey:Int, persistentSecondKey:Int): TextWatcher
    {
        return object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int)
            {
                activityMainBinding.saveButton.isEnabled = !(charSequence.isBlank() || charSequence.toString() == persistentSaver.readValue(getString(persistentFirstKey),null)) || !(secondEditText?.text.toString().isBlank() || secondEditText?.text.toString() == persistentSaver.readValue(getString(persistentSecondKey),null))
                activityMainBinding.deleteButton.isEnabled = !(charSequence.isBlank() && secondEditText?.text.toString().isBlank())
            }

            override fun afterTextChanged(editable: Editable) {}
        }
    }
}