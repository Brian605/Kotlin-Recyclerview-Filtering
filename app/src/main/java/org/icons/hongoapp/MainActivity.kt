package org.icons.hongoapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView;
    lateinit var postList:MutableList<Post>;
    lateinit var filteredList:MutableList<Post>;
   lateinit var adapter:PostAdapter
   lateinit var toolbar: MaterialToolbar
   lateinit var search: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView=findViewById(R.id.recycler);
        toolbar=findViewById(R.id.toolbar)
        search=findViewById(R.id.searchBar)

        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this);

        postList= mutableListOf()
        filteredList= mutableListOf()
        adapter=PostAdapter(this,postList)
        recyclerView.adapter=this.adapter

        fetchData();
    }

    /** Populate the recyclerview list with dummy data
     *
     */
    fun fetchData() {
        postList.clear()
         postList.add(Post("Kinetics, A hands-on Experience","Science",
            "F1_Weather and the sky","Form 1"))
        postList.add(Post("Mechanics, A hands-on Experience","Science",
            "F2_kinematics","Form 2"))
        postList.add(Post("Motion, A hands-on Experience","Science",
            "F1_Linear Motion","Form 1"))
        postList.add(Post("Genetics, A hands-on Experience","Science",
            "F1_Weather and the sky","Form 1"))
        postList.add(Post("Atoms, A hands-on Experience","Science",
            "F1_Weather and the sky","Form 1"))
        postList.add(Post("Verbs, A hands-on Experience","English",
            "F4_Advanced Verbs","Form 4"))
        postList.add(Post("Nouns, A hands-on Experience","English",
            "F3_Basics of Nouns","Form 3"))

        Utils.listSize=postList.size.toString()
adapter.notifyDataSetChanged();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.nav_filter){
FilterDialog.showDialog(supportFragmentManager)

        }
        return super.onOptionsItemSelected(item)
    }

    /** Filter the content according to class/form
     * @param value The class/form
     * @param listener Filter complete listener
     */
    fun filterClass(value:String,listener: CompleteListener){
        filteredList.clear()
        Log.e("toFilter",value)
       for(element in postList){
           if (element.form.contains(value,true)){
               filteredList.add(element)
           }
       }
        adapter= PostAdapter(this,filteredList);
        recyclerView.adapter=adapter
        Utils.listSize=filteredList.size.toString()
        Log.e("FilterList",filteredList.size.toString())
        listener.onComplete()
    }

    /**Filter the list and show only selected subject
     * @param value The subject to filter
     * @param listener Filter complete listener
     */
    fun filterSubject(value:String, listener:CompleteListener){
        val filter2: MutableList<Post> = if (filteredList.isEmpty()){

            postList.filter {
                it.subject.contains(value,true)
            } as MutableList<Post>
        }else{
            filteredList.filter {
                it.subject.contains(value, true)
            } as MutableList<Post>
        }
        filteredList.clear()
        filteredList.addAll(filter2)

        Utils.listSize=filteredList.size.toString()
        Log.e("FilterList",filteredList.size.toString()+value)
        listener.onComplete()
        adapter.notifyDataSetChanged()

    }

    /**Get list of all available units for a subject. if youre fetching this from online youd probably want to implement
     * A progress dialog alongside
     * In cases where the other filter parameters are large and not pre-defined, this logic can also be used to get a list of available
     * Subjects and Classes

    * @param subject: The subject to get a list of units for
     *@return
     * @property unitsList: A string list of all the units
    */
    fun getUnits(subject:String):List<String>{
      val unitsList:MutableList<String> = mutableListOf();
        for(element in postList){
            if (element.subject.contains(subject,true)){
                unitsList.add(element.unit)
            }
        }
        return unitsList

    }

    /**Show only the units that belong to the selected subject
     * @param subject The selected subject
     * @param unit The unit to filter
     * @param listener Filter complete listener
     */
    fun filterUnits(subject:String,units:String, listener:CompleteListener){
        val filter2: MutableList<Post> =postList.filter {
            it.subject.contains(subject,true) and it.unit.contains(units,true)
        } as MutableList<Post>

        filteredList.clear()
        filteredList.addAll(filter2)

        Utils.listSize=filteredList.size.toString()
        Log.e("FilterList",filteredList.size.toString())
        listener.onComplete()
        adapter.notifyDataSetChanged()

    }

    /** Sets the adapter to use the filtered list
     *
     */
    fun setFilterList(){
        adapter=PostAdapter(this,filteredList)
        recyclerView.adapter=adapter
    }

}