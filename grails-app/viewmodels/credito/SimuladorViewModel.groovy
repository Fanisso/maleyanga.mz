package credito

import grails.plugin.springsecurity.SpringSecurityService
import mz.maleyanga.CreditoService
import mz.maleyanga.PagamentoService
import mz.maleyanga.SettingsService
import mz.maleyanga.SimuladorService
import mz.maleyanga.TaxaService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.ContaService
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.security.Utilizador
import mz.maleyanga.settings.DefinicaoDeCredito
import mz.maleyanga.settings.Settings
import org.springframework.stereotype.Service
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList
@Service
class SimuladorViewModel {
    private Date dataPrevista
    String selecedFormaDeCalculo
    SimuladorService simuladorService
    private  Cliente selectedCliente
    int numeroDePrestacoes
    String selecedPeriodicidade
    TaxaService taxaService
    ContaService contaService
    PagamentoService pagamentoService
    CreditoService creditoService
    SpringSecurityService springSecurityService
    Credito credito = new Credito()
    SettingsService settingsService
    Settings settings
    String juros
    String jurosDeMora
    String formaDeCalculo
    DefinicaoDeCredito selectedDefinicaoDeCredito
    private ListModelList<DefinicaoDeCredito> definicoes
    Double totalPrestacoes = 0
    Double totalamortizacao=0
    Double totaljuros = 0
    private ListModelList<Cliente> clientes
    private ListModelList<Pagamento> prestacoes
    @Wire Label info
    String red = "color:red;font-size:14pt"
    String blue = "color:blue;font-size:14pt"
    boolean bt_print

    Date getDataPrevista() {
        return dataPrevista
    }

    @NotifyChange(["prestacoes"])
    void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista
        Date validade
        Calendar c = Calendar.getInstance()
        def map = [:]
        if(selectedDefinicaoDeCredito){

            1.upto(selectedDefinicaoDeCredito.numeroDePrestacoes) {

                if (selectedDefinicaoDeCredito.periodicidade == "mensal") {
                    c.add(Calendar.MONTH, 1)
                }
                if (selectedDefinicaoDeCredito.periodicidade == "quinzenal") {
                    c.add(Calendar.DATE, 15)
                }
                if (selectedDefinicaoDeCredito.periodicidade == "semanal") {
                    c.add(Calendar.DATE, 7)
                }
                if (selectedDefinicaoDeCredito.periodicidade == "diario") {
                    c.add(Calendar.DATE, 1)
                }
                if (selectedDefinicaoDeCredito.periodicidade == "doisdias") {
                    c.add(Calendar.DATE, 2)
                }
                if (selectedDefinicaoDeCredito.periodicidade == "variavel") {
                    int variavel = selectedDefinicaoDeCredito.periodoVariavel as int
                    selectedDefinicaoDeCredito.periodoVariavel = variavel as Integer
                    c.add(Calendar.DATE, variavel)
                }
            }
            c.add( Calendar.DATE,30)
            validade = c.getTime()
            Date agora = new Date()
            int dias = validade-agora

            getPrestacoes()




            }
    }

    @NotifyChange(["selectedDefinicaoDeCredito"])
    @Command
    def clean (){
        info.value = ""
        selectedDefinicaoDeCredito = null

        Calendar c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH,20)
        def map = [:]


        Date hoje = new Date()
        while (hoje.before(fim)){
            c.add(Calendar.DAY_OF_MONTH,1)
            map.put(c.getTime(),"VALIDO")
        }



    }
    String getSelecedFormaDeCalculo() {
        return selecedFormaDeCalculo
    }

    @NotifyChange(["selectedDefinicaoDeCredito"])
    void setSelecedFormaDeCalculo(String selecedFormaDeCalculo) {
        this.selecedFormaDeCalculo = selecedFormaDeCalculo
        selectedDefinicaoDeCredito.formaDeCalculo = selecedFormaDeCalculo
    }

    boolean getBt_print() {
        return bt_print
    }

    void setNumeroDePrestacoes(int numeroDePrestacoes) {
        this.numeroDePrestacoes = numeroDePrestacoes
    }

    String getSelecedPeriodicidade() {
        return selecedPeriodicidade
    }

    @NotifyChange(["selectedDefinicaoDeCredito"])
    void setSelecedPeriodicidade(String selecedPeriodicidade) {
        cb_def.children.clear()

        selectedDefinicaoDeCredito = null

        this.selecedPeriodicidade = selecedPeriodicidade
    }

    ListModelList<DefinicaoDeCredito> getDefinicoes() {

        if(definicoes==null){
            definicoes = new ListModelList<>(DefinicaoDeCredito.all)
        }

        return definicoes
    }
    DefinicaoDeCredito getSelectedDefinicaoDeCredito() {
        return selectedDefinicaoDeCredito
    }

    @NotifyChange(["selecedPeriodicidade","selectedDefinicaoDeCredito"])
    void setSelectedDefinicaoDeCredito(DefinicaoDeCredito selectedDefinicaoDeCredito) {
        this.selectedDefinicaoDeCredito = selectedDefinicaoDeCredito

    }

    Double getTotalPrestacoes() {

        return totalPrestacoes
    }

    Double getTotalamortizacao() {
        return totalamortizacao
    }

    Double getTotaljuros() {
        return totaljuros
    }

    ListModelList<Pagamento> getPrestacoes() {
        prestacoes=null
        if(prestacoes==null){
            prestacoes = new ListModelList<Pagamento>()
        }

        if (credito.periodicidade!=""&&credito.valorCreditado>0&&credito.numeroDePrestacoes){
            def prestacoes = pagamentoService.simuladorDeCredito(credito)
            totalPrestacoes = 0
            totaljuros = 0
            totalamortizacao = 0
            for(Pagamento p in prestacoes){
                totaljuros +=p.valorDeJuros
                totalamortizacao+=p.valorDeAmortizacao
                totalPrestacoes+=p.valorDaPrestacao
            }
            return  prestacoes
        }


    }

    Cliente getSelectedCliente() {
        return selectedCliente
    }

    void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente
    }

    @Init init() {
        settings = settingsService.getSettings()
    }
    ListModelList<Cliente> getClientes() {
        if(clientes==null){
            clientes = new ListModelList<Cliente>(Cliente.findAllWhere(ativo: true))
        }
        return clientes
    }
    Credito getCredito() {
        return credito
    }



    void setCredito(Credito credito) {
        this.credito = credito
    }

    Settings getSettings() {
        return settings
    }

    String getFormaDeCalculo() {
        return formaDeCalculo
    }

    @Command
    @NotifyChange(["juros","jurosDeMora","formaDeCalculo","info","prestacoes","totalPrestacoes","totaljuros","totalamortizacao","credito","bt_print"])
    def showDetails(){
        bt_print= true
        credito.numeroDePrestacoes = numeroDePrestacoes
        if(selectedDefinicaoDeCredito!=null){

            credito.periodicidade = selectedDefinicaoDeCredito.periodicidade

            credito.formaDeCalculo = selectedDefinicaoDeCredito.formaDeCalculo
            credito.percentualJurosDeDemora = selectedDefinicaoDeCredito.percentualJurosDeDemora
            credito.percentualDejuros = selectedDefinicaoDeCredito.percentualDejuros
            juros=selectedDefinicaoDeCredito.percentualDejuros
            jurosDeMora=selectedDefinicaoDeCredito.percentualJurosDeDemora
            formaDeCalculo = selectedDefinicaoDeCredito.formaDeCalculo


           /* if (selectedDefinicaoDeCredito.numeroDePrestacoes<credito.numeroDePrestacoes){
                info.value = "O número de prestações não permitido"
                info.style = red
                credito.numeroDePrestacoes = selectedDefinicaoDeCredito.numeroDePrestacoes
            }*/
            getPrestacoes()
        }





    }

    String getJuros() {
        return juros
    }

    String getJurosDeMora() {
        return jurosDeMora
    }


    @Command print(){
        simuladorService.jurosDeMora = getJurosDeMora()
        simuladorService.juros = getJuros()
        simuladorService.settings = getSettings()
        simuladorService.credito = getCredito()
        simuladorService.taxaFixa = selectedDefinicaoDeCredito.formaDeCalculo
        simuladorService.totalamortizacao = getTotalamortizacao()
        simuladorService.totaljuros = getTotaljuros()
        simuladorService.totalPrestacoes = getTotalPrestacoes()
        simuladorService.prestacoes = getPrestacoes()
        simuladorService.settings= getSettings()
        simuladorService.credito=getCredito()
        Executions.sendRedirect("/credito/printSimulador")
    }



}
