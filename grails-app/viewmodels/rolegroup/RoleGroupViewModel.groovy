package rolegroup

import grails.transaction.Transactional
import mz.maleyanga.security.Role
import mz.maleyanga.security.RoleGroup
import mz.maleyanga.security.RoleGroupRole
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.ListModelList

@Service
@Transactional
class RoleGroupViewModel {
    @Wire Label info
    private ListModelList<RoleGroup> roleGroups
    private ListModelList<Role> roles
    private ListModelList<Role> selectedRoles
    private String selectedRoleGroup
    private RoleGroup cbSelectedRg
    private Role selectedRole
    private Role selectedRoleRemove
    String message
    private boolean viewRole =false
    private String roleGroup
    private Boolean btsalvar =false
    private String filter


    String getMessage() {
        return message
    }

    RoleGroup getCbSelectedRg() {
        return cbSelectedRg
    }

    void setCbSelectedRg(RoleGroup cbSelectedRg) {
        this.cbSelectedRg = cbSelectedRg
    }


    @Command
    @NotifyChange('message')
    def groupRoles (){

        for (Role role in selectedRoles){
            RoleGroupRole.create cbSelectedRg, role, true
        }
       // Messagebox.show("Role Group "+cbSelectedRg.name+" foi atualizado com sucesso!", "cinodemica", 1,  Messagebox.INFORMATION)
       // message="Role Group "+cbSelectedRg.name+" foi atualizado com sucesso!"
        info.value = "Role Group "+cbSelectedRg.name+" foi atualizado com sucesso!"
    }
    @Command
    @NotifyChange('message')
    def deleteRole (){


            RoleGroupRole.remove cbSelectedRg, selectedRoleRemove, true

        // Messagebox.show("Role Group "+cbSelectedRg.name+" foi atualizado com sucesso!", "cinodemica", 1,  Messagebox.INFORMATION)
        // message="Role Group "+cbSelectedRg.name+" foi atualizado com sucesso!"
        info.value = "Role Group "+cbSelectedRg.name+" foi atualizado com sucesso!"
    }

    @Command
    @NotifyChange(["selectedRoles"])
    def removeSelectedtItem(){
        selectedRoles.remove(selectedRoleRemove)
    }

    @Command
    @NotifyChange (["selectedRoles"])
    def addSelectedtItem(){
        selectedRoles.add(selectedRole)
    }

    @Command
    @NotifyChange (["selectedRoles"])
    def addSelectedtItemGroup(){
        selectedRoles = new ListModelList<>()
            def rgr=RoleGroupRole.findAllByRoleGroup(cbSelectedRg)

            for (RoleGroupRole rgrr in rgr){
                for(Role r in Role.all){
                    if(rgrr.role.id.equals(r.id)){
                        selectedRoles.add(r)
                    }
                }
            }



    }

    Role getSelectedRoleRemove() {
        return selectedRoleRemove
    }

    void setSelectedRoleRemove(Role selectedRoleRemove) {
        this.selectedRoleRemove = selectedRoleRemove
    }

    ListModelList<Role> getSelectedRoles() {
        if(selectedRoles.equals(null)){
            selectedRoles = new ListModelList<Role>()
        }

        return selectedRoles
    }

    void setSelectedRoles(ListModelList<Role> selectedRoles) {
        this.selectedRoles = selectedRoles
    }

    String getFilter() {
        return filter
    }

    void setFilter(String filter) {
        this.filter = filter
    }

    ListModelList<Role> getRoles() {
        if(roles.equals(null)){
            roles = new ListModelList<>(Role.all)
        }
        return roles
    }

    Role getSelectedRole() {
        return selectedRole
    }

    void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole
    }

    Boolean getBtsalvar() {
        return btsalvar
    }

    ListModelList<RoleGroup> getRoleGroups() {
        if(roleGroups==null){
            roleGroups = new ListModelList<>(RoleGroup.all)
        }
        return roleGroups
    }

    String getSelectedRoleGroup() {
        return selectedRoleGroup
    }

    void setSelectedRoleGroup(String selectedRoleGroup) {
        this.selectedRoleGroup = selectedRoleGroup
    }

    String getRoleGroup() {
        return roleGroup
    }

    void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup
    }

    boolean getViewRole() {
        return viewRole
    }

    boolean getAtivo() {
        return ativo
    }

    void setAtivo(boolean ativo) {
        this.ativo = ativo
    }

    @NotifyChange(["selectedRoleGroup","roleGroups","message"])
    @Command
    void salvarRoleGroup(){
        while (selectedRoleGroup.equals(null)){
         //  Messagebox.show("Preecha os campos!", "cinodemica", 1, Messagebox.ERROR)
            message = "Preecha os campos!"
            return
        }
        if (RoleGroup.findAllByName(selectedRoleGroup)) {
          //  Messagebox.show("Este nome ja existe!", "cinodemica", 1, Messagebox.ERROR)
            message="Este nome ja existe!"
            return
        }
        RoleGroup rg = new RoleGroup()
        rg.name=selectedRoleGroup

        rg.save()
        roleGroups.add(rg)

      //  Messagebox.show("Role Group "+rg.name+" foi criado com sucesso!", "cinodemica", 1,  Messagebox.INFORMATION)
        message="Role Group "+rg.name+" foi criado com sucesso!"
        selectedRoleGroup=null
    }
    @NotifyChange(["btsalvar","viewRole"])
    @Command
    void addRole(){
        viewRole=true
       btsalvar= true
    }

    @NotifyChange(["btsalvar","roles","viewRole"])
    @Command
    void showRoleGroup(){
        viewRole=true
        btsalvar = false
    }
    @NotifyChange(["btsalvar","roles","viewRole"])
    @Command
    void showRole(){
        viewRole=false
        btsalvar = false
    }


    @NotifyChange(["selectedRoleGroup","roleGroups","message"])
    @Command
    void updateRoleGroup(){
        while (selectedRoleGroup.equals(null)){
          //  Messagebox.show("Preecha os campos!", "cinodemica", 1, Messagebox.ERROR)
           message="Preecha os campos!"
            return
        }
        RoleGroup rg = RoleGroup.findByName(selectedRoleGroup)
        rg.name = selectedRoleGroup

        rg.save()
        selectedRoleGroup = null
       // Messagebox.show("Os dados do Role Group "+rg.name+" foram actualizados com sucesso!", "cinodemica", 1,  Messagebox.INFORMATION)
        message = "Os dados do Role Group "+rg.name+" foram actualizados com sucesso!"
    }

    @Command
    @NotifyChange("message")
    def confirmar(){
        message="Para Eliminar o item selecionada click duas veses sobre o botão Eliminar!"
    }
    @Command
    @NotifyChange(["selectedRoleGroup","roleGroups","message"])
    void deleteRoleGroup (){
        try {

            while (selectedRoleGroup.equals(null)){
                message= "Selecione O Role Group que deseja eliminar!"
               // Messagebox.show("Selecione O Role Group que deseja eliminar!", "cinodemica", 1, Messagebox.ERROR)
                return
            }

          def srg = RoleGroup.findByName(selectedRoleGroup)
            srg.delete(flush: true)

            roleGroups=null
            selectedRoleGroup = null
            message="O Role Group "+selectedRoleGroup+" foi eliminado!"
        }catch (Exception e ){
         //   Messagebox.show("Selecione Um Group Role!")
            message="Selecione Um Group Role!"
        }


    }

    @Command
    @NotifyChange(["viewRole"])
    void fecharEditor(){
        viewRole = false

    }


    @NotifyChange("clientes")
    @Command
    void doSearch() {

        roles.clear()
        List<Role> allItems = Role.all
        if (filter == null || "".equals(filter)) {
            roles.addAll(allItems)
        } else {
            for (Role c : allItems) {
                if (c.authority.toLowerCase().indexOf(filter.toLowerCase()) >= 0) {
                    roles.add(c)
                }
            }
        }
    }


    @Init init() {
        // initialzation code here
    }



}
