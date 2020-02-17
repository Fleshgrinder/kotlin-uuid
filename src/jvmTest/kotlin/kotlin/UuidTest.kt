package kotlin

import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalStdlibApi
class UuidTest {
    @Test fun uuid3Of() {
        assertEquals("2c1e4e06-ef3a-3961-aca7-f845f66353d3", uuid3Of(UUID_NAMESPACE_DNS, "kotlinlang.org").toString())
    }
}
