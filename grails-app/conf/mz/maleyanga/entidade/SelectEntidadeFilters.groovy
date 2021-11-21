package mz.maleyanga.entidade

import org.zkoss.zk.ui.Session

/**
 * SelectEntidadeFilters
 * A filters class is used to execute code before and after a controller action is executed and also after a view is rendered
 */
class SelectEntidadeFilters {
   // def dependsOn = [MyOtherFilters]
    Session session
    def filters = {

        allExceptIndex(controller:'entidade','login', action:'select','auth', invert:true) {
                before = {

                    if (!session.user && !actionName.equals('login')) {
                        redirect(action: 'login')
                        return false
                    }else if (session.getAttribute('entidade')==null){
                        redirect(controller: 'entidade',action: 'select')
                        return false
                    }


                }

            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
      /*  all(controller:'*', action:'*', invert:true) {
            before = {

                if (session.getAttribute('entidade')==null){
                    redirect(controller: 'entidade',action: 'select')
                    return false
                }


            }

            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }*/
    }
}
