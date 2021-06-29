package org.icons.hongoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val context:Context, var postList:MutableList<Post>) :RecyclerView.Adapter<PostAdapter.PostHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view: View =LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
        val holder= PostHolder(view);
        view.setOnClickListener(View.OnClickListener {

        })
        return holder;

    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bindView(postList.get(holder.adapterPosition))
    }

    override fun getItemCount(): Int {
        return this.postList.size;
    }
    class PostHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        var titleView =itemView.findViewById<TextView>(R.id.postTitle);
        var postSubject=itemView.findViewById<TextView>(R.id.subject);
        var form=itemView.findViewById<TextView>(R.id.form);
        var postUnit=itemView.findViewById<TextView>(R.id.unit);

        fun bindView(post:Post){
            titleView.text=post.title;
            postSubject.text=post.subject;
            form.text=post.form;
            postUnit.text=post.unit;
        }
    }

}