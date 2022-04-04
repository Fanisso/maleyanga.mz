package parcela

import mz.maleyanga.CurrencyWriter
import mz.maleyanga.SessionStorageService
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.security.Utilizador
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
@Service
class PrintParcelaDuplicadoViewModel {
    SessionStorageService sessionStorageService
    Parcela parcelaInstance
    Credito creditoInstance
    List<Parcela> parcelas
    BigDecimal total =0.0
    String username
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
        Utilizador utilizador = Utilizador.findById(parcelaInstance.utilizador.id)
        username = utilizador.username
        parcelaInstance = sessionStorageService.parcela as Parcela
        if(parcelaInstance.pagamento!=null){
            //  creditoInstance = Credito.findById(parcelaService.parcelaInstance.pagamento.credito.id)
            creditoInstance = sessionStorageService.credito as Credito
            creditoInstance = Credito.find(creditoInstance)
        }
        total = parcelaInstance.valorPago
    }

    Credito getCreditoInstance() {

        return creditoInstance
    }

}
