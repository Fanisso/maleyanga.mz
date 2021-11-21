package utilizador

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import mz.maleyanga.UtilizadorService
import mz.maleyanga.security.Role
import mz.maleyanga.security.RoleGroup
import mz.maleyanga.security.RoleGroupRole
import mz.maleyanga.security.Utilizador
import mz.maleyanga.security.UtilizadorRole
import mz.maleyanga.security.UtilizadorRoleGroup
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList

@Transactional
@Service
class UtilizadorCrudViewModel {
    UtilizadorService utilizadorService
    private ListModelList<Utilizador> utilizadores
    private ListModelList<RoleGroup> roleGroups
    private String info
    def  roles = new ArrayList()
    private ListModelList<String> roles = new ArrayList<>()
    private ListModelList<String> menus = new ArrayList<>()
    private Utilizador selectedUtilizador
    private RoleGroup selectedRoleGroup
    SpringSecurityService springSecurityService

    private String filter
    private boolean ativo =true
    private boolean editView =false
    private Boolean btsalvar =false

    Utilizador getSelectedUtilizador() {
        return selectedUtilizador
    }

    String getInfo() {
        return info
    }

    String getSelectedMenu() {
        return selectedMenu
    }



    boolean getEditView() {
        return editView
    }

    RoleGroup getSelectedRoleGroup() {
        return selectedRoleGroup
    }

    void setSelectedRoleGroup(RoleGroup selectedRoleGroup) {
        this.selectedRoleGroup = selectedRoleGroup
    }

    ListModelList<RoleGroup> getRoleGroups() {

        if(roleGroups.equals(null)){
            roleGroups = new ListModelList<RoleGroup>()
        }
        roleGroups = new ListModelList<RoleGroup>(RoleGroup.findAll())
        return roleGroups
    }

    void setRoleGroups(ListModelList<RoleGroup> roleGroups) {
        this.roleGroups = roleGroups
    }

    ListModelList getRoles() {
        return roles
    }







    public  Boolean getBtsalvar() {
        return btsalvar
    }


    @NotifyChange(["btsalvar","roles","editView"])
    @Command
    void showItem(){
        roles.clear()
        selectedUtilizador=Utilizador.findByUsername(selectedUtilizador.username)
      // utilizadorService.selectedUser=selectedUtilizador

        def ugr = UtilizadorRoleGroup.findAllByUtilizador(selectedUtilizador)
        for (UtilizadorRoleGroup urg in ugr){
            roles.add(urg.roleGroup.name)
        }


        btsalvar = false
        editView = true
    }

    @NotifyChange(["selectedUtilizador","btsalvar"])
    @Command
    void addUtilizador(){
        selectedUtilizador = new Utilizador()
        btsalvar= true
    }




    @NotifyChange(["selectedUtilizador","utilizadores","info"])
    @Command
    void salvarUtilizador(){
        while (selectedUtilizador.equals(null)||selectedUtilizador.username.equals(null)||selectedUtilizador.email.equals(null)){
           //MultiLineMessageBox.show("Preecha os campos!", "Lua", 1, MultiLineMessageBox.ERROR)
            info="Preecha os campos!"
            return
        }
        if (Utilizador.findAllByUsername(selectedUtilizador.username)) {
          //  MultiLineMessageBox.show("Este nome ja existe!", "Lua", 1, MultiLineMessageBox.ERROR)
            info="Este Utilizador ja existe!"
            return
        }
        Utilizador user = new Utilizador()
        user.username=selectedUtilizador.username
        user.password=selectedUtilizador.password
        user.email=selectedUtilizador.email
        user.save(flush: true)
        utilizadores.add(user)

        // MultiLineMessageBox.show("Utilizador "+user.username+" foi criado com sucesso!", "Lua", 1,  MultiLineMessageBox.INFORMATION)
        info= "Utilizador "+user.username+" foi criado com sucesso!"

    }



    @NotifyChange(["selectedUtilizador","utilizadores","info"])
    @Command
    void updateUtilizador(){
        while (selectedUtilizador.equals(null)||selectedUtilizador.username.equals(null)||selectedUtilizador.email.equals(null)||selectedUtilizador.password.empty){
           // MultiLineMessageBox.show("Preecha os campos!", "Lua", 1, MultiLineMessageBox.ERROR)
            info="Preecha os campos!"
            return
        }
        Utilizador c = Utilizador.findById(selectedUtilizador.id)
        c.password = selectedUtilizador.password
        c.username = selectedUtilizador.username

        c.email = selectedUtilizador.email

        c.save(flush: true)
     //   selectedUtilizador = null
        info="Os dados do Utilizador "+c.username+" foram actualizados com sucesso!"
    }

    public String getFilter() {
        return filter
    }

    @NotifyChange
    void setFilter(String filter) {
        this.filter = filter
    }


    void setSelectedUtilizador (Utilizador utilizador){
        this.selectedUtilizador = utilizador
    }


    ListModelList<Utilizador> getUtilizadores() {
        if (utilizadores == null) {
            utilizadores = new ListModelList<Utilizador>(utilizadorService.all)
        }
        return utilizadores
    }


    @NotifyChange("utilizadores")
    @Command
    public void doSearch() {

        utilizadores.clear()
        def utilizadoresDb = utilizadorService.all
        List<Utilizador> allItems =utilizadorService.all
        if (filter == null || "".equals(filter)) {
            utilizadores.addAll(allItems)
        } else {
            for (Utilizador c : allItems) {
                if (
                c.username.toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    utilizadores.add(c)
                }
            }
        }
    }

    @Command
    @NotifyChange('info')
    def confirmar(){
        info="Se tem certeza que pretende eliminar este Utilizador por favor click duas vezes sobre o botão Eliminar!"
    }

    @Command
    @NotifyChange(["selectedUtilizador","utilizadores","info"])
    public void deleteUtilizador (){
        try {

            while (selectedUtilizador.equals(null)){
               // MultiLineMessageBox.show("Selecione O Utilizador que deseja eliminar!", "Lua", 1, MultiLineMessageBox.ERROR)
                info="Selecione O Utilizador que deseja eliminar!"
                return
            }
           /* MultiLineMessageBox.show("Tem certeza que deseja eliminar este Utilizador?", "Execute?", MultiLineMessageBox.YES | MultiLineMessageBox.NO,
                    MultiLineMessageBox.QUESTION, new EventListener<Event>() {
                @Override
                public void onEvent(final Event evt) throws InterruptedException {
                    if (MultiLineMessageBox.ON_YES.equals(evt.getName())) {
                        if(!UtilizadorRoleGroup.findByUtilizador(utilizador)?.roleGroup?.name.equals("rg_full")){
                            utilizador.delete()
                        }

                        utilizadores.remove(selectedUtilizador)
                        selectedUtilizador = null
                    }
                }
            }
            )*/
            if(!Utilizador.findByUsername('admin')){
                utilizador.delete()
            }else {
                info = "Não tens permissão p+ara eliminar este utilizador!"
            }

            utilizadores.remove(selectedUtilizador)
            selectedUtilizador = null

        }catch (Exception e ){
          //  MultiLineMessageBox.show("Selecione Um Utilizador!")
            info = "Selecione Um Utilizador!"
        }


    }

    @Command
    @NotifyChange(["selectedUtilizador","editView","btsalvar"])
    void fecharEditor(){
        selectedUtilizador = null
        selectedRoleGroup = null
        btsalvar = false
        editView = false

    }

    @Command
    @NotifyChange(["selectedUtilizador",'info'])
    def linkUtilizadorRoleGroup(){
        def rgrs = RoleGroupRole.findAllByRoleGroup(selectedRoleGroup)
        for (RoleGroupRole rgr in rgrs){
            UtilizadorRole.create(selectedUtilizador,rgr.role,true)
        }
        UtilizadorRoleGroup.create selectedUtilizador, selectedRoleGroup, true
        def roleDb = Role.findByAuthority('ROLE_ADMIN')
        UtilizadorRole.create(selectedUtilizador,roleDb,true)
       //   MultiLineMessageBox.show("Os papeis (Roles) do Utilizador "+utilizador.username+" foram actualizados com sucesso!", "Lua", 1,  MultiLineMessageBox.INFORMATION)
        info ="Os papeis (Roles) do Utilizador "+selectedUtilizador.username+" foram actualizados com sucesso!"
    }

    @Command
    @NotifyChange(["selectedUtilizador",'info'])
    def removeUtilizadorRoleGroup(){
        Utilizador user = springSecurityService.currentUser as Utilizador
        if (!user.authorities.any { it.authority == "UTILIZADOR_ROLE_DELETE" }) {
            info="Este utilizador não tem permissão para executar esta acção !"
            return
        }
        def urs=UtilizadorRole.findAllByUtilizador(selectedUtilizador)
        if (!urs.empty){
            for (UtilizadorRole ur in urs){
                ur.delete(flush: true)
            }
        }

        def urg = UtilizadorRoleGroup.findByUtilizador(selectedUtilizador)
        if(urg!=null){
            urg.delete(flush: true)
        }else {
            System.println(urg+'==null')
        }

        info ="Os papeis (Roles) do Utilizador "+selectedUtilizador.username+" foram actualizados com sucesso!"
    }

    @Init init() {
        selectedUtilizador = new Utilizador()
    }


}
