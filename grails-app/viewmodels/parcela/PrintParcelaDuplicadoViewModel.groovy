package parcela

import mz.maleyanga.CurrencyWriter
import mz.maleyanga.SessionStorageService
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Parcela
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
@Service
class PrintParcelaDuplicadoViewModel {
    SessionStorageService sessionStorageService
    Parcela parcelaInstance
    Credito creditoInstance
    List<Parcela> parcelas
    BigDecimal total =0.0
    String valorPorExtenso
    CurrencyWriter currencyWriter = CurrencyWriter.getInstance()

    String getValorPorExtenso() {
        return currencyWriter.write(total)
    }

    List<Parcela> getParcelas() {
        if(parcelas==null){
            parcelas = new LinkedList<Parcela>(Parcela.findAllByNumeroDoRecibo(parcelaInstance.numeroDoRecibo))
        }
        return parcelas
    }

    BigDecimal getTotal() {
       return total
    }

    Parcela getParcelaInstance() {
        return parcelaInstance
    }



    @Init init() {
       creditoInstance = sessionStorageService.credito as Credito
        parcelaInstance = sessionStorageService.parcela as Parcela
        total = parcelaInstance.valorPago
    }

    Credito getCreditoInstance() {

        return creditoInstance
    }

}
