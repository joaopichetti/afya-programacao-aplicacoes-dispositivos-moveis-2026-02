package br.com.afya.cadastroalunos.network

import br.com.afya.cadastroalunos.model.Aluno
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AlunoApiService {

    @GET("alunos")
    suspend fun listarAlunos(): List<Aluno>

    @GET("alunos/{id}")
    suspend fun buscarAlunoPorId(@Path("id") id: Int): Aluno

    @POST("alunos")
    suspend fun criarAluno(@Body aluno: Aluno): Aluno

    @PUT("alunos/{id}")
    suspend fun atualizarAluno(@Path("id") id: Int, @Body aluno: Aluno): Aluno

    @DELETE("alunos/{id}")
    suspend fun excluirAluno(@Path("id") id: Int)
}