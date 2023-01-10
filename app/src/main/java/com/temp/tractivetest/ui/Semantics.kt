package com.temp.tractivetest.ui

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

/**
 * StringId semantics that indicates item is tied to a String Id. used in testing
 */
val StringId = SemanticsPropertyKey<String?>("StringId")
var SemanticsPropertyReceiver.StringId by StringId

/**
 * Utility object to build semantics values
 */
object SemanticsUtil {
    /**
     * Semantic for proximity circle
     * @param num number of proximity circle
     */
    fun getProximitySemantics(num : Int) = "Proximity$num"
}