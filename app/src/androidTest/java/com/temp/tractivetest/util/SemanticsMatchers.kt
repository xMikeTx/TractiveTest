package com.temp.tractivetest.util

import androidx.compose.ui.test.SemanticsMatcher
import com.temp.tractivetest.ui.StringId

fun hasStringId(id : String): SemanticsMatcher =
    SemanticsMatcher.expectValue(StringId, id)