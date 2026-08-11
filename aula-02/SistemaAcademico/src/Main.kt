const val STATUS_APROVADO = "Aprovado"
const val STATUS_REPROVADO = "Reprovado"
const val STATUS_EM_EXAME = "Em Exame"

// Data class gera automaticamente toString(), equals(), hashCode() e copy()
data class Aluno(
    val id: Int,
    val nome: String,
    val nota1: Double,
    val nota2: Double,
    val frequencia: Double,
    val notaExame: Double? = null // Tipo nullable (pode ser null)
) {
    // Propriedade calculada / membro
    private val media: Double = (nota1 + nota2) / 2.0

    // Calculando média final considerando a nota do exame (se existir)
    val mediaFinal: Double =
        if (notaExame != null) {
            (media + notaExame) / 2.0
        } else {
            media
        }

    val status: String
        get() {
            return if (frequencia < 75) {
                STATUS_REPROVADO
            } else if (notaExame == null) {
                when {
                    mediaFinal >= 7.0 -> STATUS_APROVADO
                    mediaFinal >= 4.0 -> STATUS_EM_EXAME
                    else -> STATUS_REPROVADO
                }
            } else if (mediaFinal >= 6.0) {
                STATUS_APROVADO
            } else {
                STATUS_REPROVADO
            }
        }
}

class GerenciadorAcademico {
    private val listaAlunos = mutableListOf<Aluno>()
    private var proximoId = 1

    fun cadastrarAluno(nome: String, nota1: Double, nota2: Double, frequencia: Double): Aluno {
        val novoAluno = Aluno(proximoId++, nome, nota1, nota2, frequencia)
        listaAlunos.add(novoAluno)
        return novoAluno
    }

    // Método para buscar aluno (pode retornar Aluno ou null)
    private fun buscarPorId(id: Int): Aluno? {
        return listaAlunos.find { it.id == id }
    }

    fun lancarExame(idAluno: Int, notaExame: Double) {
        val alunoEncontrado = buscarPorId(idAluno)

        if (alunoEncontrado == null) {
            println("Aluno não encontrado...")
        } else if (alunoEncontrado.status == STATUS_APROVADO) {
            println("Aluno já aprovado...")
        } else {
            val alunoAtualizado = alunoEncontrado.copy(notaExame = notaExame)
            val indice = listaAlunos.indexOf(alunoEncontrado)
            listaAlunos[indice] = alunoAtualizado
            println("Nota de exame lançada para ${alunoAtualizado.nome}!")
        }
    }

    // Filtra apenas alunos aprovados usando Lambda
    fun listarAlunos() {
        if (listaAlunos.isEmpty()) {
            println("Nenhum aluno cadastrado...")
            return
        }
        println("\n--- ALUNOS CADASTRADOS (${listaAlunos.size}) ---")
        listaAlunos.forEach {
            println("- ${it.nome} (ID: ${it.id} | Média: ${it.mediaFinal} | " +
                    "Frequência: ${it.frequencia} | Status: ${it.status})")
        }
    }

    // Calcula estatísticas da turma
    fun exibirEstatisticas() {
        if (listaAlunos.isEmpty()) {
            println("Nenhum aluno cadastrado...")
            return
        }
        val mediaTurma = listaAlunos.map { it.mediaFinal }.average()
        val aprovados = listaAlunos.filter { it.status == STATUS_APROVADO }.size
        val emExame = listaAlunos.filter { it.status == STATUS_EM_EXAME }.size
        val reprovados = listaAlunos.filter { it.status == STATUS_REPROVADO }.size

        println("\n--- RESUMO DA TURMA ---")
        println("- Média Geral: ${"%.2f".format(mediaTurma)}")
        println("- Aprovados: $aprovados | Em Exame: $emExame | Reprovados: $reprovados")
    }
}

fun main() {
    val sistema = GerenciadorAcademico()

    var opcao: Int
    do {
        println("\n=== SISTEMA ACADÊMICO AFYA (KOTLIN) ===")
        println("1. Cadastrar Aluno")
        println("2. Lançar Nota de Exame")
        println("3. Listar Alunos")
        println("4. Estatísticas da Turma")
        println("5. Sair")
        print("Escolha uma opção: ")

        val entrada = readlnOrNull()?.toIntOrNull() ?: 3 // Padrão: 3 para demonstração rápida
        opcao = entrada

        when (opcao) {
            1 -> {
                println("Informe o nome do aluno:")
                val nome = readlnOrNull() ?: ""
                println("Informe a primeira nota do aluno:")
                val nota1 = readlnOrNull()?.toDoubleOrNull() ?: 0.0
                println("Informe a segunda nota do aluno:")
                val nota2 = readlnOrNull()?.toDoubleOrNull() ?: 0.0
                println("Informe a frequência do aluno (%):")
                val frequencia = readlnOrNull()?.toDoubleOrNull() ?: 0.0
                sistema.cadastrarAluno(nome, nota1, nota2, frequencia)
            }
            2 -> {
                print("ID do Aluno: ")
                val id = readlnOrNull()?.toIntOrNull() ?: 0
                print("Nota do Exame: ")
                val exame = readlnOrNull()?.toDoubleOrNull() ?: 0.0
                sistema.lancarExame(id, exame)
            }
            3 -> sistema.listarAlunos()
            4 -> sistema.exibirEstatisticas()
            5 -> println("Encerrando o programa...")
            else -> println("Opção inválida!")
        }
    } while (opcao != 5)
}