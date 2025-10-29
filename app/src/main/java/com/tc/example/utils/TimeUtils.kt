package com.tc.example.utils

/**
 * Function to return the current system time for duration tracking.
 *
 * @return [Long] current system time in milliseconds.
 */
internal fun getStartTime(): Long {
    return System.currentTimeMillis()
}

/**
 * Function to calculate elapsed time (ms) since given [startTime].
 *
 * @param startTime [Long] start time in milliseconds.
 * @return [Long] elapsed time in milliseconds.
 */
internal fun timeDiff(startTime: Long): Long {
    return System.currentTimeMillis() - startTime
}
