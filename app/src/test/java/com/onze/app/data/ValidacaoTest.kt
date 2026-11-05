package com.onze.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacaoTest {

    @Test
    fun emailValido_aceitaFormatosComuns() {
        assertTrue(emailValido("marina@email.com"))
        assertTrue(emailValido("  marina.silva+onze@mail.com.br "))
    }

    @Test
    fun emailValido_recusaFormatosInvalidos() {
        assertFalse(emailValido(""))
        assertFalse(emailValido("marina"))
        assertFalse(emailValido("marina@"))
        assertFalse(emailValido("marina@email"))
        assertFalse(emailValido("@email.com"))
    }

    @Test
    fun sha256_geraHashConhecido() {
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            "abc".sha256()
        )
    }

    @Test
    fun sha256_naoGuardaASenhaEmTextoPuro() {
        val senha = "123456"
        assertNotEquals(senha, senha.sha256())
        assertEquals(64, senha.sha256().length)
    }
}
