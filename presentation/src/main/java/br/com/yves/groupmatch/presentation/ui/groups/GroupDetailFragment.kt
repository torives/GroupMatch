package br.com.yves.groupmatch.presentation.ui.groups


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel

interface GroupDetailView {
	fun displayMatchButton()
	fun hideMatchButton()
	fun displayGroupMembers(members: List<UserViewModel>)
}

class GroupDetailFragment : Fragment(), GroupDetailView {

	private val args: GroupDetailFragmentArgs by navArgs()

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_group_detail, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

	}
	//endregion

	//region GroupDetailView
	override fun displayMatchButton() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun hideMatchButton() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun displayGroupMembers(members: List<UserViewModel>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

}
