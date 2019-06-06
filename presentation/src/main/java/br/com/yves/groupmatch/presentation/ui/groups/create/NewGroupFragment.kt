package br.com.yves.groupmatch.presentation.ui.groups.create


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_new_group.*


class NewGroupFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_new_group, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val viewModels = listOf(
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "fulaninho@gmail.com", false, "https://i.redd.it/4fz0ct0l7mo11.jpg")
		)

		val adapter = UserAdapter(Glide.with(this), viewModels)
		new_group_userRecyclerView.layoutManager = LinearLayoutManager(context)
		new_group_userRecyclerView.adapter = adapter
	}

}
