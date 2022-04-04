package taxa

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import mz.maleyanga.Taxa.Taxa
import mz.maleyanga.entidade.Entidade
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.security.Utilizador
import mz.maleyanga.settings.DefinicaoDeCredito
import org.springframework.stereotype.Service
import org.zkoss.zk.grails.*

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList



@Transactional
@Service
class TaxasViewModel {
   SpringSecurityService springSecurityService
 private Taxa taxa
   @Wire Label info
   @Wire Label ajuda
    private ListModelList<Taxa> taxas

    Taxa getTaxa() {
        return taxa
    }

    void setTaxa(Taxa taxa) {
        this.taxa = taxa
    }

    ListModelList<Taxa> getTaxas() {
        if(taxas==null){ taxas= new ListModelList<Taxa>(Taxa.all)}
        return taxas
    }

    @Command
    @NotifyChange(["taxa","taxas"])
    def addTaxa(){
        taxa = new Taxa()
    }

    @Command
    @NotifyChange(["taxas","taxa"])
    def salvarTaxa(){
      if(Taxa.findById(taxa.id)){
         info.value="Esta taxa já existe na base de dados!"
         info.style = "color:red;font-weight;font-size:14pt;background:#EAEAEA"
      }else {
         try {
            Utilizador user = springSecurityService.currentUser as Utilizador
            Entidade entidade = Entidade.findAll().first()
            if (!user.authorities.any { it.authority == "TAXA_CREATE" }) {
               info.value="Este utilizador não tem permissão para executar esta acção !"
               info.style = "color:red;font-weight;font-size:14ptpt;background:#EAEAEA"
             return
            }
            taxa.utilizador = user
            taxa.entidade = entidade
            System.println(taxa.nome)
            System.println(taxa.valor)
            System.println(taxa.percentagem)
            System.println(taxa.activo)
            System.println(taxa.entidade)
            System.println(taxa.valorMaximo)
            System.println(taxa.valorMinimo)
            System.println(taxa.recorencia)
            System.println(taxa.utilizador)
            taxa.save(flush: true)
             taxas.add(taxa)
            info.value = "A taxa "+ taxa.nome+" foi criado com sucesso!"
            info.style = "color:red;font-weight;font-size:14pt;background:#EAEAEA"
         }catch(Exception e){
            System.println(e.toString())
            info.value = e.toString()
            info.style = "color:red;font-weight;font-size:14pt;background:#EAEAEA"

         }
      }
    }


    @Command
    @NotifyChange(["taxa","taxas"])
    def fecharEditor(){

    }

   @Command
   def checkName(){
      if(Taxa.findByNome(taxa.nome)){
         info.value = "Já existe uma taxa com esta descrição!"
         info.style = "color:red;font-weight;font-size:14pt;background:#EAEAEA"
      }
   }
    @Command
    @NotifyChange(["taxa","taxas"])
    def updateTaxa(){

          try {
             Utilizador user = springSecurityService.currentUser as Utilizador
             Entidade entidade = Entidade.findAll().first()
             if (!user.authorities.any { it.authority == "TAXA_UPDATE" }) {
                info.value="Este utilizador não tem permissão para executar esta acção !"
                info.style = "color:red;font-weight;font-size:14pt;background:back"
                return
             }
             taxa.utilizador = user
             taxa.entidade = entidade
             taxa.merge(flush: true)
             info.value = "A taxa "+ taxa.nome+" foi actualizada com sucesso!"
             info.style = "color:red;font-weight;;font-size:14pt;background:#EAEAEA"
          }catch(Exception e){
             System.println(e.toString())
             info.value = e.toString()
             info.style = "color:red;font-weight;;font-size:14pt;background:#EAEAEA"

          }

    }

    @Command
    @NotifyChange(["taxa","taxas"])
    def changeAtivo(){
       taxa.activo=!taxa.activo

    }

    @Command
    @NotifyChange(["taxa","taxas"])
    def showDelMessage(){

    }

   @Command
   @NotifyChange(["taxa","taxas"])
   def checkNumber(){
      if(taxa.percentagem>100){
         info.value="valor inválido, coloque o valor correspondente de 1 a 100!"
         info.style = "color:red;font-weight;font-size:14ptpt;background:back"

      }
   }

   @Command
   def showAjuda(){
      ajuda.value = "A recorência corresponde a vezes que de deve aplicar a taxa. " +
              ", 1ºexpl: se a taxa é contínua, a recorência será = 11" +
              ", 2ºexpl: se a taxa é alternada, a recorência será = 10" +
              ", 3ºexpl: se a taxa é aplicado 2 vezes e na terceira não, a recorência será = 110"
   }

   @Command
   def clean(){
      ajuda.value = ""
      info.value = ""
   }

    @Command
    @NotifyChange(["taxa","taxas"])
    def deleteTaxa(){
       try {
          Utilizador user = springSecurityService.currentUser as Utilizador
          def taxas = new ArrayList<Taxa>()

          def de_de_creditos = DefinicaoDeCredito.all.contains(taxa)
          if (!user.authorities.any { it.authority == "TAXA_DELETE" }) {
             info.value="Este utilizador não tem permissão para executar esta acção !"
             info.style = "color:red;font-weight;font-size:14ptpt;background:back"
             return
          }

          if(de_de_creditos){
             info.value="Esta taxa não pode ser eliminada, mas pode ser inválidado!"
             info.style = "color:red;font-weight;font-size:14ptpt;background:back"
             return
          }
          taxa.delete(flush: true)
          info.value = "A taxa "+ taxa.nome+" foi eliminada com sucesso!"
          info.style = "color:red;font-weight;font-size:14ptpt;background:#EAEAEA"
           taxas.remove(taxa)
       }catch(Exception e){
          System.println(e.toString())
          info.value = e.toString()
          info.style = "color:red;font-weight;font-size:14ptpt;background:#EAEAEA"

       }
    }
}
