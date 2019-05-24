package br.com.transferr.main.util

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.enums.Color
import br.com.transferr.model.enums.EnumLanguage

/**
 * Created by Rafael Rocha on 30/10/2018.
 */
object LanguageDeviceUtil {
    fun transform(map: Map<EnumLanguage, String>): String{
        var description:String?=""
        if(ApplicationTransferr.DEVICE_LANGUAGE == ApplicationTransferr.LANG_PT) {
            map.forEach { mapItem ->
                run {
                    var key = mapItem.key
                    if (key == EnumLanguage.PT_BR) {
                        description = mapItem.value
                    }
                }
            }
        }else if(ApplicationTransferr.DEVICE_LANGUAGE == ApplicationTransferr.LANG_ES){
            map.forEach { mapItem ->
                run {
                    var key = mapItem.key
                    if (key == EnumLanguage.ES_ES) {
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

    fun getColorAsString(color:Color):String{
        return when(ApplicationTransferr.DEVICE_LANGUAGE){
            ApplicationTransferr.LANG_ES -> {
                return getColorInESFromColorEnum(color)
            }ApplicationTransferr.LANG_PT ->{
                return getColorInPTFromColorEnum(color)
            }else->{
                return getColorInENFromColorEnum(color)
            }
        }
    }

    fun getColorInPTFromColorEnum(color:Color):String{
        return when(color){
            Color.BEIGE ->{
                return "Bege"
            }
            Color.BLACK ->{
                return "Preto"
            }
            Color.BLUE ->{
                return "Azul"
            }
            Color.GREEN ->{
                return "Verde"
            }
            Color.GREY ->{
                return "Cinza"
            }
            Color.PURPLE ->{
                return "Roxo"
            }
            Color.RED ->{
                return "Vermelho"
            }
            Color.SILVER ->{
                return "Prata"
            }
            Color.WHITE -> {
                return "Branco"
            }
            Color.YELLOW ->{
                return "Amarelo"
            }
            else -> return "Não informada"
        }
    }

    fun getColorInENFromColorEnum(color:Color):String{
        return when(color){
            Color.BEIGE ->{
                return "Beige"
            }
            Color.BLACK ->{
                return "Black"
            }
            Color.BLUE ->{
                return "Blue"
            }
            Color.GREEN ->{
                return "Green"
            }
            Color.GREY ->{
                return "Grey"
            }
            Color.PURPLE ->{
                return "Purple"
            }
            Color.RED ->{
                return "Red"
            }
            Color.SILVER ->{
                return "Silver"
            }
            Color.WHITE -> {
                return "White"
            }
            Color.YELLOW ->{
                return "Yellow"
            }
            else -> return "None"
        }
    }

    fun getColorInESFromColorEnum(color:Color):String{
        return when(color){
            Color.BEIGE ->{
                return "Beige"
            }
            Color.BLACK ->{
                return "Negro"
            }
            Color.BLUE ->{
                return "Azul"
            }
            Color.GREEN ->{
                return "Verde"
            }
            Color.GREY ->{
                return "Gris"
            }
            Color.PURPLE ->{
                return "Morado"
            }
            Color.RED ->{
                return "Rojo"
            }
            Color.SILVER ->{
                return "Plata"
            }
            Color.WHITE -> {
                return "Blanco"
            }
            Color.YELLOW ->{
                return "Amarillo"
            }
            else -> return "Não informada"
        }
    }
}