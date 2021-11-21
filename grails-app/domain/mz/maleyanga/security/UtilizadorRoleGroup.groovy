package mz.maleyanga.security

import org.apache.commons.lang.builder.HashCodeBuilder

class UtilizadorRoleGroup implements Serializable {

	private static final long serialVersionUID = 1

	Utilizador utilizador
	RoleGroup roleGroup

	boolean equals(other) {
		if (!(other instanceof UtilizadorRoleGroup)) {
			return false
		}

		other.utilizador?.id == utilizador?.id &&
		other.roleGroup?.id == roleGroup?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (utilizador) builder.append(utilizador.id)
		if (roleGroup) builder.append(roleGroup.id)
		builder.toHashCode()
	}

	static UtilizadorRoleGroup get(long utilizadorId, long roleGroupId) {
		UtilizadorRoleGroup.where {
			utilizador == Utilizador.load(utilizadorId) &&
			roleGroup == RoleGroup.load(roleGroupId)
		}.get()
	}

	static boolean exists(long utilizadorId, long roleGroupId) {
		UtilizadorRoleGroup.where {
			utilizador == Utilizador.load(utilizadorId) &&
			roleGroup == RoleGroup.load(roleGroupId)
		}.count() > 0
	}

	static UtilizadorRoleGroup create(Utilizador utilizador, RoleGroup roleGroup, boolean flush = false) {
		def instance = new UtilizadorRoleGroup(utilizador: utilizador, roleGroup: roleGroup)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(Utilizador u, RoleGroup g, boolean flush = false) {
		if (u == null || g == null) return false

		int rowCount = UtilizadorRoleGroup.where {
			utilizador == Utilizador.load(u.id) &&
			roleGroup == RoleGroup.load(g.id)
		}.deleteAll()

		if (flush) { UtilizadorRoleGroup.withSession { it.flush() } }

		rowCount > 0
	}

	static void removeAll(Utilizador u, boolean flush = false) {
		if (u == null) return

		UtilizadorRoleGroup.where {
			utilizador == Utilizador.load(u.id)
		}.deleteAll()

		if (flush) { UtilizadorRoleGroup.withSession { it.flush() } }
	}

	static void removeAll(RoleGroup g, boolean flush = false) {
		if (g == null) return

		UtilizadorRoleGroup.where {
			roleGroup == RoleGroup.load(g.id)
		}.deleteAll()

		if (flush) { UtilizadorRoleGroup.withSession { it.flush() } }
	}

	static constraints = {
		utilizador validator: { Utilizador u, UtilizadorRoleGroup ug ->
			if (ug.roleGroup == null) return
			boolean existing = false
			UtilizadorRoleGroup.withNewSession {
				existing = UtilizadorRoleGroup.exists(u.id, ug.roleGroup.id)
			}
			if (existing) {
				return 'userGroup.exists'
			}
		}
	}

	static mapping = {
		id composite: ['roleGroup', 'utilizador']
		version false
	}

	 String toString() {
		 return "${utilizador + ":" + roleGroup}"
	 }
}
