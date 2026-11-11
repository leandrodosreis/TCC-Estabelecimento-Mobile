package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroRequest
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoCancelar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoConfirmar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoDescricao
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoTexto
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoDropdownSelecionado
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoDropdownTextoSelecionado
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogNovoResiduo(
    viewModel: ResiduosViewModel,
    onDismiss: () -> Unit
) {

    // Limpa o resultado anterior ao abrir o formulário
    // Também limpa ao sair da composição, para evitar estados antigos
    DisposableEffect(viewModel) {
        viewModel.limparEstadoCadastroResiduo()

        onDispose {
            viewModel.limparEstadoCadastroResiduo()
        }
    }

    // Guarda o ID do material escolhido, o backend receberá esse ID, não o nome
    var idTipoMaterial by remember {
        mutableStateOf<Int?>(null)
    }

    // Controla se a lista de materiais está aberta
    var menuMaterialExpandido by remember {
        mutableStateOf(false)
    }

    // Busca o nome correspondente ao ID selecionado, esse nome será exibido no campo
    val nomeMaterialSelecionado = viewModel.tiposMateriais
        .firstOrNull { it.idTipoMaterial == idTipoMaterial }
        ?.material ?: ""

    // Guarda a largura real do campo de material em pixels.
    var larguraCampoMaterial by remember {
        mutableStateOf(0)
    }

    // Permite converter pixels para dp.
    val density = LocalDensity.current

    // Define a largura do dropdown igual à largura do campo.
    val larguraDropdownMaterial = with(density) {
        larguraCampoMaterial.toDp()
    }

    var quantidade by remember { mutableStateOf("") }

    // Guarda a data digitada e a posição do cursor, permitindo aplicar a máscara sem perder o controle da edição
    var dataDisponivel by remember {
        mutableStateOf(TextFieldValue(""))
    }

    // Controla se o calendário está aberto
    var selecionarData by remember { mutableStateOf(false) }

    // Guarda os horários selecionados pelo relógio.
    var horarioInicial by remember { mutableStateOf("") }
    var horarioFinal by remember { mutableStateOf("") }

    // Controla qual seletor está aberto.
    var selecionarHorarioInicial by remember { mutableStateOf(false) }
    var selecionarHorarioFinal by remember { mutableStateOf(false) }

    var observacao by remember { mutableStateOf("") }

    // Fecha o formulário somente após o backend confirmar o cadastro
    LaunchedEffect(viewModel.cadastroResiduoRealizado) {
        if (viewModel.cadastroResiduoRealizado) {
            onDismiss()
        }
    }


    // =========================================
    // VALIDAÇÕES PARA DISPONIBILIZAR O RESÍDUO
    // =========================================

    // Indica se o usuário já tentou enviar o formulário.
    var tentouDisponibilizar by remember {
        mutableStateOf(false)
    }

    // Converte a data preenchida para LocalDate, caso seja válida
    val dataConvertida = if (MaskResiduo.dataValida(dataDisponivel.text)) {
        LocalDate.parse(
            dataDisponivel.text,
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
        )
    } else {
        null
    }

    // Verifica os dados do formulário
    val materialValido = idTipoMaterial != null

    val quantidadeValida = quantidade.replace(',', '.')
        .toBigDecimalOrNull()
        ?.let { it > BigDecimal.ZERO && it <= BigDecimal("9999.99") }
        ?: false

    val dataValida =
        dataConvertida != null && !dataConvertida.isBefore(LocalDate.now(ZoneId.of("America/Sao_Paulo")))

    val horarioInicialValido = MaskResiduo.horarioValido(horarioInicial)
    val horarioFinalValido = MaskResiduo.horarioValido(horarioFinal)

    val intervaloValido = MaskResiduo.intervaloHorarioValido(
        horarioInicial,
        horarioFinal
    )

    // Mensagens de erro, exibidas somente após a tentativa de envio
    val erroMaterial = tentouDisponibilizar && !materialValido
    val erroQuantidade = tentouDisponibilizar && !quantidadeValida
    val erroData = tentouDisponibilizar && !dataValida
    val erroHorarioInicial = tentouDisponibilizar && !horarioInicialValido
    val erroHorarioFinal = tentouDisponibilizar && !horarioFinalValido


    // Dialog cria uma janela sobre a tela atual
    // O Android escurece automaticamente o fundo
    Dialog(
        onDismissRequest = onDismiss,

        // Permite fechar o popup ao tocar fora dele
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {

        // Obtém os controladores da própria janela do popup
        // Assim o foco será removido dos campos que pertencem ao Dialog
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        // Verifica automaticamente o intervalo sempre que os horários mudarem, só mostra erro quando ambos estiverem preenchidos
        val erroIntervaloHorario =
            horarioInicial.isNotBlank() && horarioFinal.isNotBlank() && !MaskResiduo.intervaloHorarioValido(
                horarioInicial,
                horarioFinal
            )

        // Fundo branco e cantos arredondados do popup
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(15.dp),
            color = Branco
        ) {

            // Permite rolar o formulário quando não couber na tela
            Column(
                modifier = Modifier
                    .heightIn(max = 700.dp)
                    .verticalScroll(rememberScrollState())
                    // Remove o foco quando o usuário toca em uma área vazia.
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    ) {
                        // Remove o foco do campo selecionado
                        focusManager.clearFocus()

                        // Fecha o teclado, caso esteja aberto
                        keyboardController?.hide()
                    }
                    .padding(
                        top = 30.dp,
                        bottom = 15.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Identificação do aplicativo
                Text(
                    text = "ECOLETA",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovoResiduoConfirmar
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Título principal do formulário
                Text(
                    text = "Disponibilizar novo resíduo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = NovoResiduoTexto
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Descrição apresentada abaixo do título
                Text(
                    text = "Cadastre o material para receber ofertas de coleta. O valor será definido pela empresa coletora.",
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.widthIn(280.dp),
                    color = NovoResiduoDescricao
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo para selecionar o tipo de material
                Text(
                    text = "Tipo de material",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                // Campo responsável por abrir o dropdown de materiais
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Campo que mostra o nome do material escolhido
                    OutlinedTextField(
                        value = nomeMaterialSelecionado,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .onGloballyPositioned { coordinates ->
                                // Obtém a largura real do campo
                                larguraCampoMaterial = coordinates.size.width
                            },
                        // Impede digitação manual
                        readOnly = true,
                        singleLine = true,
                        isError = erroMaterial,
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (menuMaterialExpandido) 0.dp else 12.dp,
                            bottomEnd = if (menuMaterialExpandido) 0.dp else 12.dp
                        ),
                        placeholder = {
                            Text("Selecione um material")
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Selecionar material",
                                tint = NovoResiduoTexto
                            )
                        },
                        colors = coresCamposNovoResiduo()
                    )

                    // Captura o clique em qualquer parte do campo
                    // Não abre teclado, apenas o dropdown
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                menuMaterialExpandido = !menuMaterialExpandido
                            }
                    )
                    // Exibe a lista quando o campo estiver aberto.
                    if (menuMaterialExpandido && larguraCampoMaterial > 0) {
                        Popup(
                            alignment = Alignment.TopStart,
                            // Posiciona a lista logo abaixo do campo
                            offset = IntOffset(
                                x = 0,
                                y = with(density) { 55.dp.roundToPx() }
                            ),
                            // Fecha ao tocar fora do dropdown.
                            onDismissRequest = {
                                menuMaterialExpandido = false
                            },
                            properties = PopupProperties(
                                focusable = true
                            )
                        ) {
                            // Container das opções
                            Column(
                                modifier = Modifier
                                    .width(larguraDropdownMaterial)
                                    .heightIn(max = 300.dp)
                                    // Arredonda somente os cantos inferiores.
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 12.dp,
                                            bottomEnd = 12.dp
                                        )
                                    )
                                    .background(NovoResiduoFundoCampo)
                                    .border(
                                        BorderStroke(
                                            width = 1.dp,
                                            color = NovoResiduoBordaCampo
                                        ),
                                        shape = RoundedCornerShape(
                                            bottomStart = 12.dp,
                                            bottomEnd = 12.dp
                                        )
                                    )
                                    // Permite rolar caso a API retorne muitos materiais
                                    .verticalScroll(rememberScrollState())
                            ) {
                                // Percorre os materiais
                                viewModel.tiposMateriais.forEach { material ->
                                    // Verifica se este material está selecionado
                                    val selecionado = idTipoMaterial == material.idTipoMaterial

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            // Destaca o material selecionado
                                            .background(
                                                if (selecionado)
                                                    ResiduoDropdownSelecionado
                                                else
                                                    NovoResiduoFundoCampo
                                            )
                                            // Guarda o ID do material e fecha a lista
                                            .clickable {
                                                idTipoMaterial = material.idTipoMaterial
                                                menuMaterialExpandido = false
                                            }
                                            .padding(
                                                horizontal = 20.dp,
                                                vertical = 16.dp
                                            )
                                    ) {
                                        Text(
                                            text = material.material,
                                            fontSize = 16.sp,
                                            color = if (selecionado)
                                                ResiduoDropdownTextoSelecionado
                                            else
                                                NovoResiduoTexto
                                        )
                                    }
                                }
                            }
                        }

                    }
                }

                if (erroMaterial) {
                    Text(
                        text = "Selecione um tipo de material.",
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Campo numérico para informar a quantidade em quilogramas
                Text(
                    text = "Quantidade (kg)",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { novoValor ->
                        // Verifica se o novo texto respeita o formato e o limite máximo de 9999,99
                        if (MaskResiduo.quantidadeDigitadaValida(novoValor)) {
                            // Mantém o texto exatamente como o usuário digitou, assim, o cursor não fica pulando durante a edição
                            quantidade = novoValor
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .onFocusChanged { focusState ->
                            // Quando o usuário sai do campo, formata a quantidade com duas casas decimais
                            if (!focusState.isFocused && quantidade.isNotBlank()) {
                                quantidade = MaskResiduo.formatarQuantidade(quantidade)
                            }
                        },
                    singleLine = true,
                    isError = erroQuantidade,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = coresCamposNovoResiduo()
                )

                if (erroQuantidade) {
                    Text(
                        text = "Informe uma quantidade válida maior que zero.",
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Campo de data disponível
                Text(
                    text = "Data disponível",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = dataDisponivel,
                    onValueChange = { novoValor ->

                        // Obtém o texto digitado antes de aplicar a máscara
                        val textoDigitado = novoValor.text

                        // Verifica se o usuário está apagando caracteres
                        val apagando = textoDigitado.length < dataDisponivel.text.length

                        // Se o usuário apagou uma barra no final, remove também o número anterior para não recriar a barra
                        val textoParaFormatar =
                            if (apagando && dataDisponivel.text.endsWith("/") && textoDigitado == dataDisponivel.text.dropLast(
                                    1
                                )
                            ) {
                                textoDigitado.dropLast(1)
                            } else {
                                textoDigitado
                            }

                        // Aplica a máscara corrigida
                        // Se o dia digitado for inválido, mantém o valor anterior, assim, o usuário não consegue inserir 32, 33, 34 etc
                        val formatado =
                            MaskResiduo.formatarData(textoParaFormatar) ?: dataDisponivel.text

                        // Atualiza o campo e mantém o cursor no final
                        dataDisponivel = TextFieldValue(
                            text = formatado,
                            selection = TextRange(formatado.length)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = erroData,
                    placeholder = {
                        Text("DD/MM/AAAA")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    trailingIcon = {
                        // Permite abrir o calendário ao tocar no ícone
                        IconButton(
                            onClick = {
                                selecionarData = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Abrir calendário"
                            )
                        }
                    },
                    colors = coresCamposNovoResiduo()
                )

                if (erroData) {
                    Text(
                        text = when {
                            dataDisponivel.text.isBlank() -> "Informe a data disponível."

                            !MaskResiduo.dataValida(dataDisponivel.text) -> "Informe uma data válida."

                            else -> "A data não pode ser anterior à data atual."
                        },
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Campo para informar o horário de disponibilidade
                Text(
                    text = "Horário",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                // Organiza os horários inicial e final lado a lado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // Campo do horário inicial
                    Box(modifier = Modifier.weight(1f)) {

                        OutlinedTextField(
                            value = horarioInicial,
                            onValueChange = {},
                            readOnly = true,
                            // Ativa a aparência de erro quando o intervalo é inválido
                            isError = erroHorarioInicial || erroIntervaloHorario,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            placeholder = {
                                Text("Inicial")
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = coresCamposNovoResiduo()
                        )

                        // Captura o toque sem abrir o teclado
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    selecionarHorarioInicial = true
                                }
                        )
                    }

                    // Campo do horário final
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = horarioFinal,
                            onValueChange = {},
                            readOnly = true,
                            // Ativa a aparência de erro quando o intervalo é inválido
                            isError = erroHorarioFinal || erroIntervaloHorario,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            placeholder = {
                                Text("Final")
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = coresCamposNovoResiduo()
                        )

                        // Abre o relógio do horário final.
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    selecionarHorarioFinal = true
                                }
                        )
                    }
                }

                if (erroHorarioInicial) {
                    Text(
                        text = "Selecione o horário inicial.",
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                if (erroHorarioFinal) {
                    Text(
                        text = "Selecione o horário final.",
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                // Exibe uma mensagem quando o horário final não é posterior ao horário inicial
                if (erroIntervaloHorario) {
                    Text(
                        text = "O horário final deve ser maior que o horário inicial.",
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Campo de observações adicionais
                Text(
                    text = "Observação",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(95.dp),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    colors = coresCamposNovoResiduo()
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Exibe uma mensagem quando o backend não consegue cadastrar o resíduo
                viewModel.erroCadastroResiduo?.let { mensagemErro ->
                    Text(
                        text = mensagemErro,
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                // Botões inferiores do formulário
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cancela o cadastro e fecha o popup
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovoResiduoCancelar
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }

                    // Futuramente enviará o cadastro para o backend
                    Button(
                        onClick = {
                            // A partir desta tentativa, os erros ficam visíveis
                            tentouDisponibilizar = true

                            // Só permite continuar quando todos os campos obrigatórios estiverem preenchidos e válidos
                            val formularioValido =
                                materialValido && quantidadeValida && dataValida &&
                                        horarioInicialValido && horarioFinalValido && intervaloValido

                            if (formularioValido) {
                                val quantidadeNumerica = quantidade.replace(',', '.').toDouble()

                                // Monta o objeto que será convertido em JSON pelo Retrofit
                                val novoResiduo = ResiduoCadastroRequest(
                                    idTipoMaterial = idTipoMaterial!!,
                                    quantidade = quantidadeNumerica,
                                    dataDisponivel = dataDisponivel.text,
                                    horarioInicial = horarioInicial,
                                    horarioFinal = horarioFinal,
                                    observacao = observacao.ifBlank { null }
                                )

                                // Envia os dados para a ViewModel, que realizará o POST utilizando o ResiduoService
                                viewModel.cadastrarResiduo(novoResiduo)
                            }
                        },
                        // Impede novos envios enquanto a requisição estiver em andamento
                        enabled = !viewModel.cadastrandoResiduo,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovoResiduoConfirmar
                        )
                    ) {
                        if (viewModel.cadastrandoResiduo) {
                            // Mostra o indicador enquanto aguarda a resposta da API
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height(22.dp),
                                color = Branco,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Disponibilizar Resíduo",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }

    // Abre o relógio para escolher o horário inicial
    if (selecionarHorarioInicial) {
        SeletorHorarioResiduo(
            horarioAtual = horarioInicial,
            onConfirmar = { horarioSelecionado ->
                horarioInicial = horarioSelecionado
                selecionarHorarioInicial = false
            },
            onCancelar = {
                selecionarHorarioInicial = false
            }
        )
    }

    // Abre o relógio para escolher o horário final
    if (selecionarHorarioFinal) {
        SeletorHorarioResiduo(
            horarioAtual = horarioFinal,
            onConfirmar = { horarioSelecionado ->
                horarioFinal = horarioSelecionado
                selecionarHorarioFinal = false
            },
            onCancelar = {
                selecionarHorarioFinal = false
            }
        )
    }

    // Exibe o calendário quando o usuário toca no ícone da data
    if (selecionarData) {
        SeletorDataResiduo(
            dataAtual = dataDisponivel.text,
            onConfirmar = { dataSelecionada ->
                // Atualiza o campo com a data escolhida
                // TextFieldValue mantém o controle do cursor
                dataDisponivel = TextFieldValue(
                    text = dataSelecionada,
                    selection = TextRange(dataSelecionada.length)
                )
                // Fecha o calendário após confirmar
                selecionarData = false
            },

            onCancelar = {
                // Fecha o calendário sem alterar a data preenchida.
                selecionarData = false
            }
        )
    }
}

// Centraliza a configuração visual dos campos do formulário
// Todos os campos compartilham as mesmas cores e bordas
@Composable
private fun coresCamposNovoResiduo() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = NovoResiduoFundoCampo,
        unfocusedContainerColor = NovoResiduoFundoCampo,

        focusedBorderColor = NovoResiduoBordaCampo,
        unfocusedBorderColor = NovoResiduoBordaCampo,

        focusedTextColor = NovoResiduoTexto,
        unfocusedTextColor = NovoResiduoTexto,

        errorBorderColor = NovoResiduoErro,
        errorTextColor = NovoResiduoTexto,
        errorContainerColor = NovoResiduoFundoCampo
    )
