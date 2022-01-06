package credito

import mz.maleyanga.SessionStorageService
import mz.maleyanga.SettingsService
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.settings.Settings
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
@Service
class PlanoDePagamentoViewModel {
    SessionStorageService sessionStorageService
    SettingsService settingsService
     Credito creditoInstance
    String message
    BigDecimal totalPrestacoes = 0
    BigDecimal totalAmortizacoes = 0
    BigDecimal totalJuros = 0
    private ListModelList<Pagamento> pagamentos
    Settings settings

    @Wire  btnHello

    @Init init() {
        Credito credito = sessionStorageService.credito as Credito
        creditoInstance = Credito.findById(credito.id)
        settings = settingsService.getSettings()
        somar()
    }

    def somar(){
        pagamentos.each {
            totalPrestacoes+=it.valorDaPrestacao
            totalAmortizacoes+=it.valorDeAmortizacao
            totalJuros+=it.valorDeJuros
        }
    }

    @NotifyChange(['message'])
    @Command clickMe() {
        message = "Clicked"
    }

    Settings getSettings() {
          return settings
    }

    BigDecimal getTotalPrestacoes() {
        return totalPrestacoes
    }

    BigDecimal getTotalAmortizacoes() {
        return totalAmortizacoes
    }

    BigDecimal getTotalJuros() {
        return totalJuros
    }

    ListModelList<Pagamento> getPagamentos() {
        if(pagamentos==null){
            pagamentos = new LinkedHashSet<Pagamento>(Pagamento.findAllByCredito(creditoInstance))
            pagamentos.sort{it.id}
        }
        somar()
        return pagamentos

    }

}
