package parcela

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import mz.maleyanga.SessionStorageService
import mz.maleyanga.conta.Conta
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.ParcelaService
import mz.maleyanga.security.Utilizador
import mz.maleyanga.transacao.Transacao
import mz.maleyanga.transferencia.Transferencia
import org.springframework.security.access.method.P
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList

import java.math.RoundingMode
@Transactional
@Service
class RecibosViewModel {
    Conta contaCliente
    Parcela parcela
    Conta contaCaixa
    Utilizador utilizador
    ParcelaService parcelaService
    SessionStorageService sessionStorageService
    SpringSecurityService springSecurityService

    Conta getContaCliente() {
        return contaCliente
    }

    Utilizador getUtilizador() {
        return utilizador
    }
    @Wire Label info
    private  String filter
    private  Long id
    private  Parcela selectedParcela
    private ListModelList<Parcela> parcelas

    Conta getContaCaixa() {
        return   contaCaixa
    }
    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Parcela getSelectedParcela() {
        return selectedParcela
    }

    void setSelectedParcela(Parcela selectedParcela) {
        this.selectedParcela = selectedParcela
        sessionStorageService.parcela = selectedParcela
    }

    String getFilter() {
        return filter
    }

    void setFilter(String filter) {
        this.filter = filter
    }
    @Init init() {
      //  utilizador = Utilizador.findById(springSecurityService.principal?.id)
       /* if(parcelaService?.parcelaInstance?.id!=null){
            selectedParcela  = Parcela.findById(parcelaService?.parcelaInstance?.id)
           contaCliente = Conta.findByNumeroDaConta(parcelaService?.parcelaInstance?.pagamento?.credito?.cliente?.id?.toString())
        }*/
    }
    @Command
    @NotifyChange(["selectedParcela","parcelas"])
    def delete(){
        try {
            Utilizador user = springSecurityService.currentUser as Utilizador
            if (!user.authorities.any { it.authority == "PARCELA_DELETE" }) {
                info.value="Este utilizador não tem permissão para executar esta acção !"
                info.style = "color:red;font-weight;font-size:11pt;background:back"
                return
            }
            def parcelas = Parcela.findAllByNumeroDoRecibo(selectedParcela.numeroDoRecibo)
            BigDecimal valor = 0.0
            for(Parcela parcela in parcelas){
                if(parcela.valorPago>0.0){
                    valor = parcela.valorPago
                    this.parcela = parcela
                    parcela.valorPagoBackup = valor
                }
                utilizador = Utilizador.findById(selectedParcela.utilizador.id)
                parcela.valorParcial = 0.0
                parcela.valorPago = 0.0
                parcela.pagamento =null
                parcela.invalido = true
               parcela.merge(flush: true)
            }
            Transferencia t = new Transferencia()
            t.utilizador = utilizador
            t.origem = Conta.findById(contaCaixa.id)
            t.destino = Conta.findById(contaCliente.id)
            String descricao = "Estorno de "+contaCaixa.designacaoDaConta+" a "+contaCliente.designacaoDaConta
            t.descricao = descricao
            t.valor = valor
            t.save(flush: true)

            Transacao tDebito = new Transacao()

            tDebito.descricao = descricao
            tDebito.valor = valor

            tDebito.credito = false
            def credora
            def devedora = Conta.findById(contaCliente.id)
            tDebito.save(flush: true)
            if(parcela.formaDePagamento=="numerário"||parcela.formaDePagamento=="Paga Fácil"||parcela.formaDePagamento=="POS"||parcela.formaDePagamento=="Outra") {
               credora = Conta.findById(contaCaixa.id)
                Transacao tCredito = new Transacao()
                tCredito.credito = true
                tCredito.valor = valor
                tCredito.descricao=descricao
                tCredito.save(flush: true)
                credora.transacoes.add(tCredito)
                credora.merge(flush: true)
            }else {
                credora = Conta.findById(contaCaixa.id)
                Transacao tCredito = new Transacao()
                tCredito.credito = true
                tCredito.valor = valor
                tCredito.descricao=descricao
                tCredito.save(flush: true)
                credora.transacoes.add(tCredito)
                credora.merge(flush: true)
            }
            devedora.transacoes.add(tDebito)
            devedora.merge(flush: true)
            getParcelas()

            info.value = "Operações feitas com sucesso!"
            info.style = "color:red;font-weight;font-size:11pt;background:back"
            parcelas.remove(selectedParcela)
        }catch(Exception e){
            System.println(e.toString())
        }

    }
    @Command
    def alertDelete(){
        Utilizador user = springSecurityService.currentUser as Utilizador
        if (!user.authorities.any { it.authority == "PARCELA_DELETE" }) {
            info.value="Este utilizador não tem permissão para executar esta acção !"
            info.style = "color:red;font-weight;font-size:11pt;background:back"
        }
        if(selectedParcela.invalido){
            info.value="Este recibo já foi invalidado!"
            info.style = "color:red;font-weight;font-size:11pt;background:back"

        }

        else {
            info.value="Double Click para eliminar este credito!"
            info.style = "color:red;font-weight;font-size:11pt;background:back"
        }
    }

    @NotifyChange(["contaCliente","contaCaixa"])
    @Command
    def  showSelectedParcela(){
        try {
            info.value = ""
            contaCliente = Conta.findByNumeroDaConta(selectedParcela?.pagamento?.credito?.cliente?.id?.toString())
            contaCaixa =  Conta.findByUtilizadorAndFinalidade(selectedParcela.utilizador,'conta_caixa')
            System.println(contaCaixa)
            System.println(contaCliente)
            printRecibo()
        }catch(Exception e){
            System.println(e.toString())
        }

    }

    @NotifyChange(["selectedParcela"])
    @Command
    def fecharEditor(){
        selectedParcela = null
    }
    @Command
    findItem(){
        selectedParcela = Parcela.findById(id)
        if(selectedParcela!=null){
         //   Executions.sendRedirect("/parcela/show/"+selectedParcela.id)
        }
    }

    @Command
    def printRecibo(){
        if(selectedParcela.invalido){
            info.value = "Recibo inválido!"
            info.style = "color:red;font-weight;font-size:11pt;background:back"
            return
        }
        if(selectedParcela==null){
            info.value = "Recibo inválido!"
            info.style = "color:red;font-weight;font-size:11pt;background:back"
            return
        }else {
            parcelaService.parcelaInstance=selectedParcela
            parcelaService.creditoInstance=selectedParcela?.pagamento?.credito
          //  Executions.sendRedirect("/parcela/printParcela/")
        }

    }

    ListModelList<Parcela> getParcelas() {
        if(parcelas==null){
            parcelas = new ArrayList<>()
        }
        return parcelas
    }

    @NotifyChange(["pagamentos","info"])
    @Command
    void doSearch() {
        info.value = ""
        parcelas.clear()
        List<Parcela> allItems = Parcela.findAllByValorPagoGreaterThan(0.0)
        if (filter == null || "".equals(filter)) {
            parcelas.clear()
        } else {
            for (Parcela item : allItems) {
                if (item.dataDePagamento.format('dd-MM-yyyy').toString().toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item?.numeroDoRecibo?.indexOf(filter) >= 0 ||
                        item.utilizador?.username?.toString()?.toLowerCase()?.indexOf(filter) >= 0 ||
                        item.pagamento?.credito?.cliente?.nome?.toString()?.toLowerCase()?.indexOf(filter) >= 0 ||
                        item.pagamento?.credito?.cliente?.nome?.toString()?.indexOf(filter) >= 0 ||
                        item.utilizador?.username?.toString()?.indexOf(filter) >= 0 ||
                        item?.pagamento?.id?.toString()?.indexOf(filter) >= 0 ||
                        item?.pagamento?.credito?.id?.toString()?.indexOf(filter) >= 0 ||
                        item.pagamento?.credito?.cliente?.nome?.toLowerCase()?.indexOf(filter) >= 0 ||
                       item.descricao.toLowerCase().indexOf(filter) >= 0) {
                    parcelas.add(item)
                }

            }
        }
    }



}
