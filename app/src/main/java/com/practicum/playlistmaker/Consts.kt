package com.practicum.playlistmaker

object Consts {
    // http url service
    const val BASE_URL = "https://itunes.apple.com/"

    // constants for Shared Preferense
    const val THEME_PREFS = "THEME"
    const val HISTORY_PREFS = "HISTORY"
    const val SW_MODE = "SWITCH_MODE"
    const val SEARCH_HISTORY = "HISTORY_TRACKS"

    const val SEARCH_TEXT = "SEARCH_TEXT"
    const val SEARCH_DEBOUNCE_DELAY = 2000L
    const val CLICK_DEBOUNCE_DELAY = 1000L

    const val MAX_TRACKS_IN_HISTORY = 10

    const val TRACK_DURATION_DELAY_TIME = 1000L
    const val TRACK_DURATION_INTRO_TIME = 30L

    const val TRACK_START_TIME = "00:00"

    const val MEDIA_VIEW_PAGER_ADAPTER_ITEM_COUNT = 2

    const val DELAY_MILLIS = 300L

}