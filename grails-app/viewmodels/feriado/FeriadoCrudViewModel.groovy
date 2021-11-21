package feriado

import mz.maleyanga.feriado.Feriado
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Button
import org.zkoss.zul.ListModelList
import grails.transaction.Transactional

@Transactional
@Service
class FeriadoCrudViewModel {
    @Wire Button bt_salvar
    @Wire Button bt_eliminar
    @Wire Button bt_reset

    String message
    ListModelList feriados = Feriado.all
    Feriado selectedFeriado = new Feriado()

    ListModelList getFeriados() {
        return feriados
    }

    Feriado getSelectedFeriado() {
        return selectedFeriado
    }

    void setSelectedFeriado(Feriado selectedFeriado) {
        this.selectedFeriado = selectedFeriado
    }

    @Init init() {
        // initialzation code here
    }

    @NotifyChange(['message','selectedFeriado','feriados'])
    @Command salvarFeriado() {
        if(selectedFeriado?.designacao?.empty||selectedFeriado.data==null){
            message='Preencha os campos!'
            return
        }
        if(Feriado.findById(selectedFeriado.id)){
            def feriadoDB = Feriado.findById(selectedFeriado.id)
            feriadoDB.data = selectedFeriado.data
            feriadoDB.designacao = selectedFeriado.designacao
            feriadoDB.save(flush: true)
            bt_reset.label = 'O Feriado '+selectedFeriado.designacao+' foi atualizado com sucesso!'
            bt_reset.style='color:blue'
        }else {
            selectedFeriado.save(flush: true)
            bt_reset.label = 'O Feriado '+selectedFeriado.designacao+' criado com sucesso!'
            bt_reset.style='color:blue'
            feriados.add(selectedFeriado)
        }

        bt_eliminar.visible=false

        bt_salvar.visible = false

    }

    @Command
    @NotifyChange(['selectedFeriado','feriados','message'])
    def showItem (){
       bt_eliminar.visible = true
        bt_eliminar.label = 'eliminar'
        bt_salvar.visible = true
        bt_salvar.label = 'update'
        bt_reset.visible = true
        bt_reset.label = 'recompor'
    }

    @Command
    @NotifyChange(['selectedFeriado','feriados','message'])
    def reset (){
        bt_eliminar.visible = false
        bt_salvar.visible = true
        bt_salvar.label = 'Salvar'
        selectedFeriado= new Feriado()
        bt_reset.label = 'recomposto'
    }

    @Command
    @NotifyChange(['selectedFeriado','feriados'])
    def eliminarFeriado (){
        bt_eliminar.visible = false
        bt_salvar.visible = false

        try {
            selectedFeriado.delete(flush: true)
            bt_reset.label = 'o Feriado '+ selectedFeriado.designacao+' foi eliminado!'
            feriados.remove(selectedFeriado)
        }catch (Exception e){
            status.setRollbackOnly()
            message=e.toString()
        }

    }

    @Command
    def showButtons(){
        bt_salvar.visible=true
        bt_salvar.label = 'Salvar'
        bt_reset.label = 'reset'
        bt_reset.visible = true
    }
}
