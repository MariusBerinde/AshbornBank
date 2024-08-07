package com.example.ashborn

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.kotlin.verify

class Dependency1(val value1: Int?)
class Dependency2(val value2: String)


class SystemUnderTest(
    val dependency1: Dependency1,
    val dependency2: Dependency2
){

    fun calculate() = (dependency1.value1 ?: 0)+dependency2.value2.toInt()


}
class DemoMocck {
    @Test
    fun calculateAddsValues() {
        val doc1 = mockk<Dependency1>()
        val doc2 = mockk<Dependency2>()

        every { doc1.value1 } returns 5
        every { doc2.value2 } returns "6"

        val sut = SystemUnderTest(doc1, doc2)

        assertEquals(11, sut.calculate())

        verify {
            doc1.value1
            doc2.value2

        }

        val doc3 = mockk<Dependency1>()
        val doc4 = mockk<Dependency2>()

        every { doc3.value1 } returns null
        every { doc4.value2 } returns "6"

        val sut1 = SystemUnderTest(doc3, doc4)

        assertEquals(6, sut1.calculate())
    }
}
