package com.example.mycycle

import android.content.Context

object AppLanguage {

    fun get(
        context: Context,
        key: String
    ): String {

        val preferences = context.getSharedPreferences(
            "MyCycleSettings",
            Context.MODE_PRIVATE
        )

        val language = preferences.getString(
            "language",
            "English"
        ) ?: "English"

        val translations = mapOf(

            "Track My Period" to mapOf(
                "English" to "Track My Period",
                "isiZulu" to "Landelela Isikhathi Sami",
                "Sepedi" to "Latela Nako ya ka"
            ),
            "Start Date (YYYY-MM-DD)" to mapOf(
                "English" to "Start Date (YYYY-MM-DD)",
                "isiZulu" to "Usuku Lokuqala (YYYY-MM-DD)",
                "Sepedi" to "Letšatši la Mathomo (YYYY-MM-DD)"
            ),
            "End Date (YYYY-MM-DD)" to mapOf(
                "English" to "End Date (YYYY-MM-DD)",
                "isiZulu" to "Usuku Lokugcina (YYYY-MM-DD)",
                "Sepedi" to "Letšatši la Mafelelo (YYYY-MM-DD)"
            ),
            "Symptoms (comma-separated)" to mapOf(
                "English" to "Symptoms (comma-separated)",
                "isiZulu" to "Izimpawu (zehlukaniswe ngokhefana)",
                "Sepedi" to "Dika (di arogantšwe ka difegelwana)"
            ),
            "Cramps, headache, fatigue" to mapOf(
                "English" to "Cramps, headache, fatigue",
                "isiZulu" to "Amajaqamba, ikhanda elibuhlungu, ukukhathala",
                "Sepedi" to "Dikgohlano, hlogo ye bohloko, letsapa"
            ),
            "Happy, sad, tired..." to mapOf(
                "English" to "Happy, sad, tired...",
                "isiZulu" to "Ngijabule, ngidangele, ngikhathele...",
                "Sepedi" to "Ke thabile, ke nyamile, ke lapile..."
            ),
            "Additional notes" to mapOf(
                "English" to "Additional notes",
                "isiZulu" to "Amanothi engeziwe",
                "Sepedi" to "Dintlha tša tlaleletšo"
            ),
            "User session is invalid. Please login again." to mapOf(
                "English" to "User session is invalid. Please login again.",
                "isiZulu" to "Isikhathi somsebenzisi asivumelekile. Sicela ungene futhi.",
                "Sepedi" to "Lenaneo la modiriši ga le šome. Hle tsena gape."
            ),
            "Enter valid dates using YYYY-MM-DD." to mapOf(
                "English" to "Enter valid dates using YYYY-MM-DD.",
                "isiZulu" to "Faka izinsuku ezifanele usebenzisa YYYY-MM-DD.",
                "Sepedi" to "Tsenya matšatši a maleba o diriša YYYY-MM-DD."
            ),
            "End date cannot be before start date." to mapOf(
                "English" to "End date cannot be before start date.",
                "isiZulu" to "Usuku lokugcina alunakuba ngaphambi kosuku lokuqala.",
                "Sepedi" to "Letšatši la mafelelo le ka se be pele ga la mathomo."
            ),
            "Period saved successfully!" to mapOf(
                "English" to "Period saved successfully!",
                "isiZulu" to "Isikhathi sigcinwe ngempumelelo!",
                "Sepedi" to "Nako e bolokilwe ka katlego!"
            ),
            "Period saved, but Cosmos DB upload failed." to mapOf(
                "English" to "Period saved, but Cosmos DB upload failed.",
                "isiZulu" to "Isikhathi sigcinwe, kodwa ukuthumela ku-Cosmos DB kwehlulekile.",
                "Sepedi" to "Nako e bolokilwe, eupša go romela go Cosmos DB go paletšwe."
            ),
            "Period saved, but Cosmos DB is unavailable." to mapOf(
                "English" to "Period saved, but Cosmos DB is unavailable.",
                "isiZulu" to "Isikhathi sigcinwe, kodwa i-Cosmos DB ayitholakali.",
                "Sepedi" to "Nako e bolokilwe, eupša Cosmos DB ga e hwetšagale."
            ),
            "Period saved on this device. Server upload is pending." to mapOf(
                "English" to "Period saved on this device. Server upload is pending.",
                "isiZulu" to "Isikhathi sigcinwe kule divayisi. Sisalindele ukuthunyelwa kuseva.",
                "Sepedi" to "Nako e bolokilwe sedirišweng se. E sa emetše go romelwa go seva."
            ),
            "Saved offline! Your period will be uploaded when you reconnect and reopen this screen." to mapOf(
                "English" to "Saved offline! Your period will be uploaded when you reconnect and reopen this screen.",
                "isiZulu" to "Sigcinwe ungaxhunyiwe! Isikhathi sakho sizothunyelwa uma uxhuma futhi uvula lesi sikrini.",
                "Sepedi" to "E bolokilwe o se wa kgokagana! Nako ya gago e tla romelwa ge o kgokagana gape gomme o bula skrine se."
            ),
            "Saving..." to mapOf(
                "English" to "Saving...",
                "isiZulu" to "Kuyagcinwa...",
                "Sepedi" to "E a bolokwa..."
            ),
            "Save Period" to mapOf(
                "English" to "Save Period",
                "isiZulu" to "Gcina Isikhathi",
                "Sepedi" to "Boloka Nako"
            ),
            "Back" to mapOf(
                "English" to "Back",
                "isiZulu" to "Emuva",
                "Sepedi" to "Morago"
            ),
            "Welcome to MyCycle" to mapOf(
                "English" to "Welcome to MyCycle",
                "isiZulu" to "Siyakwamukela ku-MyCycle",
                "Sepedi" to "Re a go amogela go MyCycle"
            ),

            "Track My Period" to mapOf(
                "English" to "Track My Period",
                "isiZulu" to "Landela Isikhathi Sami",
                "Sepedi" to "Latela Nako ya ka"
            ),

            "Cycle Calendar" to mapOf(
                "English" to "Cycle Calendar",
                "isiZulu" to "Ikhalenda Lomjikelezo",
                "Sepedi" to "Tšhupamabaka ya Modikologo"
            ),

            "Symptoms & Mood" to mapOf(
                "English" to "Symptoms & Mood",
                "isiZulu" to "Izimpawu Nemizwa",
                "Sepedi" to "Dika le Maikutlo"
            ),

            "Achievements" to mapOf(
                "English" to "Achievements",
                "isiZulu" to "Izimpumelelo",
                "Sepedi" to "Diphihlelelo"
            ),

            "Health Education" to mapOf(
                "English" to "Health Education",
                "isiZulu" to "Imfundo Yezempilo",
                "Sepedi" to "Thuto ya Maphelo"
            ),

            "Settings" to mapOf(
                "English" to "Settings",
                "isiZulu" to "Izilungiselelo",
                "Sepedi" to "Dipeakanyo"
            ),

            "Logout" to mapOf(
                "English" to "Logout",
                "isiZulu" to "Phuma",
                "Sepedi" to "Tswa"
            ),


            "Settings" to mapOf(
                "English" to "Settings",
                "isiZulu" to "Izilungiselelo",
                "Sepedi" to "Dipeakanyo"
            ),

            "Language" to mapOf(
                "English" to "Language",
                "isiZulu" to "Ulimi",
                "Sepedi" to "Polelo"
            ),

            "Notifications" to mapOf(
                "English" to "Notifications",
                "isiZulu" to "Izaziso",
                "Sepedi" to "Ditsebišo"
            ),

            "Save Settings" to mapOf(
                "English" to "Save Settings",
                "isiZulu" to "Gcina Izilungiselelo",
                "Sepedi" to "Boloka Dipeakanyo"
            ),

            "Back to Home" to mapOf(
                "English" to "Back to Home",
                "isiZulu" to "Buyela Ekhaya",
                "Sepedi" to "Boela Gae"
            ),

            "Upcoming period reminder" to mapOf(
                "English" to "Upcoming period reminder",
                "isiZulu" to "Isikhumbuzi sesikhathi sokuya esikhathini",
                "Sepedi" to "Kgopotšo ya lebaka la go ya kgweding"
            ),

            "Period start reminder" to mapOf(
                "English" to "Period start reminder",
                "isiZulu" to "Isikhumbuzi sokuqala kokuya esikhathini",
                "Sepedi" to "Kgopotšo ya mathomo a go ya kgweding"
            ),

            "Cycle update reminder" to mapOf(
                "English" to "Cycle update reminder",
                "isiZulu" to "Isikhumbuzi sokubuyekezwa komjikelezo",
                "Sepedi" to "Kgopotšo ya tshedimošo ya modikologo"
            ),

            "Tracking reminder" to mapOf(
                "English" to "Tracking reminder",
                "isiZulu" to "Isikhumbuzi sokulandelela",
                "Sepedi" to "Kgopotšo ya go latela"
            ),

            "Cycle Calendar" to mapOf(
                "English" to "Cycle Calendar",
                "isiZulu" to "Ikhalenda Lomjikelezo",
                "Sepedi" to "Khalentara ya Modikologo"
            ),

            "Your saved periods:" to mapOf(
                "English" to "Your saved periods:",
                "isiZulu" to "Izikhathi zakho ezigciniwe:",
                "Sepedi" to "Dinako tša gago tše di bolokilwego:"
            ),

            "Cycle Statistics" to mapOf(
                "English" to "Cycle Statistics",
                "isiZulu" to "Izibalo Zomjikelezo",
                "Sepedi" to "Dipalo tša Modikologo"
            ),

            "Azure Cosmos DB Records" to mapOf(
                "English" to "Azure Cosmos DB Records",
                "isiZulu" to "Amarekhodi e-Azure Cosmos DB",
                "Sepedi" to "Direkoto tša Azure Cosmos DB"
            ),

            "Periods recorded" to mapOf(
                "English" to "Periods recorded",
                "isiZulu" to "Izikhathi ezirekhodiwe",
                "Sepedi" to "Dinako tše di rekhotilwego"
            ),

            "Average period length" to mapOf(
                "English" to "Average period length",
                "isiZulu" to "Ubude besikhathi obumaphakathi",
                "Sepedi" to "Botelele bja nako bja palogare"
            ),

            "Average cycle length" to mapOf(
                "English" to "Average cycle length",
                "isiZulu" to "Ubude bomjikelezo obumaphakathi",
                "Sepedi" to "Botelele bja modikologo bja palogare"
            ),

            "days" to mapOf(
                "English" to "days",
                "isiZulu" to "izinsuku",
                "Sepedi" to "matšatši"
            ),

            "Not enough data" to mapOf(
                "English" to "Not enough data",
                "isiZulu" to "Ayikho idatha eyanele",
                "Sepedi" to "Ga go na tshedimošo ye e lekanego"
            ),

            "Estimated next period" to mapOf(
                "English" to "Estimated next period",
                "isiZulu" to "Isikhathi esilandelayo esilinganisiwe",
                "Sepedi" to "Nako ye e latelago ye e akanyetšwago"
            ),

            "Record" to mapOf(
                "English" to "Record",
                "isiZulu" to "Irekhodi",
                "Sepedi" to "Rekoto"
            ),

            "Start date" to mapOf(
                "English" to "Start date",
                "isiZulu" to "Usuku lokuqala",
                "Sepedi" to "Letšatši la mathomo"
            ),

            "End date" to mapOf(
                "English" to "End date",
                "isiZulu" to "Usuku lokugcina",
                "Sepedi" to "Letšatši la mafelelo"
            ),

            "Not recorded" to mapOf(
                "English" to "Not recorded",
                "isiZulu" to "Akurekhodwanga",
                "Sepedi" to "Ga se ya rekhotwa"
            ),

            "Symptoms" to mapOf(
                "English" to "Symptoms",
                "isiZulu" to "Izimpawu",
                "Sepedi" to "Dika"
            ),

            "Mood" to mapOf(
                "English" to "Mood",
                "isiZulu" to "Isimo sengqondo",
                "Sepedi" to "Maikutlo"
            ),

            "Notes" to mapOf(
                "English" to "Notes",
                "isiZulu" to "Amanothi",
                "Sepedi" to "Dintlha"
            ),

            "to" to mapOf(
                "English" to "to",
                "isiZulu" to "kuya ku",
                "Sepedi" to "go fihla go"
            )
        )

        return translations[key]?.get(language) ?: key
    }
}
