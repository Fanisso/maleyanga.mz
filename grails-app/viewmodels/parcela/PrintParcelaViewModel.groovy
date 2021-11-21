package parcela

import mz.maleyanga.ClienteService
import mz.maleyanga.CurrencyWriter
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.ParcelaService
import org.springframework.stereotype.Service
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
@Service
class PrintParcelaViewModel {
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
        // initialzation code here
      //  parcelaInstance = Parcela.findById(parcelaService.parcelaInstance.id)
        parcelaInstance = sessionStorageService.parcela as Parcela
        if(parcelaInstance.pagamento!=null){
          //  creditoInstance = Credito.findById(parcelaService.parcelaInstance.pagamento.credito.id)
            creditoInstance = sessionStorageService.credito as Credito
        }
              total = parcelaInstance.valorPago

    }

    Credito getCreditoInstance() {
        return creditoInstance
    }
}
