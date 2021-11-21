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
        BigDecimal rateR = creditoInstance.percentualDejuros as BigDecimal
        def rate = rateR / 100
        Simulador simulador = new Simulador(nper: nper, pv: pv, rate: rate)

        //def rate = simulador.rate/100
        // BigDecimal  prestacoes = -pagamentoService.pmt(rate,simulador.nper,simulador.pv,0,0)
        prestacoes = prestacoes.setScale(2, BigDecimal.ROUND_HALF_EVEN)

        Item itm0 = new Item()

        itm0.saldoDevedor = simulador.pv.setScale(2, BigDecimal.ROUND_HALF_EVEN)

        itm0.meses = "0"
        its.add(itm0)


        for (int x = 1; x <= simulador.nper; x++) {
            def saldoDevedorLast = its.last().saldoDevedor as BigDecimal
            saldoDevedorLast = saldoDevedorLast.setScale(2, BigDecimal.ROUND_HALF_EVEN)
            System.println("saldoDevedor" + saldoDevedorLast + "*" + rate)
            BigDecimal juros = saldoDevedorLast * rate

            juros = juros.setScale(2, BigDecimal.ROUND_HALF_EVEN)
            def amortizacao = -prestacoes - juros
            System.println("amortizacao alculado(prestacoes-juros)" + amortizacao + "prestacoes" + prestacoes + "-" + juros)
            def saldoDevedor = saldoDevedorLast - amortizacao
            System.println("saldo Devedor calculado" + saldoDevedor)

            def d = new Item(meses: x.toString(), saldoDevedor: saldoDevedor, prestacoes: prestacoes, amortizacao: amortizacao, juros: juros)
            its.add(d)

        }
        return its
    }
}
