package com.my_project.contactlist.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ContactDiffCallback (
    private val oldList: List<BaseType>,
    private val newList: List<BaseType>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int):Boolean {
        val per = oldList[oldItemPosition].getItemId() == newList[newItemPosition].getItemId()
        return per
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
}