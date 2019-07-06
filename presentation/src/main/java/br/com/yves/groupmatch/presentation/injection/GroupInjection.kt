package br.com.yves.groupmatch.presentation.injection

import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.ServiceGenerator
import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import br.com.yves.groupmatch.data.group.FirestoreGroupRepository
import br.com.yves.groupmatch.data.match.FirestoreMatchRepository
import br.com.yves.groupmatch.data.match.GroupMatchService
import br.com.yves.groupmatch.data.user.FirestoreUserRepository
import br.com.yves.groupmatch.presentation.GroupMatchApplication
import br.com.yves.groupmatch.presentation.ui.groups.GroupController
import br.com.yves.groupmatch.presentation.ui.groups.GroupFragment
import br.com.yves.groupmatch.presentation.ui.groups.GroupPresenterImpl

object GroupInjection {
	val service by lazy {
		ServiceGenerator.createService(
			GroupMatchService::class.java,
			GroupMatchApplication.instance.getString(R.string.groupmatch_service_url))
	}

	fun make(view: GroupFragment): GroupController {

		val firestoreUserRepository = FirestoreMatchRepository(service)

		return GroupController(
				view,
				GroupPresenterImpl(),
				FirestoreGroupRepository(FirestoreUserRepository(), firestoreUserRepository),
				firestoreUserRepository,
				GroupMatchAuth.instance
		)
	}
}