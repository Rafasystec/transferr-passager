package br.com.transferr.model

import br.com.transferr.model.enums.EnumDeviceType
import java.util.Date


class Tourist {

   var register: Date? = null
   var email: String? = null
   var phone: String? = null
   var device: EnumDeviceType? = null
}