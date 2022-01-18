package mz.maleyanga

import mz.maleyanga.credito.Credito

class ClienteDetalhado {
    String nome
    String contacto
    String residencia
    String ativo
    String classificacao
    int qtdDeCreditos
    Set<Credito> creditos
    BigDecimal valorEmDivida
    String gestor


}
