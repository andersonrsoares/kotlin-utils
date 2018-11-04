package br.com.andersonsoares.activity

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class GeocoderUtils{
    companion object {

        fun getLocalizacao(endereco: String, context: Context, callback:(item:LatLng?) -> Unit) {
            try {
                doAsync {
                    var tentativas = 1
                    while (tentativas <= 5) {
                        val latLng = getLocalizacaoTentativas(endereco, context)
                        if (latLng != null){
                            uiThread {
                                callback(latLng)
                            }
                        }
                        Thread.sleep(700)
                        tentativas++
                    }
                    callback(null)
                }

            }catch (e:Exception){
                callback(null)
            }

        }


        private fun getLocalizacaoTentativas(endereco: String, context: Context): LatLng? {
            val geocoder = Geocoder(context)
            val address: List<Address>?
            var localizacao: LatLng? = null

            try {
                address = geocoder.getFromLocationName(endereco, 1)
                if (address == null) {
                    return null
                }

                val location = address[0]
                localizacao = LatLng(location.getLatitude(), location.getLongitude())

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return localizacao
        }

        fun getLocalizacao(location: LatLng, context: Context, callback:(item: Address?) -> Unit) {
            try {
                doAsync {
                    var tentativas = 1
                    while (tentativas <= 5) {
                        val addres = getLocalizacaoTentativas(location, context)
                        if (addres != null){
                            uiThread {
                                callback(addres)
                            }
                        }
                        Thread.sleep(500)
                        tentativas++
                    }
                    uiThread {
                        callback(null)
                    }
                }
            } catch (ex: Exception) {
                callback(null)
            }
        }

        private fun getLocalizacaoTentativas(location: LatLng, context: Context): Address? {

            val geocoder = Geocoder(context)
            val address: List<Address>?
            try {

                address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                return if (address == null) {
                    null
                } else address[0]

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return null
        }
    }

}

