package br.com.yves.groupmatch.presentation.ui.groups


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_groups.*

class GroupsFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_groups, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val groups = listOf(
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19"),
				GroupViewModel("Nome do Grupo", "Luizinho, Zezinho, Betinho, Boninho, Luluzinha, Magali", "https://i.redd.it/4fz0ct0l7mo11.jpg", true, "21/04/19")

		)
		val groupAdapter = GroupAdapter(groups, Glide.with(this))
		recyclerview_groups_list.layoutManager = LinearLayoutManager(context)
		recyclerview_groups_list.setHasFixedSize(true)
		recyclerview_groups_list.adapter = groupAdapter
	}
}
