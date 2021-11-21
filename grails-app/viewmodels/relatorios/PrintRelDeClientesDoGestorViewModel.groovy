package relatorios

import mz.maleyanga.ClienteService
import mz.maleyanga.RelatoriosService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.security.Utilizador
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList

@Service
class PrintRelDeClientesDoGestorViewModel {
    private  Utilizador gestor
    SessionStorageService sessionStorageService
    RelatoriosService relatoriosService
    ClienteService clienteService
    private  ListModelList<Credito> pendentes= new ListModelList<Credito>()
    private  ListModelList<Credito> abertos= new ListModelList<Credito>()
    private  ListModelList<Credito> fechados= new ListModelList<Credito>()
    private  ListModelList<Credito> emPagamento = new ListModelList<Credito>()
    private  ListModelList<Credito> invalidos= new ListModelList<Credito>()
    boolean aber
    boolean emPa
    boolean pend
    boolean fec
    boolean inval
    private BigDecimal totalAcreditadoAbertos
    private BigDecimal totalAcreditadoFechados
    private BigDecimal totalAcreditadoEmPagamento
    private BigDecimal totalPagoEmPagamento
    private BigDecimal totalAcreditadoPendentes
    private BigDecimal totalPagoPendentes
    private BigDecimal totalEmDividaAbertos
    private BigDecimal totalEmDividaFechados
    private BigDecimal totalPagoFechados
    private BigDecimal totalEmDividaEmPagamento
    private BigDecimal totalEmDividaPendentes
    Date ano

    BigDecimal getTotalPagoEmPagamento() {
        return totalPagoEmPagamento
    }

    BigDecimal getTotalPagoPendentes() {
        return totalPagoPendentes
    }

    BigDecimal getTotalPagoFechados() {
        return totalPagoFechados
    }

    BigDecimal getTotalAcreditadoAbertos() {
        return totalAcreditadoAbertos
    }

    BigDecimal getTotalAcreditadoFechados() {
        return totalAcreditadoFechados
    }

    BigDecimal getTotalAcreditadoEmPagamento() {
        return totalAcreditadoEmPagamento
    }

    BigDecimal getTotalAcreditadoPendentes() {
        return totalAcreditadoPendentes
    }

    BigDecimal getTotalEmDividaAbertos() {
        return totalEmDividaAbertos
    }

    BigDecimal getTotalEmDividaFechados() {
        return totalEmDividaFechados
    }

    BigDecimal getTotalEmDividaEmPagamento() {
        return totalEmDividaEmPagamento
    }

    BigDecimal getTotalEmDividaPendentes() {
        return totalEmDividaPendentes
    }

    boolean getAber() {
        return aber
    }

    boolean getEmPa() {
        return emPa
    }

    boolean getPend() {
        return pend
    }

    boolean getFec() {
        return fec
    }

    boolean getInval() {
        return inval
    }

    ListModelList<Credito> getPendentes() {
        if(pendentes==null){
            pendentes = new ListModelList<Credito>()
        }
        return pendentes
    }

    ListModelList<Credito> getAbertos() {
        if(abertos==null){
            abertos = new ListModelList<Credito>()
        }
        return abertos
    }

    ListModelList<Credito> getFechados() {
        if(fechados==null){
            fechados = new ListModelList<Credito>()
        }
        return fechados
    }

    ListModelList<Credito> getEmPagamento() {
        if(emPagamento==null){
            emPagamento = new ListModelList<Credito>()
        }
        return emPagamento
    }

    ListModelList<Credito> getInvalidos() {
        if(invalidos==null){
            invalidos = new ListModelList<Credito>()
        }
        return invalidos
    }

    @Init init() {
        // initialzation code here
        emPa = relatoriosService.emPagamento
        aber = relatoriosService.abertos
        pend = relatoriosService.pendentes
        inval = relatoriosService.invalidos
        fec =relatoriosService.fechados
        ano = relatoriosService?.ano
        gestor = sessionStorageService.getSelectedGestor() as Utilizador
        getAbertos()
        gerarRelatorio()

    }


    Utilizador getGestor() {
        return gestor
    }
    def gerarRelatorio(){
        Calendar cal = Calendar.getInstance()
        cal.setTime(relatoriosService.ano)
        int year = cal.get(Calendar.YEAR)
       totalAcreditadoAbertos =0.0
       totalAcreditadoFechados =0.0
       totalAcreditadoEmPagamento =0.0
       totalAcreditadoPendentes =0.0

       totalEmDividaAbertos =0.0
       totalEmDividaFechados =0.0
       totalEmDividaEmPagamento =0.0
       totalEmDividaPendentes =0.0
        totalPagoEmPagamento = 0.0
        totalPagoFechados = 0.0
        totalPagoPendentes = 0.0
        def clientes = Cliente.findAllByUtilizador(gestor)

        for(Cliente cliente in clientes){

            for(Credito credito in Credito.findAllByCliente(cliente)){
                Calendar ca = Calendar.getInstance()
                ca.setTime(credito.dateConcecao)
                int yea = ca.get(Calendar.YEAR)
                    if(year==yea){
                        if(aber){
                            if(credito.estado=="Aberto"){
                                abertos.add(credito)
                                totalAcreditadoAbertos+=credito.valorCreditado
                                totalEmDividaAbertos += credito.valorEmDivida
                            }
                        }
                        if(pend){
                            if (credito.estado=="Pendente"){
                                pendentes.add(credito)
                                totalAcreditadoPendentes+=credito.valorCreditado
                                totalEmDividaPendentes += credito.valorEmDivida
                                totalPagoPendentes+=credito.totalPago

                            }
                        }
                        if(fec){
                            if (credito.estado=="Fechado"){
                                fechados.add(credito)
                                totalAcreditadoFechados+=credito.valorCreditado
                                totalEmDividaFechados += credito.valorEmDivida
                                totalPagoFechados +=credito.totalPago
                            }
                        }
                        if (emPa){
                            if (credito.estado=="EmProgresso"){
                                emPagamento.add(credito)
                                totalAcreditadoEmPagamento+=credito.valorCreditado
                                totalEmDividaEmPagamento += credito.valorEmDivida
                                totalPagoEmPagamento +=credito.totalPago
                            }
                        }
                        if (inval){
                            if (credito.invalido){
                                invalidos.add(credito)

                            }
                        }
                    }




            }
        }

    }
}
