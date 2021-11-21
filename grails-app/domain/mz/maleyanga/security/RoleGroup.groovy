package mz.maleyanga.security

class RoleGroup implements Serializable{
	private static final long serialVersionUID = 1
	String name

	static mapping = {
		cache true
	}

	Set<Role> getAuthorities() {
		RoleGroupRole.findAllByRoleGroup(this).collect { it.role }
	}

	static constraints = {
		name blank: false, unique: true
	}

	 String toString() {
		return "${name}"
	}
}
