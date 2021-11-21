package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Mora
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.settings.Settings

import java.math.RoundingMode

/**
 * MoraService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class MoraService {
    def settings = Settings.findByNome("settings")
    BigDecimal getValorDeJurosDeDemora(Pagamento pagamento) {
        if (pagamento.descricao == "CAPITALIZACAO") {
            return 0.0
        }
        if (pagamento.diasDeMora == 0) {
            return 0.0
        } else if (pagamento.parcelas != null) {
            def pdjdd = pagamento.credito.percentualJurosDeDemora
            def vdp = 0.0
            if(settings.ignorarValorPagoNoPrazo){
                vdp = pagamento.valorDaPrestacao
            }else {
                vdp = pagamento.valorDaPrestacao +pagamento.getTotalPagoNoPrazo()
            }

            if (vdp > 0) {
                return 0.0
            } else {

                def valorDeMoras = vdp * pdjdd / 100 * pagamento.diasDeMora
                BigDecimal vdjdd = valorDeMoras
                return vdjdd.setScale(2,RoundingMode.DOWN)
            }

        } else {
            def pdjdd = pagamento.credito.percentualJurosDeDemora
            def vdp = pagamento.valorDaPrestacao
            def valorDeMoras = vdp * pdjdd / 100
            BigDecimal vdjdd = valorDeMoras * pagamento.diasDeMora
            return vdjdd.setScale(2, RoundingMode.DOWN)
        }

    }

    def calcularMoras(Credito credito){
        for(Pagamento p in credito.pagamentos){
            def vem = getValorDeJurosDeDemora(p)
            if(vem!=0.0){
                Mora mora = new Mora(vdjdm: vem,pagamento:p)
                def pagamentoDb = Pagamento.findById(p.id)
                pagamentoDb.moras.add(mora)
                pagamentoDb.merge(flush: true)
            }
        }

    }
}
