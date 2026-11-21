package com.example.ecoleta_estabelecimento.features.perfil

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilAtualizar
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilEstabelecimento
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenha
import com.example.ecoleta_estabelecimento.features.perfil.service.PerfilService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import com.example.ecoleta_estabelecimento.retrofit.viacep.ViaCepFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

// Dados usados pelo formulário de edição do Perfil
// Permite que o usuário digite novos valores sem alterar o perfil original antes de salvar

data class PerfilFormulario(
    val nomeEmpresa: String = "",
    val cnpj: String = "",
    val telefone: String = "",
    val email: String = "",
    val cep: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val bairro: String = "",
    val cidade: String = "",
    val nomeEstado: String = "",
    val siglaEstado: String = ""
)

class PerfilViewModel : ViewModel() {
    private val perfilService = RetrofitFactory.criarServico(PerfilService::class.java)

    // API pública ViaCEP
    private val viaCepService = ViaCepFactory.service

    // Permite cancelar uma consulta anterior ao CEP
    private var consultaCepJob: Job? = null

    // ESTADO DO PERFIL
    // Dados oficiais retornados pelo GET
    var perfil by mutableStateOf<PerfilEstabelecimento?>(null)
        private set

    // Dados temporários usados durante a edição
    var formulario by mutableStateOf(PerfilFormulario())
        private set

    // Controla se os campos permitidos estão editáveis
    var editando by mutableStateOf(false)
        private set

    // Carregamento do GET
    var carregandoPerfil by mutableStateOf(false)
        private set

    // Erro do GET
    var erroCarregamentoPerfil by mutableStateOf<String?>(null)
        private set

    // ESTADO DA ATUALIZAÇÃO
    var atualizandoPerfil by mutableStateOf(false)
        private set

    var erroAtualizacaoPerfil by mutableStateOf<String?>(null)
        private set

    var perfilAtualizado by mutableStateOf(false)
        private set

    // ESTADO DO VIACEP
    var consultandoCep by mutableStateOf(false)
        private set

    var erroCep by mutableStateOf<String?>(null)
        private set

    // Um CEP alterado só é considerado válido depois de uma consulta bem-sucedida
    var cepValidado by mutableStateOf(false)
        private set

    // ESTADO DA ALTERAÇÃO DE SENHA
    var alterandoSenha by mutableStateOf(false)
        private set

    var erroAlteracaoSenha by mutableStateOf<String?>(null)
        private set

    var senhaAlterada by mutableStateOf(false)
        private set

    // CONVERSÃO DO PERFIL PARA FORMULÁRIO
    private fun criarFormulario(
        dados: PerfilEstabelecimento
    ): PerfilFormulario {

        return PerfilFormulario(
            nomeEmpresa = dados.nomeEmpresa,
            cnpj = dados.cnpj,
            telefone = dados.telefone,
            email = dados.email,
            cep = dados.cep,
            logradouro = dados.logradouro,
            numero = dados.numero.toString(),
            bairro = dados.bairro,
            cidade = dados.nomeCidade,
            nomeEstado = dados.nomeEstado,

            // O GET retorna a propriedade "sigla".
            siglaEstado = dados.sigla
        )
    }

    // GET - BUSCAR PERFIL
    fun carregarPerfil() {
        // Evita requisições simultâneas.
        if (carregandoPerfil) return

        carregandoPerfil = true
        erroCarregamentoPerfil = null

        viewModelScope.launch {
            try {
                val resposta = perfilService.buscarPerfil()

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {

                        // response.perfil é uma lista
                        val dados = corpo.response.perfil.firstOrNull()

                        if (dados != null) {
                            // Guarda os dados oficiais
                            perfil = dados

                            // Só substitui o formulário quando não existe uma edição em andamento
                            if (!editando) {
                                formulario = criarFormulario(dados)
                            }
                        } else {
                            erroCarregamentoPerfil = "Nenhum perfil foi encontrado."
                        }
                    } else {
                        erroCarregamentoPerfil = "Não foi possível carregar o perfil."
                    }
                } else {
                    erroCarregamentoPerfil = when (resposta.code()) {
                        400 -> "Os dados do estabelecimento são inválidos."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Perfil do estabelecimento não encontrado."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao carregar o perfil. Código: ${resposta.code()}"
                    }
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                erroCarregamentoPerfil = "Não foi possível conectar ao servidor."

            } finally {
                carregandoPerfil = false
            }
        }
    }

    // INICIAR EDIÇÃO
    fun iniciarEdicao() {
        val dados = perfil ?: return

        if (atualizandoPerfil) return

        // Começa com os valores originais
        formulario = criarFormulario(dados)

        // Limpa mensagens antigas
        erroAtualizacaoPerfil = null
        perfilAtualizado = false
        erroCep = null
        consultandoCep = false
        cepValidado = true
        editando = true
    }

    // CANCELAR EDIÇÃO
    fun cancelarEdicao() {
        if (atualizandoPerfil) return

        // Cancela eventual consulta ao ViaCEP
        consultaCepJob?.cancel()
        consultaCepJob = null
        // Restaura os valores originais
        perfil?.let { dados ->
            formulario = criarFormulario(dados)
        }
        consultandoCep = false
        erroCep = null
        cepValidado = true
        erroAtualizacaoPerfil = null
        editando = false
    }

    // ALTERAÇÃO DOS CAMPOS PERMITIDOS
    fun atualizarNome(valor: String) {
        if (!editando) return

        formulario = formulario.copy(
            nomeEmpresa = valor
        )
        erroAtualizacaoPerfil = null
    }

    fun atualizarTelefone(valor: String) {
        if (!editando) return

        // O backend aceita somente dígitos
        formulario = formulario.copy(
            telefone = valor.filter { it.isDigit() }.take(25)
        )
        erroAtualizacaoPerfil = null
    }

    fun atualizarEmail(valor: String) {
        if (!editando) return

        formulario = formulario.copy(
            email = valor
        )
        erroAtualizacaoPerfil = null
    }

    fun atualizarLogradouro(valor: String) {
        if (!editando) return

        formulario = formulario.copy(
            logradouro = valor
        )
        erroAtualizacaoPerfil = null
    }

    fun atualizarNumero(valor: String) {
        if (!editando) return

        // Número permite apenas dígitos.
        formulario = formulario.copy(
            numero = valor.filter { it.isDigit() }
        )
        erroAtualizacaoPerfil = null
    }

    // MAPA DE ESTADOS
    // O ViaCEP informa a UF
        // O backend também exige o nome completo do estado na atualização
    // Este mapa funciona como alternativa caso o retorno não inclua esse nome

    private fun nomeEstadoPorSigla(
        sigla: String
    ): String {
        return mapOf(
            "AC" to "Acre",
            "AL" to "Alagoas",
            "AP" to "Amapá",
            "AM" to "Amazonas",
            "BA" to "Bahia",
            "CE" to "Ceará",
            "DF" to "Distrito Federal",
            "ES" to "Espírito Santo",
            "GO" to "Goiás",
            "MA" to "Maranhão",
            "MT" to "Mato Grosso",
            "MS" to "Mato Grosso do Sul",
            "MG" to "Minas Gerais",
            "PA" to "Pará",
            "PB" to "Paraíba",
            "PR" to "Paraná",
            "PE" to "Pernambuco",
            "PI" to "Piauí",
            "RJ" to "Rio de Janeiro",
            "RN" to "Rio Grande do Norte",
            "RS" to "Rio Grande do Sul",
            "RO" to "Rondônia",
            "RR" to "Roraima",
            "SC" to "Santa Catarina",
            "SP" to "São Paulo",
            "SE" to "Sergipe",
            "TO" to "Tocantins"
        )[sigla.uppercase()].orEmpty()
    }

    // VIA CEP - ATUALIZAR CEP
    fun atualizarCep(valor: String) {
        if (!editando || atualizandoPerfil) return

        val cepLimpo = valor
            .filter { it.isDigit() }
            .take(8)

        // Se o texto não mudou, não consulta novamente
        if (formulario.cep.filter { it.isDigit() } == cepLimpo) {
            return
        }

        // Cancela a consulta anterior, se existir
        consultaCepJob?.cancel()
        consultaCepJob = null

        // Limpa os estados da consulta
        consultandoCep = false
        erroCep = null
        erroAtualizacaoPerfil = null

        // CEP original vindo do banco
        val cepOriginal = perfil
            ?.cep
            ?.filter { it.isDigit() }
            .orEmpty()

        // Caso volte ao CEP original, restaura os dados de endereço do GET
        if (cepLimpo == cepOriginal) {
            val dados = perfil ?: return

            formulario = formulario.copy(
                cep = dados.cep,
                logradouro = dados.logradouro,
                bairro = dados.bairro,
                cidade = dados.nomeCidade,
                nomeEstado = dados.nomeEstado,
                siglaEstado = dados.sigla
            )
            cepValidado = true

            return
        }

        // Novo CEP: dados automáticos anteriores não devem ser enviados por engano
        formulario = formulario.copy(
            cep = cepLimpo,
            logradouro = "",
            bairro = "",
            cidade = "",
            nomeEstado = "",
            siglaEstado = ""
        )
        cepValidado = false

        // Ainda está digitando: não consulta
        if (cepLimpo.length != 8) {
            return
        }

        // Inicia uma nova consulta
        consultaCepJob = viewModelScope.launch {

            // Pequeno debounce para evitar chamadas desnecessárias durante a digitação
            delay(400)
            consultandoCep = true

            try {
                val resultado = viaCepService.consultarCep(cepLimpo)
                // A resposta só vale para o CEP que continua no formulário
                if (!editando || formulario.cep.filter { it.isDigit() } != cepLimpo) {
                    return@launch
                }

                // ViaCEP retorna erro = true quando o CEP não existe
                if (resultado.erro == true) {
                    erroCep = "CEP não encontrado."
                    return@launch
                }

                val cidade = resultado.localidade.orEmpty()
                val estado = resultado.uf.orEmpty().uppercase()
                val nomeEstado = resultado.estado
                    ?.takeIf { it.isNotBlank() }
                    ?: nomeEstadoPorSigla(estado)

                // Impede aceitar resposta incompleta
                if (
                    cidade.isBlank() ||
                    estado.length != 2 ||
                    nomeEstado.isBlank() ||
                    resultado.bairro.isNullOrBlank()
                ) {
                    erroCep = "Não foi possível obter o endereço completo para este CEP."
                    return@launch
                }

                // Atualiza os campos automáticos
                // O número não é modificado
                formulario = formulario.copy(
                    cep = cepLimpo,
                    logradouro = resultado.logradouro.orEmpty(),
                    bairro = resultado.bairro,
                    cidade = cidade,
                    nomeEstado = nomeEstado,
                    siglaEstado = estado
                )
                // Agora o CEP está validado
                cepValidado = true
                erroCep = null

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                if (editando) {
                    erroCep = "Não foi possível consultar o CEP. Verifique sua conexão."
                }

            } finally {
                // A consulta terminou ou foi cancelada
                consultandoCep = false
            }
        }
    }

    // PUT - ATUALIZAR PERFIL NA API
    fun atualizarPerfil(dados: PerfilAtualizar) {
        if (atualizandoPerfil) return

        atualizandoPerfil = true
        erroAtualizacaoPerfil = null
        perfilAtualizado = false

        viewModelScope.launch {
            try {
                val resposta = perfilService.atualizarPerfil(dados)

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        // Salva um retrato do formulário que foi enviado com sucesso
                        val formularioSalvo = formulario

                        // Encerra o modo edição
                        editando = false
                        perfilAtualizado = true

                        // Mantém os dados novos na interface enquanto o GET de atualização ocorre
                        formulario = formularioSalvo

                        // Atualiza o perfil oficial
                        carregarPerfil()

                    } else {
                        erroAtualizacaoPerfil =
                            corpo?.message ?: "Não foi possível atualizar o perfil."
                    }

                } else {
                    erroAtualizacaoPerfil = when (resposta.code()) {
                        400 -> "Os dados informados são inválidos."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Perfil do estabelecimento não encontrado."
                        409 -> "Conflito nos dados ou endereço informado."
                        415 -> "Formato da requisição não aceito."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao atualizar o perfil. Código: ${resposta.code()}"
                    }
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                erroAtualizacaoPerfil =
                    "Não foi possível atualizar o perfil. Verifique sua conexão."

            } finally {
                atualizandoPerfil = false
            }
        }
    }

    // PUT - VALIDAR E SALVAR EDIÇÃO
    fun salvarEdicao() {
        if (!editando || atualizandoPerfil) return

        val dados = formulario
        val numeroInteiro = dados.numero.toIntOrNull()
        erroAtualizacaoPerfil = null

        when {
            dados.nomeEmpresa.isBlank() -> {
                erroAtualizacaoPerfil = "Informe o nome da empresa."
                return
            }
            dados.telefone.isBlank() -> {
                erroAtualizacaoPerfil = "Informe o telefone."
                return
            }

            dados.email.isBlank() ||
                    !Patterns.EMAIL_ADDRESS
                        .matcher(dados.email.trim())
                        .matches() -> {
                erroAtualizacaoPerfil = "Informe um e-mail válido."
                return
            }

            dados.cep.filter { it.isDigit() }.length != 8 -> {
                erroAtualizacaoPerfil = "Informe um CEP válido com oito dígitos."
                return
            }

            consultandoCep || !cepValidado -> {
                erroAtualizacaoPerfil = "Aguarde a validação do CEP antes de salvar."
                return
            }

            dados.logradouro.isBlank() ||
                    dados.bairro.isBlank() ||
                    dados.cidade.isBlank() ||
                    dados.nomeEstado.isBlank() ||
                    dados.siglaEstado.isBlank() -> {

                erroAtualizacaoPerfil = "O endereço está incompleto."
                return
            }

            numeroInteiro == null || numeroInteiro < 1 -> {
                erroAtualizacaoPerfil = "Informe um número de endereço válido."
                return
            }
        }

        // Monta o JSON esperado pelo PUT
        val requisicao = PerfilAtualizar(
            nomeEmpresa = dados.nomeEmpresa.trim(),
            telefone = dados.telefone,
            email = dados.email.trim(),
            cep = dados.cep,
            logradouro = dados.logradouro.trim(),
            bairro = dados.bairro,
            cidade = dados.cidade,
            nomeEstado = dados.nomeEstado,
            siglaEstado = dados.siglaEstado,
            numeroLocal = numeroInteiro
        )

        atualizarPerfil(requisicao)
    }

    // Limpa as mensagens de atualização
    fun limparEstadoAtualizacaoPerfil() {

        erroAtualizacaoPerfil = null
        perfilAtualizado = false
    }

    // PATCH - ALTERAR SENHA
    fun alterarSenha(dados: PerfilSenha) {
        if (alterandoSenha) return

        alterandoSenha = true
        erroAlteracaoSenha = null
        senhaAlterada = false

        viewModelScope.launch {
            try {
                val resposta = perfilService.alterarSenha(dados)

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        senhaAlterada = true

                    } else {
                        erroAlteracaoSenha = corpo?.message?: "Não foi possível alterar a senha."
                    }

                } else {
                    erroAlteracaoSenha = when (resposta.code()) {
                        400 -> "Os dados informados são inválidos ou as senhas não coincidem."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Estabelecimento não encontrado."
                        409 -> "Senha atual incorreta ou nova senha igual à atual."
                        415 -> "Formato da requisição não aceito."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao alterar a senha. Código: ${resposta.code()}"
                    }
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                erroAlteracaoSenha = "Não foi possível alterar a senha. Verifique sua conexão."

            } finally {
                alterandoSenha = false
            }
        }
    }

    // Limpa os estados anteriores do popup
    fun limparEstadoAlteracaoSenha() {

        erroAlteracaoSenha = null
        senhaAlterada = false
    }
}