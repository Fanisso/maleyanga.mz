package relatorios

import mz.maleyanga.PagamentoComParcelas
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pagamento.Parcela
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList

class PrintExtratoDePrestacoesDoClienteDetalhadoViewModel {

    BigDecimal totalValorDaPrestacao
    BigDecimal totalPago
    BigDecimal totalParcial
    BigDecimal totalDivida
    BigDecimal totalDividaSeMoras
    BigDecimal totalMoras
    Cliente selectedCliente
    private Credito credito
    SessionStorageService sessionStorageService
    private ListModelList<Pagamento> pagamentos
    private ListModelList<PagamentoComParcelas> pagamentoComParcelas
    int moras

    int getMoras() {
        return moras
    }

    BigDecimal getTotalDividaSeMoras() {
        return totalDividaSeMoras
    }

    ListModelList<PagamentoComParcelas> getPagamentoComParcelas() {
        if(pagamentoComParcelas==null){
            pagamentoComParcelas = new ListModelList<PagamentoComParcelas>()
        }
        totalValorDaPrestacao = 0.0
        totalPago = 0.0
        totalParcial = 0.0
        totalDivida = 0.0
        totalMoras = 0.0
        totalDividaSeMoras = 0.0
        moras = 0

        for(Pagamento p in getPagamentos()){
            PagamentoComParcelas pcp = new PagamentoComParcelas()
            pcp.descricao = p.descricao
            pcp.valorDaPrestacao = p.valorDaPrestacao
            pcp.diasDeMora = p.diasDeMora.toString()
            pcp.valorDeJurosDeDemora = p.valorDeJurosDeDemora
            pcp.id=p.id
            pcp.dataPrevistoDePagamento = p.dataPrevistoDePagamento
            pcp.totalEmDividaSemMoras =p.totalEmDividaSemMoras
            pcp.totalEmDivida = p.totalEmDivida
            pagamentoComParcelas.add(pcp)

            if(!p.parcelas.empty){
                def parcelas = Parcela.findAllByPagamento(p)
                for(Parcela pa in parcelas){
                    PagamentoComParcelas pcpp = new PagamentoComParcelas()
                    pcpp.descricao = pa.descricao
                    pcpp.valorPago = pa.valorParcial
                    pcpp.totalPago = pa.valorPago
                    pcpp.dataPrevistoDePagamento = pa.dataDePagamento
                    pcpp.diario = pa.diario
                    pcpp.id=pa.id
                    pcpp.numeroDoRecibo = pa.numeroDoRecibo
                    pcpp.dataPrevistoDePagamento = pa.dataDePagamento
                    pagamentoComParcelas.add(pcpp)


                }
                PagamentoComParcelas pcpt = new PagamentoComParcelas()
                pcpt.descricao = ""

                pagamentoComParcelas.add(pcpt)
            }
            totalValorDaPrestacao += p.valorDaPrestacao
            totalPago += p.totalPago
            totalParcial += p.valorPago
            totalDivida += p.totalEmDivida
            totalMoras += p.valorDeJurosDeDemora
            totalDividaSeMoras += p.totalEmDividaSemMoras
            moras+=p.diasDeMora

        }
        return pagamentoComParcelas
    }

    BigDecimal getTotalParcial() {
        return totalParcial
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


    @Init init() {
        if(sessionStorageService.cliente!=null){
            selectedCliente = sessionStorageService.cliente as Cliente
        }

        if(sessionStorageService.credito!=null){
            credito = sessionStorageService.credito as Credito
        }


    }

    ListModelList<Pagamento> getPagamentos() {

        if(pagamentos==null){
            pagamentos = new ListModelList<Pagamento>()
        }
        pagamentos.clear()
        pagamentos = Pagamento.findAllByCredito(credito)
        return pagamentos.sort{it.id}
    }

}
