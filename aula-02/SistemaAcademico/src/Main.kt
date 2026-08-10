const val STATUS_APROVADO = "Aprovado"
const val STATUS_REPROVADO = "Reprovado"
const val STATUS_EM_EXAME = "Em Exame"

data class Aluno(
    val id: Int = 0,
    val nome: String = "",
    val nota1: Double = 0.0,
    val nota2: Double = 0.0,
    val notaExame: Double? = null // Tipo nullable (pode ser nulo)
) {
    private val media: Double = (nota1 + nota2) / 2.0

    fun calcularMediaFinal(): Double =
        if (notaExame == null) {
            media
        } else {
            (media + notaExame) / 2.0
        }

    fun resolverStatus(): String {
        val mediaFinal = calcularMediaFinal()
        return if (notaExame == null) {
            when {
                mediaFinal >= 7 -> STATUS_APROVADO
                mediaFinal >= 4 -> STATUS_EM_EXAME
                else -> STATUS_REPROVADO
            }
        } else if (mediaFinal >= 6) {
            STATUS_APROVADO
        } else {
            STATUS_REPROVADO
        }
    }
}

fun main() {
    println("Informe o nome do aluno:")
    val nome: String = readln()
    println("Informe a primeira nota:")
    val nota1: Double = readln().toDoubleOrNull() ?: 0.0
    println("Informe a segunda nota:")
    val nota2: Double = readln().toDoubleOrNull() ?: 0.0

    val aluno1 = Aluno(1, nome, nota1, nota2)
    println("Aluno: ${aluno1.nome}")
    println("Média: ${aluno1.calcularMediaFinal()}")
    println("Status: ${aluno1.resolverStatus()}")

    if (aluno1.resolverStatus() == STATUS_EM_EXAME) {
        println("Informe a nota do exame:")
        val notaExame = readln().toDoubleOrNull() ?: 0.0
        val aluno1Atualizado = aluno1.copy(notaExame = notaExame)
        println("Média após exame: ${aluno1Atualizado.calcularMediaFinal()}")
        println("Status após exame: ${aluno1Atualizado.resolverStatus()}")
    }
}