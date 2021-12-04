package relatorios

import mz.maleyanga.CreditoService
import mz.maleyanga.RelatoriosService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.UtilizadorService
import mz.maleyanga.credito.Credito
import mz.maleyanga.security.Utilizador
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Div
import org.zkoss.zul.Include
import org.zkoss.zul.ListModelList
@Service
class CreditosViewModel {
    SessionStorageService sessionStorageService
    UtilizadorService utilizadorService
    private ListModelList gestores
    private ListModelList emPagamento
    private ListModelList pendentes
    private ListModelList fechados
    private ListModelList invalidos
    private ListModelList abertos
    private ListModelList moras
    CreditoService creditoService
    private Credito credito = new Credito()
    private  Utilizador selectedGestor
    Date dataInicial
    Date dataFinal
    boolean tb_em_pagamento = false
    boolean tb_em_pendentes = false
    boolean tb_em_fechados = false
    boolean tb_em_invalidos = false
    boolean tb_abertos = false
    boolean tb_moras = false
    @Wire Div dv_print
    String relatorio = "Relatório dos extratos de créditos"

    String getRelatorio() {
        return relatorio
    }

    ListModelList getMoras() {
        if(moras==null){
            moras = new ListModelList<Credito>()
        }
        if(tb_moras){
          moras = creditoService.moras
        }
        return moras
    }


    @Command
    @NotifyChange(["c_em_pagamento","emPagamento"])
    def setEmPagamento(){
        tb_em_pagamento =true
    }
    @Command
    @NotifyChange(["c_abertos","abertos"])
    def setAbertos(){
        tb_abertos = true
    }


    @Command
    @NotifyChange(["c_pendentes","pendentes"])
    def setPendentes(){
        tb_em_pendentes = true
    }

    @Command
    @NotifyChange(["c_fechados","fechados"])
    def setFechados(){
        tb_em_fechados = true
    }

    @Command
    @NotifyChange(["c_invalidos","invalidos"])
    def setInvalidos(){
        tb_em_invalidos = true
    }

    @Command
    @NotifyChange(["moras"])
    def setEmMora(){
        tb_moras =true
        dv_print.visible = true
        sessionStorageService.creditos = getMoras()

      }

    @Init init() {

    }



    Credito getCredito() {
        return credito
    }

    void setCredito(Credito credito) {
        this.credito = credito
    }

    ListModelList<Credito> getEmPagamento() {
        if(emPagamento ==null){
            emPagamento= new ListModelList<Credito>()
        }
        emPagamento.clear()
        if(tb_em_pagamento){
            emPagamento = creditoService.getEmPagamento()
        }
      return emPagamento
    }

    ListModelList<Credito> getPendentes() {
        if(pendentes ==null){
            pendentes= new ListModelList<Credito>()
        }
        if(tb_em_pendentes){
            pendentes = creditoService.getPendentes()
        }
        return pendentes
    }

    ListModelList<Credito> getFechados() {
        if(fechados ==null) {
            fechados = new ListModelList<Credito>()
        }

        fechados.clear()
        if(tb_em_fechados){
            fechados = creditoService.getFechados()

        }
        return fechados
    }
    ListModelList<Credito> getInvalidos() {
        if(invalidos ==null){
            invalidos= new ListModelList<Credito>()
        }
        invalidos.clear()
        if(tb_em_invalidos){
            invalidos = creditoService.getInvalidos()

        }
        return invalidos
    }

    ListModelList getAbertos() {
        if(abertos ==null){
            abertos= new ListModelList<Credito>()
        }
        abertos.clear()
        if(tb_abertos){
            abertos = creditoService.getAbertos()

        }
        return abertos
    }

    Date getDataInicial() {
        return dataInicial
    }

    @NotifyChange(["dataFinal","dataInicial"])
    void setDataInicial(Date dataInicial) {
        Calendar c = Calendar.getInstance()
        c.setTime(dataInicial)
        c.add(Calendar.MONTH, 1)
        c.add(Calendar.DAY_OF_MONTH,-1)
        dataFinal = c.getTime()
        this.dataInicial = dataInicial
        sessionStorageService.dataInicial = dataInicial
        sessionStorageService.dataFinal = dataFinal
        System.println(sessionStorageService.dataInicial)
        System.println(sessionStorageService.dataFinal)
    }

    Date getDataFinal() {
        return dataFinal
    }

    void setDataFinal(Date dataFinal) {
        if(dataFinal<dataInicial){
            Calendar c = Calendar.getInstance()
            c.setTime(dataInicial)
            c.add(Calendar.DAY_OF_MONTH, 1)
            dataFinal = c.getTime()
            this.dataFinal = dataFinal

        }else {
            this.dataFinal = dataFinal
        }

        sessionStorageService.dataFinal= dataFinal
    }

    Utilizador getSelectedGestor() {
        return selectedGestor
    }

    void setSelectedGestor(Utilizador selectedGestor) {
        this.selectedGestor = selectedGestor
        sessionStorageService.selectedGestor = selectedGestor as Utilizador
    }

    ListModelList getGestores() {
        if (gestores==null){
            gestores = new ListModelList<Utilizador>()
        }
        gestores.clear()
        def utilizadores = utilizadorService.all
        for(Utilizador u in utilizadores){
            if (u.authorities.any { it.authority == "CLIENTE_GESTOR" }) {
                gestores.add(u)
            }
        }
        return gestores
    }




}
