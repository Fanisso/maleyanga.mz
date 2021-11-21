package mz.maleyanga

import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.saidas.Saida

class Diarios {
    String estado
    String descricao
    String numeroDoDiario
    Date dataDeReferencia
    Date dateCreated
    Date lastUpdated
    Date dateClosed
    BigDecimal valorPago

    Parcela parcela
    Saida saidaAtiva
    Saida saidaPassiva

    BigDecimal getTotalParcial() {
        totalParcial = 0.0
        if (parcelas == null) {
            return 0.0
        }
        for (Parcela parcela in parcelas) {
            totalParcial += parcela.valorParcial
        }
        return totalParcial
    }

    BigDecimal getTotalPago() {
        totalPago = 0.0
        if (parcelas == null) {
            return 0.0
        }
        for (Parcela parcela in parcelas) {
            totalPago += parcela.valorPago
        }
        return totalPago
    }

    BigDecimal getTotalAtivos() {
        totalAtivos = 0.0
        if (saidasAtivas == null) {
            return 0.0
        }
        for (Saida saida in saidasAtivas) {
            totalAtivos += saida.valor
        }
        return totalAtivos
    }

    BigDecimal getTotalPassivos() {
        totalPassivos = 0.0
        if (saidasPassivas == null) {
            return 0.0
        }
        for (Saida saida in saidasPassivas) {
            totalPassivos += saida.valor
        }
        return totalPassivos
    }
}
