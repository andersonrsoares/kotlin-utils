package br.com.andersonsoares.utils

import android.util.Patterns
import java.text.Normalizer
import java.util.*

private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"

private val ALLOWED_CHARACTERS_NUMBERS = "0123456789"

fun String.getRandomString(sizeOfRandomString: Int = 20): String {
    val random = Random()
    val sb = StringBuilder(sizeOfRandomString)
    for (i in 0 until sizeOfRandomString)
        sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
    return sb.toString()
}

fun String.getRandomNumber(sizeOfRandomString: Int = 20): String {
    val random = Random()
    val sb = StringBuilder(sizeOfRandomString)
    for (i in 0 until sizeOfRandomString)
        sb.append(ALLOWED_CHARACTERS_NUMBERS[random.nextInt(ALLOWED_CHARACTERS_NUMBERS.length)])
    return sb.toString()
}

fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


private fun randomiza(n: Int): Int {
    return (Math.random() * n).toInt()
}

private fun mod(dividendo: Int, divisor: Int): Int {
    return Math.round(dividendo - Math.floor((dividendo / divisor).toDouble()) * divisor).toInt()
}

//val gerador = GeraCpfCnpj()
//val cpf = gerador.cpf()
//System.out.printf("CPF: %s, Valido: %s\n", cpf, gerador.isCPF(cpf))
//
//val cnpj = gerador.cnpj()
//System.out.printf("CNPJ: %s, Valido: %s\n", cnpj, gerador.isCNPJ(cnpj))

fun String.geraCPF(comPontos:Boolean = true): String {
    val n = 9
    val n1 = randomiza(n)
    val n2 = randomiza(n)
    val n3 = randomiza(n)
    val n4 = randomiza(n)
    val n5 = randomiza(n)
    val n6 = randomiza(n)
    val n7 = randomiza(n)
    val n8 = randomiza(n)
    val n9 = randomiza(n)
    var d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10

    d1 = 11 - mod(d1, 11)

    if (d1 >= 10)
        d1 = 0

    var d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11

    d2 = 11 - mod(d2, 11)

    var retorno: String? = null

    if (d2 >= 10)
        d2 = 0
    retorno = ""

    if (comPontos)
        retorno = "" + n1 + n2 + n3 + '.'.toString() + n4 + n5 + n6 + '.'.toString() + n7 + n8 + n9 + '-'.toString() + d1 + d2
    else
        retorno = "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + d1 + d2

    return retorno
}

fun String.geraCNPJ(comPontos:Boolean = true): String {
    val n = 9
    val n1 = randomiza(n)
    val n2 = randomiza(n)
    val n3 = randomiza(n)
    val n4 = randomiza(n)
    val n5 = randomiza(n)
    val n6 = randomiza(n)
    val n7 = randomiza(n)
    val n8 = randomiza(n)
    val n9 = 0 //randomiza(n);
    val n10 = 0 //randomiza(n);
    val n11 = 0 //randomiza(n);
    val n12 = 1 //randomiza(n);
    var d1 = n12 * 2 + n11 * 3 + n10 * 4 + n9 * 5 + n8 * 6 + n7 * 7 + n6 * 8 + n5 * 9 + n4 * 2 + n3 * 3 + n2 * 4 + n1 * 5

    d1 = 11 - mod(d1, 11)

    if (d1 >= 10)
        d1 = 0

    var d2 = d1 * 2 + n12 * 3 + n11 * 4 + n10 * 5 + n9 * 6 + n8 * 7 + n7 * 8 + n6 * 9 + n5 * 2 + n4 * 3 + n3 * 4 + n2 * 5 + n1 * 6

    d2 = 11 - mod(d2, 11)

    if (d2 >= 10)
        d2 = 0

    var retorno: String? = null

    if (comPontos)
        retorno = "$n1$n2.$n3$n4$n5.$n6$n7$n8/$n9$n10$n11$n12-$d1$d2"
    else
        retorno = "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + n10 + n11 + n12 + d1 + d2

    return retorno
}



fun String.isCPF(): Boolean {
    var CPF = this

    CPF = CPF.removeCaracteresEspeciais()

    // considera-se erro CPF's formados por uma sequencia de numeros iguais
    if (CPF == "00000000000" || CPF == "11111111111" || CPF == "22222222222" || CPF == "33333333333" || CPF == "44444444444" || CPF == "55555555555" || CPF == "66666666666" || CPF == "77777777777" || CPF == "88888888888" || CPF == "99999999999" || CPF.length != 11)
        return false

    val dig10: Char
    val dig11: Char
    var sm: Int
    var i: Int
    var r: Int
    var num: Int
    var peso: Int

    // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
    try {
        // Calculo do 1o. Digito Verificador
        sm = 0
        peso = 10
        i = 0
        while (i < 9) {
            // converte o i-esimo caractere do CPF em um numero:
            // por exemplo, transforma o caractere '0' no inteiro 0
            // (48 eh a posicao de '0' na tabela ASCII)
            num = CPF[i].toInt() - 48
            sm = sm + num * peso
            peso = peso - 1
            i++
        }

        r = 11 - sm % 11
        if (r == 10 || r == 11)
            dig10 = '0'
        else
            dig10 = (r + 48).toChar() // converte no respectivo caractere numerico

        // Calculo do 2o. Digito Verificador
        sm = 0
        peso = 11
        i = 0
        while (i < 10) {
            num = CPF[i].toInt() - 48
            sm = sm + num * peso
            peso = peso - 1
            i++
        }

        r = 11 - sm % 11
        if (r == 10 || r == 11)
            dig11 = '0'
        else
            dig11 = (r + 48).toChar()

        // Verifica se os digitos calculados conferem com os digitos informados.
        return if (dig10 == CPF[9] && dig11 == CPF[10])
            true
        else
            false
    } catch (erro: InputMismatchException) {
        return false
    }

}

fun String.isCNPJ(): Boolean {
    var CNPJ = this

    CNPJ = CNPJ.removeCaracteresEspeciais()

    // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
    if (CNPJ == "00000000000000" || CNPJ == "11111111111111" || CNPJ == "22222222222222" || CNPJ == "33333333333333" || CNPJ == "44444444444444" || CNPJ == "55555555555555" || CNPJ == "66666666666666" || CNPJ == "77777777777777" || CNPJ == "88888888888888" || CNPJ == "99999999999999" || CNPJ.length != 14)
        return false

    val dig13: Char
    val dig14: Char
    var sm: Int
    var i: Int
    var r: Int
    var num: Int
    var peso: Int

    // "try" - protege o código para eventuais erros de conversao de tipo (int)
    try {
        // Calculo do 1o. Digito Verificador
        sm = 0
        peso = 2
        i = 11
        while (i >= 0) {
            // converte o i-ésimo caractere do CNPJ em um número:
            // por exemplo, transforma o caractere '0' no inteiro 0
            // (48 eh a posição de '0' na tabela ASCII)
            num = CNPJ[i].toInt() - 48
            sm = sm + num * peso
            peso = peso + 1
            if (peso == 10)
                peso = 2
            i--
        }

        r = sm % 11
        if (r == 0 || r == 1)
            dig13 = '0'
        else
            dig13 = (11 - r + 48).toChar()

        // Calculo do 2o. Digito Verificador
        sm = 0
        peso = 2
        i = 12
        while (i >= 0) {
            num = CNPJ[i].toInt() - 48
            sm = sm + num * peso
            peso = peso + 1
            if (peso == 10)
                peso = 2
            i--
        }

        r = sm % 11
        if (r == 0 || r == 1)
            dig14 = '0'
        else
            dig14 = (11 - r + 48).toChar()

        // Verifica se os dígitos calculados conferem com os dígitos informados.
        return if (dig13 == CNPJ[12] && dig14 == CNPJ[13])
            true
        else
            false
    } catch (erro: InputMismatchException) {
        return false
    }

}

fun String.removeCaracteresEspeciais(): String {
  return Normalizer.normalize(this, Normalizer.Form.NFD).replace("[^a-zA-Z]", "")
}

//fun imprimeCNPJ(CNPJ: String): String {
//    // máscara do CNPJ: 99.999.999.9999-99
//    return CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "." + CNPJ.substring(5, 8) + "." + CNPJ.substring(8, 12) + "-" + CNPJ.substring(12, 14)
//}