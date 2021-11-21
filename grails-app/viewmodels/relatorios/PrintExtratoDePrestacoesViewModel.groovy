package relatorios
import mz.maleyanga.SessionStorageService
import mz.maleyanga.SubPagamentos
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.pagamento.Pagamento
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Listbox
import org.zkoss.zul.Rows

@Service
class PrintExtratoDePrestacoesViewModel {
    Map map
    Set clientes = new HashSet<Cliente>()
    @Wire Rows rw_map = new Rows()
    @Wire Listbox lb_map = new Listbox()
    private  dataInicial
    private  dataFinal
    BigDecimal totalValorDaPrestacao
    BigDecimal totalPago
    BigDecimal totalDivida
    BigDecimal totalMoras
    boolean  excluirPagos
    boolean  excluirMoras
    List<String> keys
    ListModelList<SubPagamentos> subPagamentos

    List<String> getKeys() {

        if(keys==null){
            keys = new ListModelList<String>(map.keySet())
        }


        return keys
    }

    BigDecimal getTotalValorDaPrestacao() {
        return totalValorDaPrestacao
    }

    BigDecimal getTotalPago() {
        return totalPago
    }

    BigDecimal getTotalDivida() {
        return totalDivida
    }

    BigDecimal getTotalMoras() {
        return totalMoras
    }

    def getDataInicial() {

        return dataInicial
    }

    def getDataFinal() {

        return dataFinal
    }

    SessionStorageService sessionStorageService
    private ListModelList<Pagamento> pagamentos
    @Init init() {

        if(sessionStorageService.dataInicial!=null){
            dataInicial = sessionStorageService.getDataInicial()
        }
        if(sessionStorageService.dataFinal!=null){
            dataFinal = sessionStorageService.getDataFinal()
        }
        if(sessionStorageService.excluirPagos!=null){
            excluirPagos = sessionStorageService.getExcluirPagos()
        }
        if(sessionStorageService.excluirMoras!=null){
            excluirMoras = sessionStorageService.getExcluirMoras()
        }
        getPagamentos()
        getMap()
        getSubPagamentos()


    }

    ListModelList<Pagamento> getPagamentos() {

        if(pagamentos==null){
            pagamentos = new ListModelList<Pagamento>()
            if(excluirPagos){
                def pagaments = new ListModelList<Pagamento>(Pagamento.findAllByDataPrevistoDePagamentoBetweenAndPagoAndValorDaPrestacaoLessThan(dataInicial,dataFinal,false,0.0))
                if(excluirMoras){
                    for(Pagamento p in pagaments){
                        if(p.valorPago<p.valorDaPrestacao*(-1)){
                            pagamentos.add(p)
                        }
                    }
                }else {
                    pagamentos = pagaments
                }
            }else{
                def pagaments = new ListModelList<Pagamento>(Pagamento.findAllByDataPrevistoDePagamentoBetweenAndValorDaPrestacaoLessThan(dataInicial,dataFinal,0.0))
                if(excluirMoras){
                    for(Pagamento p in pagaments){
                        if(p.valorPago<p.valorDaPrestacao*(-1)){
                            pagamentos.add(p)
                        }
                    }
                }else {
                    pagamentos = pagaments
                }
            }
        }

        calcular()
        return pagamentos
    }
    def calcular(){
        totalMoras = 0.0
        totalPago = 0.0
        totalDivida = 0.0
        totalValorDaPrestacao = 0.0
        for(Pagamento p in pagamentos){
            totalMoras += p.valorDeJurosDeDemora
            totalPago += p.valorPago
            totalDivida += p.totalEmDivida
            totalValorDaPrestacao +=p.valorDaPrestacao
        }
    }
    def calcular(List<Pagamento> pgmts){
        totalMoras = 0.0
        totalPago = 0.0
        totalDivida = 0.0
        totalValorDaPrestacao = 0.0
        for(Pagamento p in pgmts){
            totalMoras += p.valorDeJurosDeDemora
            totalPago += p.valorPago
            totalDivida += p.totalEmDivida
            totalValorDaPrestacao +=p.valorDaPrestacao
        }
        SubPagamentos subTotal = new SubPagamentos(totalMoras,totalPago,totalDivida,totalValorDaPrestacao,"","","","Sub-totais","")
        return subTotal
    }


    Map getMap() {
        getPagamentos()
            if(map== null){
                map = new HashMap()
            }
        map.clear()
        for (Iterator iter = pagamentos.iterator(); iter.hasNext();) {
            Pagamento item = (Pagamento) iter.next()

            List list = (List) map.get(item.credito.cliente)
            if (list == null) {
                list = new ArrayList()
                map.put(item.credito.cliente, list)
            }

            list.add(item)
        }

        return map
    }

    def split(){



    }

    ListModelList<SubPagamentos> getSubPagamentos() {
        if(subPagamentos == null){
            subPagamentos = new ListModelList<SubPagamentos>()
        }
        subPagamentos.clear()
        for (Map.Entry<String, List<Pagamento>> entry : map.entrySet()) {

            List<Pagamento> list = entry.getValue()

            for(int x=0;x<list.size();x++){
                if(x==0){
                    SubPagamentos sp = new SubPagamentos(list[x].valorDeJurosDeDemora,
                            list[x].valorPago,list[x].totalEmDivida,list[x].valorDaPrestacao,
                            list[x]?.credito?.cliente?.utilizador?.username,
                            list[x].credito.cliente.nome,list[x].credito.cliente.telefone,list[x].descricao,
                            list[x].dataPrevistoDePagamento.format("dd/MM/yy"))
                    subPagamentos.add(sp)
                }else {
                    SubPagamentos sp = new SubPagamentos(list[x].valorDeJurosDeDemora,
                            list[x].valorPago,list[x].totalEmDivida,list[x].valorDaPrestacao, list[x].diasDeMora.toString(),
                            list[x].credito.numeroDoCredito,list[x].credito.periodicidade,list[x].descricao,
                            list[x].dataPrevistoDePagamento.format("dd/MM/yy"))
                    subPagamentos.add(sp)
                }


            }
            subPagamentos.add(calcular(list))
        }

        return subPagamentos
    }

    Set getClientes() {
        if(clientes ==null){
            clientes = new HashSet<Cliente>()
        }
        clientes.clear()
        for (Pagamento pagamento in pagamentos){
            clientes.add(pagamento.credito.cliente)
        }
        return clientes
    }


}
