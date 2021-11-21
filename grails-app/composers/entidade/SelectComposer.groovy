package entidade

import mz.maleyanga.entidade.Entidade
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*

class SelectComposer extends zk.grails.Composer implements Serializable {
    private static final long serialVersionUID = 1
    Listbox lb_entidades

    def afterCompose = { window ->
        renderLBEntidades()
        System.println("select page is run")
    }

    def renderLBEntidades () {

        entidadesList()
        lb_entidades.getItems() .clear()
        lb_entidades.setItemRenderer(new ListitemRenderer<Entidade>() {

            @Override
            void render(Listitem item, Entidade entidade, int i) throws Exception {
                 new Listcell(entidade.nome).setParent(item)

                item.setValue(entidade)


            }
        })
    }

    def entidadesList(){
        def entidades = new ArrayList<Entidade>()
        def entidadesDB = Entidade.all

        for(Entidade e in entidadesDB) {

            entidades.add(e)

        }
        ListModelList lml = new ListModelList(entidades,true)
        lb_entidades.setModel(lml)
    }


    @Listen("onClick = listbox#lb_entidades")
    void setEntidade (){
        String name = "lb_entidades.selectedItem.getValue().toString()"
        def entidade = Entidade.findAllByNome(name)
        System.println(name)
        session.setAttribute('entidade', entidade)
       // Executions.sendRedirect("/home/index/*")

    }
}


