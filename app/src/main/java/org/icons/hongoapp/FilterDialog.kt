package org.icons.hongoapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FilterDialog: DialogFragment(){
    lateinit var toolbar: MaterialToolbar;
    lateinit var classInput:MaterialButton;
    lateinit var subject: MaterialButton
    lateinit var unit:MaterialButton
    lateinit var filterResult:MaterialButton

    companion object{

        fun showDialog(manager: FragmentManager){
            val postDialog=FilterDialog();
            postDialog.show(manager,"Posts Dialog");

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.Theme_HongoApp_FullDialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog =dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
        }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view:View=inflater.inflate(R.layout.filter_layout,container,false);
        filterResult=view.findViewById(R.id.filter)
       classInput=view.findViewById(R.id.inputClass)
        unit=view.findViewById(R.id.inputUnit)
        toolbar=view.findViewById(R.id.toolbar)
        subject=view.findViewById(R.id.inputSubject)
        return view

          }

    override fun onResume() {
        super.onResume()
        filterResult.text= "Show:"+Utils.listSize
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener(View.OnClickListener {

            val mainActivity:MainActivity= requireActivity() as MainActivity
            mainActivity.fetchData()
            dismiss()

        })

        filterResult.text= "Show:"+Utils.listSize

        classInput.setOnClickListener(View.OnClickListener {
           val classes:Array<String> = arrayOf("Form 1","Form 2","Form 3","Form 4")
            val builder = MaterialAlertDialogBuilder(requireActivity())
            builder.setItems(classes,DialogInterface.OnClickListener { dialog, which ->
                classInput.text=classes[which];

                val mainActivity:MainActivity= requireActivity() as MainActivity
                mainActivity.filterClass(classes[which],object:CompleteListener{
                    override fun onComplete() {
                       // super.onComplete()
Log.e("lstener","complete listener called")
                            filterResult.text= "Show:"+Utils.listSize
                        dialog.dismiss()
                    }

                })


            })
            val dialog:Dialog=builder.create();
            dialog.show()

        })

        subject.setOnClickListener(View.OnClickListener {
            val subjects:Array<String> = arrayOf("Science","Maths","CRE","English")
            val builder = MaterialAlertDialogBuilder(requireActivity())
            builder.setItems(subjects) { dialog, which ->
                subject.text = subjects[which];

                val mainActivity: MainActivity = requireActivity() as MainActivity
                mainActivity.filterSubject(subjects[which], object : CompleteListener {
                    override fun onComplete() {
                        // super.onComplete()
                        Log.e("listener", "complete listener called")
                        filterResult.text = "Show:" + Utils.listSize
                        dialog.dismiss()
                    }

                })


            }
            val dialog:Dialog=builder.create();
            dialog.show()
        })

        unit.setOnClickListener(View.OnClickListener {

            val subject:String=subject.text.toString()

            if (subject.contains("select",true)){
                Toast.makeText(requireContext(),"You need to select a subject first to apply this filter",Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            val mainActivity:MainActivity= requireActivity() as MainActivity
            val unitsAvailable:Array<String> = mainActivity.getUnits(subject).distinct().toTypedArray()
            val builder = MaterialAlertDialogBuilder(requireActivity())
            builder.setItems(unitsAvailable) { dialog, which ->
                unit.text = unitsAvailable[which];
                mainActivity.filterUnits(subject,unitsAvailable[which], object : CompleteListener {
                    override fun onComplete() {
                        // super.onComplete()
                        Log.e("listener", "complete listener called")
                        filterResult.text = "Show:" + Utils.listSize
                        dialog.dismiss()
                    }

                })


            }
            val dialog:Dialog=builder.create();
            dialog.show()
        })

        filterResult.setOnClickListener(View.OnClickListener {
            dismiss()
            val mainActivity:MainActivity= requireActivity() as MainActivity
            mainActivity.setFilterList()
        })


    }
}








