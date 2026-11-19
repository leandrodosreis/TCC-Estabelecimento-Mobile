package com.example.ecoleta_estabelecimento.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import org.json.JSONObject
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/* Gerencia a sessão do estabelecimento
 *
 * Responsabilidades:
 *
 * 1. Criar ou recuperar uma chave protegida pelo Android Keystore;
 * 2. Criptografar o JWT recebido pela API;
 * 3. Salvar o JWT criptografado no armazenamento privado do app;
 * 4. Recuperar e descriptografar o JWT;
 * 5. Verificar a expiração informada no campo "exp";
 * 6. Disponibilizar o token em memória por meio do TokenSessao;
 * 7. Remover o token no logout ou quando ele expirar;
 *
 * O Context é recebido pelo construtor para permitir o acesso ao armazenamento privado do aplicativo
 */
class SessionManager(context: Context) {
    // Agrupa consts que pertencem à classe, e não a uma instância específica dela
    companion object {
        // Nome do arquivo de SharedPreferences utilizado pela sessão
        private const val PREFS_NAME = "ecoleta_sessao"

        // Nome da entrada onde o JWT criptografado será armazenado
        private const val TOKEN_KEY = "jwt_criptografado"

        // Identificador da chave AES dentro do Android Keystore, não contém os bytes da chave criptográfica
        private const val KEY_ALIAS = "ecoleta_chave_jwt"

        // AES: algoritmo de criptografia
        // GCM: modo que também verifica a integridade dos dados
        // NoPadding: não utiliza preenchimento adicional
        private const val TRANSFORMACAO = "AES/GCM/NoPadding"
    }

    // =======================================
    // 1. Armazenamento privado do aplicativo
    // =======================================

    /*
    * Obtém o SharedPreferences usado para armazenar o JWT
    *
    * applicationContext:
    * Utiliza o contexto do aplicativo, evitando guardar uma referência desnecessária a uma Activity
    *
    * getSharedPreferences:
    * Abre o arquivo de preferências ou cria um se não existir
    *
    * PREFS_NAME:
    * Define o nome do arquivo
    *
    * Context.MODE_PRIVATE:
    * Mantém esse arquivo no armazenamento privado do aplicativo
    *
    * Importante:
    * SharedPreferences não criptografa os dados automaticamente
    * Por isso, é feito a criptografia do JWT antes de salvá-lo
    */
    private val preferences = context
        .applicationContext
        .getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    // =======================================
    // 2. Criar ou recuperar a chave AES
    // =======================================

    // Retorna a chave AES protegida pelo Android Keystore
    // SecretKey é o tipo do objeto retornado

    private fun obterChave(): SecretKey {

        // Solicita uma instância do armazenamento de chaves do próprio Android
        val keyStore = KeyStore.getInstance(
            "AndroidKeyStore"
        )
        // Carrega o conteúdo do Keystore
        // null indica que não precisa fornecer um arquivo ou senha para carregá-lo
        keyStore.load(null)

        // Procura uma chave já existente com o nome KEY_ALIAS

        val chaveExistente = keyStore.getKey(
            KEY_ALIAS,
            null

            // "as? SecretKey" realiza uma conversão segura
        ) as? SecretKey
        // Caso o objeto não seja uma SecretKey, o resultado será null

        // Se a chave já existir, será reutilizada
        // Importante para descriptografar um JWT salvo em uma execução anterior
        if (chaveExistente != null) {
            return chaveExistente
        }

        // Cria um gerador de chaves AES
        // "AndroidKeyStore" indica que a chave será gerenciada pelo sistema Android, não armazenada no código
        val gerador = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        // Define as características da nova chave
        val configuracao = KeyGenParameterSpec.Builder(
            // Identificador utilizado para localizar a chave
            KEY_ALIAS,

            // A chave poderá ser usada tanto para criptografar quanto para descriptografar
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )

            // Utiliza o modo GCM
            // Esse modo protege a confidencialidade e detecta alterações nos dados criptografados
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            // Não utiliza padding adicional
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            // Define uma chave AES de 256 bits
            .setKeySize(256)
            // Finaliza a configuração da chave
            .build()

        // Entrega a configuração ao gerador
        gerador.init(configuracao)

        // Gera a chave e devolve sua referência, o material secreto permanece protegido pelo Keystore
        return gerador.generateKey()
    }

    // ======================
    // 3. Criptografar o JWT
    // ======================

    /*
    * Recebe um JWT em texto e devolve sua representação criptografada em Base64
    *
    * Exemplo conceitual:
    *
    * JWT original: eyJhbGciOiJIUzI1NiIs...
    * Resultado: uma sequência Base64 contendo bytes criptografados
    *
    * O Base64 não é a proteção de segurança
    * A proteção é realizada pela criptografia AES-GCM
    */
    private fun criptografar(token: String): String {
        // Cria o objeto que executará a criptografia
        // Ele será configurado com AES/GCM/NoPadding
        val cipher = Cipher.getInstance(TRANSFORMACAO)

        // Inicializa o Cipher no modo de criptografia usando a chave protegida pelo Android Keystore
        // O sistema também gera um IV aleatório para essa operação
        cipher.init(
            Cipher.ENCRYPT_MODE,
            obterChave()
        )

        // Converte o JWT de String para bytes UTF-8
        // Em seguida, doFinal criptografa esses bytes e devolve o conteúdo criptografado com a tag GCM
        val conteudoCriptografado = cipher.doFinal(
            token.toByteArray(Charsets.UTF_8)
        )

        // O IV é necessário para descriptografar depois
            // Ele não é secreto, mas precisa ser preservado
            // Junta: [IV] + [CONTEÚDO CRIPTOGRAFADO E TAG GCM]
        // Cada nova criptografia utiliza seu próprio IV
        val resultado = cipher.iv + conteudoCriptografado

        // Converte o conjunto de bytes para Base64
        // SharedPreferences armazena Strings, então essa conversão facilita a persistência
        // NO_WRAP evita quebras de linha no resultado.
        return Base64.encodeToString(
            resultado,
            Base64.NO_WRAP
        )
    }

    // =========================
    // 4. Descriptografar o JWT
    // =========================

    /*
    * Recebe a String Base64 salva no celular e devolve o JWT original
    *
    * A descriptografia somente terá sucesso se:
    *
    * - A chave correta estiver disponível
    * - O IV utilizado for o correto
    *  - O conteúdo criptografado não tiver sido alterado
    */
    private fun descriptografar(valor: String): String {
        // Converte a String Base64 de volta para bytes
        val bytes = Base64.decode(
            valor,
            Base64.NO_WRAP
        )

        // Esse formato possui:
        //
        // 12 bytes de IV
        // 16 bytes de tag de autenticação GCM
        // e os bytes do conteúdo criptografado
        //
        // Verifica se pelo menos o IV e a tag existem
        require(bytes.size >= 12 + 16) {
            "Conteúdo criptografado inválido"
        }

        // Extrai os 12 primeiros bytes, que representam o IV
        // O índice inicial é incluído, enquanto o índice final não é incluído
        val iv = bytes.copyOfRange(
            0,
            12
        )

        // Extrai todos os bytes que vêm depois do IV
        // Inclui o conteúdo criptografado e a tag GCM
        val conteudo = bytes.copyOfRange(
            12,
            bytes.size
        )

        // Cria o Cipher com o mesmo algoritmo usado durante a criptografia
        val cipher = Cipher.getInstance(TRANSFORMACAO)

        // Configura o Cipher para descriptografar
        cipher.init(
            // Indica que agora vai descriptografar
            Cipher.DECRYPT_MODE,
            // Recupera a chave do Android Keystore
            obterChave(),
            // Configura a tag GCM de 128 bits e fornece o IV salvo anteriormente
            GCMParameterSpec(
                128,
                iv
            )
        )

        // doFinal tenta recuperar os bytes originais
        // Se o conteúdo tiver sido adulterado ou a chave estiver incorreta, ocorrerá uma exceção
        // String(..., UTF_8) transforma os bytes descriptografados no JWT original
        return String(
            cipher.doFinal(conteudo),
            Charsets.UTF_8
        )
    }

    // ==========================
    // 5. Ler a expiração do JWT
    // ==========================

    /*
     * Obtém o campo "exp" do JWT
     *
     * Um JWT geralmente possui três partes:
     *
     * HEADER.PAYLOAD.SIGNATURE
     * O payload contém informações como o instante de expiração definido pelo backend
     *
     * O campo exp usa segundos Unix: quantidade de segundos desde 01/01/1970 UTC
     *
     * Esse valor representa um instante absoluto
     * Portanto, funciona independentemente do fuso configurado no celular
     *
     * Retorno:
     * - Long: quando a expiração é encontrada
     * - null: quando o JWT não pode ser interpretado
     */
    fun obterExpiracao(token: String): Long? {
        // runCatching executa o bloco e captura exceções
        // Assim, um token malformado não provoca uma falha inesperada nessa leitura
        return runCatching {
            val partes = token.split(".")

            require(partes.size == 3)
            // partes[0] = Header
            // partes[1] = Payload
            // partes[2] = Signature

            // Decodifica a segunda parte do JWT
            val payload = String(
                Base64.decode(
                    // Segunda parte: payload
                    partes[1],
                    // JWT utiliza Base64 URL-safe
                        // URL_SAFE aceita caracteres usados nessa variante do Base64
                            // NO_WRAP lida com o formato sem quebras de linha
                    Base64.URL_SAFE or Base64.NO_WRAP
                ),
                // Interpreta os bytes como texto UTF-8
                Charsets.UTF_8
            )

            // Converte o payload em um objeto JSON e lê o campo "exp" como número Long
            JSONObject(payload).getLong("exp")

            // getOrNull retorna o valor em caso de sucesso ou null se alguma exceção foi capturada
        }.getOrNull()
    }

    // ===================================
    // 6. Verifica se o JWT está expirado
    // ===================================

    /*
     * Confere se o token ainda está dentro do período de validade
     *
     * O backend define a expiração para 00:00 no horário America/Sao_Paulo
     *
     * O aplicativo não precisa calcular esse horário:
     * ele utiliza o instante já informado no campo exp
     *
     * Importante:
     * Esta função verifica somente a expiração declarada
     * Ela NÃO verifica a assinatura criptográfica do JWT
     * Essa validação permanece sob responsabilidade da API
     */
    fun tokenEstaValido(token: String): Boolean {
        // Tenta obter a expiração do JWT
        val expiracao = obterExpiracao(token)
            ?: return false

        // Obtém o horário atual do aparelho em milissegundos Unix
        // Divide por 1000 porque o campo exp utiliza segundos, não milissegundos
        val agora = System.currentTimeMillis() / 1000

        // Retorna true somente se a expiração estiver no futuro
        // Se expiracao == agora, já está expirado
        return expiracao > agora
    }

    // ===================
    // 7. Limpar a sessão
    // ===================

    /*
     * Remove o JWT da memória e do armazenamento local
     *
     * Será utilizada:
     * - Quando o usuário clicar em Sair
     * - Quando o JWT expirar
     * - Quando uma sessão salva não puder ser recuperada
     * - Quando a API rejeitar um token de sessão inválido
     *
     * @Synchronized evita a execução simultânea desta função
     *  por threads que utilizem a mesma instância de SessionManager
     *
     * Retorna:
     * true  -> sessão removida do armazenamento
     * false -> não foi possível confirmar a remoção
     */
    @Synchronized
    fun limparSessao(): Boolean {
        // Remove o JWT criptografado do SharedPreferences
        val removido = preferences.edit()
            .remove(TOKEN_KEY)
            .commit()

        // Se a remoção foi concluída, apagamos também
        // o token que o Retrofit consulta em memória.
        if (removido) {
            TokenSessao.limparToken()
        }

        // Informa ao chamador se foi possível concluir o encerramento da sessão
        return removido
    }

    // =============================
    // 8. Salvar o JWT após o login
    // =============================

    /*
     * Salva o JWT recebido da API
     *
     * Retorna:
     * - true: token válido e persistido
     * - false: token inválido ou falha ao salvar
     *
     * @Synchronized evita chamadas concorrentes nesta mesma instância
     */
    @Synchronized
    fun salvarToken(token: String): Boolean {
        // Antes de salvar, verifica se o JWT possui um exp futuro
        if (!tokenEstaValido(token)) {
            return false
        }

        // try permite tratar possíveis falhas, como problemas no Keystore ou armazenamento
        return try {
            // Criptografa o JWT antes de persisti-lo
            val valorCriptografado = criptografar(token)

            // Abre a edição do SharedPreferences
            val salvo = preferences.edit()
                // Armazena o token criptografado utilizando a chave de preferência TOKEN_KEY
                .putString(
                    TOKEN_KEY,
                    valorCriptografado
                )
                // Grava os dados e retorna true ou false
                .commit()

            // Somente disponibilizamos o token em memória depois de confirmar que foi salvo
            if (salvo) {
                TokenSessao.salvarToken(token)
            }

            // Devolve o resultado da gravação
            salvo
        } catch (e: Exception) {
            // Se houver falha durante a criptografia ou gravação, informamos que não foi salvo
            false
        }
    }

    // =========================================
    // 9. Recuperar o JWT ao abrir o aplicativo
    // =========================================

    /*
     * Recupera uma sessão armazenada anteriormente
     *
     * Etapas:
     * 1. Procura o token criptografado
     * 2. Tenta descriptografá-lo
     * 3. Verifica o campo exp
     * 4. Se estiver válido, coloca-o no TokenSessao
     * 5. Caso contrário, limpa a sessão
     *
     * Retorna:
     * - JWT original, se ainda estiver utilizável
     * - null, se não existir ou estiver expirado/inválido
     */
    @Synchronized
    fun recuperarToken(): String? {
        // Busca a String criptografada no armazenamento
            // Se não houver nada salvo, o retorno será null
        val valorSalvo = preferences.getString(
            TOKEN_KEY,
            null
        )

        // Se o armazenamento não possui um token, também é removido qualquer token em memória
        if (valorSalvo == null) {
            TokenSessao.limparToken()
            return null
        }

        // Tenta recuperar o JWT original
        val token = try {
            // Descriptografa o valor armazenado
            descriptografar(valorSalvo)

        } catch (e: Exception) {
            // Uma falha pode indicar:
            // - Conteúdo armazenado corrompido
            // - Chave do Keystore indisponível
            // - Dados restaurados sem a chave original
            // Nesses casos, a sessão não pode continuar
            limparSessao()

            return null
        }

        // Se o JWT estiver expirado ou sem exp válido, a sessão é removida.
        if (!tokenEstaValido(token)) {
            limparSessao()
            return null
        }

        // O JWT foi recuperado e ainda não expirou
        // O token é disponibilizado para o interceptor utilizado pela RetrofitFactory
        TokenSessao.salvarToken(token)

        // Devolve o token recuperado
        return token
    }
}