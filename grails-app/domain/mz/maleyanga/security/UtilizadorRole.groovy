package mz.maleyanga.security

import org.apache.commons.lang.builder.HashCodeBuilder

class UtilizadorRole implements Serializable {

    private static final long serialVersionUID = 1

    Utilizador utilizador
    Role role

    boolean equals(other) {
        if (!(other instanceof UtilizadorRole)) {
            return false
        }

        other.utilizador?.id == utilizador?.id &&
                other.role?.id == role?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (utilizador) builder.append(utilizador.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static UtilizadorRole get(long utilizadorId, long roleId) {
        UtilizadorRole.where {
            utilizador == Utilizador.load(utilizadorId) &&
                    role == Role.load(roleId)
        }.get()
    }

    static boolean exists(long utilizadorId, long roleId) {
        UtilizadorRole.where {
            utilizador == Utilizador.load(utilizadorId) &&
                    role == Role.load(roleId)
        }.count() > 0
    }

    static UtilizadorRole create(Utilizador utilizador, Role role, boolean flush = false) {
        def instance = new UtilizadorRole(utilizador: utilizador, role: role)
        instance.save(flush: flush, insert: true)
        instance
    }

    static boolean remove(Utilizador u, Role r, boolean flush = false) {
        if (u == null || r == null) return false

        int rowCount = UtilizadorRole.where {
            utilizador == Utilizador.load(u.id) &&
                    role == Role.load(r.id)
        }.deleteAll()

        if (flush) {
            UtilizadorRole.withSession { it.flush() }
        }

        rowCount > 0
    }

    static void removeAll(Utilizador u, boolean flush = false) {
        if (u == null) return

        UtilizadorRole.where {
            utilizador == Utilizador.load(u.id)
        }.deleteAll()

        if (flush) {
            UtilizadorRole.withSession { it.flush() }
        }
    }

    static void removeAll(Role r, boolean flush = false) {
        if (r == null) return

        UtilizadorRole.where {
            role == Role.load(r.id)
        }.deleteAll()

        if (flush) {
            UtilizadorRole.withSession { it.flush() }
        }
    }

    static constraints = {
        role validator: { Role r, UtilizadorRole ur ->
            if (ur.utilizador == null) return
            boolean existing = false
            UtilizadorRole.withNewSession {
                existing = UtilizadorRole.exists(ur.utilizador.id, r.id)
            }
            if (existing) {
                return 'userRole.exists'
            }
        }
    }

    static mapping = {
        id composite: ['role', 'utilizador']
        version false
    }

     String toString() {
         return "${utilizador + ":" + role}"
    }
}
