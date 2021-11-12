package com.example.musichub.domain

import junit.framework.TestCase

class StringUtilFunctionsKtTest : TestCase() {

    fun testFormatAsTime() {
        assertEquals("00:00:00", "0".formatAsTime())
    }


}