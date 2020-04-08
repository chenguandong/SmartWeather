package com.smart.journal.module.journal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smart.journal.db.entity.JournalBeanDBBean
import com.smart.journal.module.write.db.JournalDBHelper
import java.util.*

/**
 * @author guandongchen
 * @date 26/03/2018
 */

class JournalRepositoryImpl : JournalRepository {



    override fun getJournalBeans(): LiveData<List<JournalBeanDBBean> >{
        return JournalDBHelper.allJournals()
    }


    override fun deleteJournal(journalBeanDBBean: JournalBeanDBBean) {
        JournalDBHelper.deleteJournal( journalBeanDBBean)
    }

    override fun onLiveDataCleared() {

    }

    override fun searchJournalByKeyWord(keyWord: String): LiveData<List<JournalBeanDBBean>> {
        return JournalDBHelper.searchJournalByKeyWord(keyWord)
    }

    override fun searchJournalByTag(tagName: String): LiveData<List<JournalBeanDBBean>> {
        return JournalDBHelper.searchJournalByTag(tagName)
    }


}
