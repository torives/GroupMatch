package br.com.yves.groupmatch.presentation.ui.group.create.data

import br.com.yves.groupmatch.presentation.ui.group.create.UserViewModel
import java.io.Serializable

data class NewGroupDetailsViewModel(val members: List<UserViewModel>): Serializable