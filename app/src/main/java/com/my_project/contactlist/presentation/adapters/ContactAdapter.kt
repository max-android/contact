package com.my_project.contactlist.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.my_project.contactlist.R
import com.my_project.contactlist.utilities.visible

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ContactAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<BaseType> = mutableListOf()

    private var action: (BaseType)-> Unit = { }

    fun onItemClick(action: (BaseType) -> Unit){
        this.action = action
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            1 -> TitleHolder(inflater.inflate(R.layout.item_title, parent, false))
            2 -> ContactFriendHolder(inflater.inflate(R.layout.item_card, parent, false))
            3 -> ContactСolleagHolder(inflater.inflate(R.layout.item_card, parent, false))
            else -> throw Exception("unknown viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is ContactTitleType -> 1
            is ContactFriendType -> 2
            is ContactСolleagType -> 3
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myType = items[position]
        when(holder){
            is TitleHolder -> holder.bindTo(myType as ContactTitleType)
            is ContactFriendHolder -> holder.bindTo(myType as ContactFriendType)
            is ContactСolleagHolder -> holder.bindTo(myType as ContactСolleagType)
        }
    }

    override fun getItemCount() = items.size

    fun swapItems(newItems: List<BaseType>){
        val diffResult = DiffUtil.calculateDiff(ContactDiffCallback(this.items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    private inner class TitleHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {

        private var titleItemTextView = containerView.findViewById(R.id.titleItemTextView) as AppCompatTextView
        fun bindTo(typeContact: ContactTitleType) = with(typeContact) {
            titleItemTextView.text = title
        }
    }

    private inner class ContactFriendHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {

        private var friendGroup = containerView.findViewById(R.id.friendGroup) as Group
        private var imageView = containerView.findViewById(R.id.imageView) as SimpleDraweeView
        private var fTextView = containerView.findViewById(R.id.fTextView) as AppCompatTextView
        private var iTextView = containerView.findViewById(R.id.iTextView) as AppCompatTextView
        private var oTextView = containerView.findViewById(R.id.oTextView) as AppCompatTextView
        private var telTextView = containerView.findViewById(R.id.telTextView) as AppCompatTextView
        private var birthdateTextView = containerView.findViewById(R.id.birthdateTextView) as AppCompatTextView

        init {
            containerView.setOnClickListener { action(items[layoutPosition]) }
            friendGroup.visible()
        }

        fun bindTo(type: ContactFriendType) = with(type) {
            url?.let {
                imageView.setImageURI(it)
            }
            fTextView.text = surname
            iTextView.text = name
            oTextView.text = patronymic
            telTextView.text = phone
            birthdate?.let {
                birthdateTextView.text = it
            }
        }
    }

    private inner class ContactСolleagHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {

        private var colleaguesGroup = containerView.findViewById(R.id.colleaguesGroup) as Group
        private var imageView = containerView.findViewById(R.id.imageView) as SimpleDraweeView
        private var fTextView = containerView.findViewById(R.id.fTextView) as AppCompatTextView
        private var iTextView = containerView.findViewById(R.id.iTextView) as AppCompatTextView
        private var oTextView = containerView.findViewById(R.id.oTextView) as AppCompatTextView
        private var telTextView = containerView.findViewById(R.id.telTextView) as AppCompatTextView
        private var positionTextView = containerView.findViewById(R.id.positionTextView) as AppCompatTextView
        private var officePhoneTextView = containerView.findViewById(R.id.officePhoneTextView) as AppCompatTextView

        init {
            containerView.setOnClickListener { action(items[layoutPosition]) }
            colleaguesGroup.visible()
        }

        fun bindTo(type: ContactСolleagType) = with(type) {
            url?.let {
                imageView.setImageURI(it)
            }
            fTextView.text = surname
            iTextView.text = name
            oTextView.text = patronymic
            telTextView.text = phone
            position?.let {
                positionTextView.text = it
            }
            office_phone?.let {
                officePhoneTextView.text = it
            }
        }
    }
}