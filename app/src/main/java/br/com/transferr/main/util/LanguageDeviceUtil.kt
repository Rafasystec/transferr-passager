package br.com.transferr.main.util

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.enums.EnumLanguage

/**
 * Created by Rafael Rocha on 30/10/2018.
 */
object LanguageDeviceUtil {
    fun transform(map: Map<EnumLanguage, String>): String{
        var description:String?=""
        if(ApplicationTransferr.DEVICE_LANGUAGE == ApplicationTransferr.LANG_PT){
            map.forEach{
                mapItem ->
                run {
                    var key = mapItem.key
                    if (key == EnumLanguage.PT_BR) {
                        description = mapItem.value
                    }
                }
            }
        }else{
            map.forEach{
                mapItem ->
                run {
                    var key = mapItem.key
                    if (key == EnumLanguage.EN_USA) {
                        description = mapItem.value
                    }
                }
            }
        }
        return description!!
    }
}