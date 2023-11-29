package com.practicum.playlistmaker.data.sharing.impl

import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val app: App) : ExternalNavigator {
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
}