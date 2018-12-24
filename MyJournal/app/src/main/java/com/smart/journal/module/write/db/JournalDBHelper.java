package com.smart.journal.module.write.db;

import android.text.TextUtils;

import com.smart.journal.contants.Contancts;
import com.smart.journal.module.write.bean.JournalBean;
import com.smart.journal.module.write.bean.JournalBeanDBBean;
import com.smart.journal.module.write.bean.JournalLocationDBBean;
import com.smart.journal.tools.location.LocationTools;
import com.smart.journal.tools.location.bean.LocationBean;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author guandongchen
 * @date 2018/1/22
 */

public class JournalDBHelper {

    /**
     * 保存日记
     * @param realm
     * @param writeSectionBeans
     */
    public static void saveJournal(Realm realm,List<JournalBean> writeSectionBeans ){
        realm.executeTransaction(realm1 -> {
            JournalBeanDBBean journalBeanDBBean = realm1.createObject(JournalBeanDBBean.class,UUID.randomUUID()+"");
            StringBuilder contentSb = new StringBuilder();
            for (JournalBean journalBean:
                    writeSectionBeans) {

                if (journalBean.getItemType()==JournalBean.WRITE_TAG_IMAGE){


                    contentSb.append(Contancts.FILE_TYPE_IMAGE+journalBean.getImageURL()+Contancts.FILE_TYPE_SPLIT);
                }else{
                    if (!TextUtils.isEmpty(journalBean.getContent().trim())){
                        contentSb.append(Contancts.FILE_TYPE_TEXT+journalBean.getContent()+Contancts.FILE_TYPE_SPLIT);
                    }

                }
            }
            journalBeanDBBean.setContent(contentSb.toString());
            journalBeanDBBean.setDate(new Date());
            LocationBean locationBean =  LocationTools.getLocationBean();
            if (!TextUtils.isEmpty(locationBean.getAdress())){
                JournalLocationDBBean locationDBBean = realm1.createObject(JournalLocationDBBean.class);
                locationDBBean.setAdress(locationBean.getAdress());
                locationDBBean.setLatitude(locationBean.getLatitude());
                locationDBBean.setLongitude(locationBean.getLongitude());
                journalBeanDBBean.setLocation(locationDBBean);
            }
            journalBeanDBBean.setTags("默认");

        });
    }


    /**
     * 获取所有日记
     * @param realm
     * @return
     */
    public static RealmResults<JournalBeanDBBean> getAllJournals(Realm realm){
        return realm.where(JournalBeanDBBean.class).findAll().sort("date", Sort.DESCENDING);
    }

    /**
     * 删除日记
     * @param realm
     * @param journalBeanDBBean
     */
    public static void deleteJournal(Realm realm,JournalBeanDBBean journalBeanDBBean){

        realm.executeTransaction(realm1 -> journalBeanDBBean.deleteFromRealm());
    }
}