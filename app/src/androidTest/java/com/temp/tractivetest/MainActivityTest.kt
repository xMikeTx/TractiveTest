package com.temp.tractivetest

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.temp.tractivetest.data.CallResult
import com.temp.tractivetest.data.PetCalls
import com.temp.tractivetest.ui.SemanticsUtil
import com.temp.tractivetest.util.Util.waitUntilDoesNotExist
import com.temp.tractivetest.util.Util.waitUntilExists
import com.temp.tractivetest.util.hasStringId
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = createAndroidComposeRule<MainActivity>()

    @BindValue @MockK
    lateinit var petCall : PetCalls

    @Before
    fun init() {
        MockKAnnotations.init(this)
        hiltRule.inject()
    }

    @Test
    fun testProximityCircles(){
        //return a pet distance of 20, in that case we need to see two circles
        coEvery { petCall.getPosition() }.returns(
            CallResult.success(20)
        )
        activityRule.waitUntilExists(hasStringId(SemanticsUtil.getProximitySemantics(2)),5000)
        activityRule.onNode(hasStringId(SemanticsUtil.getProximitySemantics(1))).assertExists()
        //return a pet distance of 0, in that case we need to see 6 circles
        coEvery { petCall.getPosition() }.returns(
            CallResult.success(0)
        )
        activityRule.waitUntilExists(hasStringId(SemanticsUtil.getProximitySemantics(6)),5000)
        activityRule.onNode(hasStringId(SemanticsUtil.getProximitySemantics(1))).assertExists()
        activityRule.onNode(hasStringId(SemanticsUtil.getProximitySemantics(2))).assertExists()
        activityRule.onNode(hasStringId(SemanticsUtil.getProximitySemantics(3))).assertExists()
        activityRule.onNode(hasStringId(SemanticsUtil.getProximitySemantics(4))).assertExists()
        activityRule.onNode(hasStringId(SemanticsUtil.getProximitySemantics(5))).assertExists()

        //return a pet distance of 30, in that case we need to see no circles
        coEvery { petCall.getPosition() }.returns(
            CallResult.success(30)
        )
        activityRule.waitUntilDoesNotExist(hasStringId(SemanticsUtil.getProximitySemantics(1)),5000)
        activityRule.onNode(hasStringId(SemanticsUtil.getProximitySemantics(1))).assertDoesNotExist()
    }
}