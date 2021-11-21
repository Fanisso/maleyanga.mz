package mz.maleyanga

class PagamentoComParcelas {
    String descricao
    String diario
    String numeroDoRecibo
    BigDecimal valorParcial
    BigDecimal valorDaPrestacao
    BigDecimal valorPago     // ref aos totais parciais
    BigDecimal totalPago   // referente o valor total pago das prestacoes
    BigDecimal totalEmDivida
    BigDecimal totalEmDividaSemMoras
    BigDecimal valorDeJurosDeDemora
    String diasDeMora
    Long id
    Date dataPrevistoDePagamento
}
