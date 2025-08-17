package mz.maleyanga

import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.settings.Settings
import mz.maleyanga.simulador.Item
import mz.maleyanga.simulador.Simulador
import org.zkoss.zul.ListModelList

/**
 * SimuladorService
 * A service class encapsulates the core business logic of a Grails application
 */

class SimuladorService {

    Credito credito
    String juros
    String jurosDeMora
    String taxaFixa
    Double totalPrestacoes = 0
    Double totalamortizacao = 0
    Double totaljuros = 0
    Settings settings
    ListModelList<Pagamento> prestacoes
    Simulador simulador = new Simulador()


    ArrayList gerarExtrato(Credito creditoInstance, BigDecimal prestacoes) {
        def its = new ArrayList<Item>()

        int nper = creditoInstance.numeroDePrestacoes as int
        BigDecimal pv = creditoInstance.valorCreditado as BigDecimal

        // --- Start of new logic ---
        def annualRateDecimal = creditoInstance.percentualDejuros / 100.0
        def periodicRate = 0.0
        def divisor = 1.0

        if (creditoInstance.periodicidade == "mensal") {
            divisor = 12.0
        } else if (creditoInstance.periodicidade == "quinzenal") {
            divisor = 24.0
        } else if (creditoInstance.periodicidade == "semanal") {
            divisor = 52.0
        } else if (creditoInstance.periodicidade == "diario") {
            divisor = 365.0
        } else if (creditoInstance.periodicidade == "doisdias") {
            divisor = 365.0 / 2.0
        } else if (creditoInstance.periodicidade == "variavel") {
            if (creditoInstance.periodoVariavel > 0) {
                divisor = 365.0 / creditoInstance.periodoVariavel
            }
        }

        if (divisor > 0) {
            periodicRate = annualRateDecimal / divisor
        }
        def rate = new BigDecimal(periodicRate.toString())
        // --- End of new logic ---

        prestacoes = prestacoes.setScale(2, BigDecimal.ROUND_HALF_UP)

        Item itm0 = new Item()
        itm0.saldoDevedor = pv.setScale(2, BigDecimal.ROUND_HALF_UP)
        itm0.meses = "0"
        its.add(itm0)

        for (int x = 1; x <= nper; x++) {
            def saldoDevedorLast = its.last().saldoDevedor as BigDecimal

            BigDecimal juros = (saldoDevedorLast * rate).setScale(2, BigDecimal.ROUND_HALF_UP)
            BigDecimal amortizacao = -prestacoes - juros
            BigDecimal saldoDevedor = (saldoDevedorLast - amortizacao).setScale(2, BigDecimal.ROUND_HALF_UP)

            def d = new Item(meses: x.toString(), saldoDevedor: saldoDevedor, prestacoes: prestacoes, amortizacao: amortizacao, juros: juros)
            its.add(d)
        }

        // Adjust the last payment to ensure zero balance
        if (its.size() > 1) {
            def lastItem = its.last()
            if (lastItem.saldoDevedor != 0.0) {
                lastItem.amortizacao = lastItem.amortizacao + lastItem.saldoDevedor
                lastItem.saldoDevedor = 0.0
            }
        }

        return its
    }
}
