package com.practicum.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(shareAppLink: String) {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_link))
            type = context.getString(R.string.share_type)
            context.startActivity(this)
        }
    }

    override fun openLink(termsLink: String) {
        Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(context.getString(R.string.practicum_offer))
            context.startActivity(this)
        }
    }

    override fun openEmail(supportEmailData: EmailData) {
        Intent().apply {
            action = Intent.ACTION_SENDTO
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.user_email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_message))
            data = Uri.parse("mailto:")
            context.startActivity(this)
        }
    }
}