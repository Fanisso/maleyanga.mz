package credito

import mz.maleyanga.SimuladorService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.settings.Settings
import mz.maleyanga.simulador.Simulador
import org.springframework.stereotype.Service
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList
@Service
class PrintSimuladorViewModel {
  SimuladorService simuladorService
    String juros
    String jurosDeMora
    String taxaFixa
    Settings settings
    Double totalPrestacoes = 0
    Double totalamortizacao=0
    Double totaljuros = 0
    Credito credito
    private ListModelList<Pagamento> prestacoes


    String getJuros() {
        return  simuladorService.juros
    }

    String getJurosDeMora() {
        return simuladorService.jurosDeMora
    }

    String getTaxaFixa() {
        return simuladorService.taxaFixa
    }

    Double getTotalPrestacoes() {
        return simuladorService.totalPrestacoes
    }

    Double getTotalamortizacao() {
        return simuladorService.totalamortizacao
    }

    Double getTotaljuros() {
        return simuladorService.totaljuros
    }

    ListModelList<Pagamento> getPrestacoes() {
        return simuladorService.prestacoes
    }

    Settings getSettings() {
        return simuladorService.settings
    }

    Credito getCredito() {
        return simuladorService.credito
    }
}
