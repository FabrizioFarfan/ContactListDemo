package com.example.contactlistappproject.ui



import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistappproject.R
import com.example.contactlistappproject.adapter.ContactAdapter
import com.example.contactlistappproject.databinding.ActivityMainBinding
import com.example.contactlistappproject.ui.addfragment.AddContactFragment
import com.example.contactlistappproject.ui.deletefragment.DeleteFragment
import com.example.contactlistappproject.utils.Constants.BUNDLE_ID
import com.example.contactlistappproject.utils.DataStatus
import com.example.contactlistappproject.utils.SnackbarListener
import com.example.contactlistappproject.viewmodel.ViewModelDB
import com.example.contactlistappproject.viewmodel.isVisible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SnackbarListener  {

    @Inject
    lateinit var contactAdapter: ContactAdapter

    private val viewModel: ViewModelDB by viewModels()

    private lateinit var binding: ActivityMainBinding

    private var selectedItem = 0
    private lateinit var itemTouchHelper: ItemTouchHelper
    fun detachItemTouchHelper() {
        itemTouchHelper.attachToRecyclerView(null)
    }
    fun attachItemTouchHelper() {

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition
                val contact = contactAdapter.differ.currentList[position]
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteContact(contact)
                        showSnackbar("Item Deleted!")
                        Snackbar.make(binding.root,"Item Deleted!",Snackbar.LENGTH_LONG).apply {
                            view.setBackgroundColor(Color.RED)
                            setTextColor(Color.WHITE)
                            setActionTextColor(Color.WHITE)
                            setAction("UNDO"){
                                viewModel.saveContact(false,contact)
                            }

                        }.show()
                    }
                    ItemTouchHelper.RIGHT -> {

                        val addContactFragment = AddContactFragment()
                        val bundle = Bundle()
                        bundle.putInt(BUNDLE_ID,contact.id)
                        addContactFragment.arguments = bundle
                        addContactFragment.show(supportFragmentManager,AddContactFragment().tag)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val deleteBackgroundColor = ContextCompat.getColor(applicationContext, R.color.purple_450)
                val deleteLabelColor = ContextCompat.getColor(applicationContext, R.color.purple_600)
                val editBackgroundColor = ContextCompat.getColor(applicationContext, R.color.purple_300)
                RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete")
                    .addSwipeLeftBackgroundColor(deleteBackgroundColor)
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .setSwipeLeftLabelColor(deleteLabelColor)
                    .setSwipeLeftActionIconTint(deleteLabelColor)
                    .addSwipeRightLabel("Edit")
                    .addSwipeRightBackgroundColor(editBackgroundColor)
                    .addSwipeRightActionIcon(R.drawable.edit)
                    .setSwipeRightLabelColor(deleteLabelColor)
                    .setSwipeRightActionIconTint(deleteLabelColor)
                    .create()
                    .decorate()



                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }

        itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvContacts)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            setSupportActionBar(toolBar)
            floatingActionButton.setOnClickListener {
                AddContactFragment().show(supportFragmentManager, AddContactFragment().tag)

            }
            attachItemTouchHelper()
            viewModel.getAllContacts()
            viewModel.contactList.observe(this@MainActivity){ it ->
                when(it.status){
                    DataStatus.Status.LOADING->{
                        progressBar.isVisible(true,rvContacts)
                        emptyBody.isVisible(false,rvContacts)
                    }
                    DataStatus.Status.SUCCESS->{
                        it.isEmpty?.let {
                            showEmpty(it)
                        }
                        progressBar.isVisible(false,rvContacts)
                        contactAdapter.differ.submitList(it.data)
                        rvContacts.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = contactAdapter
                        }
                    }
                    DataStatus.Status.ERROR->{
                        progressBar.isVisible(false,rvContacts)
                        Toast.makeText(this@MainActivity,it.message,Toast.LENGTH_LONG).show()
                    }
                }
            }

            toolBar.setOnMenuItemClickListener  {
                when(it.itemId){
                    R.id.actionDeleteAll ->{
                        DeleteFragment().show(supportFragmentManager,DeleteFragment().tag)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.actionSort ->{
                        filter()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{
                        return@setOnMenuItemClickListener false
                    }
                }
            }



        }
    }

    private fun filter(){
        val builder = AlertDialog.Builder(this,R.style.CustomDialogAlert)
        val sortItem = arrayOf("Newer(Default)","Name : A-Z", "Name : Z-A")
        builder.setSingleChoiceItems(sortItem, selectedItem){ dialog, item ->
            when(item){
                0 -> viewModel.getAllContacts()
                1 -> viewModel.getSortedContactsAsc()
                2 -> viewModel.getSortedContactsDesc()
            }
            selectedItem = item
            dialog.dismiss()
        }
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        val search = menu?.findItem(R.id.actionSearch)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchByName(newText!!)
                    return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


    private fun showEmpty(isShown : Boolean){
        binding.apply {
            if (isShown){
                emptyBody.isVisible(true,rvContacts)
            }else{
                emptyBody.isVisible(false,rvContacts)
            }
        }

    }

    override fun showSnackbar(message: String) {
       val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(Color.RED) // Opcional: Fondo personalizado
        snackbar.setTextColor(Color.WHITE) // Opcional: Color de texto personalizado
        snackbar.show()

    }




}