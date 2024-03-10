package com.practicum.playlistmaker.data.sharing.impl

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val app: Application) : ExternalNavigator {
    override fun shareLink(shareAppLink: String) {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, app.getString(R.string.share_link))
            type = app.getString(R.string.share_type)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            app.startActivity(this)
        }
    }

    override fun openLink(termsLink: String) {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(app.getString(R.string.practicum_offer))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            app.startActivity(this)
        }
    }

    override fun openEmail(supportEmailData: EmailData) {
        Intent().apply {
            action = Intent.ACTION_SENDTO
            putExtra(Intent.EXTRA_EMAIL, arrayOf(app.getString(R.string.user_email)))
            putExtra(Intent.EXTRA_SUBJECT, app.getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, app.getString(R.string.support_message))
            data = Uri.parse("mailto:")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            app.startActivity(this)
        }
    }

    override fun shareText(text: String) {
        try {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                app.startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                app.applicationContext,
                app.applicationContext.getString(R.string.not_found_app),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}