package video.pal.api.models

import android.content.Context
import video.pal.R

/**
 * Import token from strings or use the provided one
 */
data class PalOptions(
    val developmentToken: String,
    val productionToken: String,
) {

    companion object {

        fun from(context: Context): PalOptions {
            val developmentKey = context.getString(R.string.pal_dev_key)
            val productionKey = context.getString(R.string.pal_prod_key)

            return PalOptions(developmentKey, productionKey)
        }
    }
}

