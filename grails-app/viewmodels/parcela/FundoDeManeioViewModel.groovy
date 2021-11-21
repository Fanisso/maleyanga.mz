package parcela

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import mz.maleyanga.DiarioService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.conta.Conta
import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.ParcelaService
import mz.maleyanga.saidas.Saida
import mz.maleyanga.security.Utilizador
import mz.maleyanga.transacao.Transacao
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Combobox
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

import java.math.RoundingMode
import java.sql.SQLException

@Service
@Transactional
class FundoDeManeioViewModel {
    SessionStorageService sessionStorageService
    private BigDecimal totalParcelas = 0.0
    private BigDecimal totalSaidas = 0.0
    ParcelaService parcelaService
    private @Wire Label info
    private @Wire Combobox cb_cd
    private Parcela parcelaEntrada
    private ListModelList<Saida> saidas
    private Diario diario
    private Saida saida
    private ListModelList<Conta> contas
    private Conta selectedConta
    private Utilizador utilizador
    private Conta contaManeio
    private ListModelList<Parcela> parcels
    private Saida selectedSaida
   private Date dataInicial
   private Date dataFinal

    SpringSecurityService springSecurityService
    DiarioService diarioService

    Date getDataInicial() {
        return dataInicial
    }

    @NotifyChange(["dataFinal","dataInicial"])
    void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial
        Calendar c = Calendar.getInstance()
        c.setTime(dataInicial)
        c.add(Calendar.MONTH, 1)
        c.add(Calendar.DAY_OF_MONTH,-1)
        dataFinal = c.getTime()
        sessionStorageService.dataInicial = dataInicial
        sessionStorageService.dataFinal = dataFinal
    }

    Date getDataFinal() {
        return dataFinal
    }

    @NotifyChange(["dataFinal","dataInicial"])
    void setDataFinal(Date dataFinal) {
        if(dataFinal<dataInicial){
            Calendar c = Calendar.getInstance()
            c.setTime(dataInicial)
            c.add(Calendar.DAY_OF_MONTH, 1)
            dataFinal = c.getTime()
            this.dataFinal = dataFinal

        }else {
            this.dataFinal = dataFinal
        }

        sessionStorageService.dataFinal= dataFinal
    }

    @NotifyChange(['saidas','saida','selectedSaida'])
    @Command
    void fecharEditor(){
        saida= null
        selectedSaida = null
    }

    @Command
    def printSaida(){

    }
    Saida getSelectedSaida() {
        return selectedSaida
    }

    @NotifyChange(["saida","selectedSaida"])
    void setSelectedSaida(Saida selectedSaida) {
        saida = null
        this.selectedSaida = selectedSaida
    }

    @NotifyChange(['contaManeio','caixas'])
    Conta getContaManeio() {
        contaManeio = Conta.findByUtilizadorAndFinalidade(getUtilizador(),'fundo_de_maneio')
        sessionStorageService.setConta(contaManeio)
        return   contaManeio
    }

    ListModelList<Conta> getContas() {
        if(contas==null){
            contas = new ListModelList<Conta>(Conta.findAllByFinalidadeOrFinalidadeOrFinalidadeOrFinalidade("fundo_de_maneio","conta_movimento","conta_caixa","conta_capital"))
        }

        return contas
    }

    List<Saida> getSaidas() {
        if(saidas==null){
            saidas = new ListModelList<Saida>(Saida.findAllByDiarioAndUtilizadorAndOrigem(diarioService.getDiario(),utilizador,contaManeio))
        }
        return saidas
    }
    Parcela getParcelaEntrada() {
        return parcelaEntrada
    }

    void setParcelaEntrada(Parcela parcelaEntrada) {
        this.parcelaEntrada = parcelaEntrada
    }
    Saida getSaida() {
        return saida
    }

    void setSaida(Saida saida) {
        this.saida = saida
    }

    @Command
    @NotifyChange(['contaManeio','tSaidas',"saidas"])
    def salvarSaida(){
        try {
            if(!contaManeio){
                info.value = "Este Utilizador não tem conta de fundo de maneio!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }
            if(Saida.findById(saida.id)){
                info.value = "Este movimento já foi lançado!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                saida = new Saida()
                return
            }
            if(0>=saida.valor){
                info.value = "Valor inválido!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
                return
            }

            if(saida.valor>contaManeio?.saldo?.setScale(2, RoundingMode.DOWN)){
                info.value = "O saldo de "+contaManeio.saldo.setScale(2,RoundingMode.DOWN)+", em caixa  não cobre o valor de "+saida.valor+" ,que pretende dar saida!"
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

            info.value = ""

            if(contaManeio==null){
                info.value="O Utilizador "+utilizador.username+" não tem nehuma conta associada de forma poder lançar pagamentos!"
                info.style ="color:red;font-weight;font-size:16px;background:back"
                return
            }

            saida.utilizador = getUtilizador()
            saida.contaOrigem=contaManeio.codigo
            saida.origem = contaManeio
            if(selectedConta!=null){
                saida.destino = selectedConta
                saida.contaDestino=selectedConta.codigo
            }
            saida.diario = diarioService.getDiario()
            saida.save(flush: true)
            if(!Saida.findById(saida.id)){
                info.value= "Erro na gravação dos dados"
                info.style ="color:red;font-weight;font-size:16px;background:back"
            }else {
                Transacao tCredito = new Transacao()
                Transacao tDebito = new Transacao()
                tCredito.valor = saida.valor
                tCredito.descricao=saida.descricao+"-"+saida.formaDePagamento
                tDebito.descricao = saida.descricao+"-"+saida.formaDePagamento
                tDebito.valor = saida.valor
                tCredito.credito = true
                tDebito.credito = false
                def credora = Conta.findById(contaManeio.id)
                def devedora = Conta?.findById(selectedConta?.id)
                if (credora.transacoes == null) {
                    credora.transacoes = new LinkedHashSet<Transacao>()
                }

                tCredito.save(flush: true)
                tDebito.save(flush: true)

                credora.transacoes.add(tCredito)

                credora.merge(flush: true)

                if(devedora!=null){
                    if (devedora.transacoes == null) {
                        devedora.transacoes = new LinkedHashSet<Transacao>()
                    }
                    devedora.transacoes.add(tDebito)
                    devedora.merge(flush: true)
                }


                parcelaService.saidaInstance = saida
                saidas.add(saida)

                info.value = "Operações feitas com sucesso!"
                info.style = "color:red;font-weight;font-size:16px;background:back"
            }


        }catch(SQLException e){
            info.value = "Erro na gravação dos dados!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            System.println(e.toString())
        }
    }

    Conta getSelectedConta() {
        return selectedConta
    }

    void setSelectedConta(Conta selectedConta) {
        this.selectedConta = selectedConta
    }

    Utilizador getUtilizador() {
        utilizador = springSecurityService.currentUser as Utilizador
        return utilizador
    }

    @Command
    @NotifyChange(["saida","parcela","selectedPagamento","parcelaEntrada","selectedSaida","contas","selectedConta"])
    def addSaida(){

        saida = new Saida()
        saida.dataDePagamento = new Date()
        saida.formaDePagamento = "numerário"
        selectedSaida = null
        selectedConta = null
    }
    @Command
    def alertDelete(){
        Utilizador user = springSecurityService.currentUser as Utilizador
        if (!user.authorities.any { it.authority == "SAIDA_DELETE" }) {
            info.value="Este utilizador não tem permissão para executar esta acção !"
            info.style = "color:red;font-weight;font-size:16px;background:back"
        }
        else if(selectedSaida.invalido){
            info.value="Este credito já foi invalidado!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
        }

        else {
            info.value="Double Click para eliminar esta operação!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
        }
    }

    @Command
    @NotifyChange(["saidas","saida","contaManeio"])
    def deleteSaida(){
        try {
            def credora = Conta.findById(selectedSaida?.destino?.id)
            def devedora = Conta.findById(selectedSaida.origem.id)
            Transacao tCredito = new Transacao()
            Transacao tDebito = new Transacao()
            if(credora){
                tCredito.valor = selectedSaida.valor
                tCredito.descricao="Estorno"
                tCredito.credito = true
                tCredito.save(flush: true)
                credora.transacoes.add(tCredito)
                credora.merge(flush: true)
            }
            tDebito.descricao = "Estorno"
            tDebito.valor = selectedSaida.valor
            tDebito.credito = false
            tDebito.save(flush: true)
            devedora.transacoes.add(tDebito)
            devedora.merge(flush: true)
            selectedSaida.delete(flush: true)

            info.value="MOVIMENTO invalidado com sucesso!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
            saidas.remove(selectedSaida)

        }catch(SQLException e){
            System.println(e.toString())
            info.value = "Erro na eliminação do movimento!"
            info.style = "color:red;font-weight;font-size:16px;background:back"
        }
    }
    List<Parcela> getParcels() {
        if (parcels==null){
            parcels = new ListModelList<Parcela>(Parcela.findAllByValorPagoGreaterThanAndDiarioAndUtilizador(0.0,diario,utilizador))
        }
        return parcels
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
    BigDecimal getSaldo() {
        return getTotalParcelas()-getTotalSaidas()
    }

}
