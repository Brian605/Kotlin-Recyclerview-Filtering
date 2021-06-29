package org.icons.hongoapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
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
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UploadDialog: DialogFragment(){
    lateinit var toolbar: MaterialToolbar;
    lateinit var classInput:MaterialButton;
    lateinit var subjectInput: TextInputEditText
    lateinit var unitInput:TextInputEditText
    lateinit var titleInput:TextInputEditText
    lateinit var postBtn:MaterialButton
    lateinit var progress:CircularProgressIndicator

    companion object{

        fun showDialog(manager: FragmentManager){
            val postDialog=UploadDialog();
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
       val view:View=inflater.inflate(R.layout.post_layout,container,false);
        postBtn=view.findViewById(R.id.save)
       classInput=view.findViewById(R.id.inputClass)
        unitInput=view.findViewById(R.id.inputUnit)
        toolbar=view.findViewById(R.id.toolbar)
        subjectInput=view.findViewById(R.id.inputSubject)
        titleInput=view.findViewById(R.id.inputTitle)
        progress=view.findViewById(R.id.progress)
        return view

          }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val mainActivity:MainActivity= requireActivity() as MainActivity
            mainActivity.fetchData()
            dismiss()

        })


        classInput.setOnClickListener(View.OnClickListener {
           val classes:Array<String> = arrayOf("Form 1","Form 2","Form 3","Form 4")
            val builder = MaterialAlertDialogBuilder(requireActivity())
            builder.setItems(classes,DialogInterface.OnClickListener { dialog, which ->
                classInput.text=classes[which];
                dialog.dismiss()

            })
            val dialog:Dialog=builder.create();
            dialog.show()

        })

        postBtn.setOnClickListener(View.OnClickListener {
            if (classInput.text.toString().contains("select",true)){
                Toast.makeText(requireActivity(),"You must select a class target",Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(unitInput.text) || TextUtils.isEmpty(titleInput.text) ||
                    TextUtils.isEmpty(subjectInput.text)){
                Toast.makeText(requireActivity(),"One or more fields is empty",Toast.LENGTH_LONG).show()
                return@OnClickListener

            }

val postClass:String=classInput.text.toString()
            val subject:String=subjectInput.text.toString()
            val title:String=titleInput.text.toString()
            val unit:String=unitInput.text.toString()

            val post=Post(title,subject,unit,postClass);
            progress.visibility=View.VISIBLE
           val db=Firebase.firestore

            db.collection("posts")
                .add(post)
                .addOnSuccessListener {
                    progress.visibility=View.GONE
                    dismiss()
                }
                .addOnFailureListener{
                    progress.visibility=View.GONE
                    it.printStackTrace()
                    Log.e("An Error Occured", it.message.toString())
                }

        })


    }
}








