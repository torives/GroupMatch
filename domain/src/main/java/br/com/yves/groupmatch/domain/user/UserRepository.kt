package br.com.yves.groupmatch.domain.user

interface UserRepository {
	fun getUser(uid: String, callback: GetUserCallback)
	fun getUsers(uids: List<String>, callback: GetUserCallback)

	interface GetUserCallback {
		fun onSuccess(user: User){}
		fun onSuccess(users: List<User>){}
		fun onFailure()
	}
}