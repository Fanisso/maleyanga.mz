package pagamento

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import mz.maleyanga.ClienteService
import mz.maleyanga.ContadorService
import mz.maleyanga.CreditoService
import mz.maleyanga.DiarioService
import mz.maleyanga.PagamentoService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.SettingsService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.conta.Conta
import mz.maleyanga.credito.Credito
import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.ParcelaService
import mz.maleyanga.pagamento.Remissao
import mz.maleyanga.saidas.Saida
import mz.maleyanga.security.Utilizador
import mz.maleyanga.transacao.Transacao
import mz.maleyanga.transferencia.Transferencia
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.Div
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Row
import java.math.RoundingMode
import java.sql.SQLException

@Transactional
@Service
class PagamentosViewModel {
    PagamentoService pagamentoService
    SessionStorageService sessionStorageService
     SettingsService settingsService
    @Wire Label info
    @Wire Button bt_update_entrada
    @Wire Label lb_pag
    @Wire Label lb_remissoes
    @Wire Row rw_co
    @Wire Div dv_filtragem
    @Wire Listbox lb_pagamentos
    @Wire Row rw_destino
    @Wire Button bt_diario
    @Wire Button bt_salvar
    @Wire Button bt_fechar_caixa
    @Wire Listbox lb_credito
    @Wire Listbox lb_creditos
    private  String filter
    private  String filterCliente
    private  boolean  allPagamentos
    private  boolean  allCreditos
    private  String id
    private  String pagamento_id = ""
    private  Pagamento selectedPagamento
    private  Pagamento selectedPagamentoo
    private  Credito selectedCredito
    private  Credito selectedCreditoo
    private ListModelList<Pagamento> pagamentos
    private ListModelList<Pagamento> pagamentoss
    private ListModelList<Parcela> parcelas
    private ListModelList<Parcela> entradas
    private ListModelList<Remissao> remissoes
    private ListModelList<Parcela> parcels
    private ListModelList<Saida> saidas
    private ListModelList<Conta> contas
    private ListModelList<Transacao> tSaidas
    private ListModelList<Transacao> tEntradas
    private ListModelList<Cliente> clientes
    private ListModelList<Cliente> clientess
    private ListModelList<Credito> creditos
    private ListModelList<Credito> creditoss
    ClienteService clienteService
    CreditoService creditoService
    private Diario diario
    private Parcela entrada
    private Cliente selectedCliente
    private Cliente selectedClientee
    private Date dia
    DiarioService diarioService
    private Parcela parcela
    private Parcela parcelaEntrada
    private Saida saida
    private Parcela sParcela
    private String recibo
    private Remissao remissao
    private BigDecimal totalParcelas = 0.0
    private BigDecimal totalSaidas = 0.0
    ParcelaService parcelaService
    SpringSecurityService springSecurityService
      ContadorService contadorService
    private  Utilizador utilizador
    private  Conta contaCliente = new Conta()
    private Remissao selectedRemissao

    private  Conta selectedConta
    private  Saida selectedSaida

    @Command
    @NotifyChange(["clientes","selectedCliente","pagamentos","selectedCredito","creditos","selectedPagamento","filterCliente"])
    void doSearchCliente() {
        info.value=""
        fecharEditor()
        clientes.clear()
        clientess.clear()
        pagamentos.clear()
        if(filterCliente.contains("/")){
            selectedCredito = Credito.findByNumeroDoCredito(filterCliente)
            if(selectedCredito){
                creditos.add(selectedCredito)
                selectedCliente = selectedCredito.cliente
                showCreditos()
                getSelectedCliente()
                return
            }else {
               lb_credito.visible = false
                creditos.clear()
                info.value+="Crédito não indentificado !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
            }
        }

        List<Cliente> allItems = clienteService.findAllByName(filterCliente)
        if (filterCliente != null &&! "".equals(filterCliente))
        {
            for (Cliente item : allItems) {
                if (item.nome.toLowerCase().indexOf(filterCliente.toLowerCase()) >= 0 ||

                        item.numeroDeIndentificao.indexOf(filterCliente) >= 0) {
                    clientes.add(item)
                    clientess.add(item)
                }
            }
            if(clientes.empty||clientess.empty){
                lb_credito.visible = false
                creditos.clear()
                pagamentos.clear()
                info.value+="Cliente não indentificado !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
            }
        }
    }

    String getFilterCliente() {
        return filterCliente
    }

    void setFilterCliente(String filterCliente) {
        this.filterCliente = filterCliente
    }

    Pagamento getSelectedPagamentoo() {
        return selectedPagamentoo
    }

    @NotifyChange(["pagamentoss","selectedPagamentoo","remissao","selectedRemissao"])
    void setSelectedPagamentoo(Pagamento selectedPagamentoo) {
        this.selectedPagamentoo = selectedPagamentoo
        selectedRemissao = null
        remissao = new Remissao()
    }

    Credito getSelectedCreditoo() {
        return selectedCreditoo
    }

    @NotifyChange(["pagamentoss","selectedPagamentoo","remissao","selectedRemissao","selectedCreditoo"])
    void setSelectedCreditoo(Credito selectedCreditoo) {
        selectedRemissao = null
        remissao = new Remissao()
        this.selectedCreditoo = selectedCreditoo
    }

    Cliente getSelectedClientee() {
        return selectedClientee
    }

    @NotifyChange(["selectedCredito","pagamentos","selectedPagamento","selectedPagamentoo","selectedCreditoo","selectedClientee","creditoss"])
    void setSelectedClientee(Cliente selectedClientee) {
        sessionStorageService.cliente = selectedClientee
        this.selectedClientee = selectedClientee
       clientess.clear()
        clientess.add(selectedClientee)
    }

    boolean getAllPagamentos() {
        return allPagamentos
    }

    void setAllPagamentos(boolean allPagamentos) {
        this.allPagamentos = allPagamentos
    }
    boolean getAllCreditos() {
        return allCreditos
    }

    void setAllCreditos(boolean allCreditos) {
        this.allCreditos = allCreditos
    }

    @Command
    @NotifyChange(["pagamentos","selectedCredito","info","allPagamentos","allCreditos"])
    void setAll() {
        allPagamentos = !allPagamentos
       getPagamentos()
    }
    @Command
    @NotifyChange(["creditos","selectedCredito","pagamentos","allPagamentos","allCreditos","info"])
    void setAllC() {
        allCreditos = !allCreditos
        getCreditos()
    }

    @Command
    @NotifyChange(["allCreditos","allPagamentos","creditos","info"])
    def cleanAllCreditos(){
        allCreditos = false
        allPagamentos = false
        info.value= ""
        getCreditos()
    }

    @Command
    @NotifyChange(["allCreditos","allPagamentos","pagamentos","info"])
    def cleanAllPagamentos(){
        info.value= ""
        allPagamentos = false
        getPagamentos()
    }

    @NotifyChange(["creditos"])
    ListModelList<Credito> getCreditos() {
        if(creditos==null){
            creditos = new ListModelList<Credito>()
        }
        creditos.clear()

        if(selectedCliente!=null){
            if(allCreditos){
                creditos = creditoService.findAllByCliente(selectedCliente)
            }else
            creditos = creditoService.findAllByClienteAndEmDivida(selectedCliente)
        }
        return creditos
    }

    ListModelList<Credito> getCreditoss() {
        if(creditoss==null){
            creditoss = new ListModelList<Credito>()
        }
        creditoss.clear()
        if(selectedClientee!=null){
            creditoss = Credito.findAllByClienteAndEmDivida(selectedClientee,true).sort{it.id}
        }
        return creditoss
    }

    Credito getSelectedCredito() {
        return selectedCredito
    }

    @NotifyChange(["pagamentos","selectedPagamento"])
    void setSelectedCredito(Credito selectedCredito) {
        this.selectedCredito = selectedCredito
        selectedPagamento = null
       getPagamentos()

    }

    @Command
    @NotifyChange(["creditos","creditoss","selectedClientee","selectedCredito","selectedCliente","selectedCredito","pagamentos","selectedPagamento"])
    def showPagamentos(){
     //   lb_credito.visible = false
        getPagamentos()
    }
    @Command
    @NotifyChange(["creditos","creditoss","selectedClientee","selectedCliente","selectedCredito","pagamentos","pagamentoss","selectedPagamentoo"])
    def showCreditos(){
        lb_credito.visible= true
        lb_creditos.visible= true
    }
    Cliente getSelectedCliente() {
        return selectedCliente
    }


    @NotifyChange(["info","selectedCredito","pagamentos","selectedPagamento","selectedPagamentoo","selectedCreditoo","creditos","selectedCliente"])
    void setSelectedCliente(Cliente selectedCliente) {
        info.value = ""
        selectedCredito = null
        selectedPagamento = null
        sessionStorageService.parcela = null
        sessionStorageService.credito = null
        pagamentos.clear()
        this.selectedCliente = selectedCliente
        clientes.clear()
        clientes.add(selectedCliente)
    }

    Remissao getSelectedRemissao() {
        return selectedRemissao
    }

    void setSelectedRemissao(Remissao selectedRemissao) {
        this.selectedRemissao = selectedRemissao
    }

    @NotifyChange(["selectedConta","pagaments"])
   @Command
   def getContaOrigem(){
      selectedConta = Conta.findByCliente(parcelaEntrada.cliente)

        System.println(selectedConta)
        rw_co.visible= false
   }



    ListModelList<Cliente> getClientes() {
        if(clientes==null){
            clientes = new ListModelList<Cliente>()
        }
        return clientes
    }

    ListModelList<Cliente> getClientess() {
        if(clientess==null){
            clientess = new ListModelList<Cliente>()
        }
        return clientess
    }

    @NotifyChange("entrada")
    Parcela getEntrada() {

        return entrada
    }

    @NotifyChange(["selectedPagamento","entrada"])
    void setEntrada(Parcela entrada) {
        info.value= ""
        this.entrada = entrada
        sessionStorageService.entrada = entrada
        if(selectedPagamento!=null&&entrada.pagamento==null){
            bt_update_entrada.label="Alocar a "+selectedPagamento.descricao+" do(a) "+selectedCredito.cliente.nome
        }

    }

    @Command
    @NotifyChange(["selectedPagamento","entrada"])
    def updateEntrada(){
        info.value=""
       entrada = Parcela.findById(entrada.id)
        System.println(entrada.id)
        Utilizador user = springSecurityService.currentUser as Utilizador
        if(entrada.utilizador!=user){
            if (!user.authorities.any { it.authority == "PARCELA_UPDATE" }) {
                info.value+="Este utilizador não tem permissão para executar esta acção !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
        }

        if(entrada.nomeDoCliente!=selectedCredito.cliente.nome){
            info.value="O nome do cliente não confere! "
            info.style = "color:red;font-weight;font-size:16px;background:back"
            if (!user.authorities.any { it.authority == "PARCELA_UPDATE" }) {
                info.value+="Este utilizador não tem permissão para executar esta acção !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
        }


        /*
        if (entrada.valorPago>selectedPagamento.totalEmDivida*(-1)){
            info.value="O Valor a alocar não deve ser superior ao valor em dívida!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        */
        if (entrada.pagamento!=null){
            info.value="Este valor já foi alocado!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        try {
            entrada.valorParcial = entrada.valorPago
            entrada.pagamento = selectedPagamento
            if(selectedPagamento.parcelas == null){
                selectedPagamento.parcelas = new LinkedHashSet<Parcela>()
            }
            selectedPagamento.parcelas.add(entrada)
            entrada.merge(failOnError: true)
            selectedPagamento.merge(failOnError: true)
            Transferencia t = new Transferencia()
            t.utilizador = utilizador
            String descricao = entrada.descricao+"-"+entrada.id
            Transferencia tr = Transferencia.findByDescricaoAndDiario(descricao,diario)
          //  System.println(tr.origem)
           // System.println(tr.destino)
            t.origem = Conta.findByNumeroDaConta(selectedCliente.id.toString())
            t.destino = Conta.findById(tr.origem.id)
            t.descricao = "Alocação de receb. em caixa"+"-"+entrada.id
            t.valor = entrada.valorParcial
            t.diario = diario
            t.save(failOnError: true)
            Transacao tCredito = new Transacao()
            Transacao tDebito = new Transacao()
            tCredito.valor = entrada.valorParcial
            tCredito.descricao="Alocação de receb. em caixa"+"-"+entrada.id
            tDebito.descricao ="Alocação de rec. em caixa"+"-"+entrada.id
            tDebito.valor = entrada.valorParcial
            tCredito.credito = true
            tDebito.credito = false
            def credora = Conta.findById(t.origem.id)
            def devedora = Conta.findById(t.destino.id)
            if (credora?.transacoes == null) {
                credora.transacoes = new LinkedHashSet<Transacao>()
            }
            if (devedora?.transacoes == null) {
                devedora.transacoes = new LinkedHashSet<Transacao>()
            }
            tCredito.save(failOnError: true)
            tDebito.save(failOnError: true)
            credora.transacoes.add(tCredito)
            devedora.transacoes.add(tDebito)
            credora.merge(failOnError: true)
            devedora.merge(failOnError: true)

            info.value = "O valor foi alocado com sucesso!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
        }catch(Exception e){
            System.println(e.toString())
            info.value = e.toString()
            info.style = "color:red;font-weight;font-size:16px;background:back"
        }

    }

    ListModelList<Parcela> getEntradas() {

        if(entradas ==null){
            entradas = new ListModelList<Parcela>()
        }
        entradas.clear()
        def parcelas = Parcela.findAllByDiario(diario)
        for(Parcela p in parcelas){
            if(p.pagamento==null){
                entradas.add(p)
            }
        }
        return entradas
    }

    Parcela getParcelaEntrada() {
        return parcelaEntrada
    }

    void setParcelaEntrada(Parcela parcelaEntrada) {
        this.parcelaEntrada = parcelaEntrada
    }

    Date getDia() {
        return dia
    }

    @Command
    @NotifyChange(["dia"])
    void setDia(Date dia) {
        this.dia = dia
        sessionStorageService.setDia(dia)
    }

    BigDecimal getTotalParcelas() {
        totalParcelas = 0.0
        for(Parcela parcela in parcels){
            totalParcelas +=parcela.valorPago
        }
        return totalParcelas
    }

    BigDecimal getTotalSaidas() {
        totalSaidas = 0.0
        for(Saida saida in saidas){
            totalSaidas +=saida.valor
        }
        return totalSaidas
    }
    List<Parcela> getParcels() {
        if (parcels==null){
            parcels = new ListModelList<Parcela>(Parcela.findAllByValorPagoGreaterThanAndDiarioAndUtilizador(0.0,diario,utilizador))
        }
        return parcels
    }

    ListModelList<Remissao> getRemissoes() {
        if(remissoes ==null){
           remissoes = new ListModelList<Remissao>()
        }
        return remissoes
    }

    Remissao getRemissao() {
        return remissao
    }

    void setRemissao(Remissao remissao) {
        this.remissao = remissao
    }

    BigDecimal getSaldo() {
        System.println("getTotalSaidas="+getTotalSaidas())
        System.println("getTotalParcelas="+getTotalParcelas())
        return getTotalParcelas()-getTotalSaidas()
    }
    List<Saida> getSaidas() {
        if(saidas==null){
            saidas = new ListModelList<Saida>(Saida.findAllByDiarioAndUtilizadorAndOrigem(diario,utilizador,contaCaixa))
        }
        return saidas
    }
    Saida getSelectedSaida() {
        return selectedSaida
    }


    void setSelectedSaida(Saida selectedSaida) {
        this.selectedSaida = selectedSaida
    }

    String getRecibo() {
        return recibo
    }

    void setRecibo(String recibo) {
        this.recibo = recibo
    }




    @Command
    @NotifyChange(["saida",'parcelaEntrada'])
    def fecharCaixa(){
        parcelaEntrada=null

        getParcels()
        getSaidas()

       saida  = new Saida()

        saida.valor = getSaldo()
        saida.dataDePagamento = new Date()
        saida.descricao ="Feixo de caixa"

    }

    @Command
    def deleteSaida(){
        try {
            Transacao tCredito = new Transacao()
            Transacao tDebito = new Transacao()
            tCredito.valor = selectedSaida.valor
            tCredito.descricao="Estorno"
            tDebito.descricao = "Estorno"
            tDebito.valor = selectedSaida.valor
            tCredito.credito = true
            tDebito.credito = false
            def credora = Conta.findById(selectedSaida.destino.id)
            def devedora = Conta.findById(selectedSaida.origem.id)

            tCredito.save(failOnError: true)
            tDebito.save(failOnError: true)
            credora.transacoes.add(tCredito)
            devedora.transacoes.add(tDebito)
            credora.merge(failOnError: true)
            devedora.merge(failOnError: true)


            selectedSaida.delete(failOnError: true)
        }catch(Exception e){
            System.println(e.toString())
        }
    }
    @Command
    def checkSelectedItem(){
        if(parcela.formaDePagamento=="transferencia bancária"||parcela.formaDePagamento=="deposito bancário"){
            rw_destino.visible =true
            saida = new Saida()
        }else {
            rw_destino.visible = false
        }
    }
    String getPagamento_id() {
        return pagamento_id
    }

    ListModelList<Transacao> gettSaidas() {
        if(tSaidas==null){
            tSaidas = new ListModelList<Transacao>()
        }
        tSaidas?.clear()
        if(contaCaixa?.id){
            for(Transacao t in contaCaixa?.transacoes){
                if (t?.credito){

                    tSaidas?.add(t)
                }

            }
        }
        tSaidas.sort{it.dateCreated}
        return tSaidas
    }

    ListModelList<Transacao> gettEntradas() {
        if(tEntradas==null){
            tSaidas = new ListModelList<Transacao>()
        }
        iEntradas.clear()
        if(contaCaixa.id){
            for(Transacao t in contaCaixa.transacoes){
                if (t.credito){
                    iEntradas.add(t)
                }

            }
        }
        return iEntradas
    }

    Conta getSelectedConta() {
        return selectedConta
    }

    void setSelectedConta(Conta selectedConta) {
        this.selectedConta = selectedConta
    }

    ListModelList<Conta> getContas() {
        if(contas==null){
            contas = new ListModelList<Conta>(Conta.findAllByFinalidadeOrFinalidade("conta_movimento","conta_capital"))
        }

        return contas
    }

    Saida getSaida() {
        return saida
    }

    void setSaida(Saida saida) {
        this.saida = saida
    }

    Parcela getsParcela() {
        return sParcela
    }

    void setsParcela(Parcela sParcela) {
        this.sParcela = sParcela
        sessionStorageService.parcela=sParcela
        sessionStorageService.credito=selectedCredito
    }

    ListModelList<Parcela> getParcelas() {
        if(parcelas==null){
            parcelas = new ListModelList<Parcela>()
        }
        if(selectedPagamento.id!=null){
            for(Parcela p in selectedPagamento.parcelas){
                parcelas.add(p)
            }
        }
        return parcelas
    }
    @NotifyChange(['contaCaixa','caixas'])
    Conta getContaCaixa() {
        return   Conta.findByUtilizadorAndFinalidade(utilizador,'conta_caixa')
    }

    @Command
    @NotifyChange(["saida","parcela","selectedPagamento","parcelaEntrada"])
    def addSaida(){
     /*   bt_fechar_caixa.visible = false
        dv_filtragem.visible = false
        lb_pagamentos.visible = false*/
        saida = new Saida()
        saida.dataDePagamento = new Date()
        saida.formaDePagamento = "numerário"
        selectedPagamento = null
        parcela = null
        parcelaEntrada = null
        remissao = null
    }



    @Command
    @NotifyChange(["saida","parcela","selectedPagamentoo","remissao"])
    def addRemissao(){
        parcela = null
        parcelaEntrada = null
        saida = null
        if(selectedPagamentoo==null){
            info.value="Seleccione uma prestação!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        if(Pagamento.findById(selectedPagamentoo.id).pago){
            info.value="Esta prestação já foi paga na totalidade!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        remissao =  new Remissao()
        remissao.pagamento = selectedPagamento
      // selectedPagamento = null

    }
    @Command
    def printSaida(){

    }
    @Command
    @NotifyChange(['contaCaixa','tSaidas',"saidas"])
    def salvarSaida(){

        if(Saida.findById(saida.id)){
            info.value = "Este Pagamento já foi lançado!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        if(0>=saida.valor){
            info.value = "Valor inválido!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        getParcels()
        getSaidas()
        if(saida.valor>getSaldo().setScale(2,RoundingMode.DOWN)){
            info.value = "O saldo de "+getSaldo().setScale(2,RoundingMode.DOWN)+", em caixa  não cobre o valor de "+saida.valor+" ,que pretende dar saida!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        if(saida.dataDePagamento==null){
            info.value = "Data inválido!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        if(saida.formaDePagamento==null){
            info.value = "Forma de Pagamento não foi selecionado!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        if(contaCliente==null){
            info.value = "ESte cliente não tem conta!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            return
        }
        info.value = ""

        if(contaCaixa==null){
            info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
            info.style ="color:red;font-weight;font-size:16px;background:back"
            selectedPagamento=null
            return
        }

        saida.utilizador = Utilizador.findById(utilizador.id)

        if (saida.hasErrors()) {

            return
        }
        saida.contaOrigem=contaCaixa.codigo
        saida.contaDestino=selectedConta.codigo
        saida.origem = contaCaixa
        saida.destino = selectedConta
        saida.diario = diario
        saida.save(failOnError: true)
        info.value = "gravação feita com sucesso!"
        info.style = "color:red;font-weight;font-size:16px;background:back"

        Transacao tCredito = new Transacao()
        Transacao tDebito = new Transacao()
        tCredito.valor = saida.valor
        tCredito.descricao=saida.descricao+"-"+saida.formaDePagamento
        tDebito.descricao = saida.descricao+"-"+saida.formaDePagamento
        tDebito.valor = saida.valor
        tCredito.credito = true
        tDebito.credito = false
        def credora = Conta.findById(contaCaixa.id)
        def devedora = Conta.findById(selectedConta.id)
        if (credora.transacoes == null) {
            credora.transacoes = new LinkedHashSet<Transacao>()
        }
        if (devedora.transacoes == null) {
            devedora.transacoes = new LinkedHashSet<Transacao>()
        }
        tCredito.save(failOnError: true)
        tDebito.save(failOnError: true)
        credora.transacoes.add(tCredito)
        devedora.transacoes.add(tDebito)
        credora.merge(failOnError: true)
        devedora.merge(failOnError: true)


        info.value = "Operações feitas com sucesso!"
        info.style = "color:red;font-weight;font-size:16px;background:back"
        parcelaService.saidaInstance = saida
        saidas.add(saida)
    }

    @Command
    @NotifyChange(['contaCaixa','tSaidas',"entradas"])
    def salvarEntrada(){
       try {
           if(parcelaEntrada.nomeDoCliente==null){
               info.value = "Digite o nome do cliente!"
               info.style = "color:red;font-weight;font-size:16px;background:back"
               return
           }
           if(parcelaEntrada.dataDePagamento>new Date()){
               parcelaEntrada.dataDePagamento = new Date()
               info.value = "Data inválida!"
               info.style = "color:red;font-weight;font-size:16px;background:back"

           }
           if(Parcela.findById(parcelaEntrada.id)){
               info.value = "Este Pagamento já foi lançado!"
               info.style = "color:red;font-weight;font-size:16px;background:back"
               return
           }

           if(selectedConta==null){
               info.value = "Selecione uma conta Destino válido!"
               info.style = "color:red;font-weight;font-size:16px;background:back"
               return
           }

           if(0>=parcelaEntrada.valorPago){
               info.value = "Valor inválido!"
               info.style = "color:red;font-weight;font-size:16px;background:back"
               return
           }
           if(parcelaEntrada.dataDePagamento==null){
               info.value = "Data inválido!"
               info.style = "color:red;font-weight;font-size:16px;background:back"
               return
           }

           if(parcelaEntrada.formaDePagamento==null){
               info.value = "Forma de Pagamento não foi selecionado!"
               info.style = "color:red;font-weight;font-size:16px;background:back"
               return
           }
           if(selectedConta==null){
               info.value = "Selecione a conta Origem!"
               info.style = "color:red;font-weight;font-size:16px;background:back"
               return
           }
           info.value = ""

           if(contaCaixa==null){
               info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
               info.style ="color:red;font-weight;font-size:16px;background:back"
               selectedPagamento=null
               return
           }
           parcelaEntrada.setNumeroDoRecibo(contadorService.gerarNumeroDaParcela())
           parcelaEntrada.diario = diario
           Utilizador util = Utilizador.findById(utilizador.id)
           parcelaEntrada.utilizador = util
           parcelaEntrada.save(failOnError: true)
         //  lancamentoEntrada(parcelaEntrada)
           getEntradas()
       }catch(Exception e){
          info.value = e.toString()
           info.style = "color:red"
       }
    }


    @NotifyChange(["selectedPagamento",'parcela',"contaOrigem","saida","pagamento_id","parcelaEntrada","entradas"])
    @Command
    addEntrada(){

       /* bt_fechar_caixa.visible =false
        dv_filtragem.visible = false
        lb_pagamentos.visible = false*/
        info.value=""
        if(utilizador?.contas==null){
            info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
            info.style ="color:red;font-weight;font-size:16px;background:back"
            selectedPagamento=null
            return
        }



        parcelaEntrada = new Parcela()
        parcelaEntrada.diario = diario
        parcelaEntrada.dataDePagamento = new Date()
        parcelaEntrada.formaDePagamento = "numerário"
        parcelaEntrada.descricao = "Recebimento em caixa"
        if(selectedCliente!=null){
            parcelaEntrada.nomeDoCliente= selectedCliente.nome
        }
        saida = null
        remissao = null



    }

    @Command
    @NotifyChange(["selectedCreditoo","creditoss","remissoes","pagamentoss","selectedPagamentoo"])
    def redemirCredito(){
        remissao = new Remissao()

        try {
            remissao.valorDaRemissao = selectedCreditoo.valorEmDivida*(-1)
            Utilizador user = springSecurityService.currentUser as Utilizador
            if (!user.authorities.any { it.authority == "REMISSAO_CREATE" }) {
                info.value="Este utilizador não tem permissão para executar esta acção !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            System.println("feita a validacao do user")
            def contaDb = Conta.findByFinalidadeAndDesignacaoDaConta("conta_movimento", "PERDAO_DA_DIVIDA")
            if(contaDb==null&&!(contaDb?.ativo)){
                info.value="Criar uma conta de movimento, associado a uma conta intregradora do passivo ,  com a designão 'PERDAO_DA_DIVIDA'!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }

            if(Remissao.findById(remissao.id)){
                info.value="Esta remissoão já existe na base de dados!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            if(remissao.valorDaRemissao>selectedCreditoo.valorEmDivida*(-1)){
                info.value="O valor da remissão não pode ser superior ao valor em dívida!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                remissao.valorDaRemissao = selectedCreditoo.valorEmDivida*(-1)
                return
            }
            if(remissao.valorDaRemissao<0){
                info.value="O valor da remissão não pode ser negativo!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                remissao.valorDaRemissao = remissao.valorDaRemissao*(-1)
                return
            }
            remissao.diario = diario
            remissao.utilizador =user
            remissao.contaOrigem = Conta.findById(contaDb.id)
            /*if(selectedPagamentoo==null){
                info.value="Selecione pelo menos uma prestação!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }*/
            Pagamento pagamento = Pagamento.findById(pagamentoss.first().id)
            remissao.pagamento = pagamento
            if(pagamento.remissoes==null){
                pagamento.remissoes = new ArrayList<Remissao>()
            }
            remissao.createdDate = new Date()
            pagamento.remissoes.add(remissao)
            remissao.save()
            Remissao remissaoDb = Remissao.findById(remissao.id)
            System.println("remissaoDb=="+remissaoDb)

            if(remissaoDb!=null){
                System.println("Remissão gravado com sucesso!")
                info.value="Remissão gravado co sucesso!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                Transacao tCredito = new Transacao()
                Transacao tDebito = new Transacao()
                tCredito.valor = remissaoDb.valorDaRemissao
                tCredito.descricao=remissaoDb.descricao+"-"+remissao.id
                tDebito.descricao = remissaoDb.descricao+"-"+remissao
                tDebito.valor = remissaoDb.valorDaRemissao
                tCredito.credito = true
                tDebito.credito = false
                def credora = Conta.findById(contaDb.id)
                def devedora = Conta.findByNumeroDaConta(pagamento.credito.cliente.id.toString())
                if (credora?.transacoes == null) {
                    credora.transacoes = new LinkedHashSet<Transacao>()
                }
                if (devedora?.transacoes == null) {
                    devedora.transacoes = new LinkedHashSet<Transacao>()
                }
                tCredito.save(failOnError: true)
                tDebito.save(failOnError: true)
                credora.transacoes.add(tCredito)
                devedora.transacoes.add(tDebito)
                credora.merge(failOnError: true)
                devedora.merge(failOnError: true)
                info.value = "Operações feitas com sucesso!"
                info.style = "color:red;font-weight;font-size:16px;background:back"

                //addRemissao()
                selectedRemissao = Remissao.findById(remissao.id)
                remissoes.add(selectedRemissao)
                pagamentoss.clear()
                selectedCreditoo=null

                // remissao =null
                // fecharEditor()

            }else {
                info.value="Erro na na gravação da remissão!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
            }


        }catch(SQLException e){
            System.println(e.toString())
        }
    }

    @NotifyChange(["remissoes","remissao","selectedPagamentoo","selectedRemissao"])
    @Command
    def salvarRemissao(){

        try {
            Utilizador user = springSecurityService.currentUser as Utilizador
            if (!user.authorities.any { it.authority == "REMISSAO_CREATE" }) {
                info.value="Este utilizador não tem permissão para executar esta acção !"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            System.println("feita a validacao do user")
            def contaDb = Conta.findByFinalidadeAndDesignacaoDaConta("conta_movimento", "PERDAO_DA_DIVIDA")
            if(contaDb==null&&!(contaDb?.ativo)){
                info.value="Criar uma conta de movimento, associado a uma conta intregradora do passivo ,  com a designão 'PERDAO_DA_DIVIDA'!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }

            if(Remissao.findById(remissao.id)){
                info.value="Esta remissoão já existe na base de dados!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            if(remissao.valorDaRemissao>Pagamento.findById(selectedPagamentoo.id).getTotalEmDivida()*(-1)){
                info.value="O valor da remissão não pode ser superior ao valor em dívida!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                remissao.valorDaRemissao = Pagamento.findById(selectedPagamentoo.id).getTotalEmDivida()*(-1)

                return
            }
            if(remissao.valorDaRemissao<0){
                info.value="O valor da remissão não pode ser negativo!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                remissao.valorDaRemissao = remissao.valorDaRemissao*(-1)
                return
            }
            remissao.diario = diario
            remissao.utilizador =user
            remissao.contaOrigem = Conta.findById(contaDb.id)
            Pagamento pagamento = Pagamento.findById(selectedPagamentoo.id)
            remissao.pagamento = pagamento
            if(pagamento.remissoes==null){
                pagamento.remissoes = new ArrayList<Remissao>()
            }
            remissao.createdDate = new Date()
            pagamento.remissoes.add(remissao)
            remissao.save()
            Remissao remissaoDb = Remissao.findById(remissao.id)
            System.println("remissaoDb=="+remissaoDb)

            if(remissaoDb!=null){
                System.println("Remissão gravado com sucesso!")
                info.value="Remissão gravado co sucesso!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                Transacao tCredito = new Transacao()
                Transacao tDebito = new Transacao()
                tCredito.valor = remissaoDb.valorDaRemissao
                tCredito.descricao=remissaoDb.descricao+"-"+remissao.id
                tDebito.descricao = remissaoDb.descricao+"-"+remissao
                tDebito.valor = remissaoDb.valorDaRemissao
                tCredito.credito = true
                tDebito.credito = false
                def credora = Conta.findById(contaDb.id)
                def devedora = Conta.findByNumeroDaConta(selectedPagamentoo.credito.cliente.id.toString())
                if (credora?.transacoes == null) {
                    credora.transacoes = new LinkedHashSet<Transacao>()
                }
                if (devedora?.transacoes == null) {
                    devedora.transacoes = new LinkedHashSet<Transacao>()
                }
                tCredito.save(failOnError: true)
                tDebito.save(failOnError: true)
                credora.transacoes.add(tCredito)
                devedora.transacoes.add(tDebito)
                credora.merge(failOnError: true)
                devedora.merge(failOnError: true)
                info.value = "Operações feitas com sucesso!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                remissoes.add(remissao)
                
               //addRemissao()
                selectedRemissao = Remissao.findById(remissao.id)
               // remissao =null
               // fecharEditor()

            }else {
                info.value="Erro na na gravação da remissão!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
            }


        }catch(Exception e){
            System.println(e.toString())
        }
    }
    @Command
    def printRecibo(){
        parcelaService.parcelaInstance=parcela
        parcelaService.creditoInstance=selectedCredito
      //  Executions.sendRedirect("/parcela/printParcela/")
    }

    @Command
    @NotifyChange(["pagamentos"])
    def showAllPagamentos(){

    }

    @Command
    @NotifyChange(['parcelas','selectedCredito','parcela','selectedPagamento','pagamentos','pagamento','contaCaixa','contaCliente','tEntradas'])
    def salvarParcela(){
        if(settingsService.getSettings().pagamentosEmOrdem){
             def pagamentos = Pagamento.findAllByCredito(selectedCredito).sort{it.id}
            for(Pagamento pagamento in pagamentos){
                if(!pagamento.pago){
                    if(selectedPagamento.id>pagamento.id){
                        info.value="Por favor efetue o pagamento da "+pagamento.descricao+" que é anterior a esta e ainda não foi paga!"
                        info.style ="color:red;font-weight;font-size:12pt;background:back"
                        selectedPagamento = pagamento
                    }
                }

            }
        }


        try {
            if(parcela.dataDePagamento>new Date()){
                parcela.dataDePagamento = new Date()
                info.value = "Data inválida!"
                info.style = "color:red;font-weight;font-size:16px;background:back"

            }
            if(Parcela.findById(parcela.id)){
                info.value = "Este Pagamento já foi lançado!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            if(parcela.formaDePagamento=="transferencia bancária"||parcela.formaDePagamento=="deposito bancário"){
                if(selectedConta==null){
                    info.value = "Selecione uma conta Destino válido!"
                    info.style = "color:red;font-weight;font-size:16px;background:back"
                    return
                }
            }
            if(0>=parcela.valorPago){
                info.value = "Valor inválido!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            if(parcela.dataDePagamento==null){
                info.value = "Data inválido!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            if(parcela.formaDePagamento==null){
                info.value = "Forma de Pagamento não foi selecionado!"
                info.style = "color:red;font-weight;font-size:14pt;background:back"
                return
            }
            if(contaCliente==null){
                info.value = "ESte cliente não tem conta!"
                info.style = "color:red;font-weight;font-size:14pt;background:back"
                return
            }
            info.value = ""
            if(selectedPagamento.pago){
                info.value = "Esta Parcela já foi paga na Totalidade!"
                info.style = "color:red;font-weight;font-size:14pt;background:back"
                return
            }
            if(contaCaixa==null){
                info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
                info.style ="color:red;font-weight;font-size:14pt;background:back"
                selectedPagamento=null
                return
            }
            List<Pagamento> pagamentoss= new LinkedList<Pagamento>(Pagamento.findAllByCredito(selectedCredito))
            // parcela.setNumeroDoRecibo(contadorService.gerarNumeroDaParcela()) // Removed to allow duplicate receipt numbers
            if (parcela.numeroDoRecibo == null || parcela.numeroDoRecibo.isEmpty()) {
                parcela.setNumeroDoRecibo(contadorService.gerarNumeroDaParcela())
            }
            Pagamento pagamento = Pagamento.findById(selectedPagamento.id)
            parcela.pagamento = pagamento
            parcela.diario = diario
            Utilizador util = Utilizador.findById(utilizador.id)
            parcela.utilizador = util
            BigDecimal totalEmDivida = parcela.pagamento.totalEmDivida*(-1)
            BigDecimal valorParcial = parcela.valorPago
            if(parcela.valorPago > totalEmDivida){
                BigDecimal valorRemanescente = parcela.valorPago - totalEmDivida
                parcela.valorParcial = totalEmDivida

                if(pagamento.parcelas==null){
                    pagamento.parcelas = new LinkedHashSet<Parcela>()
                }

                pagamento.parcelas.add(parcela)
                parcela.save(failOnError: true)
                pagamento.merge(failOnError: true)

                // Distribute the remaining value to subsequent installments
                for(int x=0;x<pagamentoss.size(); x++){
                    if(valorRemanescente > 0){
                        Pagamento proximoPagamento = Pagamento.findById(pagamentoss[x].id)

                        if(!proximoPagamento.getPago()){
                            BigDecimal dividaProximoPagamento = proximoPagamento.totalEmDivida*(-1)
                            if(dividaProximoPagamento >= valorRemanescente){
                                Parcela novaParcela = new Parcela()
                                novaParcela.diario = diario
                                novaParcela.pagamento = proximoPagamento
                                novaParcela.valorParcial = valorRemanescente
                                novaParcela.dataDePagamento = parcela.dataDePagamento
                                novaParcela.formaDePagamento = parcela.formaDePagamento
                                novaParcela.descricao="*Amortização da dívida*"+selectedPagamento.id
                                novaParcela.numeroDoRecibo=parcela.numeroDoRecibo
                                novaParcela.utilizador = util
                                if(proximoPagamento.parcelas==null){
                                    proximoPagamento.parcelas = new LinkedHashSet<Parcela>()
                                }
                                proximoPagamento.parcelas.add(novaParcela)
                                novaParcela.save(failOnError: true)
                                proximoPagamento.merge(failOnError: true)
                                valorRemanescente = 0 // All remaining value distributed
                            }else {
                                Parcela novaParcela = new Parcela()
                                novaParcela.diario = diario
                                novaParcela.pagamento = proximoPagamento
                                novaParcela.valorParcial = dividaProximoPagamento
                                novaParcela.dataDePagamento = parcela.dataDePagamento
                                novaParcela.formaDePagamento = parcela.formaDePagamento
                                novaParcela.descricao="*Amortização da dívida*"+selectedPagamento.id
                                novaParcela.numeroDoRecibo = parcela.numeroDoRecibo
                                novaParcela.utilizador = util
                                if(proximoPagamento.parcelas==null){
                                    proximoPagamento.parcelas = new LinkedHashSet<Parcela>()
                                }
                                proximoPagamento.parcelas.add(novaParcela)
                                novaParcela.save(failOnError: true)
                                proximoPagamento.merge(failOnError: true)
                                valorRemanescente -= dividaProximoPagamento // Update remaining value
                            }
                        }
                    }else break
                }


            }else {
                if(pagamento.parcelas==null){
                    pagamento.parcelas = new LinkedHashSet<Parcela>()
                }

                pagamento.parcelas.add(parcela)
                parcela.valorParcial = parcela.valorPago
                parcela.save(failOnError: true)
                pagamento.merge(failOnError: true)
              //  lancamentos(parcela)

            }

            if(parcela.formaDePagamento=="transferencia bancária"||parcela.formaDePagamento=="deposito bancário"){
                saida.formaDePagamento = parcela.formaDePagamento
                saida.valor = valorParcial
                saida.dataDePagamento = parcela.dataDePagamento
                saida.descricao=parcela.descricao

                salvarSaida()
            }

            sessionStorageService.parcela = parcela
            sessionStorageService.credito = selectedCredito
            info.value = "Pagamento efetivado com sucesso!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
            bt_salvar.label = "Pago!"

          //  selectedPagamento = Pagamento.findById(selectedPagamento.id)
           // Executions.sendRedirect("/parcela/printParcela/")
        }catch(Exception e){
            info.value = "Erro na gravação dos dados!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
            System.println(e.toString())
        }

    }
    def lancamentos(Parcela parcel){

        Transferencia t = new Transferencia()
        t.utilizador = utilizador
        t.origem = Conta.findByNumeroDaConta(selectedCredito.cliente.id.toString())
        t.destino = Conta.findById(contaCaixa.id)
        t.descricao = parcel.descricao+"-"+parcela.formaDePagamento
        t.valor = parcel.valorParcial
        t.diario = diario
        t.save(failOnError: true)
        Transacao tCredito = new Transacao()
        Transacao tDebito = new Transacao()
        tCredito.valor = parcel.valorParcial
        tCredito.descricao=parcel.descricao+"-"+parcela.formaDePagamento
        tDebito.descricao = parcel.descricao+"-"+parcela.formaDePagamento
        tDebito.valor = parcel.valorParcial
        tCredito.credito = true
        tDebito.credito = false
        def credora = Conta.findById(contaCliente.id)
        def devedora = Conta.findById(contaCaixa.id)
        if (credora?.transacoes == null) {
            credora.transacoes = new LinkedHashSet<Transacao>()
        }
        if (devedora?.transacoes == null) {
            devedora.transacoes = new LinkedHashSet<Transacao>()
        }
        tCredito.save(failOnError: true)
        tDebito.save(failOnError: true)
        credora.transacoes.add(tCredito)
        devedora.transacoes.add(tDebito)
        credora.merge(failOnError: true)
        devedora.merge(failOnError: true)
        info.value = "Operações feitas com sucesso!"
        info.style = "color:red;font-weight;font-size:14pt;background:back"
    }
    def lancamentoEntrada(Parcela parcel){

        Transferencia t = new Transferencia()
        t.utilizador = utilizador
        t.parcela = parcel
        t.origem = Conta.findById(selectedConta.id)
        t.destino = Conta.findById(contaCaixa.id)
        t.descricao = parcel.descricao+"-"+parcel.id
        t.valor = parcel.valorPago
        t.diario = diario
        t.save(failOnError: true)
        Transacao tCredito = new Transacao()
        Transacao tDebito = new Transacao()
        tCredito.valor = parcel.valorPago
        tCredito.descricao=parcel.descricao+"-"+parcel.id
        tDebito.descricao = parcel.descricao+"-"+parcel.id
        tDebito.valor = parcel.valorPago
        tCredito.credito = true
        tDebito.credito = false
        def credora = Conta.findById(selectedConta.id)
        def devedora = Conta.findById(contaCaixa.id)
        if (credora?.transacoes == null) {
            credora.transacoes = new LinkedHashSet<Transacao>()
        }
        if (devedora?.transacoes == null) {
            devedora.transacoes = new LinkedHashSet<Transacao>()
        }
        tCredito.save(failOnError: true)
        tDebito.save(failOnError: true)
        credora.transacoes.add(tCredito)
        devedora.transacoes.add(tDebito)
        credora.merge(failOnError: true)
        devedora.merge(failOnError: true)
        info.value = "Operações feitas com sucesso!"
        info.style = "color:red;font-weight;font-size:14pt;background:back"
    }
    def lancarRemissao(Remissao remis){

        Transferencia t = new Transferencia()
        t.utilizador = utilizador
        t.destino = Conta.findByNumeroDaConta(selectedCredito.cliente.id.toString())
        t.origem = Conta.findById(selectedConta.id)
        t.descricao = remis.descricao+"-"+remissao.id
        t.valor = remis.valorDaRemissao
        t.diario = diario
        t.save(failOnError: true)
        Transacao tCredito = new Transacao()
        Transacao tDebito = new Transacao()
        tCredito.valor = remis.valorDaRemissao
        tCredito.descricao=remis.descricao+"-"+remis.id
        tDebito.descricao = remis.descricao+"-"+remissao
        tDebito.valor = remis.valorDaRemissao
        tCredito.credito = true
        tDebito.credito = false
        def credora = Conta.findById(selectedConta.id)
        def devedora = Conta.findByNumeroDaConta(selectedCredito.cliente.id.toString())
        if (credora?.transacoes == null) {
            credora.transacoes = new LinkedHashSet<Transacao>()
        }
        if (devedora?.transacoes == null) {
            devedora.transacoes = new LinkedHashSet<Transacao>()
        }
        tCredito.save(failOnError: true)
        tDebito.save(failOnError: true)
        credora.transacoes.add(tCredito)
        devedora.transacoes.add(tDebito)
        credora.merge(failOnError: true)
        devedora.merge(failOnError: true)
        info.value = "Operações feitas com sucesso!"
        info.style = "color:red;font-weight;font-size:14pt;background:back"
    }
    Utilizador getUtilizador() {
        utilizador = springSecurityService.currentUser as Utilizador
        return utilizador
    }

    Conta getContaCliente() {
       return contaCliente
    }

    @Command
    @NotifyChange(['selectedPagamento','caixas','contaCaixa'])

    def verificarCondicoes(){
        info.value= ""
       // def pagamentos = Pagamento.findAllByCredito(selectedCredito)
       // def totalCreditoEmDivida = 0.0
      //  pagamentos.each {totalCreditoEmDivida+=it.totalEmDivida}
        if(parcela.valorPago>selectedCredito.valorEmDivida*(-1)){
            info.value = "O valor alocado (" +parcela.valorPago +") não deve ser maior que o total em dívida ("+ selectedCredito.valorEmDivida +") das prestações!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
            parcela.valorPago = selectedCredito.valorEmDivida*(-1)
            return
        }
        if(selectedPagamento.pago){
            info.value = "Esta Parcela já foi paga na Totalidade!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
            return
        }
        if(selectedPagamento.totalEmDivida.setScale(2, RoundingMode.HALF_EVEN)*(-1)>parcela.valorPago.setScale(2, RoundingMode.HALF_EVEN)){
            info.value = "O valor não cobre o total em dívida"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
        }
        if(selectedPagamento.totalEmDivida.setScale(2, RoundingMode.HALF_EVEN)*(-1)==parcela.valorPago.setScale(2, RoundingMode.HALF_EVEN)){
            info.value = ""
            info.style = "color:red;font-weight,font-size:14pt;background:back"
        }
        if(selectedPagamento.totalEmDivida.setScale(2, RoundingMode.HALF_EVEN)*(-1)<parcela.valorPago.setScale(2,RoundingMode.HALF_EVEN)){
            System.println(selectedPagamento.totalEmDivida*(-1)-parcela.valorPago)
            info.value = "O valor é superior ao valor em dívida desta prestação"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
        }
    }

    @Command
    def cleanInfo(){
        info.value=""
    }
    @Command
    @NotifyChange(['selectedPagamento',"parcela","selectedCliente"])
    def calcularMoras(){
        pagamentoService.calcularMoras(selectedPagamento,parcela)
        verificarCondicoes()
    }



    Parcela getParcela() {
        return parcela
    }

    void setParcela(Parcela parcela) {
        this.parcela = parcela
    }

    @NotifyChange(['selectedPagamento','saida','parcelaEntrada',"remissao","pagamentos"])
    @Command
    void fecharEditor(){
        info.value = ""
        selectedPagamento=null
        saida= null
        parcelaEntrada = null
        bt_fechar_caixa.visible =true
        dv_filtragem.visible = true
        lb_pagamentos.visible = true
        remissao = null
        pagamentos.clear()
    }

    @Command
    @NotifyChange(['selectedPagamentoo','selectedRemissao','remissoes',"selectedCreditoo","pagamentoss"])
    def fecharEditorRemissao(){
        selectedPagamentoo=null
        remissao = null
    }
    @Command
    @NotifyChange(["diario"])
    def mudarDiario(){
        diario = getDiario()
        if(diario.estado =="pendente"){
            if(diarioService.findByEstado("aberto")){
                diario = diarioService.findByEstado("aberto")
                bt_diario.style = "background:#7fb3d5;font-size:26px"
            }

        }else {
            if(diarioService.findByEstado("pendente")){
                diario = diarioService.findByEstado("pendente")
                bt_diario.style = "background:#f1948a;font-size:26px"
            }

        }
    }
    Diario getDiario() {
        return diario
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    Pagamento getSelectedPagamento() {

        return selectedPagamento
    }

    void setSelectedPagamento(Pagamento selectedPagamento) {
        bt_salvar.label="Salvar"
        this.selectedPagamento = selectedPagamento
    }

    String getFilter() {
        return filter
    }

    void setFilter(String filter) {
        this.filter = filter
    }

    @Init init() {
        getUtilizador()
        diario = diarioService.getDiario()
      //  sessionStorageService.parcela=null
      //  sessionStorageService.credito = null
       // utilizador = Utilizador.findById(springSecurityService.principal?.id)

       /* if(pagamentoService.pagamentoInstance?.id!=null){
          pagamento_id=pagamentoService.pagamentoInstance.numeroDePagamento

        }*/


    }
    @Command
    @NotifyChange(['selectedPagamento',"parcela","contaCliente","contaCaixa","saida","diario"])
    def  showSelectedPagamento(){
        info.value = ""
        diario = diarioService.getDiario()
        if(utilizador?.contas==null){
            info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
            info.style ="color:red;font-weight;font-size:14pt;background:back"
            selectedPagamento=null
            return
        }

        parcela = new Parcela()
        parcela.dataDePagamento = new Date()
        parcela.formaDePagamento = "numerário"
        parcela.descricao = "Amortização da dívida"
        saida = null
        remissao = null
      // contaCliente = Conta.findByNumeroDaConta(selectedPagamento.credito.cliente.id.toString())
        contaCliente = contadorService.getByNumeroDacoonta(selectedCliente.id.toString())
      //  def pagamentos = Pagamento.findAllByCredito(selectedPagamento.credito)
        def pagamentos = pagamentoService.getByCredito(selectedCredito)
        for(Pagamento pagamento in pagamentos){
            if(!pagamento.pago){
                if(selectedPagamento.id>pagamento.id){
                    info.value="Por favor efetue o pagamento da "+pagamento.descricao+" que é anterior a esta e ainda não foi paga!"
                    info.style ="color:red;font-weight;font-size:12pt;background:back"
                }
            }

        }


    }

    @Command
    @NotifyChange(["remissao","selectedPagamentoo","selectedRemissao"])
    def showSelectedPag(){
        remissao = new Remissao()
        selectedPagamentoo.getTotalEmDivida()
        selectedRemissao = null
    }
    @Command
    @NotifyChange(["selectedPagamento",'parcela',"contaCliente","saida","pagamento_id"])
    findItem(){
        info.value=""
           if(utilizador?.contas==null){
            info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
            info.style ="color:red;font-weight;font-size:14pt;background:back"
            selectedPagamento=null
            return
        }


        if(contaCaixa==null){
            info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
            info.style ="color:red;font-weight;font-size:14pt;background:back"
            selectedPagamento=null
            return
        }
        parcela = new Parcela()
        parcela.diario = diario
        parcela.dataDePagamento = new Date()
        parcela.formaDePagamento = "numerário"
        parcela.descricao = "Amortização da dívida"
        saida = null
        remissao = null
        selectedPagamento = Pagamento.findByNumeroDePagamento(id)


        if(selectedPagamento?.id!=null){
            //    Executions.sendRedirect("/pagamento/show/"+selectedPagamento.id)
            pagamentoService.calcularMoras(selectedPagamento)
            contaCliente = Conta.findByNumeroDaConta(selectedCredito.cliente.id.toString())
            pagamento_id=selectedPagamento.numeroDePagamento
            def pagamentos = Pagamento.findAllByCredito(selectedCredito)
            for(Pagamento pagamento in pagamentos){
                if(!pagamento.pago){
                    if(selectedPagamento.id>pagamento.id){
                        info.value="Por favor efetue o pagamento da "+pagamento.descricao+" que é anterior a esta e ainda não foi paga!"
                        info.style ="color:red;font-weight;font-size:12pt;background:back"
                    }
                }

            }
        }
    }


    ListModelList<Pagamento> getPagamentos() {
        if(pagamentos==null){
            pagamentos = new ArrayList<>()
        }
        if(allPagamentos){
            pagamentos = Pagamento.findAllByCredito(selectedCredito).sort{it.id}
        }else
            pagamentos = Pagamento.findAllByCreditoAndPago(selectedCredito,false).sort{it.id}
        return pagamentos
    }

    ListModelList<Pagamento> getPagamentoss() {
        if(pagamentoss==null){
            pagamentoss = new ArrayList<>()
        }
            pagamentoss = Pagamento.findAllByCreditoAndPago(selectedCreditoo,false).sort{it.id}
        return pagamentoss
    }

    @NotifyChange(["parcelas","sParcela"])
    @Command
    void  deleteRecibo(){

        try {
            Utilizador user = springSecurityService.currentUser as Utilizador
            if (!user.authorities.any { it.authority == "PARCELA_DELETE" }) {
                info.value="Este utilizador não tem permissão para executar esta acção !"
                info.style = "color:red;font-weight;font-size:14pt;background:back"
                return
            }
            selectedPagamento.parcelas.remove(sParcela)
            selectedPagamento.merge(failOnError: true)
          sParcela.delete(failOnError: true)
            info.value="O Pagamento selecionada foi eliminado com sucesso!"
            info.style ="color:red;font-weight;font-size:14pt;background:back"

        }catch(Exception e){
            System.println(e.toString())
            info.value="Erro na eliminação do recibo!"
            info.style ="color:red;font-weight;font-size:14pt;background:back"
        }
    }

    @Command
    @NotifyChange(["selectedRemissao"])
    def showDelMessage(){
        info.value="Faça double click para eliminar o item "+sParcela.descricao
        info.style ="color:red;font-weight;font-size:14pt;background:back"
    }

    @NotifyChange(["pagamentos","info","selectedRemissao","remissao"])
    @Command
    void doSearch() {
        info.value = ""
        pagamentos.clear()
        remissoes.clear()
        List<Pagamento> allItems = Pagamento.findAllByCredito(selectedCredito)
        List<Remissao> allItem = Remissao.all
        if (filter == null || "".equals(filter)) {
           pagamentos.clear()
            remissoes.clear()
            selectedRemissao = null
            remissao = null

        } else {
           /* for (Pagamento item : allItems) {
                if (item.credito.cliente.nome.toString().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getId().toString().indexOf(filter) >= 0 ||
                        item.dataPrevistoDePagamento.format('dd/MM/yyyy').toString().indexOf(filter) >= 0 ||
                        item.dataPrevistoDePagamento.format('dd-MM-yyyy').toString().indexOf(filter) >= 0 ||
                        item.pago.toString().toLowerCase().indexOf(filter) >= 0 ||
                        item.valorPago.toString().indexOf(filter) >= 0 ||
                        item.valorPagoAmortizacao.toString().indexOf(filter) >= 0 ||
                        item.valorDeJuros.toString().indexOf(filter) >= 0 ||
                        item.numeroDePagamento.indexOf(filter) >= 0 ||
                        item.totalEmDivida.toString().indexOf(filter) >= 0 ||
                        item.credito.cliente.nome.toLowerCase().indexOf(filter) >= 0 ||
                        item.diasDeMora.toString().indexOf(filter) >= 0 ||
                        item.valorDaPrestacao.toString().indexOf(filter) >= 0 ||
                        item.descricao.toLowerCase().indexOf(filter) >= 0) {
                    pagamentos.add(item)
                }

            }*/
            for (Remissao item : allItem) {
                if (item.pagamento.credito.cliente.nome.toString().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item.getId().toString().indexOf(filter) >= 0 ||
                        item.createdDate.format('dd/MM/yyyy').toString().indexOf(filter) >= 0 ||
                        item.createdDate.format('dd-MM-yyyy').toString().indexOf(filter) >= 0 ||
                        item.diario.numeroDoDiario.toString().toLowerCase().indexOf(filter) >= 0 ||
                        item.descricao.toString().indexOf(filter) >= 0 ||
                       item.contaOrigem.designacaoDaConta.toLowerCase().indexOf(filter) >= 0) {
                    remissoes.add(item)
                }

            }
        }
    }

    @Command
    def alertDelete(){
        Utilizador user = springSecurityService.currentUser as Utilizador
        if (!user.authorities.any { it.authority == "REMISSAO_DELETE" }) {
            info.value="Este utilizador não tem permissão para executar esta acção !"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
        }
        else if(selectedRemissao?.pagamento?.credito?.invalido){
            info.value="Este credito já foi invalidado!"
            info.style = "color:red;font-weight;font-size:14ptp;background:back"

        }

        else {
            info.value=" Faça Double Click para executar esta operação!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
        }
    }
    @Command
    @NotifyChange(["selectedRemissao","remissoes","selectedPagamento"])
    def deleteRemissao(){
        Utilizador user = springSecurityService.currentUser as Utilizador
        if (!user.authorities.any { it.authority == "REMISSAO_DELETE" }) {
            info.value="Este utilizador não tem permissão para executar esta acção !"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
            return
        }
        if (selectedRemissao.id == null) {
            info.value="Seleccione uma remissão!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
            return
        }
        try {
            selectedRemissao = Remissao.findById(selectedRemissao.id)
            def contaCapital = Conta.findById(selectedRemissao.contaOrigem.id)
            Transacao tCredito = new Transacao()
            Transacao tDebito = new Transacao()
            tCredito.setValor(selectedRemissao.valorDaRemissao)
            tDebito.setValor(selectedRemissao.valorDaRemissao)
            tCredito.descricao = "estorno da remissão da parcela  Nº:"+selectedRemissao.pagamento.numeroDePagamento
            tDebito.descricao =  "estorno da remissão da Parcela Nº:"+selectedRemissao.pagamento.numeroDePagamento
            System.println("tCredito"+tCredito.valor)
            System.println("tDebito"+tDebito.valor)
            tCredito.credito = true
            tDebito.credito = false
            tCredito.save()
            tDebito.save()
            contaCliente = Conta.findByNumeroDaConta(selectedRemissao.pagamento.credito.cliente.id.toString())
            if (contaCapital.transacoes == null) {
                contaCapital.transacoes = new LinkedHashSet<Transacao>()
            }
            if (contaCliente.transacoes == null) {
                contaCliente.transacoes = new LinkedHashSet<Transacao>()
            }
            contaCapital.transacoes.add(tDebito)
            contaCliente.transacoes.add(tCredito)
            contaCapital.merge(failOnError: true)
            contaCliente.merge(failOnError: true)
            def pagamentoDb = Pagamento.findById(selectedRemissao.pagamento.id)
            System.println(pagamentoDb)
            pagamentoDb.remissoes.remove(selectedRemissao)
            selectedRemissao.delete()
            pagamentoDb.merge failOnError: true

            info.value="REMISSÃO DA DÍVIDA invalidado com sucesso!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"

        }catch(Exception e){
            System.println(e.toString())
            info.value = "Erro na eliminação da remissão!"
            info.style = "color:red;font-weight;font-size:14pt;background:back"
        }

    }
}
