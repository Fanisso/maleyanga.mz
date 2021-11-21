package parcela

import mz.maleyanga.PagamentoService
import mz.maleyanga.ParcelaService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.pagamento.Pagamento
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList

class PrintPrestacoesDoDiaViewModel {
   ParcelaService parcelaService
    ListModelList<Pagamento> pagamentos
    PagamentoService pagamentoService
    SessionStorageService sessionStorageService
    Date dia
    BigDecimal totalEmDivida = 0.0

    @Init init() {
        Date date = sessionStorageService.getDia() as Date
      dia = getZeroTimeDate(date)

    }

    ListModelList<Pagamento> getPagamentos() {

        if(pagamentos ==null){
           // pagamentos = new ListModelList<Pagamento>(Pagamento.findAllByDataPrevistoDePagamentoAndValorDaPrestacaoLessThanAndTotalEmDividaLessThan(dia,0,0))
            pagamentos = pagamentoService.findAllByDataPrevistoDePagamento(dia)
        }

        return pagamentos
    }

    static Date getZeroTimeDate(Date fecha) {
        Date res
        Calendar calendar = Calendar.getInstance()

        calendar.setTime( fecha )
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        res = calendar.getTime()

        return res
    }

    BigDecimal getTotalEmDivida() {
        totalEmDivida = 0.0

        if(!pagamentos.empty){
            for(Pagamento p in pagamentos){
                totalEmDivida+=p?.totalEmDivida
            }
        }

        return totalEmDivida
    }
    /* def calcular(){
         if(!pagamentos.empty){
             for(Pagamento pagamento in pagamentos){
                 for(Parcela parcela in pagamento.parcelas){
                     pagamentoService.calcularJurosDeDemora(parcela)
                 }
             }
         }

     }*/
}
