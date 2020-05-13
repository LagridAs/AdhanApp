package com.example.adhanapp

import android.location.Location

interface HoraireInterface {
    fun getHoraireSalat(lat:Double,lon:Double):ArrayList<Horaire>
    fun getLastLocation():Location?
}