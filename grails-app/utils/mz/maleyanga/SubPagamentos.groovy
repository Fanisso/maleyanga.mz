package mz.maleyanga

class SubPagamentos {
    String gestor
    String cliente
    String contacto
    String descricao
    BigDecimal totalMoras = 0.0
    BigDecimal totalPago = 0.0
    BigDecimal totalDivida = 0.0
    BigDecimal totalValorDaPrestacao = 0.0
    String dataPrevistoDePagamento

    SubPagamentos(BigDecimal totalMoras, BigDecimal totalPago, BigDecimal totalDivida, BigDecimal totalValorDaPrestacao
                  , String gestor, String cliente, String contacto, String descricao, String dataPrevistoDePagamento
    ) {
        this.totalMoras = totalMoras
        this.totalPago = totalPago
        this.totalDivida = totalDivida
        this.totalValorDaPrestacao = totalValorDaPrestacao
        this.descricao = descricao
        this.gestor = gestor
        this.cliente = cliente
        this.contacto = contacto
        this.dataPrevistoDePagamento = dataPrevistoDePagamento
    }
}
